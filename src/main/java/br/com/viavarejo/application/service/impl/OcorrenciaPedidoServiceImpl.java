package br.com.viavarejo.application.service.impl;

import static br.com.viavarejo.util.DateUtils.asDate;
import static br.com.viavarejo.util.Utils.isEmpty;
import static br.com.viavarejo.util.Utils.isNull;
import static java.lang.String.format;
import static java.time.temporal.ChronoUnit.MONTHS;

import java.time.LocalDate;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.viavarejo.application.exception.NaoEncontradoException;
import br.com.viavarejo.application.exception.ValidacaoException;
import br.com.viavarejo.application.service.OcorrenciaPedidoService;
import br.com.viavarejo.domain.model.enumerator.TipoSistemaEnum;
import br.com.viavarejo.domain.model.mongodb.ocorrencia.EventoPedido;
import br.com.viavarejo.domain.model.mongodb.ocorrencia.OcorrenciaPedido;
import br.com.viavarejo.domain.model.mongodb.transportador.TransportadorAtivoIntelipost;
import br.com.viavarejo.domain.model.mongodb.transportador.TransportadorPK;
import br.com.viavarejo.domain.repository.OcorrenciaPedidoRepository;
import br.com.viavarejo.domain.repository.TransportadorAtivoIntelipostRepository;

@Service
public class OcorrenciaPedidoServiceImpl implements OcorrenciaPedidoService {

    @Autowired
    private OcorrenciaPedidoRepository repository;

    @Autowired
    private TransportadorAtivoIntelipostRepository transportadorAtivoIntelipostRep;

    @Override
    public List<OcorrenciaPedido> criar(final List<OcorrenciaPedido> lista) {

        if (isNull(lista) || isEmpty(lista)) {
            throw new ValidacaoException("Adicione ao menos um objeto para ser criado");
        }

        this.prepararIntegracoes(lista);
        return this.repository.insert(lista);
    }

    @Override
    public OcorrenciaPedido criarOcorrencia(final OcorrenciaPedido ocorrencia) {

        if (isNull(ocorrencia)) {
            throw new ValidacaoException("Adicione um objeto para ser criado");
        }

        this.prepararIntegracao(ocorrencia);
        return this.repository.insert(ocorrencia);
    }

    @Override
    public List<OcorrenciaPedido> atualizar(final List<OcorrenciaPedido> lista) {

        if (isNull(lista) || isEmpty(lista)) {
            throw new ValidacaoException("Adicione ao menos um objeto para ser atualizado");
        }

        this.prepararIntegracoes(lista);
        return this.repository.saveAll(lista);
    }

    @Override
    public OcorrenciaPedido atualizarOcorrencia(final OcorrenciaPedido ocorrencia) {

        if (isNull(ocorrencia)) {
            throw new ValidacaoException("Adicione ao menos um objeto para ser atualizado");
        }

        this.prepararIntegracao(ocorrencia);
        return this.repository.save(ocorrencia);
    }

    @Override
    public List<OcorrenciaPedido> listar() {

        final List<OcorrenciaPedido> lista = this.repository.findAll();

        if (isEmpty(lista)) {
            throw new NaoEncontradoException("Lista vazia.");
        }
        return lista;
    }

    @Override
    public OcorrenciaPedido recuperar(final String pk) {

        if (pk.isEmpty()) {
            throw new ValidacaoException("Identificador não informado.");
        }

        final OcorrenciaPedido obj = this.repository.findByCodigoPedido(pk);

        if (isNull(obj)) {
            throw new NaoEncontradoException(format("OcorrenciaPedido %1$s não encontrada.", pk));
        }
        return obj;
    }

    @Override
    public void deletar(final String pk) {

        if (pk.isEmpty()) {
            throw new ValidacaoException("Identificador não informado.");
        }

        this.repository.deleteByCodigoPedido(pk);
    }

    @Override
    public void deletar(final List<OcorrenciaPedido> lista) {

        if (isNull(lista) || isEmpty(lista)) {
            throw new ValidacaoException("Adicione ao menos um objeto para ser removido");
        }

        this.repository.deleteAll(lista);
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

    @Override
    public void prepararIntegracoes(final List<OcorrenciaPedido> entidades) {
        entidades.forEach(ocorrencia -> this.atualizaFlagIntegracaoIntelipost(ocorrencia));
    }

    @Override
    public void prepararIntegracao(final OcorrenciaPedido entidade) {
        this.atualizaFlagIntegracaoIntelipost(entidade);
    }

    /**
     * Verifica se a transportadora da Ocorrencia esta cadastrada na Intelipost, se nao estiver
     * cadastrada, envia todos seus Eventos, caso contrário, envia somente eventos do sistema S6.
     */
    private void atualizaFlagIntegracaoIntelipost(final OcorrenciaPedido ocorrencia) {

        final TransportadorAtivoIntelipost transportadorAtivoIntelipost = this.verificaTransportadoraAtivaIntelipost(ocorrencia);

        if (transportadorAtivoIntelipost == null) {
            ocorrencia.getEventos().forEach(EventoPedido::visivelIntelipost);
        } else {
            ocorrencia.getEventos().stream().filter(evento -> evento.getSistemaOrigem().equals(TipoSistemaEnum.S6.name())).forEach(
                            EventoPedido::visivelIntelipost);
        }
    }

    private TransportadorAtivoIntelipost verificaTransportadoraAtivaIntelipost(final OcorrenciaPedido ocorrencia) {

        final TransportadorPK transportadoraPK = ocorrencia.getTransportadoraPK();
        if (isNull(transportadoraPK)) {
            return null;
        }
        return this.transportadorAtivoIntelipostRep.findByCodigoEmpresaAndCodigo(transportadoraPK.getCodigoEmpresa(), transportadoraPK
                        .getCodigo());
    }
}
