# Explicação Milimétrica: Rango na Régua 🍔📏

Bem-vindo ao manual completo do **Rango na Régua**, um aplicativo Android feito em Java para listar, avaliar e cadastrar lugares para comer. Este guia explica exatamente como o projeto funciona, peça por peça.

---

## 1. O que é o projeto?
O aplicativo permite que usuários vejam uma lista de restaurantes, adicionem novos restaurantes, cliquem neles para ver detalhes, arrastem para o lado para apagar e cliquem num botão para dar votos ao lugar favorito. Ele é salvo **na nuvem** em tempo real usando o banco de dados do Google, o **Firebase Firestore**.

---

## 2. Como as pastas (pacotes) estão divididas?
O código está organizado em pastinhas para não virar uma bagunça:
* **`model` (O Molde):** Guarda a classe `Lugar.java`. É a "planta baixa" que diz quais informações um restaurante tem (nome, categoria, preço, votos, id).
* **`data` (Os Dados):** Guarda a comunicação com o banco de dados. Tem o `Catalogo.java` (uma lista falsa para testes) e o `LugarRepository.java` (o encanamento real com a nuvem do Google).
* **`adapter` (O Desenhista):** Guarda o `LugarAdapter.java`. É ele que pega os dados brutos e "desenha" cada retângulo de restaurante na lista da tela.
* **`raiz` (Telas / Activities):** Os arquivos soltos (MainActivity, NovoLugarActivity, DetalheActivity) são as telas que o usuário de fato vê e interage.

---

## 3. O Fluxo: Como as coisas acontecem?

### A. Abrindo o App (A Tela Inicial)
1. O Android abre a `MainActivity.java`.
2. A primeira coisa que a MainActivity faz é criar uma conexão com a nuvem chamando o `LugarRepository`.
3. Ela prepara um componente visual chamado `RecyclerView` (que é uma lista que rola infinitamente sem travar o celular) e contrata o `LugarAdapter` para preencher essa lista.
4. O `onResume()` é disparado (evento de quando a tela fica visível). Ele liga o "rádio" que escuta a nuvem. Assim que a nuvem responde, a lista é preenchida e o `LugarAdapter` desenha tudo na tela.

### B. Adicionando um Novo Restaurante
1. Na `MainActivity`, o usuário clica no botão redondo com um "+".
2. Isso dispara um `Intent` (intenção) que abre a `NovoLugarActivity.java`.
3. Lá, o usuário preenche os campos (nome, preço, etc).
4. Ao clicar em "Salvar", o app valida se os campos estão preenchidos.
5. Se estiver tudo OK, ele cria um novo objeto `Lugar` usando os textos digitados.
6. Ele chama o `LugarRepository.inserir()` passando esse objeto.
7. O repositório manda para o Firebase. Quando o Firebase responde "salvei!", a tela é fechada.
8. Como a `MainActivity` volta a ficar visível, ela detecta a mudança na nuvem e o novo restaurante já aparece na lista!

### C. Apagando e Votando
* **Apagar:** O usuário arrasta o item para o lado. O `ItemTouchHelper` da `MainActivity` detecta o gesto, descobre qual foi o restaurante arrastado e manda o `LugarRepository.excluir()`. Uma barrinha preta (Snackbar) aparece dando a opção de `restaurar()` se o usuário clicou sem querer.
* **Votar:** O usuário clica no botão "Votar" na lista. O `LugarAdapter` capta o clique e grita pra `MainActivity`: "Votaram nesse aqui!". A `MainActivity` manda o `LugarRepository.votar()`, que pede pro Firebase somar +1 nos votos daquele ID específico.

---

