package br.com.viavarejo.application.service;

import br.com.viavarejo.domain.model.mongodb.kitcoleta.PedidoKitColeta;

public interface ArquivoRecuperavelService {

    /**
     * Metodo responsavel por recuperar um arquivo em Base64. Esse metodo deve ser utilizado de
     * forma asincrona, utilizando a annotation {@code Async} na assinatura do metodo.</br>
     * Caso seja necessário recuperar varios arquivos de forma asincrono basta proceder da seguinte
     * forma:
     */

	String baixarArquivoZipFtp(String diretorioArquivo, String nomeArquivo, String pedido);

	void descompactaArquivoZip(String arquivo, PedidoKitColeta reversa);

	void deletarArquivosTemporarios(final String diretorio);
}
