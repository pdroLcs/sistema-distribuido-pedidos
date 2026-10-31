# Sistema Distribuído de Pedidos e Notificações

## 1. Introdução

Este projeto consiste no desenvolvimento de uma aplicação distribuída para gerenciamento de pedidos e processamento de notificações, desenvolvido como parte da disciplina de Computação Distribuída.

A aplicação utiliza uma arquitetura baseada em serviços independentes, que se comunicam por meio de requisições HTTP e troca de mensagens assíncronas. O objetivo é aplicar conceitos de sistemas distribuídos, como comunicação entre processos, processamento assíncrono, separação de responsabilidades, persistência de dados e execução em contêineres.

A infraestrutura foi projetada para ser executada localmente utilizando Docker Compose, dispensando a necessidade de serviços pagos de computação em nuvem.

## 2. Problema e objetivo da aplicação

### 2.1 Problema

Em sistemas de gerenciamento de pedidos, determinadas operações podem exigir o processamento de tarefas adicionais, como o envio de notificações relacionadas à criação de um novo pedido.

Quando todas essas responsabilidades são concentradas em uma única aplicação, o processamento de operações secundárias pode aumentar o acoplamento entre os componentes e afetar o fluxo principal do sistema.

Além disso, a execução de tarefas de forma estritamente síncrona pode fazer com que uma operação principal dependa diretamente da disponibilidade e do tempo de processamento de outros componentes.

Diante desse cenário, o projeto propõe uma arquitetura distribuída na qual o gerenciamento de pedidos e o processamento de notificações são separados, utilizando comunicação assíncrona para permitir que determinadas tarefas sejam executadas independentemente do fluxo principal da aplicação.

### 2.2 Objetivo geral

Desenvolver uma aplicação distribuída para gerenciamento de pedidos e processamento de notificações, utilizando serviços independentes e mecanismos de comunicação síncrona e assíncrona.

### 2.3 Objetivos específicos

- Implementar um serviço responsável pelo gerenciamento de pedidos.

- Implementar um serviço independente para processamento de notificações.

- Utilizar um API Gateway como ponto de entrada para as requisições da aplicação.

- Implementar comunicação síncrona por meio de APIs REST.

- Implementar comunicação assíncrona utilizando RabbitMQ.

- Utilizar bancos de dados para persistência das informações.

- Executar os principais componentes em contêineres Docker.

- Realizar experimentos para observar o comportamento e o desempenho da aplicação distribuída.

- Avaliar o comportamento do sistema diante da falha de um de seus componentes.

## 3. Arquitetura da aplicação

### 3.1 Visão geral

A aplicação utiliza uma arquitetura distribuída baseada na separação de responsabilidades entre diferentes componentes.

O fluxo principal da aplicação começa no cliente, que encaminha suas requisições ao API Gateway. O Gateway direciona as requisições ao serviço responsável pelo gerenciamento dos pedidos.

Após a criação de um pedido, o serviço publica um evento no RabbitMQ. Esse evento poderá ser consumido pelo serviço de notificações, responsável por realizar o processamento correspondente de maneira assíncrona.

A arquitetura proposta é composta pelos seguintes componentes:

- Frontend: interface de interação com o usuário.

- API Gateway: responsável por receber e encaminhar as requisições HTTP.

- pedido-service: responsável pelo gerenciamento dos pedidos.

- notificacao-service: responsável pelo processamento das notificações.

- RabbitMQ: responsável pela comunicação assíncrona entre os serviços.

- pedido-db: banco de dados utilizado pelo serviço de pedidos.

- notificacao-db: banco de dados destinado às informações do serviço de notificações.

### 3.2 Arquitetura

![Arquitetura](./arquitetura.png)

### 3.3 Descrição dos componentes

### API Gateway

O API Gateway funciona como ponto de entrada das requisições HTTP realizadas pelo cliente.

Sua responsabilidade é direcionar as requisições aos serviços correspondentes, evitando que o cliente precise conhecer diretamente os endereços internos de cada serviço.

O componente está configurado para execução na porta 8080.

### pedido-service

O pedido-service é responsável pelas operações relacionadas ao gerenciamento de pedidos.

Entre suas funcionalidades estão:

- Criar novos pedidos.

- Consultar um pedido pelo identificador.

- Listar os pedidos cadastrados.

- Persistir as informações no banco de dados PostgreSQL.

