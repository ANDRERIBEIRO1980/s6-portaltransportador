package br.com.viavarejo.web.endpoint;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.viavarejo.annotation.LogExecution;
import br.com.viavarejo.application.service.PedidoService;
import br.com.viavarejo.domain.model.mongodb.pedido.Pedido;
import br.com.viavarejo.web.BaseEndpoint;
import br.com.viavarejo.web.dto.ResponseDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@SuppressWarnings("rawtypes")
@Api(tags = {"Pedido Endpoint"})
@RestController
@RequestMapping("/pedidos")
public class PedidoEndpoint extends BaseEndpoint<Pedido> {

    @Autowired
    private PedidoService service;

    @ApiOperation(produces = "application/json", consumes = "application/json", value = "Inclusão de uma coleção de Pedidos",
                    notes = "Inclui uma coleção de Pedidos")
    @LogExecution
    @PostMapping
    public ResponseEntity<ResponseDto> criar(@ApiParam(value = "Coleção de Pedidos.",
                    required = true) @RequestBody final List<Pedido> lista) {

        this.validarBean(lista);
        return this.criarResponse(this.service.criar(lista));
    }

    @ApiOperation(produces = "application/json", consumes = "application/json", value = "Inclusão um Pedido", notes = "Inclui um Pedido")
    @LogExecution
    @PostMapping(path = "/pedido")
    public ResponseEntity<ResponseDto> criarPedido(@ApiParam(value = "Pedido.", required = true) @RequestBody final Pedido pedido) {

        this.validarBean(pedido);
        return this.criarResponse(this.service.criarPedido(pedido));
    }

    @ApiOperation(produces = "application/json", consumes = "application/json", value = "Alteração de Pedidos",
                    notes = "Alteração de coleção de Pedidos")
    @LogExecution
    @PutMapping
    public ResponseEntity<ResponseDto> atualizar(@ApiParam(value = "Coleção de Pedidos.",
                    required = true) @RequestBody final List<Pedido> lista) {

        this.validarBean(lista);
        return this.sucessoResponse(this.service.atualizar(lista));
    }

    @ApiOperation(produces = "application/json", consumes = "application/json", value = "Alteração de um Pedido",
                    notes = "Alteração de um Pedido")
    @LogExecution
    @PutMapping(path = "/pedido")
    public ResponseEntity<ResponseDto> atualizarPedido(@ApiParam(value = "Pedido.", required = true) @RequestBody final Pedido pedido) {

        this.validarBean(pedido);
        return this.sucessoResponse(this.service.atualizarPedido(pedido));
    }

    @ApiOperation(produces = "application/json", value = "Listagem de Pedidos", notes = "Listagem de Pedidos")
    @LogExecution
    @GetMapping("/listar")
    public ResponseEntity<ResponseDto> listar() {

        return this.recuperarResponse(this.service.listar());
    }

    @ApiOperation(produces = "application/json", value = "Recuperar um Pedido através do seu código",
                    notes = "Recupera um Pedido através do seu código")
    @LogExecution
    @GetMapping("/{codigo}")
    public ResponseEntity<ResponseDto> recuperar(@ApiParam(required = true,
                    value = "Código do Pedido.") @PathVariable("codigo") final Long codigoPedido) {

        return this.recuperarResponse(this.service.recuperar(codigoPedido));
    }

    @ApiOperation(produces = "application/json", value = "Exclusão de todos Pedidos", notes = "Exclui todos Pedidos cadastrados.")
    @LogExecution
    @DeleteMapping
    public ResponseEntity<ResponseDto> deletar() {

        this.service.deletar();
        return this.sucessoResponse();
    }

    @ApiOperation(produces = "application/json", value = "Exclui um Pedido", notes = "Exclui o Pedido através do seu código")
    @LogExecution
    @DeleteMapping("/{codigo}")
    public ResponseEntity<ResponseDto> deletar(@ApiParam(required = true,
                    value = "Código do Pedido") @PathVariable("codigo") final Long codigoPedido) {

        this.service.deletar(codigoPedido);
        return this.sucessoResponse();
    }

    @ApiOperation(produces = "application/json", value = "Exclusão dos Pedidos antigos",
                    notes = "Exclusão dos Pedidos criados anteriormente à um período de meses")
    @LogExecution
    @DeleteMapping("/antigos/{meses}")
    public ResponseEntity<ResponseDto> deletarAntigosPeloMes(@ApiParam(required = true,
                    value = "Meses que serão subtraídos da data atual") @PathVariable("meses") final Integer meses) {

        this.service.deletarAntigos(meses);
        return this.sucessoResponse();
    }

}
