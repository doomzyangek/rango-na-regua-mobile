package br.edu.unisenai.rangonaregua.data; // Define que este arquivo está dentro do pacote de dados (data).

import br.edu.unisenai.rangonaregua.model.Lugar; // Importa a classe Lugar, pois vamos criar listas de lugares aqui.

import java.util.ArrayList; // Importa a classe ArrayList, que serve para criar listas expansíveis.
import java.util.Arrays; // Importa utilitários para trabalhar com agrupamentos de dados.
import java.util.Collections; // Importa utilitários para organizar/mexer em coleções (listas).
import java.util.Comparator; // Importa a ferramenta de Comparador (usada para criar regras de quem vem primeiro na lista).
import java.util.LinkedHashSet; // (Import não utilizado no momento, mas mantido por segurança do seu repositório original).
import java.util.List; // Importa a interface base de Listas do Java.
import java.util.Set; // (Import não utilizado no momento, mantido).

// Classe 'final' (não pode ser herdada) chamada Catalogo. Serve como um banco de dados estático e falso (Fake Data).
public final class Catalogo {

    // Construtor privado: Isso impede que alguém tente criar um objeto (new Catalogo()) dessa classe acidentalmente.
    private Catalogo() { }

    // Função pública e estática (pode ser chamada de qualquer lugar sem precisar criar o Catalogo) que retorna um ArrayList de Lugares.
    public static ArrayList<Lugar> inicial() {
        // Cria uma nova ArrayList de Lugares usando um agrupamento pronto (Arrays.asList) que contém vários novos objetos Lugar.
        ArrayList<Lugar> lista = new ArrayList<>(Arrays.asList(
                // Cria o lugar 1 e passa nome, categoria, preço, descrição e votos.
                new Lugar("Pastel do Seu Jorge", "Salgado", 9.50,
                        "Pastel de carne com queijo e caldo de cana. Fila grande depois das 19h.", 12),
                // Cria o lugar 2.
                new Lugar("Marmita da Dona Rita", "Almoço", 22.00,
                        "Prato feito com repetição de arroz e feijão. Melhor custo por caloria da região.", 10),
                // Cria o lugar 3.
                new Lugar("Burger do Léo", "Lanche", 28.00,
                        "Hambúrguer artesanal. Caro para o dia a dia, salva o pós-prova.", 8),
                // Cria o lugar 4.
                new Lugar("Café da Tia Neide", "Café", 7.00,
                        "Pão na chapa e café coado de verdade. Abre às 6h.", 7),
                // Cria o lugar 5.
                new Lugar("Sushi da Esquina", "Japonesa", 45.00,
                        "Rodízio no almoço de terça sai por menos da metade.", 5),
                // Cria o lugar 6.
                new Lugar("Açaí 24h", "Sobremesa", 18.00,
                        "Único lugar aberto na madrugada de entrega de trabalho.", 4),
                // Cria o lugar 7.
                new Lugar("Bar do Português", "Petisco", 32.00,
                        "Porção de calabresa que alimenta uma equipe inteira de PI.", 3),
                // Cria o lugar 8.
                new Lugar("Padaria Aurora", "Salgado", 6.00,
                        "Coxinha grande e o melhor pão de queijo do quarteirão.", 2)
        )); // Fecha a criação da lista.
        
        // Chama a função ordenarPorVotos passando essa lista inteira recém-criada.
        ordenarPorVotos(lista);
        
        // Depois de organizada, devolve a lista pronta.
        return lista;
    }

    // Função pública e estática que não devolve nada (void), apenas recebe uma Lista de lugares para ordená-la.
    public static void ordenarPorVotos(List<Lugar> lista) {
        // Usa o utilitário Collections.sort para ordenar a lista, criando uma regra customizada na hora (new Comparator).
        Collections.sort(lista, new Comparator<Lugar>() {
            // Obriga a implementar o método compare, que recebe dois lugares de cada vez para comparar (a e b).
            @Override
            public int compare(Lugar a, Lugar b) {
                // Se a quantidade de votos do lugar 'b' for DIFERENTE (!=) dos votos do lugar 'a':
                if (b.getVotos() != a.getVotos()) {
                    // Retorna a comparação numérica: se 'b' for maior, joga pra cima. (Ordem decrescente de votos).
                    return Integer.compare(b.getVotos(), a.getVotos());
                }
                // Se os votos forem IGUAIS, o desempate é feito comparando o nome de 'a' com o de 'b' ignorando maiúsculas/minúsculas (ordem alfabética).
                return a.getNome().compareToIgnoreCase(b.getNome());
            }
        }); // Fecha a regra de ordenação.
    }
}
