CREATE TABLE respostas(
    id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    data_criacao TIMESTAMP NOT NULL,
    solucao TEXT,
    topico_id BIGINT NOT NULL,
    autor_id BIGINT NOT NULL,

    CONSTRAINT fk_respostas_topico FOREIGN KEY (topico_id) REFERENCES topicos(id),
    CONSTRAINT fk_respostas_autor FOREIGN KEY (autor_id) REFERENCES usuarios(id)
);
