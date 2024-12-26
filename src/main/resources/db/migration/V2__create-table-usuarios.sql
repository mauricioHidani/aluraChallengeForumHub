CREATE TABLE usuarios(
    id BIGINT NOT NULL AUTO_INCREMENT,
    nome VARCHAR(128),
    email VARCHAR(255),
    senha VARCHAR(255),

    PRIMARY KEY(id)
);
