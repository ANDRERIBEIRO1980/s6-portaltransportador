package br.com.viavarejo.application.service;

import java.util.List;

import br.com.viavarejo.domain.model.mongodb.cobranca.LoteCobranca;

public interface LoteCobrancaService {

    public LoteCobranca criar(LoteCobranca loteCobranca);

    public LoteCobranca recuperarNumeroFatura(Integer numeroFatura);

    public LoteCobranca recuperarId(String id);

    void deletarAntigos(final Integer mesesAtras);

    public List<LoteCobranca> listarSincronizacaoMainframe();

    public void removerLotesFlagSincronizacaoDesligada();

    public LoteCobranca atualizar(LoteCobranca loteCobranca);
}
