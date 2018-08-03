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
import br.com.viavarejo.application.service.OcorrenciaPedidoService;
import br.com.viavarejo.domain.model.mongodb.ocorrencia.OcorrenciaPedido;
import br.com.viavarejo.web.BaseEndpoint;
import br.com.viavarejo.web.dto.ResponseDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@SuppressWarnings("rawtypes")
@Api(tags = { "Ocorrencia Pedido Endpoint" })
@RestController
@RequestMapping("/ocorrencias")
public class OcorrenciaPedidoEndpoint extends BaseEndpoint<OcorrenciaPedido> {

    @Autowired
    private OcorrenciaPedidoService service;

    @ApiOperation(produces = "application/json", consumes = "application/json", value = "Inclusão de Ocorrências dos Pedidos",
                    notes = "Inclusão de Ocorrências dos Pedidos")
    @LogExecution
    @PostMapping
    public ResponseEntity<ResponseDto> criar(@ApiParam(value = "Coleção de ocorrências dos pedidos.",
                    required = true) @RequestBody final List<OcorrenciaPedido> lista) {

        this.validarBean(lista);
        return this.criarResponse(this.service.criar(lista));
    }

    @ApiOperation(produces = "application/json", consumes = "application/json", value = "Inclusão de Ocorrência do Pedido",
                    notes = "Inclusão de Ocorrência do Pedido")
    @LogExecution
    @PostMapping(path = "/ocorrencia")
    public ResponseEntity<ResponseDto> criar(@ApiParam(value = "Ocorrência do Pedido.",
                    required = true) @RequestBody final OcorrenciaPedido ocorrencia) {

        this.validarBean(ocorrencia);
        return this.criarResponse(this.service.criarOcorrencia(ocorrencia));
    }

    @ApiOperation(produces = "application/json", consumes = "application/json", value = "Alteração de Ocorrências dos Pedidos",
                    notes = "Alteração de Ocorrências dos Pedidos")
    @LogExecution
    @PutMapping
    public ResponseEntity<ResponseDto> atualizar(@RequestBody final List<OcorrenciaPedido> lista) {

        this.validarBean(lista);
        return this.sucessoResponse(this.service.atualizar(lista));
    }

    @ApiOperation(produces = "application/json", consumes = "application/json", value = "Alteração de Ocorrência do Pedido",
                    notes = "Alteração de Ocorrência do Pedido")
    @LogExecution
    @PutMapping(path = "/ocorrencia")
    public ResponseEntity<ResponseDto> atualizar(@ApiParam(value = "Ocorrência do Pedido.",
                    required = true) @RequestBody final OcorrenciaPedido ocorrencia) {

        this.validarBean(ocorrencia);
        return this.sucessoResponse(this.service.atualizarOcorrencia(ocorrencia));
    }

    @ApiOperation(produces = "application/json", value = "Listagem das Ocorrências dos Pedidos",
                    notes = "Listagem das Ocorrências dos Pedidos")
    @LogExecution
    @GetMapping("/listar")
    public ResponseEntity<ResponseDto> listar() {
        return this.recuperarResponse(this.service.listar());
    }

    @ApiOperation(produces = "application/json", value = "Recuperar as Ocorrências dos Pedidos",
                    notes = "Recuperar as Ocorrências dos Pedidos")
    @LogExecution
    @GetMapping("/{codigoPedido}")
    public ResponseEntity<ResponseDto> recuperar(@ApiParam(required = true,
                    value = "Código do pedido") @PathVariable("codigoPedido") final String codigoPedido) {
        return this.recuperarResponse(this.service.recuperar(codigoPedido));
    }

    @ApiOperation(produces = "application/json", value = "Exclusão das Ocorrências dos Pedidos",
                    notes = "Exclusão de Ocorrências dos Pedidos.")
    @LogExecution
    @DeleteMapping
    public ResponseEntity<ResponseDto> deletar(@ApiParam(value = "Coleção de ocorrências dos pedidos.",
                    required = true) @RequestBody final List<OcorrenciaPedido> lista) {

        this.validarBean(lista);
        this.service.deletar(lista);
        return this.sucessoResponse();
    }

    @ApiOperation(produces = "application/json", value = "Exclusão das Ocorrências dos Pedidos antigas",
                    notes = "Exclusão das Ocorrências dos Pedidos criadas anteriormente à um período de meses")
    @LogExecution
    @DeleteMapping("/antigos/{meses}")
    public ResponseEntity<ResponseDto> deletarAntigosPeloMes(@ApiParam(required = true,
                    value = "Meses que serão subtraídos da data atual") @PathVariable("meses") final Integer meses) {

        this.service.deletarAntigos(meses);
        return this.sucessoResponse();
    }
}
