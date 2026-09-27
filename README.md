# 🚗 BlindSpot API

API RESTful desenvolvida para o Challenge de **Arquitetura Orientada a Serviços (SOA)**, com foco em consulta de dados técnicos de veículos, autenticação JWT e separação em camadas.

<details open>
  <summary><strong>📑 Sumário</strong></summary>
  <ol>
    <li><a href="#informacoes">Informações</a></li>
    <li><a href="#equipe">Equipe</a></li>
    <li><a href="#visao-geral">Visão Geral</a></li>
    <li><a href="#tecnologias">Tecnologias e Componentes</a></li>
    <li><a href="#como-executar">Como Executar</a></li>
    <li><a href="#fluxo-de-uso-da-api">Fluxo de Uso da API</a></li>
    <li><a href="#endpoints-principais">Endpoints Principais</a></li>
    <li><a href="#arquitetura">Arquitetura (Camadas)</a></li>
    <li><a href="#estrutura-do-projeto">Estrutura do Projeto</a></li>
    <li><a href="#seguranca">Segurança</a></li>
    <li><a href="#banco-de-dados-e-migracoes">Banco de Dados e Migrações</a></li>
    <li><a href="#testes-automatizados">Testes Automatizados</a></li>
  </ol>
</details>

<h2 id="informacoes">ℹ️ Informações</h2>

| Campo | Valor |
| --- | --- |
| Projeto | BlindSpot API |
| Curso | Engenharia de Software |
| Disciplina | Arquitetura Orientada a Serviços (SOA) |
| Professor(a) | SALATIEL LUZ MARINHO |
| Turma | 3ESPX |
| Instituição | FIAP |

<h2 id="equipe">👥 Equipe</h2>

| Integrante | RM |
| --- | --- |
| Augusto Barcelos Barros | 565065 |
| Caio Felipe de Lima Bezerra | 556197 |
| Juan Francisco Alves Muradas | 555541 |
| Lucas Derenze Simidu | 555931 |
| Sofia Fernandes | 554873 |

<h2 id="visao-geral">📋 Visão Geral</h2>

Este projeto implementa uma API para:

  - autenticação de usuários com JWT;
  - controle de acesso por papéis (`USER` e `ADMIN`);
- consulta de marcas, modelos e versões de veículos;
- consulta de detalhes técnicos de veículos, como motor, transmissão, desempenho, dimensões, fotos e equipamentos;
- documentação automática via Swagger/OpenAPI;
- persistência em Oracle com controle de versão do banco usando Flyway.

A solução segue separação clara entre as camadas de apresentação, serviço e dados, alinhada aos critérios de avaliação SOA.

<h2 id="tecnologias">🧰 Tecnologias e Componentes</h2>

- Java 21
- Spring Boot 4
- Spring Web MVC
- Spring Security
- Spring Data JPA
- Spring Boot Validation (Hibernate Validator)
- Flyway (suporte Oracle Database)
- Oracle Database (ojdbc11)
- JWT (java-jwt - Auth0)
- Swagger / OpenAPI (springdoc-openapi)
- Project Lombok
- Maven
- Testes: JUnit 5, Mockito e AssertJ

<h2 id="como-executar">🚀 Como Executar</h2>

### Pré-requisitos

- Java 21+
- Maven ou o wrapper `mvnw`
- Oracle Database acessível

### 1. Clone o repositório

```bash
git clone https://github.com/Asteriuz/Challenge-SOA.git
cd Challenge-SOA
```

### 2. Configure as variáveis de ambiente

#### Linux/macOS

```bash
export DB_URL=jdbc:oracle:thin:@oracle.fiap.com.br:1521:orcl
export DB_USER=seu_usuario_fiap
export DB_PASSWORD=sua_senha
export JWT_SECRET=seu_segredo_jwt
```

#### Windows (PowerShell)

```powershell
$env:DB_URL="jdbc:oracle:thin:@oracle.fiap.com.br:1521:orcl"
$env:DB_USER="seu_usuario_fiap"
$env:DB_PASSWORD="sua_senha"
$env:JWT_SECRET="seu_segredo_jwt"
```

### 3. Inicie a aplicação

```bash
./mvnw spring-boot:run
```

No Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

### 4. Acesse a aplicação

- API Base: http://localhost:8080
- Swagger UI: http://localhost:8080/swagger-ui
- OpenAPI JSON: http://localhost:8080/v3/api-docs

<h2 id="fluxo-de-uso-da-api">🎯 Fluxo de Uso da API</h2>

<details>
  <summary><strong>📱 Passo 1: Busca e Filtragem de Veículos</strong></summary>

  O usuário realiza uma busca filtrada por:

  - Marca
  - Modelo
  - Ano
  - Versão

  Endpoints usados nessa etapa:

  - `GET /api/marcas?populares=true` para obter marcas populares
  - `GET /api/marcas/{marcaId}/modelos` para listar modelos por marca
  - `GET /api/modelos/{modeloId}/anos` para listar anos disponíveis
  - `GET /api/veiculos/busca?marcaId=X&modeloId=Y&ano=Z&versaoId=W` para buscar versões com filtros
</details>

