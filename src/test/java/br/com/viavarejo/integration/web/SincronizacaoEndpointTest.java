package br.com.viavarejo.integration.web;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.collection.IsEmptyCollection.empty;
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
import br.com.viavarejo.application.service.SincronizacaoService;
import br.com.viavarejo.domain.model.mongodb.sincronizacao.SincronizacaoProcesso;
import io.restassured.http.ContentType;

public class SincronizacaoEndpointTest extends EndPointConfigurationTest {

    @MockBean
    private SincronizacaoService sincronizacaoService;
    private String endpoint_root;
    private String endpoint_listar;
    private String endpoint_recuperar;
    private String endpoint_criar;
    private String endpoint_deletarTodas;
    private String endpoint_deletar;
    private String endpoint_atualizar;

    @Before
    public void init() {
        this.endpoint_root = "/sincronizacoes";
        this.endpoint_criar = endpoint_root;
        this.endpoint_deletarTodas = endpoint_root;
        this.endpoint_atualizar = endpoint_root;
        this.endpoint_listar = endpoint_root.concat("/listar");
        this.endpoint_recuperar = endpoint_root.concat("/{processo}");
        this.endpoint_deletar = endpoint_recuperar;
    }

    @Test
    @DirtiesContext
    public void listar_sincronizacoes() throws PortalTransportadorException {

        when(sincronizacaoService.listar()).thenReturn(this.getMockEntidades());

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
    public void recuperar_sincronizacao() throws PortalTransportadorException {
        SincronizacaoProcesso sincronizacao = this.getMockEntidades().get(0);
        String nomeProcesso = sincronizacao.getProcesso();
        when(sincronizacaoService.recuperar(nomeProcesso)).thenReturn(sincronizacao);

        given()
                .pathParam("processo", nomeProcesso)
                .get(this.endpoint_recuperar)
        .then()
                .log().body()
        .and()
            .statusCode(HttpStatus.OK.value())
            .body("valido", equalTo(Boolean.TRUE))
            .body("entidades[0].processo", equalTo(nomeProcesso));
    }

    @Test
    @DirtiesContext
    public void criar_sincronizacoes() throws PortalTransportadorException {
        List<SincronizacaoProcesso> sincronizacoes = this.getMockEntidades();
        when(sincronizacaoService.criar(sincronizacoes)).thenReturn(sincronizacoes);
        
        given()
                .request()
                .header("Accept", ContentType.ANY)
                .header("Content-type", ContentType.JSON)
                .body(sincronizacoes)
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
    public void deletar_sincronizacoes() throws PortalTransportadorException {
        doNothing().when(sincronizacaoService).deletar();
        
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
    public void deletar_sincronizacao() throws PortalTransportadorException {
        SincronizacaoProcesso sincronizacao = this.getMockEntidades().get(0);
        String nomeProcesso = sincronizacao.getProcesso();
        doNothing().when(sincronizacaoService).deletar(nomeProcesso);
        
        given()
                .pathParam("processo", nomeProcesso)
        .request()
                .delete(this.endpoint_deletar)
        .then()
                .log().body()
        .and()
            .statusCode(HttpStatus.OK.value());
    }
    
    @Test
    @DirtiesContext
    public void atualizar_sincronizacoes() throws PortalTransportadorException {
        List<SincronizacaoProcesso> sincronizacoes = this.getMockEntidades();
        when(sincronizacaoService.atualizar(sincronizacoes)).thenReturn(sincronizacoes);
        
        given()
                .request()
                .header("Accept", ContentType.ANY)
                .header("Content-type", ContentType.JSON)
                .body(sincronizacoes)
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
    
    private List<SincronizacaoProcesso> getMockEntidades() {
        SincronizacaoProcesso sincronizacaoMock = new SincronizacaoProcesso("Sincronizacao", new Date());
        return Arrays.asList(sincronizacaoMock);
    }
}
