package br.com.viavarejo.domain.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.DeleteQuery;
import org.springframework.data.mongodb.repository.MongoRepository;

import br.com.viavarejo.domain.model.mongodb.LogAplicacao;

public interface LogAplicacaoRepository extends MongoRepository<LogAplicacao, ObjectId> {

    LogAplicacao findByProcesso(String processo);

    @DeleteQuery("{ id: {$lte: ?0}}")
    void deleteByObjectIdOld(ObjectId id);

}
