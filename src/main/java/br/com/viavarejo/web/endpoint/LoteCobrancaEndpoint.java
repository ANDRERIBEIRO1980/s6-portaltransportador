package br.com.viavarejo.web.endpoint;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

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
import br.com.viavarejo.application.service.LoteCobrancaService;
import br.com.viavarejo.domain.model.mongodb.cobranca.LoteCobranca;
import br.com.viavarejo.web.BaseEndpoint;
import br.com.viavarejo.web.dto.ResponseDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@SuppressWarnings("rawtypes")
@Api(tags = {"Lote de cobrança API Endpoint"})
@RestController
@RequestMapping("/lote_cobrancas")
public class LoteCobrancaEndpoint extends BaseEndpoint<LoteCobranca> {

    @Autowired
    private LoteCobrancaService service;

    @ApiOperation(produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE, value = "Inclusão do lote de cobrança",
                    notes = "Inclusão do lote de cobrança")
    @LogExecution
    @PostMapping
    public ResponseEntity<ResponseDto> criar(@ApiParam(value = "Lote de cobrança.",
                    required = true) @RequestBody final LoteCobranca loteCobranca) {

        return this.criarResponse(this.service.criar(loteCobranca));
    }

    @ApiOperation(produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE,
                    value = "Recupera o lote de cobrança pelo número da fatura / boleto",
                    notes = "Recupera o lote de cobrança")
    @LogExecution
    @GetMapping("/numero_fatura/{numero_fatura}")
    public ResponseEntity<ResponseDto> recuperar(@ApiParam(required = true,
                    value = "Número da fatura / boleto") @PathVariable("numero_fatura") final Integer numeroFatura) {
        return this.recuperarResponse(this.service.recuperarNumeroFatura(numeroFatura));
    }

    @ApiOperation(produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE,
                    value = "Recupera o lote de cobrança pelo ID Via Varejo",
                    notes = "Recupera o lote de cobrança")
    @LogExecution
    @GetMapping("/id_viavarejo/{id}")
    public ResponseEntity<ResponseDto> recuperarNumeroFatura(@ApiParam(required = true,
                    value = "ID Via Varejo") @PathVariable("id") final String id) {
        return this.recuperarResponse(this.service.recuperarId(id));
    }

    @ApiOperation(produces = "application/json", value = "Exclusão de Lotes de Cobrança antigos",
                    notes = "Exclusão de Lotes de Cobrança criados anteriormente à um período de meses")
    @LogExecution
    @DeleteMapping("/antigos/{meses}")
    public ResponseEntity<ResponseDto> deletarAntigosPeloMes(@ApiParam(required = true,
                    value = "Meses que serão subtraídos da data atual") @PathVariable("meses") final Integer meses) {

        this.service.deletarAntigos(meses);
        return this.sucessoResponse();
    }

    @ApiOperation(produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE,
                    value = "Recupera somente os lotes de cobrança que deverão ser sincronizados ao mainframe",
                    notes = "Recupera os lotes de cobrança que serão sincronizados ao mainframe")
    @LogExecution
    @GetMapping("/listar/sincronizacao_viavarejo")
    public ResponseEntity<ResponseDto> listarSincronizacaoMainframe() {
        return this.recuperarResponse(this.service.listarSincronizacaoMainframe());
    }

    @ApiOperation(produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE,
                    value = "Exclusão de lote de cobranças que estão com a flag(sincronizacaoMainframe) desligada(false)",
                    notes = "Exclui todos lotes de cobranças com a flag de sincronização com mainframe desligada.")
    @LogExecution
    @DeleteMapping
    public ResponseEntity<ResponseDto> deletarLotesFlagSincronizacaoDesligada() {

        this.service.removerLotesFlagSincronizacaoDesligada();
        return this.sucessoResponse();
    }

    @ApiOperation(produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE, value = "Alteração de Lote cobrança",
                    notes = "Alteração de Lote cobrança")
    @LogExecution
    @PutMapping
    public ResponseEntity<ResponseDto> atualizar(@ApiParam(value = "Lote de cobrança.",
                    required = true) @RequestBody final LoteCobranca loteCobranca) {

        this.validarBean(loteCobranca);
        return this.sucessoResponse(this.service.atualizar(loteCobranca));
    }
}
