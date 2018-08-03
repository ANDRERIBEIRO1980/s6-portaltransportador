package br.com.viavarejo.application.service;

import java.util.List;

import br.com.viavarejo.domain.model.mongodb.kitcoleta.PedidoKitColeta;

public interface PedidoKitColetaService extends PreparacaoIntegracao<PedidoKitColeta> {

    PedidoKitColeta criarReversa(PedidoKitColeta reversa);

    List<PedidoKitColeta> atualizar(List<PedidoKitColeta> lista);

    List<PedidoKitColeta> listar();

    PedidoKitColeta recuperar(final Long codigoKitColeta);

	String baixarArquivoZipFtp(String diretorio, String arquivo, String pedido);

	void descompactaArquivoZip(String arquivo, PedidoKitColeta reversa);

    void deletar(final Long codigoKitColeta);

    void deletar();

    void deletarAntigos(final Integer mesesAtras);
}
