// Altere para a URL/Porta em que o seu API Gateway está rodando
const API_URL = 'http://localhost:8080/pedidos'; 

document.addEventListener('DOMContentLoaded', () => {
    carregarPedidos();

    const form = document.getElementById('pedidoForm');
    form.addEventListener('submit', cadastrarPedido);

    const btnAtualizar = document.getElementById('atualizarBtn');
    btnAtualizar.addEventListener('click', carregarPedidos);
});

async function carregarPedidos() {
    try {
        const response = await fetch(API_URL);
        if (!response.ok) throw new Error('Erro ao buscar pedidos');
        
        const pedidos = await response.json();
        const tbody = document.querySelector('#pedidosTable tbody');
        tbody.innerHTML = '';

        pedidos.forEach(pedido => {
            const tr = document.createElement('tr');
            tr.innerHTML = `
                <td>${pedido.id || ''}</td>
                <td>${pedido.produto || pedido.nomeProduto || 'N/D'}</td>
                <td>${pedido.quantidade || ''}</td>
                <td>R$ ${pedido.preco ? pedido.preco.toFixed(2) : '0.00'}</td>
                <td>${pedido.status || 'Criado'}</td>
            `;
            tbody.appendChild(tr);
        });
    } catch (error) {
        console.error('Erro:', error);
        // alert('Não foi possível conectar ao servidor. Verifique se o API Gateway está rodando.');
    }
}

async function cadastrarPedido(event) {
    event.preventDefault();

    const produto = document.getElementById('produto').value;
    const quantidade = parseInt(document.getElementById('quantidade').value);
    const preco = parseFloat(document.getElementById('preco').value);

    const novoPedido = {
        produto: produto,
        quantidade: quantidade,
        preco: preco
    };

    try {
        const response = await fetch(API_URL, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(novoPedido)
        });

        if (response.ok) {
            alert('Pedido cadastrado com sucesso!');
            document.getElementById('pedidoForm').reset();
            carregarPedidos(); // Atualiza a tabela
        } else {
            alert('Erro ao cadastrar pedido.');
        }
    } catch (error) {
        console.error('Erro:', error);
        alert('Erro de conexão com o API Gateway.');
    }
}