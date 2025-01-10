# 📖Forum Hub ![Static Badge](https://img.shields.io/badge/CONCLU%C3%8DDO-%236DB33F?style=flat-square&color=%236DB33F)
🌎[@Alura](https://www.alura.com.br/) [@Oracle](https://www.oracle.com/br/)<br>
🤙Alura ONE G7 Challenges<br>
🚩`Especialização Back-End/Java e Spring Framework`<br>
📅Wednesday, 18th December 2024 -📍São Paulo, Brazil<br>

![Static Badge](https://img.shields.io/badge/Spring-6DB33F?style=for-the-badge&logo=Spring&label=3.4.1&labelColor=000000)
![Static Badge](https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mysql&label=8.1.0&labelColor=1d1d1d)
![Static Badge](https://img.shields.io/badge/Flyway-CC0200?style=for-the-badge&logo=flyway&label=10.20.1&labelColor=1d1d1d)
![Static Badge](https://img.shields.io/badge/Spring%20Security-6DB33F?style=for-the-badge&logo=springsecurity&label=3.4.1&labelColor=000000)
![Static Badge](https://img.shields.io/badge/Auth0-EB5424?style=for-the-badge&logo=auth0&label=4.2.1&labelColor=1d1d1d)
![Static Badge](https://img.shields.io/badge/OpenAPI%20Initiative-6BA539?style=for-the-badge&logo=openapiinitiative&label=2.8.1&labelColor=1d1d1d)

![aluraForumHub](https://github.com/user-attachments/assets/53f16d70-1e87-4996-b8f9-14097d363a60)

## Descrição do desafio
Esté é um desafio proposto pelo programa [ONE](https://www.oracle.com/br/education/oracle-next-education/) para desenvolver uma aplicação _backend_ que simule um Forúm, com seus Tópicos que devem receber respostas dos usuário para solucioná-lo, cada tópico tratá-se de uma problemática de algum curso que um usuário abril. O foco da criação do tópico é conseguir respostas de outros usuário que possam ajudar.

Além de que, para modificações ou acesso dos recursos como exclução, atualização e alteração de status do tópico, o usuário logado deve ser o dono/criador do mesmo, ou caso seja um usuário Administrador. Também ocorre nas respostas que podem ser excluídas apenas por seus criadores.

Cursos devem ser registrados no sistema apenas por Administradores, assim como as demais operações, com a excessão da consulta de cursos.

Abaixo é possivel ver a lista das operações:

- Tópicos:
  - Registrar
  - Listagem paginada 
  - Listagem por nome de curso
  - Listagem pelos 10 primeiros por data de criação
  - Listagem por ano de criação
  - Encontrar por identificação
  - Atualizar por identificação
  - Fechar um tópico
  - Deletar por identificação
- Cursos:
  - Registrar 
  - Atualizar por identificação
  - Listagem paginada
  - Encontrar por identificação
  - Deletar por identificação
- Respostas:
  - Registrar
  - Encontrar por identificação
  - Deletar por identificação
- Usuários:
  - Registrar
  - Encontrar por nome de usuário
  - Atualizar por identificação
  - Deletar por identificação
- Perfis de usuário:
  - Registrar
  - Atualizar por identificação
  - Deletar por identificação
  - Encontrar por identificação
  - Encontrar por nome
  - Listagem paginada
- Autenticação
  - Login de usuário

## Utilização
Para conseguir utilizar o desafio ForumHub, é necessário possuir o banco de dados `MySQL` ativo para que a aplicação consiga se conectar e realizar as operações.

A aplicação possui o arquivo `docker-compose.yml` que irá servir para conseguir subir containers do `SGBD MySQL`, e para conseguir fazer esse processo, basta utilizar o comando no terminal, estando no diretório do desafio:
```bash
docker-compose up -d
```

Analise se os containers estão rodando no Docker, existirá 2 containers do `MySQL`, um servirá para o _Perfil de Aplicação_ de `Test/test` e o outro para `Production/prod`.

Para alterar o _Perfil de Aplicação_ de `Test` para `Production` ou vice-versa, basta ir no arquivo `application.properties` e modificar o campo `spring.profiles.active` de `test` para `prod` que irá carregar o arquivo correspondente:

- `test`: arquivo -> `application-test.properties`
- `prod`: arquivo -> `application-prod.properties`

### Rotas e Documentação
Para conhecer as rotas da aplicação `ForumHub`.

- _Documentação da API_ 👉 `http://{host}:{port}/swagger-ui/index.html`
- Tópicos 👉 `http://{host}:{port}/topicos/**`
- Respostas 👉 `http://{host}:{port}/respostas/**`
- Cursos 👉 `http://{host}:{port}/cursos/**`
- Usuários 👉 `http://{host}:{port}/usuarios/**`
- Perfis de usuário 👉 `http://{host}:{port}/perfis/**`
- Autenticação de usuário 👉 `http://{host}:{port}/login`

## Mapeamento Diagrama Entidade Relacionamento
```mermaid
erDiagram
    Topico ||--o{ Resposta : ""
    Topico }o--|| Usuario: ""
    Topico }o--|| Curso: ""
    Usuario }o--|{ Perfil: "perfis_de_usuario"
    Resposta }o--|| Usuario: ""
    
    Topico {
        Long id PK
        String titulo
        String mensagem
        LocalDateTime dataCriacao
        String status
        Long autor_id FK
    }
    
    Resposta {
        Long id PK
        String mensagem
        LocalDateTime dataCriacao
        String solucao
        Long topic_id FK
        Long autor_id FK
    }
    
    Curso {
        Long id PK
        String nome
        String categoria
    }
    
    Usuario {
        Long id FK
        String nome
        String email
        String senha
    }
    
    Perfil {
        Long id FK
        String nome
    }
```

## Observação
* Nos testes, realizei apenas para os Tópicos, sendo eles o básico para um mapeamento CRUD (Create, Read, Update, Delete).
