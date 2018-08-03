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
import br.com.viavarejo.application.service.RessarcimentoService;
import br.com.viavarejo.domain.model.mongodb.cobranca.Ressarcimento;
import br.com.viavarejo.domain.repository.RessarcimentoRepository;

@Service
public class RessarcimentoServiceImpl implements RessarcimentoService {

    @Autowired
    private RessarcimentoRepository repository;

    @Override
    public List<Ressarcimento> criar(final List<Ressarcimento> lista) {

        if (isNull(lista) || isEmpty(lista)) {
            throw new ValidacaoException("Adicione ao menos um objeto para ser criado");
        }
        this.prepararIntegracoes(lista);
        return this.repository.insert(lista);
    }

    @Override
    public Ressarcimento criarRessarcimento(final Ressarcimento ressarcimento) {

        if (isNull(ressarcimento)) {
            throw new ValidacaoException("Adicione um objeto para ser criado");
        }
        this.prepararIntegracao(ressarcimento);
        return this.repository.insert(ressarcimento);
    }

    @Override
    public List<Ressarcimento> atualizar(final List<Ressarcimento> lista) {

        if (isNull(lista) || isEmpty(lista)) {
            throw new ValidacaoException("Adicione ao menos um objeto para ser atualizado");
        }
        this.prepararIntegracoes(lista);
        return this.repository.saveAll(lista);
    }

    @Override
    public Ressarcimento atualizarRessarcimento(final Ressarcimento ressarcimento) {

        if (isNull(ressarcimento)) {
            throw new ValidacaoException("Adicione um objeto para ser atualizado");
        }
        this.prepararIntegracao(ressarcimento);
        return this.repository.save(ressarcimento);
    }

    @Override
    public List<Ressarcimento> listar() {

        final List<Ressarcimento> lista = this.repository.findAll();

        if (isEmpty(lista)) {
            throw new NaoEncontradoException("Lista vázia.");
        }
        return lista;
    }

    @Override
    public Ressarcimento recuperar(final Long pk) {

        if (isEmpty(pk)) {
            throw new ValidacaoException("Identificador não informado.");
        }

        final Ressarcimento obj = this.repository.findByPk(pk);

        if (isNull(obj)) {
            throw new NaoEncontradoException(format("Ressarcimento %1$s não encontrado.", pk.toString()));
        }
        return obj;
    }

    @Override
    public void deletar(final Long pk) {

        if (isEmpty(pk)) {
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

    @Override
    public void prepararIntegracoes(final List<Ressarcimento> entidades) {
        entidades.forEach(Ressarcimento::visivelIntelipost);
    }

    @Override
    public void prepararIntegracao(final Ressarcimento entidade) {
        entidade.visivelIntelipost();
    }
}
