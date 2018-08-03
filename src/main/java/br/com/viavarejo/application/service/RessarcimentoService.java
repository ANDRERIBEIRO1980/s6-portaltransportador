package br.com.viavarejo.application.service;

import java.util.List;

import br.com.viavarejo.domain.model.mongodb.cobranca.Ressarcimento;

public interface RessarcimentoService extends PreparacaoIntegracao<Ressarcimento> {

    List<Ressarcimento> criar(List<Ressarcimento> lista);

    Ressarcimento criarRessarcimento(Ressarcimento ressarcimento);

    List<Ressarcimento> atualizar(List<Ressarcimento> lista);

    Ressarcimento atualizarRessarcimento(Ressarcimento ressarcimento);

    List<Ressarcimento> listar();

    Ressarcimento recuperar(final Long pk);

    void deletar(Long pk);

    void deletar();

    void deletarAntigos(final Integer mesesAtras);
}
