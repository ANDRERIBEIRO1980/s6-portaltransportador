package br.com.viavarejo.domain.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.DeleteQuery;
import org.springframework.data.mongodb.repository.MongoRepository;

import br.com.viavarejo.domain.model.mongodb.kitcoleta.PedidoKitColeta;

public interface PedidoKitColetaRepository extends MongoRepository<PedidoKitColeta, Long> {

    PedidoKitColeta findByCodigo(Long id);

    void deleteByCodigo(Long id);

    @DeleteQuery("{ id: {$lte: ?0}}")
    void deleteByObjectIdOld(ObjectId id);

}
