package br.com.viavarejo.application.service;

import java.util.List;

import br.com.viavarejo.domain.model.mongodb.transportador.Transportador;
import br.com.viavarejo.domain.model.mongodb.transportador.TransportadorPK;

public interface TransportadorService {

    List<Transportador> criar(List<Transportador> lista);

    Transportador criarTransportador(Transportador transportador);

    List<Transportador> atualizar(List<Transportador> lista);

    Transportador atualizarTransportador(Transportador transportador);

    List<Transportador> listar();

    Transportador recuperar(TransportadorPK pk);

    void deletar(TransportadorPK pk);

    void deletar();

    void deletarAntigos(Integer mesesAtras);
}
