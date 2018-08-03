package br.com.viavarejo.domain.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.DeleteQuery;
import org.springframework.data.mongodb.repository.MongoRepository;

import br.com.viavarejo.domain.model.mongodb.cobranca.Ressarcimento;

public interface RessarcimentoRepository extends MongoRepository<Ressarcimento, Long> {

	Ressarcimento findByPk(Long pk);

    void deleteByPk(Long pk);

    @DeleteQuery("{ id: {$lte: ?0}}")
    void deleteByObjectIdOld(ObjectId id);
}