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
import br.com.viavarejo.application.service.RessarcimentoService;
import br.com.viavarejo.application.service.impl.RessarcimentoServiceImpl;
import br.com.viavarejo.domain.model.builder.MercadoriaRessarcimentoBuilder;
import br.com.viavarejo.domain.model.builder.RessarcimentoBuilder;
import br.com.viavarejo.domain.model.mongodb.cobranca.MercadoriaRessarcimento;
import br.com.viavarejo.domain.model.mongodb.cobranca.Ressarcimento;
import br.com.viavarejo.domain.repository.RessarcimentoRepository;
import br.com.viavarejo.utils.UnitTest;

public class RessarcimentoServiceTest implements UnitTest {

    private static final Integer MESES_ATRAS = 1;

    @InjectMocks
    private final RessarcimentoService service = new RessarcimentoServiceImpl();

    @Mock
    private RessarcimentoRepository repository;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    // CRIAR

    @Test
    public void criar_sucesso() throws PortalTransportadorException {
        final List<Ressarcimento> lista = this.mockEntidades();

        when(this.repository.insert(lista)).thenReturn(lista);

        final List<Ressarcimento> retorno = this.service.criar(lista);

        assertThat(retorno, not(IsEmptyCollection.empty()));
        verify(this.repository, times(1)).insert(anyListOf(Ressarcimento.class));
    }

    @Test(expected = ValidacaoException.class)
    public void criar_lista_nula() throws PortalTransportadorException {

        this.service.criarRessarcimento(null);
    }

    @Test(expected = ValidacaoException.class)
    public void criar_lista_vazia() throws PortalTransportadorException {

        this.service.criar(Collections.emptyList());
    }

    // ATUALIZAR

    @Test
    public void atualizar_sucesso() throws PortalTransportadorException {
        final List<Ressarcimento> lista = this.mockEntidades();

        when(this.repository.saveAll(lista)).thenReturn(lista);

        final List<Ressarcimento> retorno = this.service.atualizar(lista);

        assertThat(retorno, not(IsEmptyCollection.empty()));
        verify(this.repository, times(1)).saveAll(anyListOf(Ressarcimento.class));
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
        final List<Ressarcimento> lista = this.mockEntidades();

        when(this.repository.findAll()).thenReturn(lista);

        final List<Ressarcimento> retorno = this.service.listar();

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
        final Ressarcimento obj = this.mockEntidade();

        when(this.repository.findByPk(Mockito.any(Long.class))).thenReturn(obj);

        final Ressarcimento retorno = this.service.recuperar(obj.getPk());

        assertNotNull(retorno);
        verify(this.repository, times(1)).findByPk(Mockito.any(Long.class));
    }

    @Test(expected = ValidacaoException.class)
    public void recuperar_chave_nula() throws PortalTransportadorException {
        this.service.recuperar(null);
    }

    @Test(expected = ValidacaoException.class)
    public void recuperar_chave_codigo_nulo() throws PortalTransportadorException {
        this.service.recuperar(null);
    }

    @Test(expected = NaoEncontradoException.class)
    public void recuperar_sem_retorno() throws PortalTransportadorException {
        final Ressarcimento obj = this.mockEntidade();

        when(this.repository.findByPk(Mockito.any(Long.class))).thenReturn(null);

        this.service.recuperar(obj.getPk());
    }

    // DELETAR

    @Test
    public void deletar_porchave_sucesso() throws PortalTransportadorException {
        final Ressarcimento obj = this.mockEntidade();

        this.service.deletar(obj.getPk());

        verify(this.repository, times(1)).deleteByPk(Mockito.any(Long.class));
    }

    @Test(expected = ValidacaoException.class)
    public void deletar_porchave_chave_nula() throws PortalTransportadorException {

        this.service.deletar(null);
    }

    @Test(expected = ValidacaoException.class)
    public void deletar_porchave_chave_codigo_nulo() throws PortalTransportadorException {
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

    private List<Ressarcimento> mockEntidades() {
        return Arrays.asList(this.mockEntidade());
    }

    private Ressarcimento mockEntidade() {

        final MercadoriaRessarcimento mercadoria = MercadoriaRessarcimentoBuilder.init()
                        .sku("1234")
                        .descricao("produtos")
                        .quantidade(2)
                        .build();

        return RessarcimentoBuilder.init()
                        .codigo(1L)
                        .numeroPedidoEnvio("Pedido001")
                        .dataCriacao(new Date())
                        .tipo("Ressarcimento")
                        .tipoEntrega("Normal")
                        .codigoPontoControle("1")
                        .descricaoPontoControle("Dsc Ponto Controle")
                        .usuarioPontoControle("Usuario Ponto Controle")
                        .dataReferenciaFiscal(new Date())
                        .referenciaFiscal("ReferenciaFiscal")
                        .usuario("Usuario")
                        .statusColeta("Status Coleta")
                        .codigoColeta(123L)
                        .codigoPedidoInformacao(321L)
                        .mercadorias(mercadoria)
                        .build();
    }
}
