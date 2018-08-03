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
import br.com.viavarejo.application.service.FilialService;
import br.com.viavarejo.domain.model.builder.ContatoBuilder;
import br.com.viavarejo.domain.model.builder.EnderecoBuilder;
import br.com.viavarejo.domain.model.builder.FilialBuilder;
import br.com.viavarejo.domain.model.mongodb.Contato;
import br.com.viavarejo.domain.model.mongodb.Endereco;
import br.com.viavarejo.domain.model.mongodb.filial.Filial;
import br.com.viavarejo.domain.model.mongodb.filial.FilialPK;
import io.restassured.http.ContentType;

public class FilialEndpointTest extends EndPointConfigurationTest {

    private static final String EMAIL = "email@email.com.br";
    private static final String NUMERO = "2005";
    private static final String LOGRADOURO = "Santos Dumond";
    private static final String RAZAO_SOCIAL = "Empresa Razão Social Mock";
    private static final String NOME = "Empresa Mock";
    private static final String DOCUMENTO = "23.321.321/0001-85";
    private static final String REFERENCIA = "Referencia Mock";
    private static final String BAIRRO = "Bairro da Mock";
    private static final String CEP = "09176330";
    private static final String COMPLEMENTO = "Complemento Mock";
    private static final String CIDADE = "Cidade da Mock";
    private static final String ESTADO = "Estado do Mock";
    private static final String TEL_MOVEL = "993566804";
    private static final String TEL_FIXO = "44563854";
    @MockBean
    private FilialService filialService;
    private String endpoint_root;
    private String endpoint_listar;
    private String endpoint_recuperar;
    private String endpoint_criar;
    private String endpoint_deletarTodas;
    private String endpoint_deletar;
    private String endpoint_atualizar;

    @Before
    public void init() {
        this.endpoint_root = "/filiais";
        this.endpoint_criar = this.endpoint_root;
        this.endpoint_deletarTodas = this.endpoint_root;
        this.endpoint_atualizar = this.endpoint_root;
        this.endpoint_listar = this.endpoint_root.concat("/listar");
        this.endpoint_recuperar = this.endpoint_root.concat("/{empresa}/{codigo}");
        this.endpoint_deletar = this.endpoint_recuperar;
    }

    @Test
    @DirtiesContext
    public void listar_filiais() throws PortalTransportadorException {

        when(this.filialService.listar()).thenReturn(this.getMockEntidades());

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
    public void recuperar_filial() throws PortalTransportadorException {
        final Filial filial = this.getMockEntidades().get(0);
        final FilialPK filialPk = filial.getPk();
        when(this.filialService.recuperar(filialPk)).thenReturn(filial);

        given()
                .pathParam("empresa", filialPk.getEmpresa())
                .pathParam("codigo", filialPk.getCodigo())
                .get(this.endpoint_recuperar)
        .then()
                .log().body()
        .and()
            .statusCode(HttpStatus.OK.value())
            .body("valido", equalTo(Boolean.TRUE))
            .body("entidades[0].nome", equalTo(filial.getNome()));
    }

    @Test
    @DirtiesContext
    public void criar_filiais() throws PortalTransportadorException {
        final List<Filial> filiais = this.getMockEntidades();
        when(this.filialService.criar(filiais)).thenReturn(filiais);

        given()
                .request()
                .header("Accept", ContentType.ANY)
                .header("Content-type", ContentType.JSON)
                .body(filiais)
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
    public void deletar_filiais() throws PortalTransportadorException {
        doNothing().when(this.filialService).deletar();

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
    public void deletar_filial() throws PortalTransportadorException {
        final Filial filial = this.getMockEntidades().get(0);
        final FilialPK filialPk = filial.getPk();
        doNothing().when(this.filialService).deletar(filialPk);

        given()
                .pathParam("empresa", filialPk.getEmpresa())
                .pathParam("codigo", filialPk.getCodigo())
        .request()
                .delete(this.endpoint_deletar)
        .then()
                .log().body()
        .and()
            .statusCode(HttpStatus.OK.value());
    }

    @Test
    @DirtiesContext
    public void atualizar_filiais() throws PortalTransportadorException {
        final List<Filial> filiais = this.getMockEntidades();
        when(this.filialService.atualizar(filiais)).thenReturn(filiais);

        given()
                .request()
                .header("Accept", ContentType.ANY)
                .header("Content-type", ContentType.JSON)
                .body(filiais)
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
    public void atualizar_filiais_entidade_invalida() throws PortalTransportadorException {
        final List<Filial> filiais = this.getMockEntidades();

        // Campo obrigatorio para cair no bean validation
        filiais.stream().findFirst().get().getContato().setTelefoneFixo(null);

        when(this.filialService.atualizar(filiais)).thenReturn(filiais);

        given()
                .request()
                .header("Accept", ContentType.ANY)
                .header("Content-type", ContentType.JSON)
                .body(filiais)
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

    private List<Filial> getMockEntidades() {
        final Endereco endereco = EnderecoBuilder.init()
                        .logradouro(LOGRADOURO)
                        .numero(NUMERO)
                        .referencia(REFERENCIA)
                        .bairro(BAIRRO)
                        .cep(CEP)
                        .complemento(COMPLEMENTO)
                        .cidade(CIDADE)
                        .estado(ESTADO)
                        .build();
        final Contato contato = ContatoBuilder.init().email(EMAIL).telefoneMovel(TEL_MOVEL).telefoneFixo(TEL_FIXO).build();
        final Filial filialMock = FilialBuilder.init()
                        .codigoEmpresa(10)
                        .codigo(10)
                        .cnpj(DOCUMENTO)
                        .dataCriacao(new Date())
                        .nome(NOME)
                        .razaoSocial(RAZAO_SOCIAL)
                        .endereco(endereco)
                        .contato(contato)
                        .build();
        return Arrays.asList(filialMock);
    }
}
