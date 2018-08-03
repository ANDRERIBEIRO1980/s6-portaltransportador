package br.com.viavarejo.domain.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.DeleteQuery;
import org.springframework.data.mongodb.repository.MongoRepository;

import br.com.viavarejo.domain.model.mongodb.pedido.Pedido;

public interface PedidoRepository extends MongoRepository<Pedido, ObjectId> {

    Pedido findByPk(Long pk);

    void deleteByPk(Long pk);

    @DeleteQuery("{ id: {$lte: ?0}}")
    void deleteByObjectIdOld(ObjectId id);

}
