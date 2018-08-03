package br.com.viavarejo.integration.web;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.not;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;

import br.com.viavarejo.application.service.LogAplicacaoService;
import br.com.viavarejo.domain.model.mongodb.LogAplicacao;
import io.restassured.http.ContentType;

public class LogAplicacaoEndpointTest extends EndPointConfigurationTest {

    private static final String NOME_PROCESSO = "Testes Processo";

    @MockBean
    private LogAplicacaoService service;
    private String endpoint_root;
    private String endpoint_recuperar_nomeprocesso;
    private String endpoint_listar;
    private String endpoint_deletar_antigos;

    @Before
    public void init() {
        this.endpoint_root = "/log_aplicacao";
        this.endpoint_recuperar_nomeprocesso = this.endpoint_root.concat("/{nome_processo}");
        this.endpoint_listar = this.endpoint_root.concat("/listar");
        this.endpoint_deletar_antigos = this.endpoint_root.concat("/antigos/{meses}");
    }

    @Test
    @DirtiesContext
    public void recuperar_loteCobranca_pelo_numero_fatura() {
        final String nomeProcesso = NOME_PROCESSO;
        final LogAplicacao entidade = this.getMockEntidade();
        when(this.service.recuperar(nomeProcesso)).thenReturn(entidade);

        given().pathParam("nome_processo", nomeProcesso)
                        .get(this.endpoint_recuperar_nomeprocesso)
                        .then()
                        .log()
                        .body()
                        .and()
                        .statusCode(HttpStatus.OK.value())
                        .body("valido", equalTo(Boolean.TRUE));
    }

    @Test
    @DirtiesContext
    public void listar_sincronizacao_loteCobranca() {
        final List<LogAplicacao> lista = this.getMockEntidades();
        when(this.service.listar()).thenReturn(lista);

        given().request()
                        .header("Accept", ContentType.ANY)
                        .header("Content-type", ContentType.JSON)
                        .body(lista)
                        .when()
                        .get(this.endpoint_listar)
                        .then()
                        .log()
                        .headers()
                        .and()
                        .log()
                        .body()
                        .and()
                        .statusCode(HttpStatus.OK.value())
                        .body("valido", equalTo(Boolean.TRUE))
                        .body("entidades", not(empty()));
    }

    @Test
    @DirtiesContext
    public void deletarAntigos() {

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

    private List<LogAplicacao> getMockEntidades() {
        return Arrays.asList(this.getMockEntidade());
    }

    private LogAplicacao getMockEntidade() {
        final LogAplicacao entidade = new LogAplicacao();

        entidade.setProcesso(NOME_PROCESSO);

        return new LogAplicacao();
    }

}
