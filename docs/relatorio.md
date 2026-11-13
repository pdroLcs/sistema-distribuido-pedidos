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
Avaliar quantitativamente o tempo de resposta do endpoint POST /pedidos, exposto pelo API Gateway na porta 8080, sob diferentes níveis de concorrência, verificando se o fluxo síncrono de criação de pedidos mantém estabilidade enquanto o processamento assíncrono de notificações ocorre em paralelo por meio do RabbitMQ. O experimento é quantitativo e busca identificar o ponto a partir do qual o sistema passa a apresentar degradação de desempenho.

- Procedimento:

A aplicação é executada localmente via Docker Compose, com todos os componentes ativos: api-gateway, pedido-service, notificacao-service, rabbitmq, pedido-db e notificacao-db. As medições são realizadas por meio de um script que envia requisições HTTP concorrentes ao Gateway, com corpo JSON de estrutura idêntica, variando apenas o identificador do cliente. Para cada rodada são disparadas 200 requisições, distribuídas em cinco níveis de concorrência: 1, 5, 10, 25 e 50 clientes simultâneos. Antes de cada rodada executa-se um aquecimento de dez requisições descartadas, para eliminar o custo de inicialização do pool de conexões. A tabela de pedidos é truncada entre as rodadas, garantindo que todos os cenários partam do mesmo estado inicial. Para cada rodada são registrados o tempo individual de cada requisição, do qual se extraem média, mediana, percentil 95 e tempo total da rodada. O consumo de CPU e memória dos contêineres é coletado durante a execução, como evidência auxiliar na identificação de gargalos.

- Resultados:

- Análise: A análise deve verificar três pontos. Primeiro, se o crescimento da latência é aproximadamente linear em relação à concorrência, o que indicaria que o sistema responde de forma previsível dentro de sua capacidade. Segundo, se existe um nível de concorrência a partir do qual a latência cresce de forma desproporcional — comportamento característico de saturação de recurso compartilhado, tipicamente o pool de conexões do pedido-service com o PostgreSQL. Terceiro, se o tempo total da rodada estabiliza em determinado ponto, o que indicaria que o sistema deixou de ser limitado pela taxa de chegada das requisições e passou a ser limitado por sua própria vazão máxima. Os dados de CPU dos contêineres devem ser usados para corroborar ou refutar a hipótese de que o gargalo está no acesso ao banco, e não na comunicação entre os serviços.

### 7.2 Experimento 2 — [A definir]

- Objetivo:
Medir quantitativamente a vazão do notificacao-service no consumo de eventos publicados no RabbitMQ e comparar o tempo de resposta percebido pelo cliente com o tempo efetivo de conclusão do processamento da notificação, demonstrando o desacoplamento temporal proporcionado pela comunicação assíncrona. O experimento também avalia o efeito da configuração de prefetch sobre a vazão e a latência individual de entrega.

- Procedimento:
- São publicados 500 pedidos em sequência por meio do endpoint POST /pedidos, com o notificacao-service ativo e consumindo a fila associada à exchange pedido.exchange com routing key pedido.criado. O pedido-service registra em log o instante da publicação de cada evento, e o notificacao-service registra o instante de consumo e de conclusão do processamento de cada mensagem. A partir desses registros são calculadas a vazão média do consumidor em mensagens por segundo, a latência de entrega — intervalo entre a publicação e o início do consumo — e o tempo médio de processamento interno. O experimento é repetido em duas configurações do consumidor: com prefetch igual a 1, processando uma mensagem por vez, e com prefetch igual a 10, permitindo múltiplas mensagens em processamento simultâneo. Entre as rodadas a fila é drenada, evitando medição de resíduos da execução anterior.
- 
- Resultados:

- Análise:
- A análise deve verificar se o tempo de resposta do fluxo principal permanece estável independentemente da configuração do consumidor, o que confirmaria o desacoplamento temporal: o cliente não aguarda o processamento da notificação para receber a confirmação de criação do pedido. Deve também avaliar o trade-off entre vazão e latência individual — espera-se que o aumento do prefetch eleve a vazão agregada ao custo de maior espera individual por mensagem. Por fim, a diferença entre o tempo de resposta percebido pelo cliente e o tempo total de conclusão do processamento da notificação quantifica o ganho obtido com a mensageria: em uma implementação síncrona, esses tempos seriam somados no caminho crítico da requisição.

## 8. Experimento de falha

Será realizado um experimento envolvendo a interrupção de um dos componentes da aplicação, com o objetivo de observar como os demais componentes se comportam diante da indisponibilidade de uma parte do sistema.

- Componente interrompido:
- O notificacao-service é interrompido durante a operação normal da aplicação. Em uma segunda etapa do mesmo experimento, o RabbitMQ também é interrompido, para observar o comportamento do pedido-service diante da indisponibilidade do broker de mensagens.

- Procedimento:
- Com a aplicação em operação normal, o contêiner do notificacao-service é parado por meio do comando docker compose stop notificacao-service. Em seguida são criados 50 novos pedidos pelo API Gateway, registrando-se o status HTTP e o tempo de resposta de cada requisição. O estado da fila é consultado via rabbitmqctl list_queues para verificar o acúmulo de mensagens não consumidas. Após esse cenário, o notificacao-service é reiniciado e observa-se o comportamento dos eventos que haviam ficado acumulados. Em uma segunda etapa, o RabbitMQ é parado com docker compose stop rabbitmq, e novas tentativas de criação de pedidos são realizadas, além de consultas e listagens, para verificar quais operações permanecem funcionais e se há divergência entre os dados persistidos no banco e os eventos efetivamente publicados na fila.

- Comportamento observado:

- Análise:
- A análise deve avaliar dois aspectos. O primeiro é o isolamento de falhas: se a indisponibilidade do consumidor não afetou o fluxo principal, isso confirma que a comunicação assíncrona atua como elemento de desacoplamento, com a fila funcionando simultaneamente como buffer e como mecanismo de tolerância a falhas — a indisponibilidade de um consumidor converte-se em atraso, e não em perda de dados. O segundo é a dependência do broker: se a indisponibilidade do RabbitMQ interrompe o fluxo de escrita, isso expõe que a publicação do evento está posicionada no caminho crítico da operação. Nesse caso, a análise deve discutir as duas leituras possíveis — a priorização da consistência entre persistência e publicação, evitando pedidos sem evento correspondente, e o acoplamento ao broker como fragilidade arquitetural — e as alternativas de mitigação, como o padrão transactional outbox, no qual o evento é gravado na mesma transação do pedido e publicado posteriormente por um processo separado, ou a publicação com retentativa em segundo plano. De forma geral, o experimento evidencia que o grau de resiliência de uma arquitetura distribuída depende de onde as dependências são posicionadas no fluxo principal, e não apenas da quantidade de serviços em que a aplicação é dividida.
