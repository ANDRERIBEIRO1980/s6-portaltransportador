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
import br.com.viavarejo.application.service.SincronizacaoService;
import br.com.viavarejo.domain.model.mongodb.sincronizacao.SincronizacaoProcesso;
import br.com.viavarejo.domain.repository.SincronizacaoRepository;

@Service
public class SincronizacaoServiceImpl implements SincronizacaoService {

    @Autowired
    private SincronizacaoRepository repository;

    @Override
    public List<SincronizacaoProcesso> criar(final List<SincronizacaoProcesso> lista) {

        if (isNull(lista) || isEmpty(lista)) {
            throw new ValidacaoException("Adicione ao menos um objeto para ser criado");
        }
        return this.repository.insert(lista);
    }

    @Override
    public SincronizacaoProcesso criarSincronizacao(final SincronizacaoProcesso processo) {

        if (isNull(processo)) {
            throw new ValidacaoException("Adicione um objeto para ser criado");
        }
        return this.repository.insert(processo);
    }

    @Override
    public List<SincronizacaoProcesso> atualizar(final List<SincronizacaoProcesso> lista) {

        if (isNull(lista) || isEmpty(lista)) {
            throw new ValidacaoException("Adicione ao menos um objeto para ser atualizado");
        }
        return this.repository.saveAll(lista);
    }

    @Override
    public SincronizacaoProcesso atualizarSincronizacao(final SincronizacaoProcesso processo) {

        if (isNull(processo)) {
            throw new ValidacaoException("Adicione um objeto para ser atualizado");
        }
        return this.repository.save(processo);
    }

    @Override
    public List<SincronizacaoProcesso> listar() {

        final List<SincronizacaoProcesso> lista = this.repository.findAll();

        if (isEmpty(lista)) {
            throw new NaoEncontradoException("Lista vazia.");
        }
        return lista;
    }

    @Override
    public SincronizacaoProcesso recuperar(final String nomeProcesso) {

        if (isNull(nomeProcesso) || nomeProcesso.isEmpty()) {
            throw new ValidacaoException("Identificador não informado.");
        }

        final SincronizacaoProcesso obj = this.repository.findByProcesso(nomeProcesso);

        if (isNull(obj)) {
            throw new NaoEncontradoException(format("Sincronização do processo %1$s não encontrado.", nomeProcesso));
        }
        return obj;
    }

    @Override
    public void deletar(final String nomeProcesso) {

        if (isNull(nomeProcesso) || nomeProcesso.isEmpty()) {
            throw new ValidacaoException("Identificador não informado.");
        }

        this.repository.deleteByProcesso(nomeProcesso);
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
}
