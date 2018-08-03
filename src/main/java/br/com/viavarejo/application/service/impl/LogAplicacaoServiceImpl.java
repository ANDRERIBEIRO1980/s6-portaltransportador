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
import br.com.viavarejo.application.service.LogAplicacaoService;
import br.com.viavarejo.domain.model.mongodb.LogAplicacao;
import br.com.viavarejo.domain.repository.LogAplicacaoRepository;

@Service
public class LogAplicacaoServiceImpl implements LogAplicacaoService {

    @Autowired
    private LogAplicacaoRepository repository;

    @Override
    public List<LogAplicacao> listar() {

        final List<LogAplicacao> lista = this.repository.findAll();

        if (isEmpty(lista)) {
            throw new NaoEncontradoException("Lista vázia.");
        }
        return lista;
    }

    @Override
    public LogAplicacao recuperar(final String nomeProcesso) {

        if (isEmpty(nomeProcesso)) {
            throw new ValidacaoException("Identificador não informado.");
        }

        final LogAplicacao obj = this.repository.findByProcesso(nomeProcesso);

        if (isNull(obj)) {
            throw new NaoEncontradoException(format("LogAplicacao com data %1$s não encontrado.", nomeProcesso));
        }
        return obj;
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
