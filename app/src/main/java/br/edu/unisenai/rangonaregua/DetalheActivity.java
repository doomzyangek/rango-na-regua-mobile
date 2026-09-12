package br.edu.unisenai.rangonaregua; // Pacote base.

import android.os.Bundle; // Guardião de estado.
import android.widget.TextView; // Componente visual apenas de Leitura de texto (o usuário não digita aqui).

import androidx.activity.EdgeToEdge; // Tela de ponta a ponta.
import androidx.appcompat.app.AppCompatActivity; // Tela moderna base.
import androidx.appcompat.widget.Toolbar; // Barra de topo.
import androidx.core.graphics.Insets; // Margens.
import androidx.core.view.ViewCompat; // Compatibilidade visual.
import androidx.core.view.WindowInsetsCompat; // Espaçamento sistêmico.

import br.edu.unisenai.rangonaregua.model.Lugar; // Importa nosso molde para poder receber um lugar.

import java.text.NumberFormat; // Formatador de números (importado, mas não utilizado).
import java.util.Locale; // Tradutor de região para a formatação de dinheiro (importado, não utilizado).

// Declara a classe DetalheActivity (Tela que mostra todas as informações quando clicamos na lista).
public class DetalheActivity extends AppCompatActivity {

    // Nasce a tela.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); // Original do Android.
        EdgeToEdge.enable(this); // Ativa tela de borda a borda.
        setContentView(R.layout.activity_detalhe); // Conecta o Java com a tela XML dos detalhes.
        
        // Empurra os textos para não ficarem embaixo do relógio e botões virtuais.
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Configuração da Toolbar (barrinha superior):
        // Acha a barra pelo ID do XML.
        Toolbar toolbar = findViewById(R.id.toolbarDetalhe);
        // Define ela como a barra de comandos oficial.
        setSupportActionBar(toolbar);
        // Dá poder à seta de voltar da barra: se clicar, a tela de detalhes fecha (finish) e você retorna pra lista.
        toolbar.setNavigationOnClickListener(v -> finish());

        // A Mágica de Puxar o Dado:
        // A intenção (getIntent) que abriu essa tela trazia uma mala extra (Extra). 
        // Vamos abrí-la usando a senha "obj" (que foi a palavra chave usada lá na MainActivity).
        // Como o Extra é "Serializable" e genérico, a gente faz um Casting "(Lugar)", forçando e avisando ao Java que aquilo ali é um restaurante, garantido.
        Lugar dados = (Lugar) getIntent().getSerializableExtra("obj");

        // Encontra todos os TextViews (rótulos de texto) da tela pelo ID e separa as variáveis em memória.
        TextView txtNome = findViewById(R.id.txtDetalheNome); // Rótulo do Nome
        TextView txtCategoria = findViewById(R.id.txtDetalheCategoria); // Rótulo da Categoria
        TextView txtPreco = findViewById(R.id.txtDetalhePreco); // Rótulo do Preço
        TextView txtVotos = findViewById(R.id.txtDetalheVotos); // Rótulo de Votos
        TextView txtObservacao = findViewById(R.id.txtDetalheObservacao); // Rótulo da Observação completa

        // Pega o que estava dentro do objeto 'dados' que veio pela mala, e joga como texto (setText) na tela pro usuário ler.
        // Pega o nome do objeto e mostra na tela.
        txtNome.setText(dados.getNome());
        // Pega a categoria e mostra.
        txtCategoria.setText(dados.getCategoria());
        // Monta um textinho "R$ " e cola (concatena usando +) com o valor do preço médio que veio no objeto.
        txtPreco.setText("R$ " + dados.getPrecoMedio());
        // Cola os votos com a palavra texto " votos " atrás.
        txtVotos.setText(dados.getVotos() + " votos ");
        // Joga a observação gigantesca na tela.
        txtObservacao.setText(dados.getObservacao());
    }
}
