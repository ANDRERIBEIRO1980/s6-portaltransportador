package br.com.viavarejo.application.service;

import java.util.List;

public interface PreparacaoIntegracao<T> {

    /**
     * Metodo responsavel por preparar as entidades para o {@code estado} de integração.
     *
     * @param entidades que terão os estados preparados para a integração
     */
    void prepararIntegracoes(List<T> entidades);

    /**
     * Metodo responsavel por preparar a entidade para o {@code estado} de integração.
     *
     * @param entidade que tera o estado preparado para a integração
     */
    void prepararIntegracao(T entidade);
}
