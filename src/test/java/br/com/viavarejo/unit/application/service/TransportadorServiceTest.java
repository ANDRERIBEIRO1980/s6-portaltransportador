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
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import br.com.viavarejo.application.exception.NaoEncontradoException;
import br.com.viavarejo.application.exception.PortalTransportadorException;
import br.com.viavarejo.application.exception.ValidacaoException;
import br.com.viavarejo.application.service.TransportadorService;
import br.com.viavarejo.application.service.impl.TransportadorServiceImpl;
import br.com.viavarejo.domain.model.builder.TransportadorBuilder;
import br.com.viavarejo.domain.model.enumerator.TipoAcaoRegistroEnum;
import br.com.viavarejo.domain.model.mongodb.transportador.Transportador;
import br.com.viavarejo.domain.model.mongodb.transportador.TransportadorPK;
import br.com.viavarejo.domain.repository.TransportadorRepository;
import br.com.viavarejo.utils.UnitTest;

public class TransportadorServiceTest implements UnitTest {

    private static final Integer MESES_ATRAS = 1;

    private static final Integer CODIGO = 10;
    private static final Integer CODIGO_EMPRESA = 20;
    private static final String DOCUMENTO = "28.706.143/0001-32";

    @InjectMocks
    private final TransportadorService service = new TransportadorServiceImpl();

    @Mock
    private TransportadorRepository repository;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    // CRIAR

    @Test
    public void criar_sucesso() throws PortalTransportadorException {
        final List<Transportador> lista = this.mockEntidades();

        when(this.repository.insert(lista)).thenReturn(lista);

        final List<Transportador> retorno = this.service.criar(lista);

        assertThat(retorno, not(IsEmptyCollection.empty()));
        verify(this.repository, times(1)).insert(anyListOf(Transportador.class));
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
        final List<Transportador> lista = this.mockEntidades();

        when(this.repository.saveAll(lista)).thenReturn(lista);

        final List<Transportador> retorno = this.service.atualizar(lista);

        assertThat(retorno, not(IsEmptyCollection.empty()));
        verify(this.repository, times(1)).saveAll(anyListOf(Transportador.class));
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
        final List<Transportador> lista = this.mockEntidades();

        when(this.repository.findAll()).thenReturn(lista);

        final List<Transportador> retorno = this.service.listar();

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
        final Transportador obj = this.mockEntidade();

        when(this.repository.findByPk(Mockito.any(TransportadorPK.class))).thenReturn(obj);

        final Transportador retorno = this.service.recuperar(obj.getPk());

        assertNotNull(retorno);
        verify(this.repository, times(1)).findByPk(Mockito.any(TransportadorPK.class));
    }

    @Test(expected = ValidacaoException.class)
    public void recuperar_chave_nula() throws PortalTransportadorException {

        this.service.recuperar(null);
    }

    @Test(expected = ValidacaoException.class)
    public void recuperar_chave_empresa_nula() throws PortalTransportadorException {

        this.service.recuperar(new TransportadorPK(null, CODIGO));
    }

    @Test(expected = ValidacaoException.class)
    public void recuperar_chave_codigo_nulo() throws PortalTransportadorException {

        this.service.recuperar(new TransportadorPK(CODIGO_EMPRESA, null));
    }

    @Test(expected = NaoEncontradoException.class)
    public void recuperar_sem_retorno() throws PortalTransportadorException {
        final Transportador obj = this.mockEntidade();

        when(this.repository.findByPk(Mockito.any(TransportadorPK.class))).thenReturn(null);

        this.service.recuperar(obj.getPk());
    }

    // DELETAR

    @Test
    public void deletar_porchave_sucesso() throws PortalTransportadorException {
        final Transportador obj = this.mockEntidade();

        this.service.deletar(obj.getPk());

        verify(this.repository, times(1)).deleteByPk(Mockito.any(TransportadorPK.class));
    }

    @Test(expected = ValidacaoException.class)
    public void deletar_porchave_chave_nula() throws PortalTransportadorException {

        this.service.deletar(null);
    }

    @Test(expected = ValidacaoException.class)
    public void deletar_porchave_chave_empresa_nula() throws PortalTransportadorException {

        this.service.deletar(new TransportadorPK(null, CODIGO));
    }

    @Test(expected = ValidacaoException.class)
    public void deletar_porchave_chave_codigo_nulo() throws PortalTransportadorException {

        this.service.deletar(new TransportadorPK(CODIGO_EMPRESA, null));
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

    private List<Transportador> mockEntidades() {
        return Arrays.asList(this.mockEntidade());
    }

    private Transportador mockEntidade() {
        return TransportadorBuilder.init()
                        .cnpj(DOCUMENTO)
                        .codigo(CODIGO)
                        .codigoEmpresa(CODIGO_EMPRESA)
                        .tipoAcao(TipoAcaoRegistroEnum.NOVO)
                        .build();
    }
}
