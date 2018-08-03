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
import br.com.viavarejo.application.service.TransportadorService;
import br.com.viavarejo.domain.model.mongodb.transportador.Transportador;
import br.com.viavarejo.domain.model.mongodb.transportador.TransportadorPK;
import br.com.viavarejo.web.BaseEndpoint;
import br.com.viavarejo.web.dto.ResponseDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@SuppressWarnings("rawtypes")
@Api(tags = {"Transportador Endpoint"})
@RestController
@RequestMapping("/transportadoras")
public class TransportadorEndpoint extends BaseEndpoint<Transportador> {

    @Autowired
    private TransportadorService service;

    @ApiOperation(produces = "application/json", consumes = "application/json", value = "Inclusão de Transportadoras",
                    notes = "Inclusão de Transportadoras")
    @LogExecution
    @PostMapping
    public ResponseEntity<ResponseDto> criar(@ApiParam(value = "Coleção de transportadoras.",
                    required = true) @RequestBody final List<Transportador> lista) {

        this.validarBean(lista);
        return this.criarResponse(this.service.criar(lista));
    }

    @ApiOperation(produces = "application/json", consumes = "application/json", value = "Inclusão de Transportadora",
                    notes = "Inclusão de Transportadora")
    @LogExecution
    @PostMapping(path = "/transportadora")
    public ResponseEntity<ResponseDto> criarTransportador(@ApiParam(value = "Transportadora.",
                    required = true) @RequestBody final Transportador transportador) {

        this.validarBean(transportador);
        return this.criarResponse(this.service.criarTransportador(transportador));
    }

    @ApiOperation(produces = "application/json", consumes = "application/json", value = "Alteração de Transportadoras",
                    notes = "Alteração de Transportadoras")
    @LogExecution
    @PutMapping
    public ResponseEntity<ResponseDto> atualizar(@ApiParam(value = "Coleção de transportadoras.",
                    required = true) @RequestBody final List<Transportador> lista) {

        this.validarBean(lista);
        return this.sucessoResponse(this.service.atualizar(lista));
    }

    @ApiOperation(produces = "application/json", consumes = "application/json", value = "Alteração de Transportadora",
                    notes = "Alteração de Transportadora")
    @LogExecution
    @PutMapping(path = "/transportadora")
    public ResponseEntity<ResponseDto> atualizarTransportador(@ApiParam(value = "Transportadora.",
                    required = true) @RequestBody final Transportador transportador) {

        this.validarBean(transportador);
        return this.sucessoResponse(this.service.atualizarTransportador(transportador));
    }

    @ApiOperation(produces = "application/json", value = "Listagem de Transportadoras", notes = "Listagem de Transportadoras")
    @LogExecution
    @GetMapping("/listar")
    public ResponseEntity<ResponseDto> listar() {

        return this.recuperarResponse(this.service.listar());
    }

    @ApiOperation(produces = "application/json", value = "Recupera uma Transportadora",
                    notes = "Recupera uma Transportadora através do código empresa e código fornecedor")
    @LogExecution
    @GetMapping("/{codigoEmpresa}/{codigo}")
    public ResponseEntity<ResponseDto> recuperar(@ApiParam(required = true,
                    value = "Código empresa") @PathVariable("codigoEmpresa") final Integer codigoEmpresa, @ApiParam(required = true,
                                    value = "Código fornecedor") @PathVariable("codigo") final Integer codigo) {

        return this.recuperarResponse(this.service.recuperar(new TransportadorPK(codigoEmpresa, codigo)));
    }

    @ApiOperation(produces = "application/json", value = "Exclusão de Transportadoras", notes = "Exclui todas transportadoras cadastradas.")
    @LogExecution
    @DeleteMapping
    public ResponseEntity<ResponseDto> deletar() {

        this.service.deletar();
        return this.sucessoResponse();
    }

    @ApiOperation(produces = "application/json", value = "Exclui uma Transportadora",
                    notes = "Exclui a Transportadora através do códgio empresa e código fornecedor")
    @LogExecution
    @DeleteMapping("/{codigoEmpresa}/{codigo}")
    public ResponseEntity<ResponseDto> deletar(@ApiParam(required = true,
                    value = "Código empresa") @PathVariable("codigoEmpresa") final Integer codigoEmpresa, @ApiParam(required = true,
                                    value = "Código fornecedor") @PathVariable("codigo") final Integer codigo) {

        this.service.deletar(new TransportadorPK(codigoEmpresa, codigo));
        return this.sucessoResponse();
    }

}
