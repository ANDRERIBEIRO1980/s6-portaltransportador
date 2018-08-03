package br.com.viavarejo.application.service.impl;

import static br.com.viavarejo.util.DateUtils.asDate;
import static br.com.viavarejo.util.Utils.isEmpty;
import static br.com.viavarejo.util.Utils.isNull;
import static java.lang.String.format;
import static java.time.temporal.ChronoUnit.MONTHS;

import java.time.LocalDate;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.viavarejo.application.exception.NaoEncontradoException;
import br.com.viavarejo.application.exception.ValidacaoException;
import br.com.viavarejo.application.service.MetodoEntregaService;
import br.com.viavarejo.domain.model.mongodb.MetodoEntrega;
import br.com.viavarejo.domain.repository.MetodoEntregaRepository;

@Service
public class MetodoEntregaServiceImpl implements MetodoEntregaService {

    @Autowired
    private MetodoEntregaRepository repository;

    @Override
    public List<MetodoEntrega> criar(final List<MetodoEntrega> lista) {

        if (isNull(lista) || isEmpty(lista)) {
            throw new ValidacaoException("Adicione ao menos um objeto para ser criado");
        }
        return this.repository.insert(lista);
    }

    @Override
    public MetodoEntrega criarMetodo(final MetodoEntrega metodo) {

        if (isNull(metodo)) {
            throw new ValidacaoException("Adicione um objeto para ser criado");
        }
        return this.repository.insert(metodo);
    }

    @Override
    public List<MetodoEntrega> atualizar(final List<MetodoEntrega> lista) {

        if (isNull(lista) || isEmpty(lista)) {
            throw new ValidacaoException("Adicione ao menos um objeto para ser atualizado");
        }
        return this.repository.saveAll(lista);
    }

    @Override
    public MetodoEntrega atualizarMetodo(final MetodoEntrega metodo) {

        if (isNull(metodo)) {
            throw new ValidacaoException("Adicione um objeto para ser atualizado");
        }
        return this.repository.save(metodo);
    }

    @Override
    public List<MetodoEntrega> listar() {

        final List<MetodoEntrega> lista = this.repository.findAll();

        if (isEmpty(lista)) {
            throw new NaoEncontradoException("Lista vázia.");
        }
        return lista;
    }

    @Override
    public MetodoEntrega recuperar(final Integer codigo) {

        if (isEmpty(codigo)) {
            throw new ValidacaoException("Identificador não informado.");
        }

        final MetodoEntrega obj = this.repository.findByCodigo(codigo);

        if (isNull(obj)) {
            throw new NaoEncontradoException(format("Metodo de Entrega %1$s não encontrado.", codigo));
        }
        return obj;
    }

    @Override
    public void deletar(final Integer codigo) {

        if (isEmpty(codigo)) {
            throw new ValidacaoException("Identificador não informado.");
        }
        this.repository.deleteByCodigo(codigo);
    }

    @Override
    public void deletar() {
        this.repository.deleteAll();
    }

    @Override
    public void deletarAntigos(final Integer mesesAtras) {

        if (isNull(mesesAtras)) {
            throw new ValidacaoException("Parâmetro não informado.");
        }

        final LocalDate dateAgo = LocalDate.now().minus(mesesAtras, MONTHS);
        final ObjectId mongoId = new ObjectId(asDate(dateAgo));

        this.repository.deleteByObjectIdOld(mongoId);
    }
}
