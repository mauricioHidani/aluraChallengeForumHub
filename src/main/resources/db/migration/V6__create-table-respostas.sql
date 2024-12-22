CREATE TABLE respostas(
    id BINARY(16) NOT NULL PRIMARY KEY,
    data_criacao TIMESTAMP NOT NULL,
    solucao TEXT,
    topico_id BINARY(16) NOT NULL,
    autor_id BIGINT NOT NULL,

    CONSTRAINT fk_respostas_topico FOREIGN KEY (topico_id) REFERENCES topicos(id),
    CONSTRAINT fk_respostas_autor FOREIGN KEY (autor_id) REFERENCES usuarios(id)
);
