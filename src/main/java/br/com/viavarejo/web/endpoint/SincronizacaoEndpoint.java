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
import br.com.viavarejo.application.service.SincronizacaoService;
import br.com.viavarejo.domain.model.mongodb.sincronizacao.SincronizacaoProcesso;
import br.com.viavarejo.web.BaseEndpoint;
import br.com.viavarejo.web.dto.ResponseDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@SuppressWarnings("rawtypes")
@Api(tags = {"Sincronizacao Endpoint"})
@RestController
@RequestMapping("/sincronizacoes")
public class SincronizacaoEndpoint extends BaseEndpoint<SincronizacaoProcesso> {

    @Autowired
    private SincronizacaoService service;

    @ApiOperation(produces = "application/json", consumes = "application/json", value = "Inclusão de Sincronizações de Processos")
    @LogExecution
    @PostMapping
    public ResponseEntity<ResponseDto> criar(@RequestBody final List<SincronizacaoProcesso> lista) {

        return this.criarResponse(this.service.criar(lista));
    }

    @ApiOperation(produces = "application/json", consumes = "application/json", value = "Inclusão de Sincronização de Processo")
    @LogExecution
    @PostMapping(path = "/sincronizacao")
    public ResponseEntity<ResponseDto> criarSincronizacao(@RequestBody final SincronizacaoProcesso processo) {

        return this.criarResponse(this.service.criarSincronizacao(processo));
    }

    @ApiOperation(produces = "application/json", consumes = "application/json", value = "Alteração de Sincronizações de Processos")
    @LogExecution
    @PutMapping
    public ResponseEntity<ResponseDto> atualizar(@RequestBody final List<SincronizacaoProcesso> lista) {

        return this.sucessoResponse(this.service.atualizar(lista));
    }

    @ApiOperation(produces = "application/json", consumes = "application/json", value = "Alteração de Sincronização de Processo")
    @LogExecution
    @PutMapping(path = "/sincronizacao")
    public ResponseEntity<ResponseDto> atualizarSincronizacao(@RequestBody final SincronizacaoProcesso processo) {

        return this.sucessoResponse(this.service.atualizarSincronizacao(processo));
    }

    @ApiOperation(produces = "application/json", value = "Listagem de Sincronização de Processos")
    @LogExecution
    @GetMapping("/listar")
    public ResponseEntity<ResponseDto> listar() {

        return this.recuperarResponse(this.service.listar());
    }

    @ApiOperation(produces = "application/json", value = "Recuperar uma Sincronização de Processo através do seu nome")
    @LogExecution
    @GetMapping("/{processo}")
    public ResponseEntity<ResponseDto> recuperar(@PathVariable("processo") final String nomeProcesso) {

        return this.recuperarResponse(this.service.recuperar(nomeProcesso));
    }

    @ApiOperation(produces = "application/json", value = "Exclusão de Sincronizações de Processos")
    @LogExecution
    @DeleteMapping
    public ResponseEntity<ResponseDto> deletar() {

        this.service.deletar();
        return this.sucessoResponse();
    }

    @ApiOperation(produces = "application/json", value = "Exclusão de uma Sincronização de Processo através do seu nome")
    @LogExecution
    @DeleteMapping("/{processo}")
    public ResponseEntity<ResponseDto> deletar(@PathVariable("processo") final String nomeProcesso) {

        this.service.deletar(nomeProcesso);
        return this.sucessoResponse();
    }
}
