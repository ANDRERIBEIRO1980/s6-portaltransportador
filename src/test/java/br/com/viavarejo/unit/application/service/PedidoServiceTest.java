package br.com.viavarejo.unit.application.service;

import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.anyListOf;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.hamcrest.collection.IsEmptyCollection;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import br.com.viavarejo.application.exception.NaoEncontradoException;
import br.com.viavarejo.application.exception.PortalTransportadorException;
import br.com.viavarejo.application.exception.ValidacaoException;
import br.com.viavarejo.application.service.PedidoService;
import br.com.viavarejo.application.service.impl.PedidoServiceImpl;
import br.com.viavarejo.domain.model.builder.PedidoBuilder;
import br.com.viavarejo.domain.model.mongodb.pedido.Pedido;
import br.com.viavarejo.domain.repository.PedidoRepository;
import br.com.viavarejo.utils.UnitTest;

public class PedidoServiceTest implements UnitTest {

    private static final Integer MESES_ATRAS = 1;

    private static final Long CODIGO = 10L;
    private static final Integer TIPO_PEDIDO_ENVIO = 1;

    @InjectMocks
    private final PedidoService service = new PedidoServiceImpl();

    @Mock
    private PedidoRepository repository;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    // CRIAR

    @Test
    public void criar_sucesso() throws PortalTransportadorException {
        final List<Pedido> lista = this.mockEntidades();

        when(this.repository.insert(lista)).thenReturn(lista);

        final List<Pedido> retorno = this.service.criar(lista);

        assertThat(retorno, not(IsEmptyCollection.empty()));
        verify(this.repository, times(1)).insert(anyListOf(Pedido.class));
    }

    @Test(expected = ValidacaoException.class)
    public void criar_lista_nula() throws PortalTransportadorException {

        this.service.criar(null);
    }

    @Test(expected = ValidacaoException.class)
    public void criar_lista_vazia() throws PortalTransportadorException {

        this.service.criar(Collections.emptyList());
    }

    // ATUALIZAR

    @Test
    public void atualizar_sucesso() throws PortalTransportadorException {
        final List<Pedido> lista = this.mockEntidades();
        when(this.repository.saveAll(lista)).thenReturn(lista);

        final List<Pedido> retorno = this.service.atualizar(lista);

        assertThat(retorno, not(IsEmptyCollection.empty()));
        verify(this.repository, times(1)).saveAll(ArgumentMatchers.any());
    }

    @Test(expected = ValidacaoException.class)
    public void atualizar_lista_nula() throws PortalTransportadorException {

        this.service.atualizar(null);
    }

    @Test(expected = ValidacaoException.class)
    public void atualizar_lista_vazia() throws PortalTransportadorException {

        this.service.atualizar(Collections.emptyList());
    }

    // LISTAR

    @Test
    public void listar_sucesso() throws PortalTransportadorException {
        final List<Pedido> lista = this.mockEntidades();

        when(this.repository.findAll()).thenReturn(lista);

        final List<Pedido> retorno = this.service.listar();

        assertThat(retorno, not(IsEmptyCollection.empty()));
        verify(this.repository, times(1)).findAll();
    }

    @Test(expected = NaoEncontradoException.class)
    public void listar_lista_vazia() throws PortalTransportadorException {

        when(this.repository.findAll()).thenReturn(Collections.emptyList());

        this.service.listar();
    }

    // RECUPERAR

    @Test
    public void recuperar_sucesso() throws PortalTransportadorException {
        final Pedido obj = this.mockEntidade();

        when(this.repository.findByPk(Mockito.anyLong())).thenReturn(obj);

        final Pedido retorno = this.service.recuperar(obj.getPk());

        assertNotNull(retorno);
        verify(this.repository, times(1)).findByPk(Mockito.anyLong());
    }

    @Test(expected = ValidacaoException.class)
    public void recuperar_chave_nula() throws PortalTransportadorException {

        this.service.recuperar(null);
    }

    @Test(expected = NaoEncontradoException.class)
    public void recuperar_sem_retorno() throws PortalTransportadorException {
        final Pedido obj = this.mockEntidade();

        when(this.repository.findByPk(Mockito.anyLong())).thenReturn(null);

        this.service.recuperar(obj.getPk());
    }

    // DELETAR

    @Test
    public void deletar_porchave_sucesso() throws PortalTransportadorException {
        final Pedido obj = this.mockEntidade();

        this.service.deletar(obj.getPk());

        verify(this.repository, times(1)).deleteByPk(Mockito.anyLong());
    }

    @Test(expected = ValidacaoException.class)
    public void deletar_porchave_chave_nula() throws PortalTransportadorException {

        this.service.deletar(null);
    }

    @Test
    public void deletar_todos_sucesso() throws PortalTransportadorException {
        this.service.deletar();
        verify(this.repository, times(1)).deleteAll();
    }

    @Test(expected = ValidacaoException.class)
    public void deletarAntigos_parametroNulo() {
        this.service.deletarAntigos(null);
    }

    @Test
    public void deletarAntigos_sucesso() {
        this.service.deletarAntigos(MESES_ATRAS);
        verify(this.repository, times(1)).deleteByObjectIdOld(Matchers.any());
    }

    // ===================================================================================================

    private List<Pedido> mockEntidades() {
        return Arrays.asList(this.mockEntidade());
    }

    private Pedido mockEntidade() {
        return PedidoBuilder.init().codigo(CODIGO).embarcador(21, 1200).transportadora(21, 1001).tipoPedidoEnvio(TIPO_PEDIDO_ENVIO).build();
    }
}
