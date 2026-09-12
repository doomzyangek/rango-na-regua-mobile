package br.edu.unisenai.rangonaregua; // Pasta raiz do aplicativo.

import static android.app.ProgressDialog.show; // Importa atalho (obsoleto) para janela de carregamento (não usado efetivamente aqui).

import android.content.Intent; // Classe usada para navegar entre telas.
import android.os.Bundle; // Classe que guarda o estado salvo da tela caso ela seja destruída e recriada.
import android.util.Log; // Classe usada para imprimir mensagens de erro no console do desenvolvedor.
import android.view.View; // Classe de componentes visuais genéricos.

import androidx.activity.EdgeToEdge; // Ferramenta que permite o app usar a tela inteira (atrás da bateria/relógio).
import androidx.annotation.NonNull; // Anotação para garantir que algo não é nulo.
import androidx.appcompat.app.AppCompatActivity; // Classe base moderna para Telas do Android.
import androidx.appcompat.widget.Toolbar; // Componente visual da barra superior (não usado nesta tela).
import androidx.core.graphics.Insets; // Ferramenta de cálculo de margens.
import androidx.core.view.ViewCompat; // Ferramenta de compatibilidade visual.
import androidx.core.view.WindowInsetsCompat; // Lida com os espaços do sistema (bateria, barra de navegação).
import androidx.recyclerview.widget.ItemTouchHelper; // Classe que cuida da lógica de arrastar e soltar da lista.
import androidx.recyclerview.widget.LinearLayoutManager; // Define que a lista será arranjada em formato vertical/linha a linha.
import androidx.recyclerview.widget.RecyclerView; // A lista super poderosa do Android.

import com.google.android.material.floatingactionbutton.FloatingActionButton; // Botão redondo flutuante.
import com.google.android.material.snackbar.Snackbar; // Barra de mensagens preta que aparece no rodapé.
import com.google.firebase.firestore.ListenerRegistration; // Recibo de que estamos escutando o Firebase.

import java.util.ArrayList; // Ferramenta para criar Listas dinâmicas.
import java.util.List; // O conceito de Lista.

import br.edu.unisenai.rangonaregua.adapter.LugarAdapter; // Importa nosso desenhista da lista.
import br.edu.unisenai.rangonaregua.data.Catalogo; // Import mantido por compatibilidade.
import br.edu.unisenai.rangonaregua.data.LugarRepository; // Importa a conexão com o banco.
import br.edu.unisenai.rangonaregua.model.Lugar; // Importa o molde de dados Lugar.

// Declaração da Tela Inicial. Ela 'extends' AppCompatActivity (é uma tela) e 'implements' LugarAdapter.Acao (assinou o contrato para responder cliques).
public class MainActivity extends AppCompatActivity implements LugarAdapter.Acao{

    private LugarRepository repository; // Declara uma variável para guardar a conexão com o Firebase.
    private ListenerRegistration registro; // Declara a variável que guarda o nosso "rádio" ligado no banco.

    static List<Lugar> listaLugar = new ArrayList<>(); // Cria a Lista de lugares vazia que vai guardar os dados. 'static' significa que a lista sobrevive a vida da tela.
    LugarAdapter adapter; // Declara o Adapter (a ponte entre a lista de dados e o visual).

