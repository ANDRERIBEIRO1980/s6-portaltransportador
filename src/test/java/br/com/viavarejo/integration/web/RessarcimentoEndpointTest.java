package br.com.viavarejo.integration.web;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.empty;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;

import br.com.viavarejo.application.exception.PortalTransportadorException;
import br.com.viavarejo.application.service.RessarcimentoService;
import br.com.viavarejo.domain.model.builder.MercadoriaRessarcimentoBuilder;
import br.com.viavarejo.domain.model.builder.RessarcimentoBuilder;
import br.com.viavarejo.domain.model.mongodb.cobranca.MercadoriaRessarcimento;
import br.com.viavarejo.domain.model.mongodb.cobranca.Ressarcimento;
import io.restassured.http.ContentType;

public class RessarcimentoEndpointTest extends EndPointConfigurationTest {

    @MockBean
    private RessarcimentoService service;
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
        this.endpoint_root = "/ressarcimentos";
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
    public void listar_filiais() throws PortalTransportadorException {

        when(this.service.listar()).thenReturn(this.getMockEntidades());

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
    public void recuperar() throws PortalTransportadorException {
        final Ressarcimento obj = this.getMockEntidades().get(0);
        final Long codigo = obj.getPk();

        when(this.service.recuperar(codigo)).thenReturn(obj);
        given()
                .pathParam("codigo", codigo)
                .get(this.endpoint_recuperar)
        .then()
                .log().body()
        .and()
            .statusCode(HttpStatus.OK.value())
                        .body("valido", equalTo(Boolean.TRUE));
    }

    @Test
    @DirtiesContext
    public void criar() throws PortalTransportadorException {
        final List<Ressarcimento> lista = this.getMockEntidades();
        when(this.service.criar(lista)).thenReturn(lista);

        given()
                .request()
                .header("Accept", ContentType.ANY)
                .header("Content-type", ContentType.JSON)
                .body(lista)
        .when()
        .post(this.endpoint_criar)
        .then()
                .log().headers()
        .and()
                .log().body()
        .and()
            .statusCode(HttpStatus.CREATED.value())
            .body("valido", equalTo(Boolean.TRUE));
    }

    @Test
    @DirtiesContext
    public void deletar_todas() throws PortalTransportadorException {
        doNothing().when(this.service).deletar();

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
    public void deletar() throws PortalTransportadorException {
        final Ressarcimento obj = this.getMockEntidades().get(0);
        final Long codigo = obj.getPk();

        doNothing().when(this.service).deletar(codigo);

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
    public void deletarAntigos_sucesso() {

        final Integer mesesPassados = 1;
        doNothing().when(this.service).deletarAntigos(mesesPassados);

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
    public void atualizar() throws PortalTransportadorException {
        final List<Ressarcimento> lista = this.getMockEntidades();
        when(this.service.atualizar(lista)).thenReturn(lista);

        given()
                .request()
                .header("Accept", ContentType.ANY)
                .header("Content-type", ContentType.JSON)
                        .body(lista)
        .when()
        .put(this.endpoint_atualizar)
        .then()
                .log().headers()
        .and()
                .log().body()
        .and()
            .statusCode(HttpStatus.OK.value())
            .body("valido", equalTo(Boolean.TRUE));
    }

    @Test
    @DirtiesContext
    public void atualizar_entidade_invalida() throws PortalTransportadorException {
        final List<Ressarcimento> lista = new ArrayList<>();
        lista.add(new Ressarcimento());

        when(this.service.atualizar(lista)).thenReturn(lista);

        given()
                .request()
                .header("Accept", ContentType.ANY)
                .header("Content-type", ContentType.JSON)
                .body(lista)
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

    private List<Ressarcimento> getMockEntidades() {

        final MercadoriaRessarcimento mercadoria = MercadoriaRessarcimentoBuilder.init()
                        .sku("1234")
                        .descricao("produtos")
                        .quantidade(2)
                        .build();

        final Ressarcimento obj = RessarcimentoBuilder.init()
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

        return Arrays.asList(obj);
    }
}
