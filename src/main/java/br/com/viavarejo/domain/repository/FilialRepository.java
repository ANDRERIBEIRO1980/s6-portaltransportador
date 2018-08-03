package br.com.viavarejo.domain.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.DeleteQuery;
import org.springframework.data.mongodb.repository.MongoRepository;

import br.com.viavarejo.domain.model.mongodb.filial.Filial;
import br.com.viavarejo.domain.model.mongodb.filial.FilialPK;

public interface FilialRepository extends MongoRepository<Filial, ObjectId> {

    Filial findByPk(FilialPK pk);

    void deleteByPk(FilialPK pk);

    @DeleteQuery("{ id: {$lte: ?0}}")
    void deleteByObjectIdOld(ObjectId id);

}
