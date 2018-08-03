package br.com.viavarejo.application.service.impl;

import static br.com.viavarejo.util.DateUtils.asDate;
import static br.com.viavarejo.util.Utils.isNull;
import static br.com.viavarejo.util.Utils.isNullOrEmpty;
import static br.com.viavarejo.util.Utils.not;
import static java.lang.String.format;
import static java.time.temporal.ChronoUnit.MONTHS;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.viavarejo.application.exception.NaoEncontradoException;
import br.com.viavarejo.application.exception.ValidacaoException;
import br.com.viavarejo.application.service.LoteCobrancaService;
import br.com.viavarejo.domain.model.mongodb.cobranca.LoteCobranca;
import br.com.viavarejo.domain.repository.LoteCobrancaRepository;

@Service
public class LoteCobrancaServiceImpl implements LoteCobrancaService {

    @Autowired
    private LoteCobrancaRepository repository;

    @Override
    public LoteCobranca criar(final LoteCobranca loteCobranca) {
        if (isNull(loteCobranca)) {
            throw new ValidacaoException("Adicione um objeto para ser criado");
        }
        loteCobranca.prepararSincronizacao();
        return this.repository.insert(loteCobranca);
    }

    @Override
    public LoteCobranca recuperarNumeroFatura(final Integer numeroFatura) {
        if (isNullOrEmpty(numeroFatura)) {
            throw new ValidacaoException(format("Número fatura / boleto %1$s inválido.", numeroFatura));
        }

        final LoteCobranca loteCobranca = this.repository.findByNumeroFatura(numeroFatura);

        if (isNull(loteCobranca)) {
            throw new NaoEncontradoException(format("Lote cobrança de número de fatura / boleto %1$s não encontrado.", numeroFatura));
        }
        return loteCobranca;
    }

    @Override
    public LoteCobranca recuperarId(final String id) {
        if (isNullOrEmpty(id) || not(ObjectId.isValid(id))) {
            throw new ValidacaoException(format("Id %1$s inválido.", id));
        }

        final Optional<LoteCobranca> loteCobranca = this.repository.findById(new ObjectId(id));

        if (!loteCobranca.isPresent()) {
            throw new NaoEncontradoException(format("Lote cobrança de id %1$s não encontrado.", id));
        }
        return loteCobranca.get();
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
    public List<LoteCobranca> listarSincronizacaoMainframe() {
        final List<LoteCobranca> lotes = this.repository.findBySincronizacaoMainframeIs(Boolean.TRUE);

        if (lotes.isEmpty()) {
            throw new NaoEncontradoException("Não existem lote de cobranças à integrar.");
        }
        return lotes;
    }

    @Override
    public void removerLotesFlagSincronizacaoDesligada() {
        this.repository.deleteBySincronizacaoMainframeIs(Boolean.FALSE);
    }

    @Override
    public LoteCobranca atualizar(final LoteCobranca loteCobranca) {
        if (isNull(loteCobranca)) {
            throw new ValidacaoException("Adicione ao menos um objeto para ser atualizado");
        }
        return this.repository.save(loteCobranca);
    }
}
