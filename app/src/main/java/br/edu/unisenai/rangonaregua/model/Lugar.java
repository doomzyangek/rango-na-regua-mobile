package br.edu.unisenai.rangonaregua.model; // Declara que este arquivo pertence à pasta (pacote) 'model' dentro do projeto.

import com.google.firebase.firestore.DocumentId; // Importa a ferramenta do Firebase que identifica automaticamente o ID do documento.

import java.io.Serializable; // Importa a interface que permite transformar este objeto em uma cadeia de bytes para ser transferido entre telas.

// Declaração da classe Lugar. 'public' significa que outras partes do app podem usá-la. 'implements Serializable' habilita a transferência de dados.
public class Lugar implements Serializable {

    @DocumentId // Essa anotação (palavra com @) avisa o Firebase: "A variável abaixo vai guardar a chave única de identificação lá da nuvem".
    private String id; // Cria a variável 'id' do tipo texto (String). 'private' significa que só esta classe pode mexer nela diretamente.

    private String nome; // Cria a variável de texto 'nome' para guardar o nome do restaurante.
    private String categoria; // Cria a variável de texto 'categoria' para guardar o tipo de comida (Lanche, Almoço, etc).
    private double precoMedio; // Cria a variável de número decimal (double) 'precoMedio' para guardar o valor (ex: 28.50).
    private String observacao; // Cria a variável de texto 'observacao' para guardar descrições adicionais.
    private int votos; // Cria a variável de número inteiro (int) 'votos' para guardar quantas curtidas o lugar tem.

    // Construtor vazio. É obrigatório ter isso para o Firebase conseguir puxar os dados da internet e preencher um Lugar automaticamente.
    public Lugar(){}

    // Construtor com parâmetros. Uma função especial usada quando NÓS (programadores) queremos criar um Lugar do zero fornecendo os dados.
    public Lugar(String nome, String categoria, double precoMedio, String observacao, int votos) {
        this.nome = nome; // Pega o 'nome' recebido nos parênteses e salva na variável interna (this.nome).
        this.categoria = categoria; // Pega a 'categoria' recebida e salva na variável interna.
        this.precoMedio = precoMedio; // Pega o 'precoMedio' recebido e salva na variável interna.
        this.observacao = observacao; // Pega a 'observacao' recebida e salva na variável interna.
        this.votos = votos; // Pega a quantidade de 'votos' recebida e salva na variável interna.
    }

    // Getters: São métodos públicos que servem apenas para entregar a informação que está escondida (private) para quem pedir.
    public String getNome() { return nome; } // Quando chamado, devolve (return) o que está guardado em 'nome'.
    public String getCategoria() { return categoria; } // Quando chamado, devolve (return) o que está guardado em 'categoria'.
    public double getPrecoMedio() { return precoMedio; } // Quando chamado, devolve (return) o que está guardado em 'precoMedio'.
    public String getObservacao() { return observacao; } // Quando chamado, devolve (return) o que está guardado em 'observacao'.
    public int getVotos() { return votos; } // Quando chamado, devolve (return) o que está guardado em 'votos'.
    public String getId() { return id; } // Quando chamado, devolve (return) o que está guardado no 'id' do Firebase.

    // Setters: São métodos públicos que servem para alterar a informação que está escondida.
    public void setId(String id) { this.id = id; } // Recebe um texto e substitui o 'id' atual por este novo. Útil para o Firebase.
    public void setVotos(int votos) { this.votos = votos; } // Recebe um número inteiro e atualiza a quantidade de 'votos' atual.
}
