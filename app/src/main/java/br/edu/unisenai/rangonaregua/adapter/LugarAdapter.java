package br.edu.unisenai.rangonaregua.adapter; // Declara que o arquivo está na pasta 'adapter'

import android.view.LayoutInflater; // Ferramenta para ler um arquivo XML e transformar em elementos reais na tela.
import android.view.View; // Classe base para qualquer coisa visual (botões, textos, layouts).
import android.view.ViewGroup; // Contêiner que agrupa outras Views (como a lista inteira).
import android.widget.Button; // Componente visual de Botão.
import android.widget.TextView; // Componente visual de Texto.

import androidx.annotation.NonNull; // Anotação que garante que um parâmetro não pode vir nulo (vazio).
import androidx.recyclerview.widget.RecyclerView; // O componente de lista otimizada do Android.

import java.util.List; // Interface genérica para listas.

import br.edu.unisenai.rangonaregua.R; // O arquivo "R" mapeia todos os layouts, cores e textos do projeto.
import br.edu.unisenai.rangonaregua.data.Catalogo; // (Import mantido do código base original).
import br.edu.unisenai.rangonaregua.model.Lugar; // Importa o nosso molde de dados.

// O Adapter é o "Montador". Ele herda (extends) as regras do RecyclerView.Adapter.
public class LugarAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Lugar> lugares; // Variável para armazenar a lista de lugares que recebemos.
    private static final int CARD_LIDER = 0; // Constante fixa (0) que usaremos para identificar o layout do restaurante líder.
    private static final int CARD_NORMAL = 1; // Constante fixa (1) que usaremos para os outros restaurantes normais.

    // Uma interface é como um "contrato". Ela define funções que alguém (neste caso, a MainActivity) vai ter que cumprir.
    public interface Acao {
        void votar(Lugar lugar); // Função que será disparada ao clicar para votar.
        void detalhes(Lugar lugar); // Função que será disparada ao clicar no card inteiro.
    }
    private Acao acao; // Variável para guardar quem está implementando o contrato acima.
    // -- FI (Fim da Interface)

    // O Construtor do Adapter. Quando criamos ele, temos que passar a lista de dados e quem vai responder pelas "Acoes".
    public LugarAdapter(List<Lugar> lugares, Acao acao) {
        this.lugares = lugares; // Salva a lista recebida na variável da classe.
        this.acao = acao; // Salva o observador de cliques (Acao) na variável da classe.
    }

    // Função que o Android chama pra perguntar: "Que tipo de visual esse item número 'position' precisa ter?"
    @Override
    public int getItemViewType(int position) {
        // Se a posição for 0 (o primeiríssimo da lista), retorne 0 (CARD_LIDER). Senão, retorne 1 (CARD_NORMAL).
        return position == 0 ? CARD_LIDER : CARD_NORMAL;
    }

    // Função que o Android chama para CRIAR os retângulos em branco (ViewHolder) na memória.
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Verifica que tipo de visual foi pedido...
        if (viewType == CARD_LIDER) {
            // Usa o LayoutInflater para pegar o XML "item_lider" e transformá-lo num objeto View real ('tela').
            View tela = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lider, parent, false);
            // Retorna um novo ViewHolderLider entregando essa tela recém-inflada.
            return new ViewHolderLider(tela);
        } else {
            // Se não for líder, usa o LayoutInflater para inflar o XML "item_lugar" (o normal).
            View tela = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lugar, parent, false);
            // Retorna um novo ViewHolder entregando a tela.
            return new ViewHolder(tela);
        }
    }

    // Função que o Android chama para LIGAR (Bind) os dados de um 'Lugar' específico num ViewHolder já criado.
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Lugar item = lugares.get(position); // Pega o objeto Lugar exato desta posição.

        // Se a "moldura" (holder) for do tipo ViewHolderLider...
        if (holder instanceof ViewHolderLider) {

            // Converte (cast) a moldura genérica para o tipo ViewHolderLider para acessarmos as variáveis dele.
            ViewHolderLider vh = (ViewHolderLider) holder;
            // Pega o texto do Nome e joga no campo txtNomeLider.
            vh.txtNomeLider.setText(item.getNome());
            // Pega a Categoria e joga no campo, colando um "R$ " errado que estava no código original.
            vh.txtCategoriaLider.setText(("R$ " + item.getCategoria()));
            // Pega o Preço e joga no campo de texto do líder.
            vh.txtPrecoLider.setText(("R$ " + item.getPrecoMedio()));;
            // Pega os Votos e joga no campo formatado com espaços em volta.
            vh.txtVotosLider.setText(" " + item.getVotos() + " ");
            // Pega a Observação e joga no campo txtObsLider.
            vh.txtObsLider.setText(item.getObservacao());
            
            // Configura o botão de votar do líder: Quando clicado (v), avisa a interface passando o 'item'.
            vh.btnVotarLider.setOnClickListener(v -> acao.votar(item));
            // Configura o card inteiro: Quando clicado, avisa a interface acionando 'detalhes'.
            vh.itemView.setOnClickListener(v -> acao.detalhes(item));

        } else { // Se a moldura for do tipo normal (ViewHolder)...

            // Converte para a classe ViewHolder normal.
            ViewHolder vh = (ViewHolder) holder;
            // Preenche o campo txtPosicao somando +1 (para a posição 1 do Java virar o "2º lugar" visual).
            vh.txtPosicao.setText(String.valueOf(position + 1));
            // Preenche o nome.
            vh.txtNome.setText(item.getNome());
            // Preenche a categoria.
            vh.txtCategoria.setText(item.getCategoria());
            // Preenche o preço.
            vh.txtPreco.setText(("R$ " + item.getPrecoMedio()));
            // Preenche os votos.
            vh.txtVotos.setText(" " + item.getVotos() + " ");
            
            // Configura o clique no botão votar normal.
            vh.btnVotar.setOnClickListener(v -> acao.votar(item));
            // Configura o clique no card inteiro normal.
            vh.itemView.setOnClickListener(v -> acao.detalhes(item));
        }
    }

    // Função onde o Android pergunta qual o tamanho total da lista.
    @Override
    public int getItemCount() {
        return lugares.size(); // Retorna a quantidade de itens que tem na lista 'lugares'.
    }

    // Classe interna: O "Guarda-referências" dos itens normais. Ele acha e segura os componentes do XML.
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Declara as variáveis que representarão os componentes da tela.
        TextView txtPosicao, txtNome, txtCategoria, txtPreco, txtVotos;
        Button btnVotar;

        // O construtor recebe a View inteira (a linha da lista inflada).
        public ViewHolder(View itemView) {
            super(itemView); // Manda a View para a superclasse RecyclerView.ViewHolder cuidar.
            // Procura dentro da View o componente com ID 'txtPosicao' e salva na variável.
            txtPosicao = itemView.findViewById(R.id.txtPosicao);
            // Faz o mesmo para o nome...
            txtNome = itemView.findViewById(R.id.txtNome);
            // Categoria...
            txtCategoria = itemView.findViewById(R.id.txtCategoria);
            // Preço...
            txtPreco = itemView.findViewById(R.id.txtPreco);
            // Votos...
            txtVotos = itemView.findViewById(R.id.txtVotos);
            // E o botão.
            btnVotar = itemView.findViewById(R.id.btnVotar);
        }
    }

    // Classe interna: O "Guarda-referências" do layout líder.
    public static class ViewHolderLider extends RecyclerView.ViewHolder {
        // Declara as variáveis que compõem o layout do Líder (ele tem a Observacao a mais).
        TextView txtNomeLider, txtCategoriaLider, txtPrecoLider, txtVotosLider, txtObsLider;
        Button btnVotarLider;

        // Construtor recebe a View.
        public ViewHolderLider(View itemView) {
            super(itemView);
            // Procura e salva cada componente do XML do Líder nas variáveis respectivas.
            txtNomeLider = itemView.findViewById(R.id.txtNomeLider);
            txtCategoriaLider = itemView.findViewById(R.id.txtCategoriaLider);
            txtPrecoLider = itemView.findViewById(R.id.txtPrecoLider);
            txtVotosLider = itemView.findViewById(R.id.txtVotosLider);
            btnVotarLider = itemView.findViewById(R.id.btnVotarLider);
            txtObsLider = itemView.findViewById(R.id.txtObservacaoLider);
        }
    }
}
