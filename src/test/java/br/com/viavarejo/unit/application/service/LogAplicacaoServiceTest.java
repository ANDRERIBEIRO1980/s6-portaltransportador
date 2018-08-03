package br.com.viavarejo.unit.application.service;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import br.com.viavarejo.application.exception.NaoEncontradoException;
import br.com.viavarejo.application.exception.PortalTransportadorException;
import br.com.viavarejo.application.exception.ValidacaoException;
import br.com.viavarejo.application.service.LogAplicacaoService;
import br.com.viavarejo.application.service.impl.LogAplicacaoServiceImpl;
import br.com.viavarejo.domain.model.mongodb.LogAplicacao;
import br.com.viavarejo.domain.repository.LogAplicacaoRepository;
import br.com.viavarejo.utils.UnitTest;

public class LogAplicacaoServiceTest implements UnitTest {

    private static final Integer MESES_ATRAS = 1;

    private static final String PROCESSO_DE_TESTE = "Processo de Teste";

    @InjectMocks
    private final LogAplicacaoService service = new LogAplicacaoServiceImpl();

    @Mock
    private LogAplicacaoRepository repository;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    // LISTAR

    @Test
    public void listar_sucesso() throws PortalTransportadorException {
        final List<LogAplicacao> lista = this.mockEntidades();

        when(this.repository.findAll()).thenReturn(lista);

        final List<LogAplicacao> retorno = this.service.listar();

        assertNotNull(retorno);
        verify(this.repository, times(1)).findAll();
    }

    @Test(expected = NaoEncontradoException.class)
    public void listar_lista_vazia() throws PortalTransportadorException {

        when(this.repository.findAll()).thenReturn(Collections.emptyList());

        final List<LogAplicacao> retorno = this.service.listar();

        assertTrue(retorno.isEmpty());
    }

    // RECUPERAR

    @Test
    public void recuperarProcesso_sucesso() {
        final LogAplicacao entidade = this.mockEntidade();

        when(this.repository.findByProcesso(anyString())).thenReturn(entidade);

        final LogAplicacao retorno = this.service.recuperar(PROCESSO_DE_TESTE);

        assertThat(retorno, notNullValue());
        verify(this.repository, times(1)).findByProcesso(anyString());
    }

    @Test(expected = NaoEncontradoException.class)
    public void recuperarProcesso_nao_existente() {

        when(this.repository.findByProcesso(anyString())).thenReturn(null);
        this.service.recuperar(PROCESSO_DE_TESTE);
    }

    @Test(expected = ValidacaoException.class)
    public void recuperarProcesso_insucesso() {
        this.service.recuperar(null);
    }

    // DELETAR

    @Test(expected = ValidacaoException.class)
    public void deletarAntigos_parametroNulo() {
        this.service.deletarAntigos(null);
    }

    @Test
    public void deletarAntigos_sucesso() {
        this.service.deletarAntigos(MESES_ATRAS);
        verify(this.repository, times(1)).deleteByObjectIdOld(ArgumentMatchers.any());
    }

    //

    private List<LogAplicacao> mockEntidades() {
        return Arrays.asList(this.mockEntidade());
    }

    private LogAplicacao mockEntidade() {
        final LogAplicacao entidade = new LogAplicacao();

        entidade.setProcesso(PROCESSO_DE_TESTE);

        return entidade;

    }
}
