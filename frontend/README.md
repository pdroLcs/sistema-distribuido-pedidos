# Frontend

Interface web para cadastrar pedidos e consultar a lista de pedidos. O frontend é servido pelo Nginx e envia as requisições para o API Gateway.

## Como executar

O frontend é iniciado pelo Docker Compose junto com os demais serviços. Configure o arquivo `.env` na raiz do projeto conforme as instruções do [README principal](../README.md#configuração) e execute, a partir da raiz:

```powershell
docker compose up --build
```

Após a inicialização, acesse a aplicação em [http://localhost:3000](http://localhost:3000). Para que as operações funcionem, o API Gateway e os demais serviços também precisam estar em execução.