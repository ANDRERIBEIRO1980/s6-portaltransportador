package br.com.viavarejo.application.service;

import java.util.List;

import br.com.viavarejo.domain.model.mongodb.filial.Filial;
import br.com.viavarejo.domain.model.mongodb.filial.FilialPK;

public interface FilialService {

    List<Filial> criar(List<Filial> lista);

    Filial criarFilial(Filial filial);

    List<Filial> atualizar(List<Filial> lista);

    Filial atualizarFilial(Filial filial);

    List<Filial> listar();

    Filial recuperar(final FilialPK pk);

    void deletar(FilialPK pk);

    void deletar();

    void deletarAntigos(final Integer mesesAtras);
}
