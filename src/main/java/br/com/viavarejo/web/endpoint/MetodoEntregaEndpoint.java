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
import br.com.viavarejo.application.service.MetodoEntregaService;
import br.com.viavarejo.domain.model.mongodb.MetodoEntrega;
import br.com.viavarejo.web.BaseEndpoint;
import br.com.viavarejo.web.dto.ResponseDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@SuppressWarnings("rawtypes")
@Api(tags = {"Metodo de Entrega Endpoint"})
@RestController
@RequestMapping("/metodos_entrega")
public class MetodoEntregaEndpoint extends BaseEndpoint<MetodoEntrega> {

    @Autowired
    private MetodoEntregaService service;

    @ApiOperation(produces = "application/json", consumes = "application/json", value = "Inclusão de Métodos de Entrega")
    @LogExecution
    @PostMapping
    public ResponseEntity<ResponseDto> criar(@ApiParam(value = "Métodos de Entrega",
                    required = true) @RequestBody final List<MetodoEntrega> lista) {

        this.validarBean(lista);
        return this.criarResponse(this.service.criar(lista));
    }

    @ApiOperation(produces = "application/json", consumes = "application/json", value = "Inclusão de Metodo de Entrega")
    @LogExecution
    @PostMapping(path = "/metodo_entrega")
    public ResponseEntity<ResponseDto> criarMetodo(@ApiParam(value = "Metodo de Entrega",
                    required = true) @RequestBody final MetodoEntrega metodo) {

        this.validarBean(metodo);
        return this.criarResponse(this.service.criarMetodo(metodo));
    }

    @ApiOperation(produces = "application/json", consumes = "application/json", value = "Alteração de Métodos de Entrega")
    @LogExecution
    @PutMapping
    public ResponseEntity<ResponseDto> atualizar(@ApiParam(value = "Métodos de Entrega",
                    required = true) @RequestBody final List<MetodoEntrega> lista) {

        this.validarBean(lista);
        return this.sucessoResponse(this.service.atualizar(lista));
    }

    @ApiOperation(produces = "application/json", consumes = "application/json", value = "Alteração do Metodos de Entrega")
    @LogExecution
    @PutMapping(path = "/metodo_entrega")
    public ResponseEntity<ResponseDto> atualizarMetodo(@ApiParam(value = "Metodo de Entrega",
                    required = true) @RequestBody final MetodoEntrega metodo) {

        this.validarBean(metodo);
        return this.sucessoResponse(this.service.atualizarMetodo(metodo));
    }

    @ApiOperation(produces = "application/json", value = "Listagem de Métodos de Entrega")
    @LogExecution
    @GetMapping("/listar")
    public ResponseEntity<ResponseDto> listar() {

        return this.recuperarResponse(this.service.listar());
    }

    @ApiOperation(produces = "application/json", value = "Recuperar Método de Entrega através do seu identificador",
                    notes = "Recupera Método de Entrega através de seu código")
    @LogExecution
    @GetMapping("/{codigo}")
    public ResponseEntity<ResponseDto> recuperar(@ApiParam(required = true,
                    value = "Código Método de Entrega") @PathVariable("codigo") final Integer codigo) {

        return this.recuperarResponse(this.service.recuperar(codigo));
    }

    @ApiOperation(produces = "application/json", value = "Exclusão dos Métodos de Entrega",
                    notes = "Exclui todos Métodos de Entrega cadastrados.")
    @LogExecution
    @DeleteMapping
    public ResponseEntity<ResponseDto> deletar() {

        this.service.deletar();
        return this.sucessoResponse();
    }

    @ApiOperation(produces = "application/json", value = "Exclusão de um Método de Entrega através do seu identificador",
                    notes = "Exclui o Método de Entrega através de seu código")
    @LogExecution
    @DeleteMapping("/{codigo}")
    public ResponseEntity<ResponseDto> deletar(@ApiParam(required = true,
                    value = "Código Método de Entrega") @PathVariable("codigo") final Integer codigo) {

        this.service.deletar(codigo);
        return this.sucessoResponse();
    }

}
