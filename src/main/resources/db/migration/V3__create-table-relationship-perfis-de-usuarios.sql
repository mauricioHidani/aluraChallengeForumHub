CREATE TABLE perfis_de_usuarios(
    usuario_id BIGINT NOT NULL,
    perfil_id BIGINT NOT NULL,

    PRIMARY KEY(usuario_id, perfil_id),
    CONSTRAINT fk_usuario_id FOREIGN KEY(usuario_id) REFERENCES usuarios(id),
    CONSTRAINT fk_perfil_id FOREIGN KEY(perfil_id) REFERENCES perfis(id)
);

