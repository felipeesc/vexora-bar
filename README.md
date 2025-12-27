# Vexora API

Vexora é uma aplicação **Spring Boot 4** que implementa autenticação JWT, gerenciamento de usuários e segurança básica com **Spring Security**.

O projeto utiliza:

- **Java 17**
- **Spring Boot 4**
- **Spring Security**
- **Spring Data JPA**
- **PostgreSQL**
- **JWT (io.jsonwebtoken)**

---

## Rodando o banco de dados com Docker

```bash
  docker run -d --name vexora-postgres -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=postgres -e POSTGRES_DB=vexora -p 5433:5432 postgres:16

```
## Caso o container exista basta rodar 

```bash
 docker start vexora-postgres
```


> O banco estará disponível em `localhost:5433` com usuário `postgres` e senha `postgres`.

---

## Configuração do `application.yml`

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5433/vexora
    username: postgres
    password: postgres
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true

jwt:
  secret: "SEU_TOKEN_BASE64_AQUI"
  expiration: 86400000 
```

> O segredo JWT deve ser uma **chave base64 com 256 bits ou mais**, gerada com o método `Keys.secretKeyFor(SignatureAlgorithm.HS256)`.

---

## Endpoints

### 1. Cadastro de usuário (`signup`)

```http
POST /auth/signup
Content-Type: application/json

{
  "username": "teste",
  "password": "123456",
  "role": "ROLE_USER"
}
```

**Exemplo com `curl`:**

```bash
  curl -X POST http://localhost:8080/auth/signup \
-H "Content-Type: application/json" \
-d '{
  "username": "teste",
  "password": "123456",
  "role": "ROLE_USER"
}'
```

> Retorna `201 Created` se o usuário for criado com sucesso.

---

### 2. Login (`login`)

```http
POST /auth/login
Content-Type: application/json

{
  "username": "teste",
  "password": "123456"
}
```

**Exemplo com `curl`:**

```bash
  curl -X POST http://localhost:8080/auth/login \
-H "Content-Type: application/json" \
-d '{
  "username": "teste",
  "password": "123456"
}'
```

**Resposta:**

```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9..."
}
```

> Este token deve ser usado para acessar endpoints protegidos.

---

### 3. Acessando endpoints protegidos

Para acessar qualquer endpoint protegido, adicione o token JWT no cabeçalho `Authorization`:

```bash
  curl -X GET http://localhost:8080/api/xxx \
-H "Authorization: Bearer SEU_TOKEN_AQUI"
```

---

## Estrutura do projeto

```
com.product.vexora
│
├── entity
│   └── User.java
│
├── repository
│   └── UserRepository.java
│
├── service
│   ├── AuthService.java (interface e impl)
│   ├── JwtService.java (interface e impl)
│   └── UserService.java (interface e impl)
│
├── dto
│   ├── LoginDto.java
│   └── SignupDto.java
│
├── exception
│   ├── InvalidPasswordException.java
│   ├── UserAlreadyExistsException.java
│   └── UserNotFoundException.java
│
└── security
    ├── JwtAuthenticationFilter.java
    └── SecurityConfig.java
```

---

## Observações

- **Chave JWT:** Certifique-se de usar uma chave base64 com pelo menos **256 bits**.
- **Testes:** Utilize `curl` ou Postman para testar os endpoints.
- **Banco:** O Docker garante que o PostgreSQL esteja isolado e fácil de resetar.

---