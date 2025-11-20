-- Criação da tabela de Usuários
CREATE TABLE usuarios (
    email VARCHAR(255) PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL
);

-- Criação da tabela de Tarefas
CREATE TABLE tarefas (
    titulo VARCHAR(255) PRIMARY KEY,
    descricao TEXT,
    tipo VARCHAR(50),
    criado_por VARCHAR(255),
    data_cadastro DATE,
    deadline DATE,
    data_conclusao DATE,
    prioridade INT,
    CONSTRAINT fk_tarefa_usuario FOREIGN KEY (criado_por) REFERENCES usuarios(email)
);

-- Criação da tabela de Eventos
CREATE TABLE eventos (
    titulo VARCHAR(255) PRIMARY KEY,
    descricao TEXT,
    tipo VARCHAR(50),
    criado_por VARCHAR(255),
    data_cadastro DATE,
    deadline DATE,
    CONSTRAINT fk_evento_usuario FOREIGN KEY (criado_por) REFERENCES usuarios(email)
);

-- Criação da tabela de Subtarefas
CREATE TABLE subtarefas (
    id BIGSERIAL PRIMARY KEY,
    titulo VARCHAR(255) NOT NULL,
    status BOOLEAN NOT NULL,
    tarefa_titulo VARCHAR(255),
    CONSTRAINT fk_subtarefa_tarefa FOREIGN KEY (tarefa_titulo) REFERENCES tarefas(titulo) ON DELETE CASCADE
);
