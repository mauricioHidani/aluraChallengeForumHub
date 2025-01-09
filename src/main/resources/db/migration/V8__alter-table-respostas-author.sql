ALTER TABLE respostas DROP FOREIGN KEY fk_respostas_autor;
ALTER TABLE respostas MODIFY autor_id BIGINT NULL;
ALTER TABLE respostas ADD CONSTRAINT fk_respostas_autor FOREIGN KEY (autor_id) REFERENCES usuarios(id);
