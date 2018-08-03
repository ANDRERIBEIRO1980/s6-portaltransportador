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
import br.com.viavarejo.application.service.TransportadorService;
import br.com.viavarejo.domain.model.enumerator.TipoAcaoRegistroEnum;
import br.com.viavarejo.domain.model.mongodb.transportador.Transportador;
import br.com.viavarejo.domain.model.mongodb.transportador.TransportadorPK;
import br.com.viavarejo.domain.repository.TransportadorRepository;

@Service
public class TransportadorServiceImpl implements TransportadorService {

    @Autowired
    private TransportadorRepository repository;

    @Override
    public List<Transportador> criar(final List<Transportador> lista) {

        if (isNull(lista) || isEmpty(lista)) {
            throw new ValidacaoException("Adicione ao menos um objeto para ser criado");
        }
        this.prepararTransportadorIntegracao(lista, TipoAcaoRegistroEnum.NOVO);
        return this.repository.insert(lista);
    }

    @Override
    public Transportador criarTransportador(final Transportador transportador) {

        if (isNull(transportador)) {
            throw new ValidacaoException("Adicione um objeto para ser criado");
        }
        this.prepararTransportadorIntegracao(transportador, TipoAcaoRegistroEnum.NOVO);
        return this.repository.insert(transportador);
    }

    @Override
    public List<Transportador> atualizar(final List<Transportador> lista) {

        if (isNull(lista) || isEmpty(lista)) {
            throw new ValidacaoException("Adicione ao menos um objeto para ser atualizado");
        }
        this.prepararTransportadorIntegracao(lista, TipoAcaoRegistroEnum.ATUALIZACAO);
        return this.repository.saveAll(lista);
    }

    @Override
    public Transportador atualizarTransportador(final Transportador transportador) {

        if (isNull(transportador)) {
            throw new ValidacaoException("Adicione um objeto para ser atualizado");
        }
        this.prepararTransportadorIntegracao(transportador, TipoAcaoRegistroEnum.ATUALIZACAO);
        return this.repository.save(transportador);
    }

    @Override
    public List<Transportador> listar() {

        final List<Transportador> lista = this.repository.findAll();

        if (isEmpty(lista)) {
            throw new NaoEncontradoException("Lista vázia.");
        }
        return lista;
    }

    @Override
    public Transportador recuperar(final TransportadorPK pk) {

        if (isNull(pk) || isNull(pk.getCodigoEmpresa()) || isNull(pk.getCodigo())) {
            throw new ValidacaoException("Identificador não informado.");
        }

        final Transportador obj = this.repository.findByPk(pk);

        if (isNull(obj)) {
            throw new NaoEncontradoException(format("Transportador %1$s não encontrada.", pk.toString()));
        }
        return obj;
    }

    @Override
    public void deletar(final TransportadorPK pk) {

        if (isNull(pk) || isNull(pk.getCodigoEmpresa()) || isNull(pk.getCodigo())) {
            throw new ValidacaoException("Identificador não informado.");
        }

        this.repository.deleteByPk(pk);
    }

    @Override
    public void deletar() {
        this.repository.deleteAll();
    }

    private void prepararTransportadorIntegracao(final List<Transportador> transportadoras, final TipoAcaoRegistroEnum acao) {
        transportadoras.forEach(t -> t.prepararEntidade(acao));
    }

    private void prepararTransportadorIntegracao(final Transportador transportador, final TipoAcaoRegistroEnum acao) {
        transportador.prepararEntidade(acao);
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
}
