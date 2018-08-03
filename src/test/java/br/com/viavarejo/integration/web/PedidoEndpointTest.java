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
import br.com.viavarejo.application.service.PedidoService;
import br.com.viavarejo.domain.model.builder.ClienteBuilder;
import br.com.viavarejo.domain.model.builder.ComplementoBuilder;
import br.com.viavarejo.domain.model.builder.PedidoBuilder;
import br.com.viavarejo.domain.model.builder.VolumeBuilder;
import br.com.viavarejo.domain.model.enumerator.TipoVolumeEnum;
import br.com.viavarejo.domain.model.mongodb.pedido.Cliente;
import br.com.viavarejo.domain.model.mongodb.pedido.Complemento;
import br.com.viavarejo.domain.model.mongodb.pedido.Pedido;
import br.com.viavarejo.domain.model.mongodb.pedido.Volume;
import io.restassured.http.ContentType;

public class PedidoEndpointTest extends EndPointConfigurationTest {

    private static final String NUMERO_ROMANEIO = "256432";
    private static final String NOME_CLIENTE = "Cliente Mock";
    private static final String NUMERO_PEDIDO = "23321321000185";
    @MockBean
    private PedidoService pedidoService;
    private String endpoint_root;
    private String endpoint_listar;
    private String endpoint_recuperar;
    private String endpoint_deletarTodas;
    private String endpoint_deletar;
    private String endpoint_deletar_antigos;

    @Before
    public void init() {
        this.endpoint_root = "/pedidos";
        this.endpoint_deletarTodas = this.endpoint_root;
        this.endpoint_listar = this.endpoint_root.concat("/listar");
        this.endpoint_recuperar = this.endpoint_root.concat("/{codigo}");
        this.endpoint_deletar = this.endpoint_recuperar;
        this.endpoint_deletar_antigos = this.endpoint_root.concat("/antigos/{meses}");
    }

    @Test
    @DirtiesContext
	public void listar_pedido() throws PortalTransportadorException {

        when(this.pedidoService.listar()).thenReturn(this.getMockEntidades());

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
		final Pedido pedido = this.getMockEntidades().get(0);
        final Long codigoPedido = pedido.getPk();
        when(this.pedidoService.recuperar(codigoPedido)).thenReturn(pedido);

        given()
                        .pathParam("codigo", codigoPedido)
                .get(this.endpoint_recuperar)
        .then()
                .log().headers()
        .and()
                .log().body()
        .and()
            .statusCode(HttpStatus.OK.value())
            .body("valido", equalTo(Boolean.TRUE))
				.body("entidades[0].cliente.nome", equalTo(pedido.getCliente().getNome()));
    }

    @Test
    @DirtiesContext
	public void deletar_pedido() throws PortalTransportadorException {
        doNothing().when(this.pedidoService).deletar();

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
	public void deletar_pedido_pelo_codigo() throws PortalTransportadorException {
		final Pedido pedido = this.getMockEntidades().get(0);
		final Long codigo = pedido.getPk();
		doNothing().when(this.pedidoService).deletar(codigo);

		final Long codigoPedido = pedido.getPk();
        doNothing().when(this.pedidoService).deletar(codigoPedido);

        given()
                        .pathParam("codigo", codigoPedido)
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
        doNothing().when(this.pedidoService).deletarAntigos(mesesPassados);

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

    private List<Pedido> getMockEntidades() {
        final Cliente cliente = ClienteBuilder.init().nome(NOME_CLIENTE).build();
        final Volume volume = VolumeBuilder.init().id(50).tipo(TipoVolumeEnum.CAIXA).build();
        final Complemento complemento = ComplementoBuilder.init()
                        .numeroRomaneio(NUMERO_ROMANEIO)
                        .build();

        final Pedido obj = PedidoBuilder.init()
                        .codigo(55L)
                        .numeroPedidoEnvio(NUMERO_PEDIDO)
                        .dataDespacho(new Date())
                        .comCliente(cliente)
                        .comVolume(volume)
                        .complemento(complemento)
                        .build();
		return Arrays.asList(obj);
    }
}
