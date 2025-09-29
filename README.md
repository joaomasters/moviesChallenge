# Golden Raspberry Awards Challenge 

Este projeto foi desenvolvido como solução para o desafio **Golden Raspberry Awards**.  
Ele é composto por **duas aplicações**:

- **Backend**: API RESTful desenvolvida em **Spring Boot + Java 17**, responsável por processar e disponibilizar os dados dos filmes.
- **Frontend**: Aplicação em **Angular + Angular Material**, que consome a API e exibe os dados em uma interface.

---

##  Estrutura do Projeto

```bash
.
├── movies/        # Aplicação Spring Boot
├── frontend/       # Aplicação Angular
└── README.md       # Este arquivo

Backend (Spring Boot)
** Tecnologias

Java 17

Spring Boot 3

Spring Data JPA + H2 (banco em memória)

OpenCSV (leitura do CSV)

JUnit 5 + SpringBootTest (testes de integração)

** Funcionalidades

Importa o arquivo movielist.csv automaticamente no startup.

Endpoints RESTful para:

GET /api/movies → lista todos os filmes

GET /api/movies/intervals → produtores com menor e maior intervalo entre prêmios

GET /api/movies/years-with-multiple-winners → anos com mais de um vencedor

GET /api/movies/studios-with-win-count → estúdios com mais vitórias

GET /api/movies/winners-by-year/{year} → vencedores por ano específico

**  ## Rodando o backend

Na pasta movies/:

./mvnw spring-boot:run


A API ficará disponível em:
 http://localhost:8080/api/movies

**  Testes de integração

Na pasta movies/:

./mvnw test

Banco de dados H2

O backend usa H2 em memória, já populado automaticamente com movielist.csv.
Você pode acessar o console H2 em:

http://localhost:8080/h2-console

Configuração padrão:

JDBC URL: jdbc:h2:mem:moviesdb

User: sa

Password: (vazio)

Rodar testes do backend
./mvnw test


Isso executa os testes de integração.

** ## Frontend (Angular)
**  Tecnologias

Angular 17

Angular Material

RxJS / HttpClient

Jasmine + Karma (testes unitários)

 Funcionalidades

Dashboard com 4 painéis:

Anos com mais de um vencedor

Top 3 estúdios com mais vitórias

Produtores com maior/menor intervalo entre prêmios

Busca por vencedores de um ano específico

Lista de Filmes:

Paginação

Filtro por ano

Filtro por vencedor (Yes / No)

**  Rodando o frontend

Na pasta frontend/:

npm install
ng serve -o


A aplicação ficará disponível em:
 http://localhost:4200

Testes unitários

Na pasta frontend/:

ng test

 Requisitos Atendidos

 Backend com API RESTful (nível 2 de Richardson)

 Banco em memória (H2)

 Importação automática de CSV

 Testes de integração no backend

 Frontend Angular responsivo (mínimo 768x1280)

 Dashboard com os 4 painéis exigidos

 Lista de filmes com filtros (ano / vencedor) e paginação

 Testes unitários no frontend

 Documentação (README)

## **  Referências da API Externa

Swagger UI: https://challenge.outsera.tech/swagger-ui/index.html
