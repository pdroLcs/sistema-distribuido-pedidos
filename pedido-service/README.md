# API de Pedidos

Esta API permite consultar e cadastrar pedidos. Com o serviço disponível, use a URL base `http://localhost:8080`.

## Endpoints

| Método | Caminho | Descrição |
|--------|---------|-----------|
| `GET` | `/pedidos` | Retorna todos os pedidos |
| `POST` | `/pedidos` | Cadastra um pedido |

### Listar pedidos

```powershell
curl.exe http://localhost:8080/pedidos
```

A resposta é uma lista JSON com status `200 OK`. Se ainda não houver pedidos, será `[]`. Cada pedido contém `id`, `nome`, `quantidade` e `valorTotal`.

### Cadastrar pedido

Envie um JSON com nome, quantidade e valor total:

```powershell
curl.exe -X POST http://localhost:8080/pedidos `
  -H "Content-Type: application/json" `
  -d "{\"nome\":\"Teclado\",\"quantidade\":2,\"valorTotal\":250.00}"
```

```json
{
  "nome": "Teclado",
  "quantidade": 2,
  "valorTotal": 250.0
}
```

O cadastro bem-sucedido retorna `201 Created` e o pedido criado, incluindo o `id` gerado:

```json
{
  "id": 1,
  "nome": "Teclado",
  "quantidade": 2,
  "valorTotal": 250.0
}
```

O nome é obrigatório e aceita até 100 caracteres. A quantidade e o valor total devem ser maiores que zero. Requisições com dados inválidos retornam `400 Bad Request`.