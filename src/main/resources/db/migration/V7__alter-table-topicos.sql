ALTER TABLE topicos DROP FOREIGN KEY fk_topicos_autor;
ALTER TABLE topicos MODIFY autor_id BIGINT NULL;
ALTER TABLE topicos ADD CONSTRAINT fk_topicos_autor FOREIGN KEY (autor_id) REFERENCES usuarios(id);
