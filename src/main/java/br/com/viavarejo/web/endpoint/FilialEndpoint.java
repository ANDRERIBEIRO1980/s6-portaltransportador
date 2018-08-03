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
import br.com.viavarejo.application.service.FilialService;
import br.com.viavarejo.domain.model.mongodb.filial.Filial;
import br.com.viavarejo.domain.model.mongodb.filial.FilialPK;
import br.com.viavarejo.web.BaseEndpoint;
import br.com.viavarejo.web.dto.ResponseDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@SuppressWarnings("rawtypes")
@Api(tags = {"Filial Endpoint"})
@RestController
@RequestMapping("/filiais")
public class FilialEndpoint extends BaseEndpoint<Filial> {

    @Autowired
    private FilialService service;

    @ApiOperation(produces = "application/json", consumes = "application/json", value = "Inclusão de Filiais")
    @LogExecution
    @PostMapping
    public ResponseEntity<ResponseDto> criar(@ApiParam(value = "Coleção de filiais.",
                    required = true) @RequestBody final List<Filial> lista) {

        this.validarBean(lista);
        return this.criarResponse(this.service.criar(lista));
    }

    @ApiOperation(produces = "application/json", consumes = "application/json", value = "Inclusão de Filial")
    @LogExecution
    @PostMapping(path = "/filial")
    public ResponseEntity<ResponseDto> criarFilial(@ApiParam(value = "Filial.", required = true) @RequestBody final Filial filial) {

        this.validarBean(filial);
        return this.criarResponse(this.service.criarFilial(filial));
    }

    @ApiOperation(produces = "application/json", consumes = "application/json", value = "Alteração de Filiais")
    @LogExecution
    @PutMapping
    public ResponseEntity<ResponseDto> atualizar(@ApiParam(value = "Coleção de filiais.",
                    required = true) @RequestBody final List<Filial> lista) {

        this.validarBean(lista);
        return this.sucessoResponse(this.service.atualizar(lista));
    }

    @ApiOperation(produces = "application/json", consumes = "application/json", value = "Alteração de Filial")
    @LogExecution
    @PutMapping(path = "/filial")
    public ResponseEntity<ResponseDto> atualizarFilial(@ApiParam(value = "Filial.", required = true) @RequestBody final Filial filial) {

        this.validarBean(filial);
        return this.sucessoResponse(this.service.atualizarFilial(filial));
    }

    @ApiOperation(produces = "application/json", value = "Listagem de Filiais")
    @LogExecution
    @GetMapping("/listar")
    public ResponseEntity<ResponseDto> listar() {

        return this.recuperarResponse(this.service.listar());
    }

    @ApiOperation(produces = "application/json", value = "Recuperar uma Sincronização de Processo através do seu nome",
                    notes = "Recupera uma Filial através do código empresa e código filial")
    @LogExecution
    @GetMapping("/{empresa}/{codigo}")
    public ResponseEntity<ResponseDto> recuperar(@ApiParam(required = true,
                    value = "Código empresa") @PathVariable("empresa") final Integer empresa, @ApiParam(required = true,
                                    value = "Código filial") @PathVariable("codigo") final Integer codigo) {

        return this.recuperarResponse(this.service.recuperar(new FilialPK(empresa, codigo)));
    }

    @ApiOperation(produces = "application/json", value = "Exclusão de Filiais", notes = "Exclui todas filiais cadastradas.")
    @LogExecution
    @DeleteMapping
    public ResponseEntity<ResponseDto> deletar() {

        this.service.deletar();
        return this.sucessoResponse();
    }

    @ApiOperation(produces = "application/json", value = "Exclusão de uma Filial através do seu identificador",
                    notes = "Exclui a Filial através do código empresa e código filial")
    @LogExecution
    @DeleteMapping("/{empresa}/{codigo}")
    public ResponseEntity<ResponseDto> deletar(@ApiParam(required = true,
                    value = "Código empresa") @PathVariable("empresa") final Integer empresa, @ApiParam(required = true,
                                    value = "Código filial") @PathVariable("codigo") final Integer codigo) {

        this.service.deletar(new FilialPK(empresa, codigo));
        return this.sucessoResponse();
    }

}
