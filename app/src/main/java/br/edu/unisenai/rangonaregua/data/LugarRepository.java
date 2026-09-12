package br.edu.unisenai.rangonaregua.data; // Define que este arquivo está no pacote de dados.

import com.google.android.gms.tasks.Task; // Importa 'Task', que representa uma tarefa que roda em segundo plano (assíncrona).
import com.google.firebase.firestore.DocumentReference; // Importa a referência para um documento específico no Firestore.
import com.google.firebase.firestore.EventListener; // Importa o ouvinte (espião) de eventos do Firestore.
import com.google.firebase.firestore.FieldValue; // Importa manipuladores de valores do Firebase (como o 'incrementar').
import com.google.firebase.firestore.FirebaseFirestore; // Importa o controlador principal do banco de dados Firestore.
import com.google.firebase.firestore.ListenerRegistration; // Importa o recibo de registro do ouvinte (pra poder desligar depois se quiser).
import com.google.firebase.firestore.Query; // Importa o sistema de montagem de consultas (perguntas) ao banco.
import com.google.firebase.firestore.QuerySnapshot; // Importa a "foto" (estado atual) dos dados devolvidos pelo banco.

import br.edu.unisenai.rangonaregua.model.Lugar; // Importa a nossa planta baixa (molde) de Lugar.

// Classe responsável por intermediar (fazer o encanamento) entre o app e o banco Firebase.
public class LugarRepository {

    // Função pública para inserir. Recebe o 'Lugar' que queremos salvar e devolve uma 'Task' (tarefa) contendo a referência criada.
    public Task<DocumentReference> inserir (Lugar item){
        //Conexão do banco: Pega a instância única (conexão aberta) do Firestore.
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        //Cria (ou acessa) a coleção "lugares" e adiciona (add) o nosso objeto 'item' lá dentro. Retorna essa tarefa.
        return db.collection("lugares").add(item);
    }

    // Função para ficar escutando a nuvem ao vivo. Recebe um EventListener (código que vai ser avisado das mudanças) e devolve o registro dessa escuta.
    public ListenerRegistration lerRealTime (EventListener<QuerySnapshot> callback) {
        // Pega a conexão com o banco.
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Acessa a coleção "lugares"...
        return db.collection("lugares")
                // ...ordena (orderBy) pela coluna "votos" de forma decrescente (DESCENDING, maior pro menor)...
                .orderBy("votos", Query.Direction.DESCENDING)
                // ...e pendura o ouvinte (addSnapshotListener) que foi passado. Qualquer mudança, o 'callback' é disparado.
                .addSnapshotListener(callback);
    }

    // Função para votar num lugar. Recebe o 'Lugar' que vai receber o voto e devolve uma tarefa vazia (Void, só para avisar se deu certo ou errado).
    public Task<Void> votar (Lugar item){
        // Pega a conexão com o banco.
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        // Acessa a coleção "lugares", procura especificamente pelo documento que tem o ID do nosso item (document(item.getId()))...
        return db.collection("lugares").document(item.getId())
                // ...e manda atualizar (update) apenas o campo "votos", incrementando (somando) 1 ao valor que já está lá na nuvem.
                .update("votos", FieldValue.increment(1));
    }

    // Função para excluir um lugar do banco.
    public Task<Void> excluir (Lugar item){
        // Pega a conexão com o banco.
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        // Acessa a coleção, acha o documento pelo ID e manda o comando de deletar (delete).
        return db.collection("lugares").document(item.getId())
                .delete();
    }

    // Função para desfazer uma exclusão. Restaura o item exatamente como estava.
    public Task<Void> restaurar(Lugar item){
        // Pega a conexão com o banco.
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        // Acessa a coleção "lugares", cria a referência usando o mesmo ID que o item já tinha...
        return db.collection("lugares").document(item.getId())
                // ...e define (set) todos os dados do objeto de volta no banco. Se não existir, ele recria.
                .set(item);
    }
}
