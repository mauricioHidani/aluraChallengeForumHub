CREATE TABLE respostas_do_topico(
    topico_id BIGINT NOT NULL,
    resposta_id BIGINT NOT NULL,

    PRIMARY KEY (topico_id, resposta_id),
    CONSTRAINT fk_respostas_do_topico_topico FOREIGN KEY (topico_id) REFERENCES topicos(id),
    CONSTRAINT fk_respostas_do_topico_resposta FOREIGN KEY (resposta_id) REFERENCES respostas(id)
);
