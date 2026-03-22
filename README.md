# Vexora API

Vexora é uma aplicação **Spring Boot 4** que implementa autenticação JWT, gerenciamento de usuários e segurança com **Spring Security**.
A Vexora API foi projetada para gerenciar comandas, produtos, estoque e relatórios para bares e restaurantes.

O projeto utiliza:

- **Java 17**
- **Spring Boot 4**
- **Spring Security**
- **Spring Data JPA**
- **PostgreSQL**
- **JWT (io.jsonwebtoken)**
- **Spring Actuator** (health checks)
- **Bucket4j** (rate limiting)

---

## 🚀 Como Rodar

### Pré-requisitos

- Java 17+
- PostgreSQL rodando na porta 5433 (ou configure no `application.yaml`)
- Maven

### 1. Configurar variáveis de ambiente

```bash
# Copie o arquivo de exemplo
cp .env.example .env

# Edite o .env com suas configurações (ou use os valores padrão para dev)
```

**Variáveis obrigatórias:**

| Variável | Descrição | Exemplo |
|----------|-----------|---------|
| `JWT_SECRET` | Chave para assinar tokens JWT (mín. 44 chars Base64 = 256 bits) | `SUA_CHAVE_BASE64_AQUI` |

> Gere uma chave segura com: `openssl rand -base64 32`

**Variáveis opcionais:**

| Variável | Descrição | Padrão |
|----------|-----------|--------|
| `JWT_EXPIRATION` | Tempo de expiração do token (ms) | `86400000` (24h) |
| `CORS_ORIGINS` | URLs permitidas para CORS, separadas por vírgula | `http://localhost:5173,http://localhost:3000` |

### 2. Carregar variáveis de ambiente

**Windows PowerShell:**
```powershell
Get-Content .env | ForEach-Object { 
    if ($_ -match '^([^#].+?)=(.*)$') { 
        [System.Environment]::SetEnvironmentVariable($matches[1], $matches[2], 'Process') 
    } 
}
```

**Linux/Mac/Git Bash:**
```bash
export $(grep -v '^#' .env | xargs)
```

### 3. Rodar o banco de dados com Docker

```bash
docker run -d --name vexora-postgres \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_PASSWORD=postgres \
  -e POSTGRES_DB=vexora \
  -p 5433:5432 postgres:16
```

Se o container já existir:
```bash
docker start vexora-postgres
```

> O banco estará disponível em `localhost:5433` com usuário `postgres` e senha `postgres`.

### 4. Rodar a aplicação

```bash
./mvnw spring-boot:run
```

### 5. Primeiro acesso

No primeiro boot, o sistema cria automaticamente um usuário admin com **senha aleatória segura**.
**Verifique o log** para ver a senha gerada:

```
═══════════════════════════════════════════════════════════════
✅ Usuário administrador padrão criado!
📧 Username: admin
🔑 Senha: [senha-gerada-aqui]
⚠️  IMPORTANTE: Altere esta senha após o primeiro login!
═══════════════════════════════════════════════════════════════
```

---

## 🔒 Segurança

- **Senhas fortes obrigatórias:** mínimo 8 caracteres, com maiúscula, minúscula, número e caractere especial (`@$!%*?&#`)
- **JWT sem valor padrão:** a variável `JWT_SECRET` é obrigatória para iniciar a aplicação
- **Rate limiting:** máximo de 10 req/min por IP em `/auth/login` e `/auth/signup`; 100 req/min para demais endpoints
- **Roles fixas no signup:** novos usuários são sempre criados como `FUNCIONARIO`; promoção de role é feita somente por `ADMIN`
- **CORS configurável** via variável de ambiente `CORS_ORIGINS`
- **Health checks** disponíveis em `/actuator/health` e `/actuator/info` (sem autenticação)

---

## 📋 Comandas

### Funcionalidades já implementadas

- **Abrir comanda** — cria uma nova comanda vinculada a uma mesa e, opcionalmente, a um cliente
- **Adicionar item à comanda** — adiciona produtos e registra automaticamente a **saída de estoque**
- **Remover item da comanda** — remove um item e registra a **entrada de estoque** correspondente
- **Calcular comanda** — calcula o valor total em tempo real, sem fechar a comanda
- **Fechar comanda** — finaliza a comanda, impedindo novas alterações
- **Buscar comanda por ID** — retorna os dados completos de uma comanda específica
- **Listar / filtrar comandas** — busca com filtros de status (aberta/fechada), período de datas e mesa

---

## 📦 Produtos

### Funcionalidades já implementadas

- **Adicionar produto** — cadastra produto com categoria, unidade de medida e controle de estoque
- **Editar produto** — atualiza informações (preço, estoque mínimo, etc.)
- **Remover produto** — exclui um produto do sistema
- **Buscar produto por ID** — retorna os detalhes de um produto específico
- **Listar produtos com paginação** — suporta `page`, `size`, `sortBy` e `sortDir`

---

## 📊 Relatórios

### Funcionalidades já implementadas

- **Faturamento diário / semanal / mensal**
- **Produto mais vendido** (dia, semana, mês)
- **Relatório de estoque** — estoque atual, produtos abaixo do mínimo e histórico de movimentações

---

## Configuração do `application.yaml`

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
  secret: ${JWT_SECRET}           # OBRIGATÓRIO — sem valor padrão
  expiration: ${JWT_EXPIRATION:86400000}

