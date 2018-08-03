package br.com.viavarejo.application.service.impl;

import static br.com.viavarejo.util.DateUtils.asDate;
import static br.com.viavarejo.util.Utils.isEmpty;
import static br.com.viavarejo.util.Utils.isNotEmpty;
import static br.com.viavarejo.util.Utils.isNull;
import static java.lang.String.format;
import static java.time.temporal.ChronoUnit.MONTHS;

import java.time.LocalDate;
import java.util.List;

import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.viavarejo.application.exception.NaoEncontradoException;
import br.com.viavarejo.application.exception.ValidacaoException;
import br.com.viavarejo.application.service.ArquivoRecuperavelService;
import br.com.viavarejo.application.service.PedidoKitColetaService;
import br.com.viavarejo.domain.model.mongodb.kitcoleta.PedidoKitColeta;
import br.com.viavarejo.domain.repository.PedidoKitColetaRepository;

@Service
public class PedidoKitColetaServiceImpl implements PedidoKitColetaService {

    protected final Logger logger = Logger.getLogger(PedidoKitColetaServiceImpl.class);

    @Autowired
    private ArquivoRecuperavelService arquivoService;

    @Autowired
    private PedidoKitColetaRepository repository;

	@Override
    public PedidoKitColeta criarReversa(final PedidoKitColeta reversa) {

		if (isNull(reversa)) {
            throw new ValidacaoException("Adicione um objeto para ser criado");
        }

		final String arquivoZipTemp = this.baixarArquivoZipFtp(reversa.getDiretorioArquivoZip(),reversa.getNomeArquivoZip(), reversa.getNumeroPedidoOriginal());

		if (isNotEmpty(arquivoZipTemp)) {
			this.descompactaArquivoZip(arquivoZipTemp, reversa);
		}

		this.prepararIntegracao(reversa);

		return this.repository.insert(reversa);
    }

	@Override
	public String baixarArquivoZipFtp(final String diretorio, final String arquivo, final String pedido) {
		return this.arquivoService.baixarArquivoZipFtp(diretorio, arquivo, pedido);
	}

	@Override
	public void descompactaArquivoZip(final String arquivo, final PedidoKitColeta reversa) {
		this.arquivoService.descompactaArquivoZip(arquivo, reversa);
	}

    @Override
    public List<PedidoKitColeta> atualizar(final List<PedidoKitColeta> lista) {

        if (isNull(lista) || isEmpty(lista)) {
            throw new ValidacaoException("Adicione ao menos um objeto para ser atualizado");
        }

        this.prepararIntegracoes(lista);
        return this.repository.saveAll(lista);
    }

    @Override
    public List<PedidoKitColeta> listar() {

        final List<PedidoKitColeta> lista = this.repository.findAll();

        if (isEmpty(lista)) {
            throw new NaoEncontradoException("Lista vázia.");
        }
        return lista;
    }

    @Override
    public PedidoKitColeta recuperar(final Long codigoKitColeta) {

        if (isNull(codigoKitColeta)) {
            throw new ValidacaoException("Identificador não informado.");
        }

        final PedidoKitColeta obj = this.repository.findByCodigo(codigoKitColeta);

        if (isNull(obj)) {
            throw new NaoEncontradoException(format("Pedido do KitColeta de código %1$s não encontrado.", codigoKitColeta));
        }
        return obj;
    }

    @Override
    public void deletar(final Long codigoKitColeta) {

        if (isNull(codigoKitColeta)) {
            throw new ValidacaoException("Identificador não informado.");
        }

        this.repository.deleteByCodigo(codigoKitColeta);
    }

    @Override
    public void deletar() {
        this.repository.deleteAll();
    }

    @Override
    public void deletarAntigos(final Integer mesesAtras) {

        if (isNull(mesesAtras)) {
            throw new ValidacaoException("Parâmetro não informado.");
        }

        final LocalDate dateAgo = LocalDate.now().minus(mesesAtras, MONTHS);
        final ObjectId mongoId = new ObjectId(asDate(dateAgo));

        this.repository.deleteByObjectIdOld(mongoId);
    }

    /**
     * @see {@link PedidoKitColeta#prepararEntidade()}
     */
    @Override
    public void prepararIntegracoes(final List<PedidoKitColeta> reversas) {
        reversas.forEach(PedidoKitColeta::prepararEntidade);
    }

    /**
     * @see {@link PedidoKitColeta#prepararEntidade()}
     */
    @Override
    public void prepararIntegracao(final PedidoKitColeta reversa) {
        reversa.prepararEntidade();
    }



}
