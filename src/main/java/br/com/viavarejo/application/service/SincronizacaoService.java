package br.com.viavarejo.application.service;

import java.util.List;

import br.com.viavarejo.domain.model.mongodb.sincronizacao.SincronizacaoProcesso;

public interface SincronizacaoService {

    List<SincronizacaoProcesso> criar(List<SincronizacaoProcesso> lista);

    SincronizacaoProcesso criarSincronizacao(SincronizacaoProcesso processo);

    List<SincronizacaoProcesso> atualizar(List<SincronizacaoProcesso> lista);

    SincronizacaoProcesso atualizarSincronizacao(SincronizacaoProcesso processo);

    List<SincronizacaoProcesso> listar();

    SincronizacaoProcesso recuperar(final String nomeProcesso);

    void deletar(String nomeProcesso);

    void deletar();

    void deletarAntigos(final Integer mesesAtras);
}
