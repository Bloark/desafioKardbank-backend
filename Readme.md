# API de Gerenciamento de Pessoas

Esta é uma API de gerenciamento de pessoas que permite realizar operações CRUD (Criar, Ler, Atualizar, Deletar) em registros de pessoas. Ela oferece endpoints para adicionar, buscar, atualizar e deletar pessoas, bem como listar todas as pessoas cadastradas.

## Tecnologias Utilizadas

- Java
- Spring Boot
- Spring Data JPA
- Spring Web
- Banco de Dados (configurado no arquivo `application.properties`)
- Biblioteca de Validação de Telefone (AbstractAPI)

## Configuração do Ambiente

Antes de executar a API, certifique-se de ter o seguinte ambiente configurado:

1. Java Development Kit (JDK) versão 8 ou superior
2. Maven
3. Banco de Dados configurado (MySQL, PostgreSQL, etc.)

## Como Executar a API

1. Clone o repositório do projeto para sua máquina local.
2. Abra o projeto em sua IDE de preferência.
3. Configure as propriedades do banco de dados no arquivo `application.properties`.
4. Execute o comando `mvn spring-boot:run` para iniciar a aplicação.
5. A API estará disponível no seguinte endereço: `http://localhost:8080`.

## Endpoints

### Listar Todas as Pessoas

- Método: GET
- URL: `/pessoas`

Este endpoint retorna uma lista com todas as pessoas cadastradas.

### Buscar Pessoa por ID

- Método: GET
- URL: `/pessoas/{id}`

Este endpoint retorna os dados de uma pessoa específica, com base no ID fornecido.

### Adicionar Pessoa

- Método: POST
- URL: `/pessoas`
- Corpo da Requisição: JSON contendo os dados da pessoa a ser adicionada

Este endpoint permite adicionar uma nova pessoa à base de dados. O corpo da requisição deve conter os dados da pessoa no formato JSON.

### Atualizar Pessoa

- Método: PUT
- URL: `/pessoas/{id}`
- Corpo da Requisição: JSON contendo os dados atualizados da pessoa

Este endpoint permite atualizar os dados de uma pessoa existente, com base no ID fornecido. O corpo da requisição deve conter os dados atualizados da pessoa no formato JSON.

### Deletar Pessoa

- Método: DELETE
- URL: `/pessoas/{id}`

Este endpoint permite deletar uma pessoa da base de dados, com base no ID fornecido.

## Tratamento de Erros

A API trata erros através do uso de exceções. Caso ocorra algum erro durante o processamento de uma requisição, a API retornará uma resposta com o código de status HTTP apropriado e uma mensagem de erro descritiva.

## Considerações Finais

Esta API de gerenciamento de pessoas fornece uma maneira simples e eficiente de realizar operações CRUD em registros de pessoas. Utilize os endpoints disponíveis para interagir com a API e gerenciar as informações das pessoas cadastradas.

Caso tenha alguma dúvida ou encontre algum problema, sinta-se à vontade para entrar em contato com a equipe de suporte. Aproveite o uso da API e tenha uma ótima experiência!

