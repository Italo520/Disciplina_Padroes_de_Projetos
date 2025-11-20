# Projeto Lista de Tarefas e Eventos em Java

## 📜 Descrição do Projeto

Este é um sistema de gerenciamento de tarefas e eventos desenvolvido em Java, utilizando a biblioteca Swing para a interface gráfica de usuário (GUI) e Maven para o gerenciamento de dependências. O sistema permite que múltiplos usuários se cadastrem, façam login e gerenciem suas próprias listas de tarefas e eventos. Os dados são persistidos em arquivos JSON.

O projeto foi estruturado seguindo princípios de orientação a objetos e padrões de projeto como MVC (Model-View-Controller), Repository, Service Layer, Singleton, Strategy, Observer e Factory.

## ✨ Funcionalidades
* **Gestão de Usuários**: Cadastro e autenticação de usuários com senha segura utilizando BCrypt.
* **Gerenciamento de Tarefas**: Criação, edição e exclusão de tarefas, com título, descrição, prioridade e prazo. O progresso é calculado automaticamente com base nas subtarefas.
* **Gerenciamento de Subtarefas**: Adição de sub-itens para melhor organização de tarefas complexas.
* **Gerenciamento de Eventos**: Cadastro de eventos com data marcada e acompanhamento de contagem regressiva.
* **Persistência de Dados**: Armazenamento em arquivos JSON (`dados_globais.json`, `usuarios.json`) utilizando a biblioteca Jackson.
* **Relatórios**: Geração de relatórios de tarefas em PDF e Excel.
* **Notificações**: Envio de relatórios diários por e-mail.

## 📂 Estrutura do Projeto

O projeto segue uma arquitetura em camadas bem definida:

* **`controller`**: Contém os controladores (`AppController`, `AuthController`, `TaskController`, `EventController`) que intermediam a comunicação entre a interface gráfica e a lógica de negócios.
* **`entity`**: Contém as classes de domínio (`Tarefa`, `Evento`, `Usuario`, `Subtarefa`, etc.).
* **`repository`**: Responsável pela persistência de dados (`IUserRepository`, `ITarefaRepository`, etc.), implementando o padrão Repository.
* **`service`**: Contém a lógica de negócios (`UserServiceImpl`, `TaskServiceImpl`, etc.), definida por interfaces.
* **`ui`**: Contém as classes da interface gráfica (Swing), organizadas em pacotes (`telaPrincipal`, `telasusuario`, `TelasDialogo`).
* **`util`**: Classes utilitárias para envio de e-mail (`Mensageiro`) e geração de arquivos (`Central`).

## 🛠️ Tecnologias e Dependências

* **Java 17+**
* **Maven**: Gerenciamento de dependências e build.
* **Swing**: Interface Gráfica.
* **FlatLaf**: Temas modernos para Swing.
* **Jackson**: Serialização e desserialização de JSON.
* **JBCrypt**: Hashing de senhas.
* **iText**: Geração de PDFs.
* **Apache POI**: Geração de planilhas Excel.
* **Jakarta Mail**: Envio de e-mails.

## ▶️ Como Executar o Projeto

1.  Clone o repositório.
2.  Certifique-se de ter o Maven e o JDK instalados.
3.  Na raiz do projeto, execute:
    ```bash
    mvn clean install
    mvn exec:java -Dexec.mainClass="br.com.todolist.Main"
    ```

## 📚 Documentação do Código

Todo o código fonte foi documentado utilizando Javadoc. As classes principais possuem descrições detalhadas de suas responsabilidades, métodos e parâmetros.

### Padrões de Projeto Utilizados

*   **Singleton**: `AppController`, `SessionManager`.
*   **Repository**: Camada de persistência (`UserRepositoryImpl`, etc.).
*   **Strategy**: Cálculo de progresso de tarefas (`IProgressCalculationStrategy`).
*   **Observer**: Notificação de mudanças em tarefas e eventos.
*   **Factory**: Criação de itens (`IItemFactory`).
*   **Facade**: `AppController` simplifica o acesso aos serviços.

---
Desenvolvido para fins acadêmicos - Disciplina de POO.
