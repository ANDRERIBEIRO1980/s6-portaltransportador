package br.com.viavarejo.unit.application.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
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
import br.com.viavarejo.application.service.OcorrenciaPedidoService;
import br.com.viavarejo.application.service.impl.OcorrenciaPedidoServiceImpl;
import br.com.viavarejo.domain.model.builder.OcorrenciaPedidoBuilder;
import br.com.viavarejo.domain.model.enumerator.TipoOperacaoOcorrenciaEnum;
import br.com.viavarejo.domain.model.mongodb.ocorrencia.OcorrenciaPedido;
import br.com.viavarejo.domain.repository.OcorrenciaPedidoRepository;
import br.com.viavarejo.domain.repository.TransportadorAtivoIntelipostRepository;
import br.com.viavarejo.utils.UnitTest;

public class OcorrenciaPedidoServiceTest implements UnitTest {

    private static final Integer MESES_ATRAS = 1;

    @InjectMocks
    private final OcorrenciaPedidoService service = new OcorrenciaPedidoServiceImpl();

    @Mock
    private OcorrenciaPedidoRepository repository;

    @Mock
    private TransportadorAtivoIntelipostRepository transportadorAtivoIntelipostRep;

    @Before
    public void init() throws PortalTransportadorException {
        MockitoAnnotations.initMocks(this);
    }

    // CRIAR

    @Test
    public void criar_sucesso() throws PortalTransportadorException {
        final List<OcorrenciaPedido> lista = this.mockEntidades();

        when(this.repository.insert(lista)).thenReturn(lista);

        this.service.criar(lista);

        verify(this.repository, times(1)).insert(ArgumentMatchers.anyList());
    }

    @Test(expected = ValidacaoException.class)
    public void criar_lista_nula() throws PortalTransportadorException {

        this.service.criarOcorrencia(null);

        verify(this.repository, times(0)).insert(ArgumentMatchers.anyList());
    }

    @Test(expected = ValidacaoException.class)
    public void criar_lista_vazia() throws PortalTransportadorException {

        this.service.criar(Collections.emptyList());

        verify(this.repository, times(0)).insert(ArgumentMatchers.anyList());
    }

    // ATUALIZAR

    @Test
    public void atualizar_sucesso() throws PortalTransportadorException {
        final List<OcorrenciaPedido> lista = this.mockEntidades();

        when(this.repository.saveAll(lista)).thenReturn(lista);

        this.service.atualizar(lista);

        verify(this.repository, times(1)).saveAll(ArgumentMatchers.anyList());
    }

    @Test(expected = ValidacaoException.class)
    public void atualizar_lista_nula() throws PortalTransportadorException {

        this.service.atualizarOcorrencia(null);

        verify(this.repository, times(0)).saveAll(ArgumentMatchers.anyList());
    }

    @Test(expected = ValidacaoException.class)
    public void atualizar_lista_vazia() throws PortalTransportadorException {

        this.service.atualizar(Collections.emptyList());

        verify(this.repository, times(0)).saveAll(ArgumentMatchers.anyList());
    }

    // LISTAR

    @Test
    public void listar_sucesso() throws PortalTransportadorException {
        final List<OcorrenciaPedido> lista = this.mockEntidades();

        when(this.repository.findAll()).thenReturn(lista);

        final List<OcorrenciaPedido> retorno = this.service.listar();

        assertNotNull(retorno);
        verify(this.repository, times(1)).findAll();
    }

    @Test(expected = NaoEncontradoException.class)
    public void listar_lista_vazia() throws PortalTransportadorException {

        when(this.repository.findAll()).thenReturn(Collections.emptyList());

        final List<OcorrenciaPedido> retorno = this.service.listar();

        assertTrue(retorno.isEmpty());
    }

    // RECUPERAR

    @Test
    public void recuperar_sucesso() throws PortalTransportadorException {
        final OcorrenciaPedido obj = this.mockEntidade();

        when(this.repository.findByCodigoPedido(Mockito.anyString())).thenReturn(obj);

        final OcorrenciaPedido retorno = this.service.recuperar(obj.getCodigoPedido());

        assertNotNull(retorno);
        verify(this.repository, times(1)).findByCodigoPedido(Mockito.anyString());
    }

    @Test(expected = ValidacaoException.class)
    public void recuperar_chave_vazia() throws PortalTransportadorException {

        final OcorrenciaPedido retorno = this.service.recuperar(StringUtils.EMPTY);

        assertNull(retorno);
        verify(this.repository, times(0)).findByCodigoPedido(Mockito.anyString());
    }

    @Test(expected = NaoEncontradoException.class)
    public void recuperar_sem_retorno() throws PortalTransportadorException {
        final OcorrenciaPedido obj = this.mockEntidade();

        when(this.repository.findByCodigoPedido(Mockito.anyString())).thenReturn(null);

        final OcorrenciaPedido retorno = this.service.recuperar(obj.getCodigoPedido());

        assertNull(retorno);
        verify(this.repository, times(0)).findByCodigoPedido(Mockito.anyString());
    }

    // DELETAR

    @Test
    public void deletar_porchave_sucesso() throws PortalTransportadorException {
        final OcorrenciaPedido obj = this.mockEntidade();

        this.service.deletar(obj.getCodigoPedido());

        verify(this.repository, times(1)).deleteByCodigoPedido(Mockito.anyString());
    }

    @Test(expected = ValidacaoException.class)
    public void deletar_porchave_chave_vazia() throws PortalTransportadorException {
        this.service.deletar(StringUtils.EMPTY);

        verify(this.repository, times(0)).deleteByCodigoPedido(Mockito.anyString());
    }

    @Test
    public void deletar_lista_sucesso() throws PortalTransportadorException {
        final List<OcorrenciaPedido> lista = this.mockEntidades();

        this.service.deletar(lista);

        verify(this.repository, times(1)).deleteAll(lista);
    }

    @Test(expected = ValidacaoException.class)
    public void deletar_lista_vazia() throws PortalTransportadorException {
        this.service.deletar(Collections.emptyList());

        verify(this.repository, times(0)).deleteAll(Collections.emptyList());
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

    private List<OcorrenciaPedido> mockEntidades() {
        return Arrays.asList(this.mockEntidade());
    }

    private OcorrenciaPedido mockEntidade() {
        return OcorrenciaPedidoBuilder.init()
                        .transportadora(21, 1001)
                        .tipoOperacao(TipoOperacaoOcorrenciaEnum.NORMAL)
                        .codigoPedido("1")
                        .build();
    }
}
