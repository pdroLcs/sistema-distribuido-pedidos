const API_URL = 'http://localhost:8080/pedidos'; 
const NOTIFICACOES_API_URL = 'http://localhost:8080/notificacoes';

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

    const btnNotificacoes = document.getElementById('notificacoesBtn');
    if (btnNotificacoes) {
        btnNotificacoes.addEventListener('click', alternarNotificacoes);
    }
});

async function alternarNotificacoes() {
    const botao = document.getElementById('notificacoesBtn');
    const secao = document.getElementById('notificacoesSection');
    const status = document.getElementById('notificacoesStatus');
    const lista = document.getElementById('notificacoesList');

    const mostrar = secao.hidden;
    secao.hidden = !mostrar;
    botao.setAttribute('aria-expanded', String(mostrar));
    botao.textContent = mostrar ? 'Ocultar notificações' : 'Visualizar notificações';

    if (!mostrar) return;

    status.textContent = 'Carregando notificações...';
    lista.replaceChildren();

    try {
        const response = await fetch(NOTIFICACOES_API_URL);
        if (!response.ok) throw new Error('Erro ao buscar notificações');

        const notificacoes = await response.json();
        if (!Array.isArray(notificacoes)) throw new Error('Resposta inválida ao buscar notificações');

        status.textContent = notificacoes.length ? '' : 'Nenhuma notificação encontrada.';
        notificacoes.forEach(notificacao => {
            const item = document.createElement('li');
            const mensagem = document.createElement('p');
            mensagem.textContent = notificacao.mensagem || 'Notificação sem mensagem';
            item.appendChild(mensagem);

            const detalhes = document.createElement('small');
            const data = notificacao.criadaEm ? new Date(notificacao.criadaEm) : null;
            const dataFormatada = data && !Number.isNaN(data.getTime())
                ? data.toLocaleString('pt-BR')
                : 'Data indisponível';
            detalhes.textContent = `Pedido #${notificacao.pedidoId || 'N/D'} · ${dataFormatada}`;
            item.appendChild(detalhes);
            lista.appendChild(item);
        });
    } catch (error) {
        console.error('Erro ao carregar notificações:', error);
        status.textContent = 'Não foi possível carregar as notificações. Tente novamente.';
    }
}

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