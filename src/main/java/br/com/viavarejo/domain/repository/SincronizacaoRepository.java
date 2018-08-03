package br.com.viavarejo.domain.repository;

import java.io.Serializable;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.DeleteQuery;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import br.com.viavarejo.domain.model.mongodb.sincronizacao.SincronizacaoProcesso;

@Repository
public interface SincronizacaoRepository extends MongoRepository<SincronizacaoProcesso, String>, Serializable {

    SincronizacaoProcesso findByProcesso(String nomePrecesso);

    void deleteByProcesso(String nomeProcesso);

    @DeleteQuery("{ id: {$lte: ?0}}")
    void deleteByObjectIdOld(ObjectId id);
}
