package br.com.viavarejo.integration.web;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.not;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;

import br.com.viavarejo.application.service.LoteCobrancaService;
import br.com.viavarejo.domain.model.builder.LoteCobrancaBuilder;
import br.com.viavarejo.domain.model.enumerator.StatusAprovacaoLoteCobrancaEnum;
import br.com.viavarejo.domain.model.mongodb.cobranca.LoteCobranca;
import io.restassured.http.ContentType;

public class LoteCobrancaEndpointTest extends EndPointConfigurationTest {

    @MockBean
    private LoteCobrancaService loteCobrancaService;
    private String endpoint_root;
    private String endpoint_criar;
    private String endpoint_recuperar_numero_boleto;
    private String endpoint_recuperar_id;
    private String endpoint_listar;
    private String endpoint_atualizar;
    private String endpoint_deletar;
    private String endpoint_deletar_antigos;

    @Before
    public void init() {
        this.endpoint_root = "/lote_cobrancas";
        this.endpoint_criar = this.endpoint_root;
        this.endpoint_recuperar_numero_boleto = this.endpoint_root.concat("/numero_fatura/{numero_fatura}");
        this.endpoint_recuperar_id = this.endpoint_root.concat("/id_viavarejo/{id}");
        this.endpoint_listar = this.endpoint_root.concat("/listar/sincronizacao_viavarejo");
        this.endpoint_atualizar = this.endpoint_root;
        this.endpoint_deletar = this.endpoint_root;
        this.endpoint_deletar_antigos = this.endpoint_root.concat("/antigos/{meses}");
    }

    @Test
    @DirtiesContext
    public void recuperar_loteCobranca_pelo_numero_fatura() {
        final Integer numerofatura = 123456789;
        final LoteCobranca loteCobranca = this.getMockEntidade();
        when(this.loteCobrancaService.recuperarNumeroFatura(numerofatura)).thenReturn(loteCobranca);

        given().pathParam("numero_fatura", numerofatura)
                        .get(this.endpoint_recuperar_numero_boleto)
                        .then()
                        .log()
                        .body()
                        .and()
                        .statusCode(HttpStatus.OK.value())
                        .body("valido", equalTo(Boolean.TRUE));
    }

    @Test
    @DirtiesContext
    public void recuperar_loteCobranca_pelo_id() {
        final String id = "5b2a6ef6927d5026bc2747dc";
        final LoteCobranca loteCobranca = this.getMockEntidade();
        when(this.loteCobrancaService.recuperarId(id)).thenReturn(loteCobranca);

        given().pathParam("id", id).get(this.endpoint_recuperar_id).then().log().body().and().statusCode(HttpStatus.OK.value()).body(
                        "valido", equalTo(Boolean.TRUE));
    }

    @Test
    @DirtiesContext
    public void criar_loteCobranca() {
        final LoteCobranca loteCobranca = this.getMockEntidade();
        when(this.loteCobrancaService.criar(loteCobranca)).thenReturn(loteCobranca);

        given().request()
                        .header("Accept", ContentType.ANY)
                        .header("Content-type", ContentType.JSON)
                        .body(loteCobranca)
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
    public void listar_sincronizacao_loteCobranca() {
        final List<LoteCobranca> lotes = this.getMockEntidades();
        when(this.loteCobrancaService.listarSincronizacaoMainframe()).thenReturn(lotes);

        given().request()
                        .header("Accept", ContentType.ANY)
                        .header("Content-type", ContentType.JSON)
                        .body(lotes)
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
    public void deletar_loteCobranca_com_flag_integracao_desligada() {
        final LoteCobranca lote = this.getMockEntidade();
        doNothing().when(this.loteCobrancaService).removerLotesFlagSincronizacaoDesligada();

        given().request()
                        .header("Accept", ContentType.ANY)
                        .header("Content-type", ContentType.JSON)
                        .body(lote)
                        .when()
                        .delete(this.endpoint_deletar)
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
    public void nao_atualizar_loteCobranca_com_erros_bean_validation() {
        final LoteCobranca lote = this.getMockEntidade();
        when(this.loteCobrancaService.atualizar(lote)).thenReturn(lote);

        given().request()
                        .header("Accept", ContentType.ANY)
                        .header("Content-type", ContentType.JSON)
                        .body(lote)
                        .when()
                        .put(this.endpoint_atualizar)
                        .then()
                        .log()
                        .headers()
                        .and()
                        .log()
                        .body()
                        .and()
                        .statusCode(HttpStatus.BAD_REQUEST.value())
                        .body("valido", equalTo(Boolean.FALSE))
                        .body("entidades", is(empty()));
    }

    @Test
    @DirtiesContext
    public void deletarAntigos() {

        final Integer mesesPassados = 1;
        doNothing().when(this.loteCobrancaService).deletarAntigos(mesesPassados);

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

    private List<LoteCobranca> getMockEntidades() {
        return Arrays.asList(this.getMockEntidade());
    }

    private LoteCobranca getMockEntidade() {
        return LoteCobrancaBuilder.init()
                        .formaPagamento("Boleto")
                        .numeroFatura(123456789)
                        .valorDebito(new BigDecimal("50.00"))
                        .codigoContasReceber(5000)
                        .codigoContasPagar(1000)
                        .statusAprovacao(StatusAprovacaoLoteCobrancaEnum.VAZIO)
                        .observacao("Não possui")
                        .build();
    }
}
