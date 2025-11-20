# Relatório de Refatoração da Camada de Persistência

## Objetivo
Substituir a persistência baseada em arquivos JSON por um banco de dados relacional PostgreSQL, mantendo a integridade arquitetural, princípios SOLID e Clean Code.

## Alterações Realizadas

### 1. Introdução de Dependências
Foram adicionadas as dependências do driver JDBC do PostgreSQL e do Hibernate Core (JPA) no `pom.xml`.

### 2. Mapeamento Objeto-Relacional (ORM)
As entidades de domínio foram anotadas com anotações JPA (`@Entity`, `@Table`, `@Id`, `@OneToMany`, `@ManyToOne`, etc.) para permitir o mapeamento automático para tabelas do banco de dados.
- **Usuario**: Mapeado para a tabela `usuarios`. Chave primária: `email`.
- **Itens**: Mapeado como `@MappedSuperclass` para compartilhar atributos comuns.
- **Tarefa**: Mapeado para a tabela `tarefas`. Chave primária: `titulo`.
- **Evento**: Mapeado para a tabela `eventos`. Chave primária: `titulo`.
- **Subtarefa**: Promovida a entidade JPA, mapeada para a tabela `subtarefas` com chave primária autogerada (`id`) e relacionamento `@ManyToOne` com `Tarefa`.

### 3. Conexão com o Banco de Dados
Criada a classe `DatabaseConnection` seguindo o padrão **Singleton**. Isso garante uma única instância da `EntityManagerFactory` durante o ciclo de vida da aplicação, otimizando recursos.

### 4. Implementação dos Repositórios (Repository Pattern)
Novas classes foram criadas implementando as interfaces existentes (`IUserRepository`, `ITarefaRepository`, `IEventoRepository`):
- `UserRepositoryPostgres`
- `TarefaRepositoryPostgres`
- `EventoRepositoryPostgres`

Essas implementações utilizam o `EntityManager` para realizar operações CRUD, substituindo a manipulação de arquivos JSON. O princípio **Open/Closed (OCP)** foi respeitado, pois as novas funcionalidades foram adicionadas criando novas classes, sem modificar a lógica dos repositórios antigos (que poderiam ser mantidos se necessário).

### 5. Injeção de Dependência (DIP)
A classe `Main` e o `AppController` foram ajustados para instanciar as implementações PostgreSQL (`...Postgres`) em vez das implementações JSON (`...Impl`). Isso demonstra o Princípio da Inversão de Dependência, onde os módulos de alto nível dependem de abstrações (interfaces), permitindo a troca da implementação de baixo nível (persistência) com facilidade.

### 6. Script SQL
Um script `init.sql` foi gerado com o DDL necessário para criar as tabelas e restrições (chaves primárias e estrangeiras), garantindo a integridade referencial, especialmente entre `tarefas` e `subtarefas` (ON DELETE CASCADE).

## Justificativas Arquiteturais

- **JPA/Hibernate**: Escolhido pela robustez, facilidade de mapeamento e independência de banco de dados.
- **Singleton**: Adequado para gerenciar objetos pesados e únicos como a fábrica de conexões do banco.
- **Clean Code**: O código dos repositórios é focado apenas na persistência, delegando regras de negócio para os serviços (que não foram alterados).
