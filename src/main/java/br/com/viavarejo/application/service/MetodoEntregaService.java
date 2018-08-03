package br.com.viavarejo.application.service;

import java.util.List;

import br.com.viavarejo.domain.model.mongodb.MetodoEntrega;

public interface MetodoEntregaService {

    List<MetodoEntrega> criar(List<MetodoEntrega> lista);

    MetodoEntrega criarMetodo(MetodoEntrega metodo);

    List<MetodoEntrega> atualizar(List<MetodoEntrega> lista);

    MetodoEntrega atualizarMetodo(MetodoEntrega metodo);

    List<MetodoEntrega> listar();

    MetodoEntrega recuperar(final Integer codigo);

    void deletar(Integer codigo);

    void deletar();

    void deletarAntigos(final Integer mesesAtras);
}
