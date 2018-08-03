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
import br.com.viavarejo.application.service.RessarcimentoService;
import br.com.viavarejo.domain.model.mongodb.cobranca.Ressarcimento;
import br.com.viavarejo.web.BaseEndpoint;
import br.com.viavarejo.web.dto.ResponseDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@SuppressWarnings("rawtypes")
@Api(tags = {"Ressarcimento Endpoint"})
@RestController
@RequestMapping("/ressarcimentos")
public class RessarcimentoEndpoint extends BaseEndpoint<Ressarcimento> {

    @Autowired
    private RessarcimentoService service;

    @ApiOperation(produces = "application/json", consumes = "application/json", value = "Inclusão de Ressarcimentos")
    @LogExecution
    @PostMapping
    public ResponseEntity<ResponseDto> criar(@ApiParam(value = "Coleção de ressarcimentos.",
                    required = true) @RequestBody final List<Ressarcimento> lista) {

        this.validarBean(lista);
        return this.criarResponse(this.service.criar(lista));
    }

    @ApiOperation(produces = "application/json", consumes = "application/json", value = "Inclusão de Ressarcimento")
    @LogExecution
    @PostMapping(path = "/ressarcimento")
    public ResponseEntity<ResponseDto> criarRessarcimento(@ApiParam(value = "Ressarcimento.",
                    required = true) @RequestBody final Ressarcimento ressarcimento) {

        this.validarBean(ressarcimento);
        return this.criarResponse(this.service.criarRessarcimento(ressarcimento));
    }

    @ApiOperation(produces = "application/json", consumes = "application/json", value = "Alteração de Ressarcimento")
    @LogExecution
    @PutMapping
    public ResponseEntity<ResponseDto> atualizar(@ApiParam(value = "Coleção de ressarcimentos.",
                    required = true) @RequestBody final List<Ressarcimento> lista) {

        this.validarBean(lista);
        return this.sucessoResponse(this.service.atualizar(lista));
    }

    @ApiOperation(produces = "application/json", consumes = "application/json", value = "Alteração de Ressarcimento")
    @LogExecution
    @PutMapping(path = "/ressarcimento")
    public ResponseEntity<ResponseDto> atualizarRessarcimento(@ApiParam(value = "Ressarcimento.",
                    required = true) @RequestBody final Ressarcimento ressarcimento) {

        this.validarBean(ressarcimento);
        return this.sucessoResponse(this.service.atualizarRessarcimento(ressarcimento));
    }

    @ApiOperation(produces = "application/json", value = "Listagem de Ressarcimento")
    @LogExecution
    @GetMapping("/listar")
    public ResponseEntity<ResponseDto> listar() {

        return this.recuperarResponse(this.service.listar());
    }

    @ApiOperation(produces = "application/json", value = "Recuperar um Ressarcimento através do seu nome",
                    notes = "Recupera um Ressarcimento através do código empresa e código filial")
    @LogExecution
    @GetMapping("/{codigo}")
    public ResponseEntity<ResponseDto> recuperar(@ApiParam(required = true,
                    value = "Código para Ressarcimento") @PathVariable("codigo") final Long codigo) {

        return this.recuperarResponse(this.service.recuperar(codigo));
    }

    @ApiOperation(produces = "application/json", value = "Exclusão de um Ressarcimento",
                    notes = "Excluí todos os ressarcimentos cadastrados.")
    @LogExecution
    @DeleteMapping
    public ResponseEntity<ResponseDto> deletar() {

        this.service.deletar();
        return this.sucessoResponse();
    }

    @ApiOperation(produces = "application/json", value = "Exclusão de um Ressarcimento através do seu identificador",
                    notes = "Excluí o Ressarcimento através de seu código")
    @LogExecution
    @DeleteMapping("/{codigo}")
    public ResponseEntity<ResponseDto> deletar(@ApiParam(required = true,
                    value = "Código para Ressarcimento") @PathVariable("codigo") final Long codigo) {

        this.service.deletar(codigo);
        return this.sucessoResponse();
    }

    @ApiOperation(produces = "application/json", value = "Exclusão dos Ressarcimentos antigos",
                    notes = "Exclusão dos Ressarcimentos criados anteriormente à um período de meses")
    @LogExecution
    @DeleteMapping("/antigos/{meses}")
    public ResponseEntity<ResponseDto> deletarAntigosPeloMes(@ApiParam(required = true,
                    value = "Meses que serão subtraídos da data atual") @PathVariable("meses") final Integer meses) {

        this.service.deletarAntigos(meses);
        return this.sucessoResponse();
    }

}
