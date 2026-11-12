# API Gateway

O API Gateway é o ponto de entrada das requisições da aplicação. Ele recebe as chamadas do frontend e as encaminha ao `pedido-service`.

## Roteamento

Requisições para `http://localhost:8080/pedidos/**` são encaminhadas ao `pedido-service` na porta `8081`. O Gateway também permite as requisições do frontend em `http://localhost:3000` e `http://127.0.0.1:3000`.

## Como executar

O Gateway é executado pelo Docker Compose junto com os demais serviços. Configure o arquivo `.env` na raiz do projeto conforme as instruções do [README principal](../README.md#configuração) e, também na raiz, execute:

```powershell
docker compose up --build
```

Após a inicialização, o Gateway estará disponível em [http://localhost:8080](http://localhost:8080). Para usar a aplicação, acesse o [frontend](http://localhost:3000). Consulte a documentação da [API de pedidos](../pedido-service/) para detalhes dos endpoints.