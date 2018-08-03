package br.com.viavarejo.integration.web;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.empty;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;

import br.com.viavarejo.application.exception.PortalTransportadorException;
import br.com.viavarejo.application.service.PedidoKitColetaService;
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
import io.restassured.http.ContentType;

public class PedidoKitColetaEndpointTest extends EndPointConfigurationTest {

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

    @MockBean
    private PedidoKitColetaService kitColetaService;
    private String endpoint_root;
    private String endpoint_listar;
    private String endpoint_recuperar;
    private String endpoint_criar;
    private String endpoint_deletarTodas;
    private String endpoint_deletar;
    private String endpoint_atualizar;
    private String endpoint_deletar_antigos;

    @Before
    public void init() {
        this.endpoint_root = "/reversas";
        this.endpoint_criar = this.endpoint_root;
        this.endpoint_deletarTodas = this.endpoint_root;
        this.endpoint_atualizar = this.endpoint_root;
        this.endpoint_listar = this.endpoint_root.concat("/listar");
        this.endpoint_recuperar = this.endpoint_root.concat("/{codigo}");
        this.endpoint_deletar = this.endpoint_recuperar;
        this.endpoint_deletar_antigos = this.endpoint_root.concat("/antigos/{meses}");
    }

    @Test
    @DirtiesContext
    public void listar_kitColeta() throws PortalTransportadorException {

        when(this.kitColetaService.listar()).thenReturn(this.getMockEntidades());

        given()
                .get(this.endpoint_listar)
        .then()
                .log().body()
        .and()
            .statusCode(HttpStatus.OK.value())
            .body("valido", equalTo(Boolean.TRUE))
            .body("entidades[0]", not(empty()));
    }

    @Test
    @DirtiesContext
    public void recuperar_kitColeta() throws PortalTransportadorException {
        final PedidoKitColeta kitColeta = this.getMockEntidade();
        final Long codigo = kitColeta.getCodigo();
        when(this.kitColetaService.recuperar(codigo)).thenReturn(kitColeta);

        given()
                .pathParam("codigo", codigo)
                .get(this.endpoint_recuperar)
        .then()
                .log().body()
        .and()
            .statusCode(HttpStatus.OK.value())
            .body("valido", equalTo(Boolean.TRUE))
            .body("entidades[0].cliente.nome", is(equalTo(NOME_CLIENTE)));
    }

	/*
	 * @Test
	 * 
	 * @DirtiesContext public void criar_kitColeta() throws
	 * PortalTransportadorException { final List<PedidoKitColeta> kits =
	 * this.getMockEntidades();
	 * when(this.kitColetaService.criarReversas(kits)).thenReturn(kits);
	 * 
	 * given() .request() .header("Accept", ContentType.ANY)
	 * .header("Content-type", ContentType.JSON) .body(kits) .when()
	 * .post(this.endpoint_criar) .then() .log().headers() .and() .log().body()
	 * .and() .body("valido", is(equalTo(Boolean.TRUE))) .body("mensagens",
	 * empty()); }
	 */

	/*
	 * @Test
	 * 
	 * @DirtiesContext public void
	 * criar_kitColeta_com_erros_campos_obrigatorios() throws
	 * PortalTransportadorException { final PedidoKitColeta kit =
	 * this.getMockEntidade(); kit.setCodigo(null);
	 * 
	 * final List<PedidoKitColeta> kits = Arrays.asList(kit);
	 * when(this.kitColetaService.criar(kits)).thenReturn(kits);
	 * 
	 * given() .request() .header("Accept", ContentType.ANY)
	 * .header("Content-type", ContentType.JSON) .body(kits) .when()
	 * .post(this.endpoint_criar) .then() .log().headers() .and() .log().body()
	 * .and() .statusCode(HttpStatus.BAD_REQUEST.value()) .body("valido",
	 * is(equalTo(Boolean.FALSE))) .body("mensagens", not(empty())); }
	 */

    @Test
    @DirtiesContext
    public void deletar_kitsColeta() throws PortalTransportadorException {
        doNothing().when(this.kitColetaService).deletar();

        given()
                .request()
                .delete(this.endpoint_deletarTodas)
        .then()
                .log().body()
        .and()
            .statusCode(HttpStatus.OK.value());
    }

    @Test
    @DirtiesContext
    public void deletar_kitColeta() throws PortalTransportadorException {
        final PedidoKitColeta kitColeta = this.getMockEntidade();
        final Long codigo = kitColeta.getCodigo();
        doNothing().when(this.kitColetaService).deletar(codigo);

        given()
                .pathParam("codigo", codigo)
        .request()
                .delete(this.endpoint_deletar)
        .then()
                .log().body()
        .and()
            .statusCode(HttpStatus.OK.value());
    }

    @Test
    @DirtiesContext
    public void deletarAntigos() {

        final Integer mesesPassados = 1;
        doNothing().when(this.kitColetaService).deletarAntigos(mesesPassados);

        given().request()
                        .header("Accept", ContentType.ANY)
                        .header("Content-type", ContentType.JSON)
                        .pathParam("meses", mesesPassados)
                        .when()
                        .delete(this.endpoint_deletar_antigos)
                        .then()
                        .log()
                        .headers()
                        .and()
                        .log()
                        .body()
                        .and()
                        .statusCode(HttpStatus.OK.value())
                        .body("valido", equalTo(Boolean.TRUE))
                        .body("entidades", is(empty()));
    }

    @Test
    @DirtiesContext
    public void atualizar_kitsColeta() throws PortalTransportadorException {
        final List<PedidoKitColeta> kits = this.getMockEntidades();
        when(this.kitColetaService.atualizar(kits)).thenReturn(kits);

        given()
                .request()
                .header("Accept", ContentType.ANY)
                .header("Content-type", ContentType.JSON)
                .body(kits)
        .when()
        .put(this.endpoint_atualizar)
        .then()
                .log().headers()
        .and()
                .log().body()
        .and()
            .statusCode(HttpStatus.OK.value())
            .body("valido", equalTo(Boolean.TRUE))
            .body("mensagens", empty());
    }

    @Test
    @DirtiesContext
    public void atualizar_kitsColeta_com_erro_campos_obrigatorios() throws PortalTransportadorException {
        final PedidoKitColeta kit = this.getMockEntidade();
        kit.setCodigo(null);

        final List<PedidoKitColeta> kits = Arrays.asList(kit);
        when(this.kitColetaService.atualizar(kits)).thenReturn(kits);

        given()
                .request()
                .header("Accept", ContentType.ANY)
                .header("Content-type", ContentType.JSON)
                .body(kits)
        .when()
        .put(this.endpoint_atualizar)
        .then()
                .log().headers()
        .and()
                .log().body()
        .and()
                        .statusCode(HttpStatus.BAD_REQUEST.value())
            .body("valido", equalTo(Boolean.FALSE))
            .body("mensagens", not(empty()));
    }

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
                        .canalDeVenda(CANAL_VENDA)
                        .entregaEhAgendada(Boolean.FALSE)
                        .metodoDeEntrega("211001")
                        .dataCriacao(new Date())
                        .dataEstimadaDeEntrega(new Date())
                        .transportadora(new TransportadorPK(10, 10))
                        .embarcador(new FilialPK(20, 20))
                        .cliente(cliente)
                        .addVolume(volume)
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
