package br.edu.unisenai.rangonaregua; // Pasta raiz do projeto.

import static br.edu.unisenai.rangonaregua.MainActivity.listaLugar; // Import que antigamente puxava a lista da MainActivity, hoje inutilizado.

import android.content.Intent; // Navegação entre telas.
import android.os.Bundle; // Guardião de estado.
import android.widget.Button; // Componente visual Botão.
import android.widget.EditText; // Componente visual Caixa de Texto (onde o usuário digita).
import android.widget.Toast; // Balãozinho cinza que sobe da base para dar avisos rápidos.

import androidx.activity.EdgeToEdge; // Tela inteira.
import androidx.appcompat.app.AppCompatActivity; // Tela base.
import androidx.appcompat.widget.Toolbar; // Barra de menu do topo.
import androidx.core.graphics.Insets; // Margens.
import androidx.core.view.ViewCompat; // View compatível.
import androidx.core.view.WindowInsetsCompat; // Lida com barra de status.

import br.edu.unisenai.rangonaregua.data.LugarRepository; // O encanamento do Firestore.
import br.edu.unisenai.rangonaregua.model.Lugar; // O molde de dados.

// Declara a classe NovoLugarActivity, que é uma tela de cadastro.
public class NovoLugarActivity extends AppCompatActivity {

    // Método chamado quando a tela nasce.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); // Chamada base.
        EdgeToEdge.enable(this); // Passa o layout atrás do relógio.
        setContentView(R.layout.activity_novo_lugar); // Atrela ao XML de visual do cadastro.
        
        // Afasta o conteúdo para que o rodapé do celular e a barra de status de cima não escondam nossos campos.
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Acha a barrinha azul/colorida superior usando o ID dela do XML (toolbarNovo).
        Toolbar toolbar = findViewById(R.id.toolbarNovo);
        // Transforma ela na barra de ação oficial da tela (ActionBar).
        setSupportActionBar(toolbar);
        // Avisa que quando o usuário clicar na setinha para voltar na barra, a tela fará 'finish()', que significa "se destruir" e fechar.
        toolbar.setNavigationOnClickListener(v -> finish());

        //Carregar os componentes: Encontra as 4 caixas de digitação do XML e guarda nas variáveis de código para podermos ler depois.
        EditText edtNome = findViewById(R.id.edtNome); // Caixa nome
        EditText edtCategoria = findViewById(R.id.edtCategoria); // Caixa categoria
        EditText edtPreco = findViewById(R.id.edtPreco); // Caixa preço
        EditText edtObservacao = findViewById(R.id.edtObservacao); // Caixa observação
        Button btnSalvar = findViewById(R.id.btnSalvar); // E encontra o botão Salvar.

        // Cria um ouvinte para disparar uma função quando o botão salvar for clicado (setOnClickListener).
        btnSalvar.setOnClickListener(v -> {
            // Começa a verificação de segurança (Ifs encadeados):
            // Pega o texto do nome (getText), transforma em String (toString) e pergunta: Está vazio (isEmpty)?
            if (edtNome.getText().toString().isEmpty()) {
                // Se sim, pinta o campo de vermelho apontando um erro "Obrigatório".
                edtNome.setError("Obrigatório");
            } 
            // Se passou do nome, faz a mesma pergunta para a categoria.
            else if (edtCategoria.getText().toString().isEmpty()) {
                edtCategoria.setError("Obrigatório");
            } 
            // Se passou, pergunta para o preço.
            else if (edtPreco.getText().toString().isEmpty()) {
                edtPreco.setError("Obrigatório");
            } 
            // Se não barrou em nenhum "if", significa que os 3 obrigatórios foram preenchidos. Entra no "Else".
            else {
                // Gravar Dados: Vai instanciar (criar) um objeto usando o nosso molde Lugar.
                Lugar novo = new Lugar(
                        edtNome.getText().toString(), // Manda o texto digitado no nome
                        edtCategoria.getText().toString(), // Texto da categoria
                        Double.parseDouble(edtPreco.getText().toString()), // Transforma (Parse) o texto digitado do preço em um número Double de verdade.
                        edtObservacao.getText().toString(), // Texto da observação
                        0); // Inicia sempre com ZERO votos.
                        
                //listaLugar.add(novo); (Código antigo desativado).

                //Serve para adicionar a informação dentro da coluna "lugares"
                // Cria a ponte (conexão) do repositório.
                LugarRepository repository = new LugarRepository();
                // Chama a função de inserir passando o 'novo' objeto. 
                repository.inserir(novo)
                                // Pendura um observador para caso dê certo (Success):
                                .addOnSuccessListener(command -> {
                                    // Pede pro sistema mostrar o balãozinho Toast escrito "Sucesso", rapidinho (LENGTH_SHORT).
                                    Toast.makeText(this, "Sucesso", Toast.LENGTH_SHORT).show();
                                })
                                // Pendura um observador para caso dê erro (Failure) de internet/banco:
                                        .addOnFailureListener(e -> {
                                            // Pede pra mostrar balão Toast escrito "Deu ruim".
                                            Toast.makeText(this, "Deu ruim", Toast.LENGTH_SHORT).show();
                                        });

                // Independentemente de se a internet demorar a salvar ou não, manda a tela de cadastro fechar (finish)
                // e devolve o controle do app para a tela Inicial de onde viemos.
                finish();
            }
        });
    }
}
