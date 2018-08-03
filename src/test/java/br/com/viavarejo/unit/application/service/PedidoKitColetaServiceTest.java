package br.com.viavarejo.unit.application.service;

import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.anyListOf;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
import br.com.viavarejo.application.service.ArquivoRecuperavelService;
import br.com.viavarejo.application.service.PedidoKitColetaService;
import br.com.viavarejo.application.service.impl.PedidoKitColetaServiceImpl;
import br.com.viavarejo.domain.model.builder.ClienteBuilder;
import br.com.viavarejo.domain.model.builder.ContatoBuilder;
import br.com.viavarejo.domain.model.builder.EnderecoBuilder;
import br.com.viavarejo.domain.model.builder.NotaFiscalBuilder;
import br.com.viavarejo.domain.model.builder.PedidoKitColetaBuilder;
import br.com.viavarejo.domain.model.builder.VolumeBuilder;
import br.com.viavarejo.domain.model.enumerator.TipoDocumentoEnum;
import br.com.viavarejo.domain.model.enumerator.TipoPessoaEnum;
import br.com.viavarejo.domain.model.enumerator.TipoVolumeEnum;
import br.com.viavarejo.domain.model.mongodb.Contato;
import br.com.viavarejo.domain.model.mongodb.Endereco;
import br.com.viavarejo.domain.model.mongodb.filial.FilialPK;
import br.com.viavarejo.domain.model.mongodb.kitcoleta.PedidoKitColeta;
import br.com.viavarejo.domain.model.mongodb.pedido.Cliente;
import br.com.viavarejo.domain.model.mongodb.pedido.NotaFiscal;
import br.com.viavarejo.domain.model.mongodb.pedido.Volume;
import br.com.viavarejo.domain.model.mongodb.transportador.TransportadorPK;
import br.com.viavarejo.domain.repository.PedidoKitColetaRepository;
import br.com.viavarejo.utils.UnitTest;

public class PedidoKitColetaServiceTest implements UnitTest {

    private static final Integer MESES_ATRAS = 1;

    private static final String REFERENCIA = "Próximo à estação de trem";
    private static final String COMPLEMENTO = "Prédio da Loja";
    private static final String NUMERO_ENDERECO = "168";
    private static final String LOGRADOURO = "Rua João Pessoa";
    private static final String BAIRRO = "Centro";
    private static final String CEP = "095-75460";
    private static final String CIDADE = "São Caetano do Sul";
    private static final String ESTADO = "SP";
    private static final String PAIS = "Brasil";
    private static final String CANAL_VENDA = "E-Commerce";
    private static final String NUMERO_PEDIDO_ENVIO = "15151212";
    private static final Integer TIPO_PEDIDO_ENVIO = 1;
    private static final String NOME_CLIENTE = "João";
    private static final String SOBRE_NOME = " da silva Mock";
    private static final String CPF = "315.645.549-89";
    private static final String E_MAIL = "joao@mock.com.br";
    private static final String TEL_FIXO = "4453-8695";
    private static final String TEL_MOVEL = "99546-8508";
    private static final String NOME_VOLUME = "Volume Mock";
    private static final String NATUREZA_PRODUTO = "Venda Mockada";
    private static final String SERIE_NF = "MOCK-1516";
    private static final String NUMERO_NF = "65432145";
    private static final String CHAVE_NF = "MK";
    private static final String CODIGO_CFOP = "789";

    @InjectMocks
    private final PedidoKitColetaService service = new PedidoKitColetaServiceImpl();

    @Mock
    private PedidoKitColetaRepository repository;

    @Mock
    private ArquivoRecuperavelService arquivoService;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    // CRIAR

	/*
	 * @Test public void criar_sucesso() throws PortalTransportadorException {
	 * final PedidoKitColeta kit = this.getMockEntidade(); // final String
	 * diretorioArquivoDanfe = kit.getDiretorioArquivoDanfe(); final String
	 * nomeArquivoDanfe = kit.getNomeArquivoDanfe(); // final String
	 * diretorioArquivoLaudo = kit.getDiretorioArquivoLaudo(); final String
	 * nomeArquivoLaudo = kit.getNomeArquivoLaudo(); final List<PedidoKitColeta>
	 * kits = Arrays.asList(kit); final CompletableFuture<String> danfe =
	 * CompletableFuture.completedFuture("ArquivoDanfeBase64"); final
	 * CompletableFuture<String> laudo =
	 * CompletableFuture.completedFuture("ArquivoLaudoBase64");
	 *
	 * when(this.repository.insert(kits)).thenReturn(kits); //
	 * when(this.arquivoService.recuperarArquivoBase64(diretorioArquivoDanfe, //
	 * nomeArquivoDanfe)).thenReturn(danfe); //
	 * when(this.arquivoService.recuperarArquivoBase64(diretorioArquivoLaudo, //
	 * nomeArquivoLaudo)).thenReturn(laudo);
	 *
	 * final List<PedidoKitColeta> retornoEsperado = this.service.criar(kits);
	 *
	 * assertThat(retornoEsperado, not(IsEmptyCollection.empty()));
	 * verify(this.repository, times(1)).insert(kits); }
	 */
	/*
	 * @Test(expected = ValidacaoException.class) public void criar_lista_nula()
	 * throws PortalTransportadorException {
	 * 
	 * this.service.criar(null); }
	 * 
	 * @Test(expected = ValidacaoException.class) public void
	 * criar_lista_vazia() throws PortalTransportadorException {
	 * 
	 * this.service.criar(Collections.emptyList()); }
	 */

    // ATUALIZAR

