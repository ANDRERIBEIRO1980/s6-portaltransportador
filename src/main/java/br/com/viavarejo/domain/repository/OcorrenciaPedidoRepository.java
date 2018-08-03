package br.com.viavarejo.domain.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.DeleteQuery;
import org.springframework.data.mongodb.repository.MongoRepository;

import br.com.viavarejo.domain.model.mongodb.ocorrencia.OcorrenciaPedido;

public interface OcorrenciaPedidoRepository extends MongoRepository<OcorrenciaPedido, ObjectId> {

    OcorrenciaPedido findByCodigoPedido(String codigoPedido);

    void deleteByCodigoPedido(String codigoPedido);

    @DeleteQuery("{ id: {$lte: ?0}}")
    void deleteByObjectIdOld(ObjectId id);
}
