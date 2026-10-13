# Estágios CPS — Controle de Estágios

Sistema web para cadastro e acompanhamento de contratos de estágio de alunos do Centro Paula Souza. Projeto da disciplina de Laboratório de Engenharia de Software (FATEC).

## Funcionalidades

- **Cursos**: nome e coordenador.
- **Escolas**: última instituição de ensino do aluno (nome e endereço).
- **Empresas**: dados da empresa concedente e do contato responsável.
- **Alunos**: dados pessoais, curso, escola de origem e tipo de ensino cursado.
- **Contratos (estágios)**: vínculo entre aluno e empresa, com período, carga horária, área, supervisor e status.

Cada cadastro tem listagem com busca, formulário de inclusão/edição e exclusão.

### Regras de negócio

- RA do aluno, nome do curso, nome da escola e razão social da empresa não podem se repetir.
- Todo contrato é criado com status **Em análise**; o status só pode ser alterado na edição.
- Um aluno só pode ter um contrato **Em análise** ou **Ativo** por vez.
- A data de término do contrato deve ser posterior à data de início.
- Aluno e empresa de um contrato não podem ser alterados depois de criado (se estiver errado, exclua e cadastre outro).
- Não é possível excluir um aluno ou empresa que possua contratos, nem um curso ou escola que possua alunos vinculados.

## Tecnologias

- Java 21
- Spring Boot 4 (Web MVC, Data JPA, Validation)
- Thymeleaf + Bootstrap 5 + Font Awesome
- MySQL / MariaDB
- Lombok e MapStruct
- Springdoc OpenAPI (Swagger UI)

## Pré-requisitos

- JDK 21
- MySQL 8+ ou MariaDB rodando em `localhost:3306`

Não é necessário instalar o Maven: o projeto usa o Maven Wrapper (`mvnw`).

## Configuração do banco

As configurações ficam em `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/form_estagio?createDatabaseIfNotExist=true
spring.datasource.username=SEU_USUARIO
spring.datasource.password=SUA_SENHA
```

Ajuste o usuário e a senha para os do seu banco local. O banco `form_estagio` é criado automaticamente na primeira execução e as tabelas são geradas/atualizadas pelo Hibernate (`ddl-auto=update`).

O usuário precisa ter permissão para criar o banco, ou o banco deve ser criado antes:

```sql
CREATE DATABASE form_estagio;
```

## Como executar

Linux / macOS:

```bash
./mvnw spring-boot:run
```

Windows:

```bat
mvnw.cmd spring-boot:run
```

Depois acesse <http://localhost:8080>.

Para gerar o `.jar`:

```bash
./mvnw clean package
java -jar target/form-estagio-0.0.1-SNAPSHOT.jar
```

## Testes

```bash
./mvnw test
```

O teste atual sobe o contexto completo da aplicação, então o banco precisa estar acessível.

## Rotas principais

| Página | URL |
|---|---|
| Início | `/` |
| Alunos | `/aluno` |
| Cursos | `/curso` |
| Escolas | `/escola` |
| Empresas | `/empresa` |
| Contratos | `/contrato` |
| Swagger UI | `/swagger-ui/index.html` |

## Estrutura do projeto

```
src/main/java/br/edu/fatec/form_estagio/
├── controller/   # Controllers MVC (rotas e páginas)
├── dto/          # Records de cadastro, atualização e listagem
├── enums/        # StatusContrato, TipoEnsino
├── mapper/       # Conversões DTO <-> entidade (MapStruct)
├── model/        # Entidades JPA
├── repository/   # Repositórios Spring Data
└── service/      # Regras de negócio
src/main/resources/
├── templates/    # Páginas Thymeleaf (uma pasta por cadastro + fragmentos)
└── static/       # CSS e imagens
```