    @Test
    public void atualizar_sucesso() throws PortalTransportadorException {
        final List<PedidoKitColeta> kits = this.getMockEntidades();

        when(this.repository.saveAll(kits)).thenReturn(kits);

        final List<PedidoKitColeta> retorno = this.service.atualizar(kits);

        assertThat(retorno, not(IsEmptyCollection.empty()));
        verify(this.repository, times(1)).saveAll(anyListOf(PedidoKitColeta.class));
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
        final List<PedidoKitColeta> kits = this.getMockEntidades();

        when(this.repository.findAll()).thenReturn(kits);

        final List<PedidoKitColeta> retorno = this.service.listar();

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
        final PedidoKitColeta kit = this.getMockEntidade();

        when(this.repository.findByCodigo(Mockito.anyLong())).thenReturn(kit);

        final PedidoKitColeta retorno = this.service.recuperar(kit.getCodigo());

        assertNotNull(retorno);
        verify(this.repository, times(1)).findByCodigo(Mockito.anyLong());
    }

    @Test(expected = ValidacaoException.class)
    public void recuperar_chave_nula() throws PortalTransportadorException {

        this.service.recuperar(null);
    }

    @Test(expected = NaoEncontradoException.class)
    public void recuperar_sem_retorno() throws PortalTransportadorException {
        final PedidoKitColeta kit = this.getMockEntidade();

        when(this.repository.findByCodigo(Mockito.anyLong())).thenReturn(null);

        this.service.recuperar(kit.getCodigo());
    }

    // DELETAR

    @Test
    public void deletar_porchave_sucesso() throws PortalTransportadorException {
        final PedidoKitColeta kit = this.getMockEntidade();

        this.service.deletar(kit.getCodigo());

        verify(this.repository, times(1)).deleteByCodigo(Mockito.anyLong());
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

    private List<PedidoKitColeta> getMockEntidades() {
        return Arrays.asList(this.getMockEntidade());
    }

    private PedidoKitColeta getMockEntidade() {
        final Contato contato = this.getContatoMock();
        final Endereco endereco = this.getEnderecoMock();
        final Cliente cliente = this.getClienteMock(contato, endereco);
        final NotaFiscal notaFiscal = this.getNFMock();
        final Volume volume = this.getVolumeMock(notaFiscal);
        return PedidoKitColetaBuilder.init()
                        .codigo(1000L)
                        .valorFreteDoCliente(new BigDecimal(19.9).setScale(2, RoundingMode.FLOOR))
                        .numeroPedidoEnvio(NUMERO_PEDIDO_ENVIO)
                        .tipoPedidoEnvio(TIPO_PEDIDO_ENVIO)
                        .canalDeVenda(CANAL_VENDA)
                        .entregaEhAgendada(Boolean.FALSE)
                        .metodoDeEntrega("211001")
                        .dataCriacao(new Date())
                        .dataEstimadaDeEntrega(new Date())
                        .embarcador(new FilialPK(21, 1200))
                        .transportadora(new TransportadorPK(21, 1001))
                        .cliente(cliente)
                        .addVolume(volume)
                        .diretorioArquivoDanfe("C:\\diretorio_arquivo_danfe")
                        .nomeArquivoDanfe("arquivo_danfe")
                        .diretorioArquivoLaudo("C:\\diretorio_arquivo_laudo")
                        .nomeArquivoLaudo("arquivo_laudo")
                        .build();
    }

    private Contato getContatoMock() {
        return ContatoBuilder.init().email(E_MAIL).telefoneFixo(TEL_FIXO).telefoneMovel(TEL_MOVEL).build();
    }

    private Endereco getEnderecoMock() {
        return EnderecoBuilder.init()
                        .pais(PAIS)
                        .estado(ESTADO)
                        .cidade(CIDADE)
                        .cep(CEP)
                        .bairro(BAIRRO)
                        .logradouro(LOGRADOURO)
                        .numero(NUMERO_ENDERECO)
                        .complemento(COMPLEMENTO)
                        .referencia(REFERENCIA)
                        .build();
    }

    private Cliente getClienteMock(final Contato contato, final Endereco endereco) {
        return ClienteBuilder.init()
                        .tipoDePessoa(TipoPessoaEnum.PF)
                        .nome(NOME_CLIENTE)
                        .sobreNome(SOBRE_NOME)
                        .tipoDoDocumento(TipoDocumentoEnum.CPF)
                        .numeroDoDocumento(CPF)
                        .contato(contato)
                        .endereco(endereco)
                        .build();
    }

    private NotaFiscal getNFMock() {
        return NotaFiscalBuilder.init()
                        .serieNF(SERIE_NF)
                        .numeroNF(NUMERO_NF)
                        .chave(CHAVE_NF)
                        .dataEmissao(new Date())
                        .valorTotalProduto(new BigDecimal(35.0).setScale(2, RoundingMode.FLOOR))
                        .valorTotal(new BigDecimal(35.0).setScale(2, RoundingMode.FLOOR))
                        .codigoCFOP(CODIGO_CFOP)
                        .build();
    }

    private Volume getVolumeMock(final NotaFiscal notaFiscal) {
        return VolumeBuilder.init()
                        .id(10)
                        .nome(NOME_VOLUME)
                        .tipo(TipoVolumeEnum.CAIXA)
                        .peso(new BigDecimal(1.2).setScale(2, RoundingMode.FLOOR))
                        .largura(new BigDecimal(0.15).setScale(2, RoundingMode.FLOOR))
                        .altura(new BigDecimal(0.25).setScale(2, RoundingMode.FLOOR))
                        .comprimento(new BigDecimal(0.35).setScale(2, RoundingMode.FLOOR))
                        .naturezaDoProduto(NATUREZA_PRODUTO)
                        .quantidade(1)
                        .notaFiscal(notaFiscal)
                        .build();
    }
}
