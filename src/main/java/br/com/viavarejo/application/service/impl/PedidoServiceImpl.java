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
import br.com.viavarejo.application.service.PedidoService;
import br.com.viavarejo.domain.model.mongodb.pedido.Pedido;
import br.com.viavarejo.domain.repository.PedidoRepository;

@Service
public class PedidoServiceImpl implements PedidoService {

    @Autowired
    private PedidoRepository repository;

    @Override
    public List<Pedido> criar(final List<Pedido> lista) {

        if (isNull(lista) || isEmpty(lista)) {
            throw new ValidacaoException("Adicione ao menos um objeto para ser criado");
        }
        this.prepararIntegracoes(lista);
        return this.repository.insert(lista);
    }

    @Override
    public Pedido criarPedido(final Pedido pedido) {

        if (isNull(pedido)) {
            throw new ValidacaoException("Adicione um objeto para ser criado");
        }
        this.prepararIntegracao(pedido);
        return this.repository.insert(pedido);
    }


    @Override
    public List<Pedido> atualizar(final List<Pedido> lista) {

        if (isNull(lista) || isEmpty(lista)) {
            throw new ValidacaoException("Adicione ao menos um objeto para ser atualizado");
        }
        this.prepararIntegracoes(lista);
        return this.repository.saveAll(lista);
    }

    @Override
    public Pedido atualizarPedido(final Pedido pedido) {

        if (isNull(pedido)) {
            throw new ValidacaoException("Adicione um objeto para ser atualizado");
        }
        this.prepararIntegracao(pedido);
        return this.repository.save(pedido);
    }

    @Override
    public List<Pedido> listar() {

        final List<Pedido> lista = this.repository.findAll();

        if (isEmpty(lista)) {
            throw new NaoEncontradoException("Lista vazia.");
        }
        return lista;
    }

    @Override
    public Pedido recuperar(final Long codigoPedido) {

        if (isNull(codigoPedido)) {
            throw new ValidacaoException("Identificador não informado.");
        }

        final Pedido obj = this.repository.findByPk(codigoPedido);

        if (isNull(obj)) {
            throw new NaoEncontradoException(format("Pedido de código %1$s não encontrado.", codigoPedido));
        }
        return obj;
    }

    @Override
    public void deletar(final Long codigoPedido) {

        if (isNull(codigoPedido)) {
            throw new ValidacaoException("Identificador não informado.");
        }

        this.repository.deleteByPk(codigoPedido);
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

    /**
     * @see {@link Pedido#prepararEntidade()}
     */
    @Override
    public void prepararIntegracoes(final List<Pedido> entidades) {
        entidades.forEach(Pedido::prepararEntidade);
    }

    /**
     * @see {@link Pedido#prepararEntidade()}
     */
    @Override
    public void prepararIntegracao(final Pedido entidade) {
        entidade.prepararEntidade();
    }
}
