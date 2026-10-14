# API de Pedidos

Esta API permite consultar e cadastrar pedidos. Com o serviço disponível, use a URL base `http://localhost:8080`.

## Como rodar localmente (sem Docker)

É necessário ter Java 21 e PostgreSQL instalado e em execução localmente. Crie o banco `pedidos_db` (por exemplo, com `psql -U postgres -c "CREATE DATABASE pedidos_db;"`).

No PowerShell, dentro da pasta `pedido-service`, configure a conexão com o seu PostgreSQL local e inicie a aplicação:

```powershell
$env:DATABASE_URL = "jdbc:postgresql://localhost:5432/pedidos_db"
$env:DATABASE_USERNAME = "postgres"
$env:DATABASE_PASSWORD = "<senha do seu usuário PostgreSQL>"
.\gradlew.bat bootRun
```

A API ficará disponível em `http://localhost:8080`.

## Endpoints

| Método | Caminho | Descrição |
|--------|---------|-----------|
| `GET` | `/pedidos` | Retorna todos os pedidos |
| `GET` | `/pedidos/{id}` | Retorna um pedido específico |
| `POST` | `/pedidos` | Cadastra um pedido |

### Listar pedidos

```powershell
curl.exe http://localhost:8080/pedidos
```

A resposta é uma lista JSON com status `200 OK`. Se ainda não houver pedidos, será `[]`. Cada pedido contém `id`, `nome`, `quantidade` e `valorTotal`.

```json
[
  {
    "id": 1,
    "nome": "Teclado",
    "quantidade": 2,
    "valorTotal": 250.0
  },
  {
    "id": 2,
    "nome": "Mouse",
    "quantidade": 1,
    "valorTotal": 350.0
  }
]
```

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

### Buscar pedido por ID

Coloque o ID do pedido na URL para buscar um pedido específico:

```powershell
curl.exe http://localhost:8080/pedidos/1
```

A resposta é um JSON com status `200 OK`:

```json
{
  "id": 1,
  "nome": "Teclado",
  "quantidade": 2,
  "valorTotal": 250.0
}
```

Caso não exista um pedido com o ID informado, a resposta será `404 Not Found`:

```json
{
  "status": 404,
  "mensagem": "Produto não encontrado",
  "erros": [
    "Produto não encontrado"
  ],
  "timestamp": "2026-10-14T22:32:16.379387700Z"
}
```