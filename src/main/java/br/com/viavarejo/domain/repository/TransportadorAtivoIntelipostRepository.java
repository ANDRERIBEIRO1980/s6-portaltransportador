package br.com.viavarejo.domain.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.DeleteQuery;
import org.springframework.data.mongodb.repository.MongoRepository;

import br.com.viavarejo.domain.model.mongodb.transportador.TransportadorAtivoIntelipost;

public interface TransportadorAtivoIntelipostRepository
		extends MongoRepository<TransportadorAtivoIntelipost, ObjectId> {

	TransportadorAtivoIntelipost findByCodigoEmpresaAndCodigo(Integer codigoEmpresa, Integer codigo);

    @DeleteQuery("{ id: {$lte: ?0}}")
    void deleteByObjectIdOld(ObjectId id);
}
