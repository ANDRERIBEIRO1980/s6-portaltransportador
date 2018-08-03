package br.com.viavarejo.domain.repository;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.DeleteQuery;
import org.springframework.data.mongodb.repository.MongoRepository;

import br.com.viavarejo.domain.model.mongodb.cobranca.LoteCobranca;

public interface LoteCobrancaRepository extends MongoRepository<LoteCobranca, ObjectId> {

    LoteCobranca findByNumeroFatura(Integer numeroFatura);

    @DeleteQuery("{ id: {$lte: ?0}}")
    void deleteByObjectIdOld(ObjectId id);
    
    List<LoteCobranca> findBySincronizacaoMainframeIs(Boolean flag);

    void deleteBySincronizacaoMainframeIs(Boolean flag);
}
