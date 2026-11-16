package com.pastelaria.familia.service;

import com.pastelaria.familia.dto.PedidoDTO;
import com.pastelaria.familia.entity.Entrega;
import com.pastelaria.familia.entity.ItemPedido;
import com.pastelaria.familia.entity.Pedido;
import com.pastelaria.familia.entity.Produto;
import com.pastelaria.familia.entity.Usuario;
import com.pastelaria.familia.exception.ResourceNotFoundException;
import com.pastelaria.familia.repository.EntregaRepository;
import com.pastelaria.familia.repository.ItemPedidoRepository;
import com.pastelaria.familia.repository.PedidoRepository;
import com.pastelaria.familia.repository.ProdutoRepository;
import com.pastelaria.familia.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ItemPedidoRepository itemPedidoRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private EntregaRepository entregaRepository;

    public List<Pedido> listarTodos() {
        return pedidoRepository.findAll();
    }

    public List<Pedido> listarPorUsuario(String usuarioId) {
        return pedidoRepository.findByUsuarioId(usuarioId);
    }

    public Optional<Pedido> buscarPorId(String id) {
        return pedidoRepository.findById(id);
    }

    @Transactional
    public Pedido criarPedido(PedidoDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("Dados do pedido não podem ser nulos.");
        }
        if (dto.getItens() == null || dto.getItens().isEmpty()) {
            throw new IllegalArgumentException("O pedido deve conter ao menos um item.");
        }

        // Valida ou associa a um usuário existente no banco para respeitar a Foreign Key
        String usuarioId = dto.getUsuarioId();
        if (usuarioId != null && !usuarioId.isBlank()) {
            if (!usuarioRepository.existsById(usuarioId)) {
                throw new ResourceNotFoundException("Usuário informado não foi encontrado com o ID: " + usuarioId);
            }
        } else {
            // Caso seja pedido anônimo/balcão (sem usuarioId), utiliza o primeiro usuário cliente para respeitar a integridade referencial
            usuarioId = usuarioRepository.findAll().stream()
                    .filter(u -> !"admin".equalsIgnoreCase(u.getTipo()))
                    .findFirst()
                    .map(Usuario::getId)
                    .orElseGet(() -> usuarioRepository.findAll().stream()
                            .findFirst()
                            .map(Usuario::getId)
                            .orElseThrow(() -> new ResourceNotFoundException("Nenhum usuário cadastrado no banco para vincular ao pedido.")));
        }

        // 1. Valida os itens, busca os produtos e calcula o valor total
        BigDecimal valorTotal = BigDecimal.ZERO;
        List<ItemPedido> itensParaSalvar = new ArrayList<>();

        for (PedidoDTO.ItemPedidoDTO itemDto : dto.getItens()) {
            if (itemDto.getIdProduto() == null || itemDto.getIdProduto().isBlank()) {
                throw new IllegalArgumentException("ID do produto é obrigatório no item.");
            }
            int quantidade = (itemDto.getQuantidade() != null && itemDto.getQuantidade() > 0) ? itemDto.getQuantidade() : 1;

            Produto produto = produtoRepository.findById(itemDto.getIdProduto())
                    .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado com ID: " + itemDto.getIdProduto()));

            BigDecimal subtotal = produto.getPreco().multiply(BigDecimal.valueOf(quantidade));
            valorTotal = valorTotal.add(subtotal);

            ItemPedido itemPedido = new ItemPedido();
            itemPedido.setProdutoId(String.valueOf(produto.getId()));
            itemPedido.setDescricao(produto.getNome());
            itemPedido.setQuantidade(quantidade);
            itemPedido.setPrecoUnitario(produto.getPreco());

            itensParaSalvar.add(itemPedido);
        }

        // 2. Cria e salva o Pedido primeiro com todos os campos obrigatórios preenchidos (evita erros de valor_total nulo)
        Pedido pedido = new Pedido();
        pedido.setUsuarioId(usuarioId);
        pedido.setEnderecoEntrega(dto.getEnderecoEntrega() != null && !dto.getEnderecoEntrega().isBlank() ? dto.getEnderecoEntrega() : "Balcão / Retirada");
        pedido.setTipoEntrega(dto.getTipoEntrega() != null && !dto.getTipoEntrega().isBlank() ? dto.getTipoEntrega() : "DELIVERY");
        pedido.setFormaPagamento(dto.getFormaPagamento() != null && !dto.getFormaPagamento().isBlank() ? dto.getFormaPagamento() : "PIX");
        pedido.setTelefone(dto.getTelefone() != null ? dto.getTelefone() : "");
        pedido.setStatus("pendente");
        pedido.setValorTotal(valorTotal);

        Pedido pedidoSalvo = pedidoRepository.save(pedido);

        // 3. Salva os itens vinculando o ID do pedido já persistido no banco (respeita a Foreign Key)
        for (ItemPedido item : itensParaSalvar) {
            item.setPedidoId(pedidoSalvo.getId());
            itemPedidoRepository.save(item);
        }

        // 4. Se for entrega (DELIVERY), cria automaticamente o registro de rastreio de entrega
        if ("DELIVERY".equalsIgnoreCase(pedidoSalvo.getTipoEntrega())) {
            Entrega entrega = new Entrega();
            entrega.setPedidoId(pedidoSalvo.getId());
            entrega.setStatusEntrega("aguardando_envio");
            entregaRepository.save(entrega);
        }

        return pedidoSalvo;
    }

    public Pedido salvar(Pedido pedido) {
        return pedidoRepository.save(pedido);
    }

    @Transactional
    public void deletar(String id) {
        // Remove entrega associada se existir
        entregaRepository.findByPedidoId(id).ifPresent(entregaRepository::delete);
        // Remove os itens do pedido
        List<ItemPedido> itens = itemPedidoRepository.findByPedidoId(id);
        if (!itens.isEmpty()) {
            itemPedidoRepository.deleteAll(itens);
        }
        // Remove o pedido
        pedidoRepository.deleteById(id);
    }
}
