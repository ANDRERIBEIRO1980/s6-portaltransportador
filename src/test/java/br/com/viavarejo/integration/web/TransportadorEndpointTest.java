package br.com.viavarejo.integration.web;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;

import br.com.viavarejo.application.exception.PortalTransportadorException;
import br.com.viavarejo.application.service.TransportadorService;
import br.com.viavarejo.domain.model.builder.ContatoTransportadorBuilder;
import br.com.viavarejo.domain.model.builder.TransportadorBuilder;
import br.com.viavarejo.domain.model.mongodb.transportador.ContatoTransportador;
import br.com.viavarejo.domain.model.mongodb.transportador.Transportador;
import br.com.viavarejo.domain.model.mongodb.transportador.TransportadorPK;
import io.restassured.http.ContentType;

public class TransportadorEndpointTest extends EndPointConfigurationTest {

    private static final String EMAIL = "transportadora@transp.com.br";
    private static final String DOCUMENTO = "23.321.321/0001-85";
    @MockBean
    private TransportadorService transportadoraService;
    private String endpoint_root;
    private String endpoint_listar;
    private String endpoint_recuperar;
    private String endpoint_criar;
    private String endpoint_deletarTodas;
    private String endpoint_deletar;
    private String endpoint_atualizar;

    @Before
    public void init() {
        this.endpoint_root = "/transportadoras";
        this.endpoint_criar = this.endpoint_root;
        this.endpoint_deletarTodas = this.endpoint_root;
        this.endpoint_atualizar = this.endpoint_root;
        this.endpoint_listar = this.endpoint_root.concat("/listar");
        this.endpoint_recuperar = this.endpoint_root.concat("/{codigoEmpresa}/{codigoFornecedor}");
        this.endpoint_deletar = this.endpoint_recuperar;
    }

    @Test
    public void listar_transportadoras() throws PortalTransportadorException {

        when(this.transportadoraService.listar()).thenReturn(this.getMockEntidades());

        given().get(this.endpoint_listar)
                        .then()
                        .log()
                        .body()
                        .and()
                        .statusCode(HttpStatus.OK.value())
                        .body("valido", equalTo(Boolean.TRUE))
                        .body("entidades[0]", not(empty()));
    }

    @Test
    public void recuperar_transportadora() throws PortalTransportadorException {
        final Transportador transportadora = this.getMockEntidades().get(0);
        final TransportadorPK transportadoraPk = transportadora.getPk();
        when(this.transportadoraService.recuperar(transportadoraPk)).thenReturn(transportadora);

        given().pathParam("codigoEmpresa", transportadoraPk.getCodigoEmpresa())
                        .pathParam("codigoFornecedor", transportadoraPk.getCodigo())
                        .get(this.endpoint_recuperar)
                        .then()
                        .log()
                        .body()
                        .and()
                        .statusCode(HttpStatus.OK.value())
                        .body("valido", equalTo(Boolean.TRUE));
    }

    @Test
    public void criar_transportadoras() throws PortalTransportadorException {
        final List<Transportador> transportadoras = this.getMockEntidades();
        when(this.transportadoraService.criar(transportadoras)).thenReturn(transportadoras);

        given().request()
                        .header("Accept", ContentType.ANY)
                        .header("Content-type", ContentType.JSON)
                        .body(transportadoras)
                        .when()
                        .post(this.endpoint_criar)
                        .then()
                        .log()
                        .headers()
                        .and()
                        .log()
                        .body()
                        .and()
                        .statusCode(HttpStatus.CREATED.value())
                        .body("valido", equalTo(Boolean.TRUE));
    }

    @Test
    @DirtiesContext
    public void deletar_transportadoras() throws PortalTransportadorException {
        doNothing().when(this.transportadoraService).deletar();

        given().request().delete(this.endpoint_deletarTodas).then().log().body().and().statusCode(HttpStatus.OK.value());
    }

    @Test
    @DirtiesContext
    public void deletar_transportadora() throws PortalTransportadorException {
        final Transportador transportadora = this.getMockEntidades().get(0);
        final TransportadorPK transportadoraPk = transportadora.getPk();
        doNothing().when(this.transportadoraService).deletar(transportadoraPk);

        given().pathParam("codigoEmpresa", transportadoraPk.getCodigoEmpresa())
                        .pathParam("codigoFornecedor", transportadoraPk.getCodigo())
                        .request()
                        .delete(this.endpoint_deletar)
                        .then()
                        .log()
                        .body()
                        .and()
                        .statusCode(HttpStatus.OK.value());
    }

    @Test
    public void atualizar_transportadoras() throws PortalTransportadorException {
        final Transportador transportador = this.getMockEntidade();
        final List<Transportador> transportadoras = Arrays.asList(transportador);

        when(this.transportadoraService.atualizar(transportadoras)).thenReturn(transportadoras);

        given().request()
                        .header("Accept", ContentType.ANY)
                        .header("Content-type", ContentType.JSON)
                        .body(transportadoras)
                        .when()
                        .put(this.endpoint_atualizar)
                        .then()
                        .log()
                        .headers()
                        .and()
                        .log()
                        .body()
                        .and()
                        .statusCode(HttpStatus.OK.value())
                        .body("valido", equalTo(Boolean.TRUE));
    }

    private List<Transportador> getMockEntidades() {
        return Arrays.asList(this.getMockEntidade());
    }

    private Transportador getMockEntidade() {
        final ContatoTransportador contato = ContatoTransportadorBuilder.init().email(EMAIL).build();
        return TransportadorBuilder.init()
                        .codigoEmpresa(20)
                        .codigo(20)
                        .cnpj(DOCUMENTO)
                        .contatoComercial(contato)
                        .build();
    }
}
