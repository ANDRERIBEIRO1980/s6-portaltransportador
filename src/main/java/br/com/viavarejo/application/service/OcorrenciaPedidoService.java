package br.com.viavarejo.application.service;

import java.util.List;

import br.com.viavarejo.domain.model.mongodb.ocorrencia.OcorrenciaPedido;

public interface OcorrenciaPedidoService extends PreparacaoIntegracao<OcorrenciaPedido> {

    List<OcorrenciaPedido> criar(List<OcorrenciaPedido> lista);

    OcorrenciaPedido criarOcorrencia(OcorrenciaPedido ocorrencia);

    List<OcorrenciaPedido> atualizar(List<OcorrenciaPedido> lista);

    OcorrenciaPedido atualizarOcorrencia(OcorrenciaPedido ocorrencia);

    List<OcorrenciaPedido> listar();

    OcorrenciaPedido recuperar(final String pk);

    void deletar(String pk);

    void deletar();

    void deletar(List<OcorrenciaPedido> lista);

    void deletarAntigos(final Integer mesesAtras);
}
