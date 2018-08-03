package br.com.viavarejo.domain.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.DeleteQuery;
import org.springframework.data.mongodb.repository.MongoRepository;

import br.com.viavarejo.domain.model.mongodb.transportador.Transportador;
import br.com.viavarejo.domain.model.mongodb.transportador.TransportadorPK;

public interface TransportadorRepository extends MongoRepository<Transportador, ObjectId> {

    public Transportador findByPk(TransportadorPK pk);

    public void deleteByPk(TransportadorPK pk);

    @DeleteQuery("{ id: {$lte: ?0}}")
    void deleteByObjectIdOld(ObjectId id);
}
