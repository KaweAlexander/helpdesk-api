# HelpDesk API

API REST de help desk (central de chamados): clientes abrem chamados, técnicos os atendem e ambos trocam mensagens dentro de cada chamado.

> Projeto em desenvolvimento.

## Tecnologias

- Java 21
- Spring Boot 4.1 (Web MVC, Data JPA, Validation)
- PostgreSQL
- Maven

## Como rodar

**Pré-requisitos:** Java 21, Maven (ou o `mvnw`) e PostgreSQL.

1. Crie o banco de dados:
   ```sql
   CREATE DATABASE helpdesk;
   ```
2. Defina as variáveis de ambiente:
   ```bash
   export DB_PASSWORD=sua_senha
   export DB_USERNAME=postgres   # opcional, o padrão é "postgres"
   ```
   No Windows (PowerShell): `$env:DB_PASSWORD="sua_senha"`.
3. Inicie a aplicação:
   ```bash
   ./mvnw spring-boot:run
   ```
   Ou, se tiver o Maven instalado: `mvn spring-boot:run`.

A API sobe em `http://localhost:8080`. As tabelas são criadas automaticamente (`ddl-auto=update`).

## Estrutura do projeto

```
src/main/java/com/kawedev/HelpDesk_API/
├── controller/    → Endpoints REST
├── service/       → Regras de negócio
├── repository/    → Acesso ao banco (Spring Data JPA)
├── model/         → Entidades JPA e enums (Perfil, Status)
└── exceptions/    → Tratamento global de erros
```

## Modelo de dados

- **Usuario:** nome, e-mail, senha e perfil (`CLIENTE`, `TECNICO` ou `ADMIN`).
- **Chamado:** título, descrição, data de criação, status (`ABERTO`, `EM_ATENDIMENTO` ou `FINALIZADO`), cliente que abriu e técnico responsável.
- **Mensagem:** conteúdo, data de envio, autor e o chamado ao qual pertence.

```
Usuario (cliente) 1──N Chamado N──1 Usuario (técnico)
Chamado 1──N Mensagem N──1 Usuario (autor)
```

## Endpoints disponíveis

| Método | Rota | Descrição |
|---|---|---|
| POST | `/api/usuarios/registrar` | Cadastra um usuário (`nome`, `email`, `senha`) |
| PATCH | `/api/usuarios/{id}/perfil?perfil=TECNICO` | Altera o perfil de um usuário |
| DELETE | `/api/usuarios/{id}` | Remove um usuário |
| POST | `/api/chamados?clienteId={id}` | Abre um chamado (`titulo`, `descricao`) |

Exemplo:

```http
POST /api/chamados?clienteId=1
Content-Type: application/json

{ "titulo": "Não consigo acessar o sistema", "descricao": "Aparece erro 500 ao entrar." }
```

## Em desenvolvimento

A camada de serviço já tem as regras abaixo, mas ainda faltam os endpoints que as expõem:

- Buscar chamado por id.
- Assumir um chamado (técnico) e finalizá-lo.
- Listar chamados por cliente, por técnico e os que estão abertos.
- Enviar e listar mensagens de um chamado.

## Melhorias planejadas

- Autenticação e autorização (por exemplo Spring Security + JWT). Hoje a API não tem login.
- Criptografar a senha dos usuários (BCrypt). Hoje ela é salva como texto.
- Não devolver a senha nas respostas da API (usar DTOs).
- Testes automatizados dos endpoints.
