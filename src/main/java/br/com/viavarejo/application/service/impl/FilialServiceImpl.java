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
import br.com.viavarejo.application.service.FilialService;
import br.com.viavarejo.domain.model.mongodb.filial.Filial;
import br.com.viavarejo.domain.model.mongodb.filial.FilialPK;
import br.com.viavarejo.domain.repository.FilialRepository;

@Service
public class FilialServiceImpl implements FilialService {

    @Autowired
    private FilialRepository repository;

    @Override
    public List<Filial> criar(final List<Filial> lista) {

        if (isNull(lista) || isEmpty(lista)) {
            throw new ValidacaoException("Adicione ao menos um objeto para ser criado");
        }
        lista.forEach(Filial::prepararEntidade);
        return this.repository.insert(lista);
    }

    @Override
    public Filial criarFilial(final Filial filial) {
        if (isNull(filial)) {
            throw new ValidacaoException("Adicione um objeto para ser criado");
        }
        filial.prepararEntidade();
        return this.repository.insert(filial);
    }

    @Override
    public List<Filial> atualizar(final List<Filial> lista) {

        if (isNull(lista) || isEmpty(lista)) {
            throw new ValidacaoException("Adicione ao menos um objeto para ser atualizado");
        }
        lista.forEach(Filial::prepararEntidade);
        return this.repository.saveAll(lista);
    }

    @Override
    public Filial atualizarFilial(final Filial filial) {
        if (isNull(filial)) {
            throw new ValidacaoException("Adicione um objeto para ser atualizado");
        }
        filial.prepararEntidade();
        return this.repository.save(filial);
    }

    @Override
    public List<Filial> listar() {

        final List<Filial> lista = this.repository.findAll();

        if (isEmpty(lista)) {
            throw new NaoEncontradoException("Lista vázia.");
        }
        return lista;
    }

    @Override
    public Filial recuperar(final FilialPK pk) {

        if (isNull(pk) || isNull(pk.getEmpresa()) || isNull(pk.getCodigo())) {
            throw new ValidacaoException("Identificador não informado.");
        }

        final Filial obj = this.repository.findByPk(pk);

        if (isNull(obj)) {
            throw new NaoEncontradoException(format("Filial %1$s não encontrada.", pk.toString()));
        }
        return obj;
    }

    @Override
    public void deletar(final FilialPK pk) {

        if (isNull(pk) || isNull(pk.getEmpresa()) || isNull(pk.getCodigo())) {
            throw new ValidacaoException("Identificador não informado.");
        }
        this.repository.deleteByPk(pk);
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
