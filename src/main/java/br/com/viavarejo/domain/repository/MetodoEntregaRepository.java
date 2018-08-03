package br.com.viavarejo.domain.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.DeleteQuery;
import org.springframework.data.mongodb.repository.MongoRepository;

import br.com.viavarejo.domain.model.mongodb.MetodoEntrega;

public interface MetodoEntregaRepository extends MongoRepository<MetodoEntrega, ObjectId> {

    MetodoEntrega findByCodigo(Integer codigo);

    void deleteByCodigo(Integer codigo);

    @DeleteQuery("{ id: {$lte: ?0}}")
    void deleteByObjectIdOld(ObjectId id);
}