cors:
  allowed-origins: ${CORS_ORIGINS:http://localhost:5173,http://localhost:3000}
```

> O segredo JWT deve ser uma **chave Base64 com pelo menos 256 bits (44 caracteres)**. Não há valor padrão — a aplicação não iniciará sem esta variável configurada.

---

## Endpoints

### 1. Cadastro de usuário (`signup`)

Novos usuários são criados sempre com role `FUNCIONARIO`.

A senha deve ter **no mínimo 8 caracteres** e conter pelo menos: uma letra maiúscula, uma minúscula, um número e um caractere especial (`@$!%*?&#`).

```http
POST /auth/signup
Content-Type: application/json

{
  "username": "joao.silva",
  "password": "Senha@123"
}
```

**Exemplo com `curl`:**

```bash
curl -X POST http://localhost:8080/auth/signup \
  -H "Content-Type: application/json" \
  -d '{"username": "joao.silva", "password": "Senha@123"}'
```

> Retorna `201 Created` se o usuário for criado com sucesso.

---

### 2. Login (`login`)

```http
POST /auth/login
Content-Type: application/json

{
  "username": "joao.silva",
  "password": "Senha@123"
}
```

**Exemplo com `curl`:**

```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username": "joao.silva", "password": "Senha@123"}'
```

**Resposta:**

```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "role": "FUNCIONARIO",
  "username": "joao.silva"
}
```

> Este token deve ser usado para acessar endpoints protegidos.

---

### 3. Acessando endpoints protegidos

Para acessar qualquer endpoint protegido, adicione o token JWT no cabeçalho `Authorization`:

```bash
curl -X GET http://localhost:8080/api/produtos \
  -H "Authorization: Bearer SEU_TOKEN_AQUI"
```

---

### 4. Setup inicial de admin (apenas se não houver admin)

Este endpoint só funciona enquanto não existir nenhum `ADMIN` no sistema.

```http
POST /auth/setup-admin
Content-Type: application/json

{
  "username": "joao.silva"
}
```

> Após a promoção, este endpoint retorna `403 Forbidden` para todas as requisições seguintes.

---

## Estrutura do projeto

```
com.product.vexora
│
├── config
│   ├── CorsConfig.java
│   ├── DataInitializer.java
│   ├── JwtAuthenticationFilter.java
│   ├── RateLimitFilter.java
│   └── SecurityConfig.java
│
├── controller
│   ├── AuthController.java
│   ├── CategoriaController.java
│   ├── ComandaController.java
│   ├── MovimentacaoController.java
│   ├── ProdutoController.java
│   ├── RelatorioController.java
│   └── UserController.java
│
├── dto
│   ├── request/
│   │   ├── CreateCategoriaRequest.java
│   │   └── CreateUserRequest.java
│   ├── response/
│   │   ├── CategoriaResponse.java
│   │   ├── CurrentUserResponse.java
│   │   └── UserResponse.java
│   ├── ComandaItemDTO.java
│   ├── ComandaItemRequestDTO.java
│   ├── ComandaRequestDTO.java
│   ├── ComandaResponseDTO.java
│   ├── FaturamentoDTO.java
│   ├── LoginDto.java
│   ├── MovimentacaoDto.java
│   ├── ProdutoMaisVendidoDto.java
│   ├── ProdutoRequestDto.java
│   ├── ProdutoResponseDto.java
│   └── SignupDto.java
│
├── entity
│   ├── Categoria.java
│   ├── Comanda.java
│   ├── ComandaItem.java
│   ├── Movimentacao.java
│   ├── Pagamento.java
│   ├── Produto.java
│   └── User.java
│
├── enums
│   ├── MetodoPagamento.java
│   ├── Role.java
│   ├── TipoMovimentacao.java
│   └── UnidadeMedida.java
│
├── exception
│   ├── CategoriaJaExisteException.java
│   ├── CategoriaNaoEncontradaException.java
│   ├── ComandaAbertaException.java
│   ├── ComandaFechadaException.java
│   ├── ComandaNaoEncontradaException.java
│   ├── EstoqueInsuficienteException.java
│   ├── GlobalExceptionHandler.java
│   ├── InvalidPasswordException.java
│   ├── ItemNaoEncontradoException.java
│   ├── MesaObrigatoriaException.java
│   ├── PagamentoInvalidoException.java
│   ├── ProdutoNotFoundException.java
│   ├── UnauthorizedRoleException.java
│   ├── UserAlreadyExistsException.java
│   └── UserNotFoundException.java
│
├── repository
│   ├── CategoriaRepository.java
│   ├── ComandaItemRepository.java
│   ├── ComandaRepository.java
│   ├── MovimentacaoRepository.java
│   ├── ProdutoRepository.java
│   └── UserRepository.java
│
├── service
│   ├── AuthService (interface e impl)
│   ├── CategoriaService (interface e impl)
│   ├── ComandaService (interface e impl)
│   ├── JwtService (interface e impl)
│   ├── MovimentacaoService (interface e impl)
│   ├── ProdutoService (interface e impl)
│   ├── RelatorioService (interface e impl)
│   └── UserService (interface e impl)
│
└── VexoraApplication.java
```

---

## Observações

- **Chave JWT:** variável de ambiente `JWT_SECRET` é **obrigatória**; use Base64 com no mínimo 256 bits (44 caracteres).
- **Admin:** na primeira execução, a senha do admin é gerada aleatoriamente e exibida no log.
- **Rate limiting:** endpoints de autenticação estão limitados a 10 req/min por IP.
- **Testes:** utilize `curl`, Postman ou a coleção Bruno em `bruno-collection/`.
- **Banco:** o Docker garante que o PostgreSQL esteja isolado e fácil de resetar.

---