- Publicar eventos relacionados à criação de pedidos no RabbitMQ.

O serviço disponibiliza endpoints REST para realizar essas operações.

### notificacao-service

O notificacao-service será responsável por receber e processar os eventos publicados pelo serviço de pedidos.

Sua comunicação com o pedido-service será realizada por meio do RabbitMQ, permitindo que o processamento das notificações ocorra de maneira assíncrona e independente das requisições HTTP originais.

### RabbitMQ

O RabbitMQ é utilizado como sistema de mensageria da aplicação.

Sua função é intermediar a comunicação assíncrona entre os serviços, permitindo que o produtor publique mensagens sem precisar aguardar diretamente o processamento realizado pelo consumidor.

No projeto, o pedido-service utiliza o mecanismo de publicação de mensagens do RabbitMQ para enviar eventos relacionados à criação de pedidos.

### Persistência de dados

O projeto utiliza PostgreSQL como mecanismo de persistência do serviço de pedidos.

O banco de dados é executado em um contêiner Docker independente, e seus dados são armazenados em um volume persistente denominado pedido-db-data.

A arquitetura prevê a separação dos bancos de dados por serviço, permitindo maior independência entre os componentes.

## 4. Comunicação entre os componentes

A aplicação utiliza dois mecanismos principais de comunicação: comunicação síncrona por HTTP/REST e comunicação assíncrona por mensageria.

### 4.1 Comunicação síncrona

A comunicação síncrona é utilizada entre o cliente, o API Gateway e o serviço de pedidos.

O cliente realiza uma requisição HTTP ao Gateway, que encaminha a solicitação ao serviço responsável pela operação.

Atualmente, o pedido-service disponibiliza os seguintes endpoints:

| Método | Endpoint        | Descrição                              |
|--------|-----------------|----------------------------------------|
| GET    | `/pedidos`      | Lista os pedidos cadastrados           |
| GET    | `/pedidos/{id}` | Consulta um pedido pelo identificador  |
| POST   | `/pedidos`      | Cria um novo pedido                    |

### 4.2 Comunicação assíncrona

A comunicação assíncrona é realizada por meio do RabbitMQ.

Quando um novo pedido é criado, o pedido-service persiste os dados no banco PostgreSQL e publica um evento contendo informações do pedido.

O evento é enviado utilizando:

- Exchange: pedido.exchange

- Routing key: pedido.criado

O objetivo é permitir que o serviço de notificações receba e processe essas informações independentemente do fluxo de criação do pedido.

## 5. Persistência

A persistência dos dados é realizada por meio do PostgreSQL, executado em um contêiner independente.

O serviço de pedidos utiliza o banco pedido-db, cuja porta interna é 5432. Para permitir o acesso a partir da máquina hospedeira, a porta 5433 é disponibilizada externamente.

Os dados são armazenados em um volume Docker, garantindo sua preservação mesmo após a interrupção ou remoção dos contêineres.

A configuração de persistência do serviço de notificações será documentada após sua implementação.

## 6. Execução da aplicação

A infraestrutura utiliza Docker Compose para inicializar e gerenciar os componentes.

A execução é realizada a partir do diretório raiz do projeto:

```cmd
docker compose up --build
```

Para executar os contêineres em segundo plano:

```cmd
docker compose up --build -d
```

Para acompanhar os logs de um serviço:

```cmd
docker compose logs -f pedido-service
```

Para interromper a aplicação:

```cmd
docker compose down
```

A configuração utiliza variáveis de ambiente para definir credenciais e parâmetros de conexão com o banco de dados e o RabbitMQ.

## 7. Experimentos

Esta seção será preenchida após a realização dos experimentos.

Serão realizados dois experimentos relacionados ao funcionamento da aplicação distribuída, sendo pelo menos um quantitativo.

Os experimentos deverão avaliar aspectos como desempenho, concorrência e comportamento da comunicação entre os componentes.

### 7.1 Experimento 1 — [A definir]

- Objetivo:

- Procedimento:

- Resultados:

- Análise:

### 7.2 Experimento 2 — [A definir]

- Objetivo:

- Procedimento:

- Resultados:

- Análise:

## 8. Experimento de falha

Será realizado um experimento envolvendo a interrupção de um dos componentes da aplicação, com o objetivo de observar como os demais componentes se comportam diante da indisponibilidade de uma parte do sistema.

- Componente interrompido:

- Procedimento:

- Comportamento observado:

- Análise: