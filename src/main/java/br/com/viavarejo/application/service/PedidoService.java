package br.com.viavarejo.application.service;

import java.util.List;

import br.com.viavarejo.domain.model.mongodb.pedido.Pedido;

public interface PedidoService extends PreparacaoIntegracao<Pedido> {

    List<Pedido> criar(List<Pedido> lista);

    Pedido criarPedido(Pedido pedido);

    List<Pedido> atualizar(List<Pedido> lista);

    Pedido atualizarPedido(Pedido pedido);

    List<Pedido> listar();

    Pedido recuperar(final Long codigoPedido);

    void deletar(final Long codigoPedido);

    void deletar();

    void deletarAntigos(final Integer mesesAtras);
}
