package br.com.viavarejo.application.service;

import java.util.List;

import br.com.viavarejo.domain.model.mongodb.LogAplicacao;

public interface LogAplicacaoService {

    List<LogAplicacao> listar();

    LogAplicacao recuperar(final String nomeProcesso);

    void deletarAntigos(final Integer mesesAtras);
}