    // Método que é chamado quando a tela é criada pela primeira vez (Ciclo de Vida do Android).
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); // OBRIGATÓRIO: Chama a versão original do onCreate para garantir que tudo básico carregue.
        EdgeToEdge.enable(this); // Ativa o modo tela cheia para o app passar atrás da barra de status.
        setContentView(R.layout.activity_main); // Amarra esta classe Java ao arquivo de visual XML 'activity_main'.
        
        // Configura uma margem para que os itens da tela não fiquem sobrepostos pelos botões físicos/virtuais do celular.
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars()); // Mede o espaço gasto pelo celular
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom); // Empurra a tela (padding) para não cobrir.
            return insets; // Retorna os cálculos aplicados.
        });

        //Carregar o Database
        //listaLugar = Catalogo.inicial(); (Código antigo que preenchia lista falsa. Está comentado/desligado).
        
        // Instancia (liga) a nossa classe de encanamento do Firebase.
        repository = new LugarRepository();

        // Encontra o botão flutuante redondo (FAB) pelo ID que está no XML.
        FloatingActionButton btNovo = findViewById(R.id.fabNovo);
        // Diz o que deve acontecer ao clicar (setOnClickListener) no botão:
        btNovo.setOnClickListener(v -> {
            // Cria uma 'Intenção' (Intent) dizendo: "Saia daqui (this) e vá para a tela de novo lugar (NovoLugarActivity)".
            Intent intent = new Intent(this, NovoLugarActivity.class);
            // Manda o sistema executar a intenção, trocando a tela de fato.
            startActivity(intent);
        });

        // Carrega o RecycleView (A lista rolável) procurando o componente no XML pelo ID 'rvLugares'.
        RecyclerView rvLugares = findViewById(R.id.rvLugares);
        // Informa que a lista vai se comportar num formato linear vertical (uma linha abaixo da outra).
        rvLugares.setLayoutManager(new LinearLayoutManager(this));

        // Cria o nosso Adapter passando: a lista de dados vazia, e "this" (a própria MainActivity como sendo a ouvinte das Acoes).
        adapter = new LugarAdapter(listaLugar, this);
        // Gruda o Adapter no RecyclerView. Agora eles trabalham juntos.
        rvLugares.setAdapter(adapter);

        // Chama a função interna abaixo que configura o "Arrastar para o lado para apagar".
        configDeslizar();
    }

    // Função auxiliar que criamos para configurar a exclusão com deslize.
    private void configDeslizar(){
        // Cria uma nova regra de toque (SimpleCallback) que não responde pra arrastar pra cima/baixo (0), mas aceita ESQUERDA e DIREITA (LEFT | RIGHT).
        ItemTouchHelper.SimpleCallback deslizar =
                new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                    
                    // Esse método serveria para quando você reordena itens movendo pra cima ou baixo. Aqui não usamos.
                    @Override
                    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                        return false; // Retorna falso porque não permitimos reordenação vertical.
                    }

                    // Método disparado assim que o usuário terminar de deslizar um card pro lado.
                    @Override
                    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                        // Descobre, através do ViewHolder deslizado, qual era a posição dele na lista.
                        int posicao = viewHolder.getAdapterPosition();

                        // Pega na nossa 'listaLugar' exatamente o objeto que estava naquela posição.
                        Lugar item = listaLugar.get(posicao);
                        // Manda nosso encanamento do Firebase apagar este lugar da nuvem.
                        repository.excluir(item);

                        // Cria e mostra um Snackbar (balãozinho preto embaixo da tela).
                        Snackbar.make(findViewById(R.id.rvLugares) // Atrala o balão na base da lista.
                                        , "Lugar Removido" // A mensagem principal.
                                        , Snackbar.LENGTH_LONG) // Duração longa para sumir.
                                        // Cria um botão "Desfazer" (setAction) no balão.
                                        .setAction("Desfazer", v ->
                                            // Se clicar em desfazer, pede pro Firebase restaurar o item.
                                            repository.restaurar(item))
                                                // Manda exibir o balão (show).
                                                .show();
                    }
                };

        // Cria o controlador final de toque passando nossas regras...
        new ItemTouchHelper(deslizar)
                // ...e gruda definitivamente esse controlador na lista visual.
                .attachToRecyclerView(findViewById(R.id.rvLugares));
    }

    // O onResume é um evento do Android. Ele roda sempre que o app volta para o foco principal (ex: quando você fecha a tela de cadastro e volta pra cá).
    @Override
    protected void onResume() {
        super.onResume(); // Chama versão base do Android.

        // Manda o repositório começar a ler o Firebase em tempo real. O "rádio" fica sintonizado.
        registro = repository.lerRealTime((value, error) -> {
            // Se ocorreu algum erro na escuta (ex: falta de internet)...
            if(error != null) {
                Log.e("ERRO", error.getMessage()); // Grava o erro no console (logcat) para o programador ler.
                return; // Encerra a função, não faz mais nada.
            }

            // Se a conexão foi sucesso: limpa a lista antiga do nosso celular.
            listaLugar.clear();
            // Pega o 'value' (que é a foto instantânea da nuvem devolvida pelo Firebase), transforma todos os documentos em objetos do molde 'Lugar.class', e adiciona de uma vez na nossa lista.
            listaLugar.addAll(value.toObjects(Lugar.class));
            // Grita pro Adapter da lista: "Ei, os dados mudaram! Redesenhe todos os itens!".
            adapter.notifyDataSetChanged();
        });
    }

    // Essa é a implementação do contrato 'votar' que a MainActivity assinou com o LugarAdapter.
    @Override
    public void votar(Lugar lugar) {
        // Códigos antigos e manuais de voto estão comentados.
        //lugar.setVotos(lugar.getVotos() + 1);
        //Catalogo.ordenarPorVotos(listaLugar);
        //adapter.notifyDataSetChanged();
        
        // Pede ao encanamento do banco para enviar o voto para a nuvem.
        repository.votar(lugar);
    }

    // Essa é a implementação do contrato 'detalhes'. É chamado quando clicamos no card do restaurante.
    @Override
    public void detalhes(Lugar lugar) {
        // Cria a intenção de pular para a DetalheActivity.
        Intent rota = new Intent(this, DetalheActivity.class);
        // Anexa dentro da intenção (putExtra) uma mala extra chamada "obj", contendo todo o objeto do restaurante clicado. (Por isso ele precisa ser Serializable!)
        rota.putExtra("obj",lugar);
        // Manda o sistema realizar o salto de tela.
        startActivity(rota);
    }
}
