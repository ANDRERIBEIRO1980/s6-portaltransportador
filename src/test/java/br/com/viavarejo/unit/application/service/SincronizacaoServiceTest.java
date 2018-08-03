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
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
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
import br.com.viavarejo.application.service.SincronizacaoService;
import br.com.viavarejo.application.service.impl.SincronizacaoServiceImpl;
import br.com.viavarejo.domain.model.mongodb.sincronizacao.SincronizacaoProcesso;
import br.com.viavarejo.domain.repository.SincronizacaoRepository;
import br.com.viavarejo.utils.UnitTest;

public class SincronizacaoServiceTest implements UnitTest {

    private static final Integer MESES_ATRAS = 1;

    private static final String PROCESSO = "NomeProcesso";
    private static final Date ULTIMA_SINCRONIZACAO = new Date();

    @InjectMocks
    private final SincronizacaoService service = new SincronizacaoServiceImpl();

    @Mock
    private SincronizacaoRepository repository;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    // CRIAR

    @Test
    public void criar_sucesso() throws PortalTransportadorException {
        final List<SincronizacaoProcesso> lista = this.mockEntidades();

        when(this.repository.insert(lista)).thenReturn(lista);

        final List<SincronizacaoProcesso> retorno = this.service.criar(lista);

        assertThat(retorno, not(IsEmptyCollection.empty()));
        verify(this.repository, times(1)).insert(anyListOf(SincronizacaoProcesso.class));
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
        final List<SincronizacaoProcesso> lista = this.mockEntidades();

        when(this.repository.saveAll(lista)).thenReturn(lista);

        final List<SincronizacaoProcesso> retorno = this.service.atualizar(lista);

        assertThat(retorno, not(IsEmptyCollection.empty()));
        verify(this.repository, times(1)).saveAll(anyListOf(SincronizacaoProcesso.class));
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
        final List<SincronizacaoProcesso> lista = this.mockEntidades();

        when(this.repository.findAll()).thenReturn(lista);

        final List<SincronizacaoProcesso> retorno = this.service.listar();

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
        final SincronizacaoProcesso obj = this.mockEntidade();

        when(this.repository.findByProcesso(Mockito.anyString())).thenReturn(obj);

        final SincronizacaoProcesso retorno = this.service.recuperar(obj.getProcesso());

        assertNotNull(retorno);
        verify(this.repository, times(1)).findByProcesso(Mockito.anyString());
    }

    @Test(expected = ValidacaoException.class)
    public void recuperar_chave_nula() throws PortalTransportadorException {

        this.service.recuperar(null);
    }

    @Test(expected = ValidacaoException.class)
    public void recuperar_chave_vazia() throws PortalTransportadorException {

        this.service.recuperar(StringUtils.EMPTY);
    }

    @Test(expected = NaoEncontradoException.class)
    public void recuperar_sem_retorno() throws PortalTransportadorException {
        final SincronizacaoProcesso obj = this.mockEntidade();

        when(this.repository.findByProcesso(Mockito.anyString())).thenReturn(null);

        this.service.recuperar(obj.getProcesso());
    }

    // DELETAR

    @Test
    public void deletar_porchave_sucesso() throws PortalTransportadorException {
        final SincronizacaoProcesso obj = this.mockEntidade();

        this.service.deletar(obj.getProcesso());

        verify(this.repository, times(1)).deleteByProcesso(Mockito.anyString());
    }

    @Test(expected = ValidacaoException.class)
    public void deletar_porchave_chave_nula() throws PortalTransportadorException {

        this.service.deletar(null);
    }

    @Test(expected = ValidacaoException.class)
    public void deletar_porchave_chave_vazia() throws PortalTransportadorException {

        this.service.deletar(StringUtils.EMPTY);
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

    private List<SincronizacaoProcesso> mockEntidades() {
        return Arrays.asList(this.mockEntidade());
    }

    private SincronizacaoProcesso mockEntidade() {
        return new SincronizacaoProcesso(PROCESSO, ULTIMA_SINCRONIZACAO);
    }
}
