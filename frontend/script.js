const API_URL = 'http://localhost:8080/pedidos'; 

document.addEventListener('DOMContentLoaded', () => {
    carregarPedidos();

    // Ouve o clique do botão de salvar em vez do submit do form
    const btnSalvar = document.getElementById('salvarBtn');
    if (btnSalvar) {
        btnSalvar.addEventListener('click', cadastrarPedido);
    }

    const btnAtualizar = document.getElementById('atualizarBtn');
    if (btnAtualizar) {
        btnAtualizar.addEventListener('click', carregarPedidos);
    }
});

async function carregarPedidos() {
    try {
        const response = await fetch(API_URL);
        if (!response.ok) throw new Error('Erro ao buscar pedidos');
        
        const pedidos = await response.json();
        const tbody = document.querySelector('#pedidosTable tbody');
        if (!tbody) return;
        
        tbody.innerHTML = '';

        pedidos.forEach(pedido => {
            const tr = document.createElement('tr');
            tr.innerHTML = `
                <td>${pedido.id || ''}</td>
                <td>${pedido.nome || 'N/D'}</td>
                <td>${pedido.quantidade || ''}</td>
                <td>R$ ${pedido.valorTotal ? pedido.valorTotal.toFixed(2) : '0.00'}</td>
                <td>Criado</td>
            `;
            tbody.appendChild(tr);
        });
    } catch (error) {
        console.error('Erro ao carregar:', error);
    }
}

async function cadastrarPedido(event) {
    if (event) event.preventDefault();

    const produto = document.getElementById('produto').value;
    const quantidade = parseInt(document.getElementById('quantidade').value);
    const precoUnitario = parseFloat(document.getElementById('preco').value);

    if (!produto || isNaN(quantidade) || quantidade <= 0 || isNaN(precoUnitario) || precoUnitario <= 0) {
        alert('Por favor, preencha todos os campos corretamente.');
        return;
    }

    // CORREÇÃO: Calculamos o valor total correto (quantidade * preço unitário)
    const valorCalculado = quantidade * precoUnitario;

    const novoPedido = {
        nome: produto,
        quantidade: quantidade,
        valorTotal: valorCalculado
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
            document.getElementById('produto').value = '';
            document.getElementById('quantidade').value = '1';
            document.getElementById('preco').value = '';
            carregarPedidos(); 
        } else {
            const erroTxt = await response.text();
            console.error('Erro do servidor:', erroTxt);
            alert('Erro ao cadastrar pedido no servidor.');
        }
    } catch (error) {
        console.error('Erro de rede:', error);
        alert('Erro de conexão com o API Gateway.');
    }
}