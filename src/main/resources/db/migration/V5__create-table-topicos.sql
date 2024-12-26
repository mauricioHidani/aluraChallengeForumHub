CREATE TABLE topicos(
    id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    titulo VARCHAR(128) NOT NULL,
    mensagem VARCHAR(16000),
    data_criacao TIMESTAMP,
    status VARCHAR(128),
    autor_id BIGINT NOT NULL,
    curso_id BIGINT NOT NULL,

    CONSTRAINT fk_topicos_autor FOREIGN KEY (autor_id) REFERENCES usuarios(id),
    CONSTRAINT fk_topicos_curso FOREIGN KEY (curso_id) REFERENCES cursos(id)
);