<details>
  <summary><strong>🚗 Passo 2: Visualização de Detalhes Técnicos</strong></summary>

  Após selecionar um veículo, o usuário acessa informações detalhadas:

  - Informações básicas: marca, modelo, ano e preço
  - Motor: cilindrada, potência e combustível
  - Transmissão: tipo e quantidade de marchas
  - Desempenho: aceleração, velocidade máxima e consumo
  - Dimensões: comprimento, largura, altura e peso
  - Segurança: airbags, controle de tração e sistemas assistivos
  - Conforto: ar-condicionado, volante multifuncional e banco aquecido
  - Tecnologia: painel digital, câmera 360° e sistema de som premium
  - Fotos e galeria
  - Sugestões de comparação

  Endpoints principais:

  - `GET /api/veiculos/{idVersao}` para obter detalhes completos do veículo
  - `GET /api/veiculos/{idVersao}/mais-comparados` para obter sugestões para comparação
</details>

<h2 id="endpoints-principais">🌐 Endpoints Principais</h2>

| Método | Endpoint | Descrição | Autenticação |
| --- | --- | --- | --- |
| POST | `/api/auth/login` | Login e geração do token JWT | Não |
| POST | `/api/auth/cadastro` | Cadastro de novo usuário | Não |
| GET | `/api/marcas` | Lista marcas com filtro `populares` | Sim |
| GET | `/api/marcas/{marcaId}/modelos` | Lista modelos de uma marca | Sim |
| GET | `/api/modelos/{modeloId}/anos` | Lista anos de um modelo | Sim |
| GET | `/api/modelos/{modeloId}/versoes` | Lista versões por modelo/ano | Sim |
| GET | `/api/veiculos/busca` | Busca de versões por filtros | Sim |
| GET | `/api/veiculos/{idVersao}` | Detalhes completos de uma versão | Sim |
| GET | `/api/veiculos/{idVersao}/mais-comparados` | Sugestões de versões para comparação | Sim |
| GET | `/api/auth/me` | Dados do usuário autenticado | Sim |
| GET | `/api/auth/admin` | Acesso restrito a administradores | Sim |

Documentação interativa: http://localhost:8080/swagger-ui

<h2 id="arquitetura">🏗️ Arquitetura (Camadas)</h2>

![Arquitetura SOA](ArquiteturaSOA.png)

<h2 id="estrutura-do-projeto">📁 Estrutura do Projeto</h2>

```text
Challenge-SOA/
├── pom.xml
├── README.md
├── src/
│   ├── main/
│   │   ├── java/br/com/blindspot/
│   │   │   ├── BlindSpotApplication.java
│   │   │   └── api/
│   │   │       ├── config/
│   │   │       ├── controller/
│   │   │       ├── domain/
│   │   │       ├── dto/
│   │   │       ├── exception/
│   │   │       ├── repository/
│   │   │       ├── security/
│   │   │       └── service/
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── application-dev.properties
│   │       └── db/
│   │           ├── migration/
│   │           └── testdata/
│   └── test/java/br/com/blindspot/
│       └── BlindSpotApplicationTests.java
└── target/
```

<h2 id="seguranca">🔐 Segurança</h2>

- Autenticação com JWT Bearer Token.
- Perfis de acesso mapeados por papel:
  - `USER` para usuários cadastrados pela aplicação
  - `ADMIN` para acesso restrito a áreas administrativas
- Usuário administrativo de apoio criado na inicialização do perfil `dev`:
  - usuário: `admin`
  - senha: `Admin@123`
- Endpoints públicos:
  - `POST /api/auth/login`
  - `POST /api/auth/cadastro`
  - `/v3/api-docs/**`
  - `/swagger-ui/**`
- Endpoints protegidos por autenticação:
  - `/api/marcas/**`
  - `/api/modelos/**`
  - `/api/veiculos/**`
  - `/api/auth/me`
- Endpoint protegido por autorização de papel:
  - `GET /api/auth/admin` exige `ROLE_ADMIN`
- Demais endpoints exigem token válido no header:

```http
Authorization: Bearer <seu_token>
```

<h2 id="banco-de-dados-e-migracoes">🗄️ Banco de Dados e Migrações</h2>

Migrações versionadas com Flyway:

- `V1__create_users.sql` cria a estrutura de usuários
- `V2__create_vehicles.sql` cria a estrutura de veículos
- `V999__insert_dados_mock.sql` carrega dados de apoio e teste

Configuração principal em `src/main/resources/application-dev.properties`.

Os usuários são carregados com seus papéis a partir de `ROLE_NAME` na tabela `BS_USERS`.

<h2 id="testes-automatizados">🧪 Testes Automatizados</h2>

O projeto inclui uma suíte de testes automatizados que cobre os principais comportamentos da API, com cenários de sucesso, erro e acesso não autorizado.

### O que está coberto

- `AuthServiceTest` valida login, cadastro e conflito de usuário já existente.
- `AuthControllerTest` valida as respostas dos endpoints de autenticação.
- `MarcaControllerTest` valida a busca de marcas e modelos.
- `RequestValidationTest` valida as regras de `@NotBlank` e `@Size` dos DTOs.
- `GlobalExceptionHandlerTest` valida o tratamento de erros para `401`, `403` e `409`.

### Como executar

```powershell
./mvnw test
```

No Windows:

```powershell
.\mvnw.cmd test
```

### Resultado esperado

Ao executar a suíte, os testes passam sem depender de banco de dados externo, pois a cobertura foi construída com testes unitários e validação de DTOs. Na validação mais recente, 14 testes passaram com sucesso.
