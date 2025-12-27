# Vexora API

Vexora é uma aplicação **Spring Boot 4** que implementa autenticação JWT, gerenciamento de usuários e segurança básica com **Spring Security**.
A Vexora API foi projetada para gerenciar comandas, produtos, estoque e relatórios.

O projeto utiliza:

- **Java 17**
- **Spring Boot 4**
- **Spring Security**
- **Spring Data JPA**
- **PostgreSQL**
- **JWT (io.jsonwebtoken)**


## 📋 Comandas

### Funcionalidades já implementadas

- **Abrir comanda**  
  Cria uma nova comanda vinculada a uma mesa e, opcionalmente, a um cliente.

- **Adicionar item à comanda**  
  Adiciona produtos à comanda aberta e registra automaticamente a **movimentação de saída** no estoque.

- **Remover item da comanda**  
  Remove um item previamente adicionado e registra a **entrada de estoque** correspondente (cancelamento).

- **Calcular comanda**  
  Calcula o valor total da comanda em tempo real, sem necessidade de fechá-la.

- **Fechar comanda**  
  Finaliza a comanda, impedindo novas alterações.

- **Buscar comanda por ID**  
  Retorna os dados completos de uma comanda específica.

- **Listar / filtrar comandas**  
  Permite buscar todas as comandas com filtros como:
    - comandas abertas / fechadas
    - período de datas
    - mesa

---

## 📦 Produtos

### Funcionalidades já implementadas

- **Adicionar produto**  
  Cadastra um novo produto com categoria, unidade de medida e controle de estoque.

- **Editar produto**  
  Atualiza informações do produto (preço, estoque mínimo, etc).

- **Remover produto**  
  Exclui um produto do sistema.

- **Buscar produto por ID**  
  Retorna os detalhes de um produto específico.

- **Listar todos os produtos**  
  Retorna todos os produtos cadastrados.

---

## 📊 Relatórios

### Funcionalidades já implementadas

- **Faturamento semanal**  
  Retorna o total faturado em uma semana específica.

- **Faturamento mensal**  
  Retorna o total faturado no mês.

- **Produto mais vendido na semana**  
  Identifica o produto com maior volume de vendas na semana.

- **Produto mais vendido no mês**  
  Identifica o produto com maior volume de vendas no mês.

### Funcionalidades a desenvolver

- **Faturamento diário**  
  Retorna o total faturado em um dia específico.

- **Produto mais vendido no dia**  
  Identifica o produto com maior saída diária.

- **Relatório de estoque**  
  Relatório contendo:
    - estoque atual
    - produtos abaixo do estoque mínimo
    - histórico de movimentações


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
├── config
│   ├── JwtAuthenticationFilter.java
│   └── SecurityConfig.java
│
├── controller
│   ├── AuthController.java
│   ├── ComandaController.java
│   ├── MovimentacaoController.java
│   ├── ProdutoController.java
│   └── RelatorioController.java
│
├── dto
│   ├── ComandaItemDTO.java
│   ├── ComandaItemRequestDTO.java
│   ├── ComandaRequestDTO.java
│   ├── ComandaResponseDTO.java
│   ├── FaturamentoDTO.java
│   ├── LoginDTO.java
│   ├── MovimentacaoDTO.java
│   ├── ProdutoMaisVendidoDTO.java
│   ├── ProdutoRequestDTO.java
│   ├── ProdutoResponseDTO.java
│   └── SignupDTO.java
│
├── entity
│   ├── Comanda.java
│   ├── ComandaItem.java
│   ├── Funcionario.java
│   ├── Movimentacao.java
│   ├── Produto.java
│   └── User.java
│
├── enums
│   ├── CategoriaProduto
│   ├── Role
│   ├── TipoMovimentacao
│   └── UnidadeMedida
│
├── exception
│   ├── InvalidPasswordException
│   ├── UserAlreadyExistsException
│   └── UserNotFoundException
│
├── repository
│   ├── ComandaItemRepository
│   ├── ComandaRepository
│   ├── MovimentacaoRepository
│   ├── ProdutoRepository
│   └── UserRepository
│
├── service
│   ├── AuthService (interface e impl)
│   ├── ComandaService (interface e impl)
│   ├── JwtService (interface e impl)
│   ├── MovimentacaoService (interface e impl)
│   ├── ProdutoService (interface e impl)
│   ├── RelatorioService (interface e impl)
│   └── UserService (interface e impl)
└──── 




```

---

## Observações

- **Chave JWT:** Certifique-se de usar uma chave base64 com pelo menos **256 bits**.
- **Testes:** Utilize `curl` ou Postman para testar os endpoints.
- **Banco:** O Docker garante que o PostgreSQL esteja isolado e fácil de resetar.

---