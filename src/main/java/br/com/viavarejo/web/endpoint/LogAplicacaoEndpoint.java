package br.com.viavarejo.web.endpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.viavarejo.annotation.LogExecution;
import br.com.viavarejo.application.service.LogAplicacaoService;
import br.com.viavarejo.domain.model.mongodb.LogAplicacao;
import br.com.viavarejo.web.BaseEndpoint;
import br.com.viavarejo.web.dto.ResponseDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@SuppressWarnings("rawtypes")
@Api(tags = {"Log Aplicacao Endpoint"})
@RestController
@RequestMapping("/log_aplicacao")
public class LogAplicacaoEndpoint extends BaseEndpoint<LogAplicacao> {

    @Autowired
    private LogAplicacaoService service;

    @ApiOperation(produces = "application/json", value = "Listagem Log MongoDB")
    @LogExecution
    @GetMapping("/listar")
    public ResponseEntity<ResponseDto> listar() {

        return this.recuperarResponse(this.service.listar());
    }

    @ApiOperation(produces = "application/json", value = "Recuperar Log MongoDB pela data")
    @LogExecution
    @GetMapping("/{nome_processo}")
    public ResponseEntity<ResponseDto> recuperar(@ApiParam(required = true,
                    value = "Nome do Processo") @PathVariable("nome_processo") final String nomeProcesso) {

        return this.recuperarResponse(this.service.recuperar(nomeProcesso));
    }

    @ApiOperation(produces = "application/json", value = "Exclusão Log MongoDB antigos")
    @LogExecution
    @DeleteMapping("/antigos/{meses}")
    public ResponseEntity<ResponseDto> deletarAntigosPeloMes(@ApiParam(required = true,
                    value = "Meses que serão subtraídos da data atual") @PathVariable("meses") final Integer meses) {

        this.service.deletarAntigos(meses);
        return this.sucessoResponse();
    }

}
