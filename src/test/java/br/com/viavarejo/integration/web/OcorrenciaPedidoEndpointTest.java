package br.com.viavarejo.integration.web;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.empty;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;

import br.com.viavarejo.application.exception.PortalTransportadorException;
import br.com.viavarejo.application.service.OcorrenciaPedidoService;
import br.com.viavarejo.domain.model.builder.OcorrenciaPedidoBuilder;
import br.com.viavarejo.domain.model.enumerator.TipoOperacaoOcorrenciaEnum;
import br.com.viavarejo.domain.model.mongodb.ocorrencia.EventoPedido;
import br.com.viavarejo.domain.model.mongodb.ocorrencia.OcorrenciaPedido;
import io.restassured.http.ContentType;

public class OcorrenciaPedidoEndpointTest extends EndPointConfigurationTest {

    @MockBean
    private OcorrenciaPedidoService ocorrenciaService;
    private String endpoint_root;
    private String endpoint_listar;
    private String endpoint_recuperar;
    private String endpoint_criar;
    private String endpoint_deletarTodas;
    private String endpoint_atualizar;
    private String endpoint_deletar_antigos;

    @Before
    public void init() {
        this.endpoint_root = "/ocorrencias";
        this.endpoint_criar = this.endpoint_root;
        this.endpoint_deletarTodas = this.endpoint_root;
        this.endpoint_atualizar = this.endpoint_root;
        this.endpoint_listar = this.endpoint_root.concat("/listar");
        this.endpoint_recuperar = this.endpoint_root.concat("/{codigoPedido}");
        this.endpoint_deletar_antigos = this.endpoint_root.concat("/antigos/{meses}");
    }

    @Test
    @DirtiesContext
    public void listar_ocorrencias_pedido() throws PortalTransportadorException {

        when(this.ocorrenciaService.listar()).thenReturn(this.getMockEntidades());

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
    public void recuperar_pedido() throws PortalTransportadorException {
        final OcorrenciaPedido mockObj = this.getMockEntidades().get(0);
        final String codigoPedido = mockObj.getCodigoPedido();
        when(this.ocorrenciaService.recuperar(codigoPedido)).thenReturn(mockObj);

        given()
                .pathParam("codigoPedido", codigoPedido)
                .get(this.endpoint_recuperar)
        .then()
                .log().body()
        .and()
            .statusCode(HttpStatus.OK.value())
            .body("valido", equalTo(Boolean.TRUE));
    }

    @Test
    @DirtiesContext
    public void criar_ocorrencias_pedido_erro_campos_obrigatorios() throws PortalTransportadorException {
        final List<OcorrenciaPedido> ocorrencias = this.getMockEntidades();
        when(this.ocorrenciaService.criar(ocorrencias)).thenReturn(ocorrencias);

        given()
                .request()
                .header("Accept", ContentType.ANY)
                .header("Content-type", ContentType.JSON)
                .body(ocorrencias)
        .when()
        .post(this.endpoint_criar)
        .then()
                .log().headers()
        .and()
                .log().body()
        .and()
                        .statusCode(HttpStatus.BAD_REQUEST.value())
                        .body("valido", equalTo(Boolean.FALSE));
    }

    @Test
    @DirtiesContext
    public void deletar_ocorrencias_pedido_erro_campos_obrigatorios() throws PortalTransportadorException {
        final List<OcorrenciaPedido> ocorrencias = this.getMockEntidades();

        doNothing().when(this.ocorrenciaService).deletar();

        given()
            .request()
            .header("Accept", ContentType.ANY)
            .header("Content-type", ContentType.JSON)
            .body(ocorrencias)
            .delete(this.endpoint_deletarTodas)
        .then()
            .log()
            .headers()
            .and()
            .log()
            .body()
        .and()
                        .statusCode(HttpStatus.BAD_REQUEST.value())
            .body("valido", equalTo(Boolean.FALSE));
    }

    @Test
    @DirtiesContext
    public void deletarAntigos() {

        final Integer mesesPassados = 1;
        doNothing().when(this.ocorrenciaService).deletarAntigos(mesesPassados);

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
    public void atualizar_ocorrencias_pedido_erro_campos_obrigatorios() throws PortalTransportadorException {
        final List<OcorrenciaPedido> ocorrencias = this.getMockEntidades();
        when(this.ocorrenciaService.atualizar(ocorrencias)).thenReturn(ocorrencias);

        given()
                .request()
                .header("Accept", ContentType.ANY)
                .header("Content-type", ContentType.JSON)
                .body(ocorrencias)
        .when()
        .put(this.endpoint_atualizar)
        .then()
                .log().headers()
        .and()
                .log().body()
        .and()
                        .statusCode(HttpStatus.BAD_REQUEST.value())
                        .body("valido", equalTo(Boolean.FALSE));
    }

    private List<OcorrenciaPedido> getMockEntidades() {
        final OcorrenciaPedido ocorrenciaMock = OcorrenciaPedidoBuilder.init()
                        .codigoPedido("500")
                        .codigoVolume("30")
                        .tipoOperacao(TipoOperacaoOcorrenciaEnum.NORMAL)
                        .numeroNotaFiscal("000632500")
                        .comEvento(new EventoPedido(new Date(), 1, "7744MF", "Ocorrência registrada", "S6"))
                        .build();
        return Arrays.asList(ocorrenciaMock);
    }
}
