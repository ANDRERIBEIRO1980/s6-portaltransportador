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
import br.com.viavarejo.application.service.PedidoKitColetaService;
import br.com.viavarejo.domain.model.mongodb.kitcoleta.PedidoKitColeta;
import br.com.viavarejo.web.BaseEndpoint;
import br.com.viavarejo.web.dto.ResponseDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@SuppressWarnings("rawtypes")
@Api(tags = {"KitColeta Endpoint"})
@RestController
@RequestMapping("/reversas")
public class PedidoKitColetaEndpoint extends BaseEndpoint<PedidoKitColeta> {

    @Autowired
    private PedidoKitColetaService service;

    @ApiOperation(produces = "application/json", consumes = "application/json", value = "Inclusão de um Pedido do KitColeta",
                    notes = "Inclui um Pedido do KitColeta")
    @LogExecution
    @PostMapping(path = "/reversa")
    public ResponseEntity<ResponseDto> criarReversa(@ApiParam(value = "Pedido do KitColeta.",
                    required = true) @RequestBody final PedidoKitColeta reversa) {

        this.validarBean(reversa);
        return this.criarResponse(this.service.criarReversa(reversa));
    }

    @ApiOperation(produces = "application/json", consumes = "application/json", value = "Alteração de Pedidos do KitColeta",
                    notes = "Alteração de coleção de Pedidos do KitColeta")
    @LogExecution
    @PutMapping
    public ResponseEntity<ResponseDto> atualizar(@ApiParam(value = "Coleção de Pedidos do KitColeta.",
                    required = true) @RequestBody final List<PedidoKitColeta> lista) {

        this.validarBean(lista);
        return this.sucessoResponse(this.service.atualizar(lista));
    }

    @ApiOperation(produces = "application/json", value = "Listagem de Pedidos do KitColeta", notes = "Listagem de Pedidos do KitColeta")
    @LogExecution
    @GetMapping("/listar")
    public ResponseEntity<ResponseDto> listar() {

        return this.recuperarResponse(this.service.listar());
    }

    @ApiOperation(produces = "application/json", value = "Recuperar um Pedido Kit Coleta através do seu código",
                    notes = "Recupera um Pedido Kit Coleta através do seu código")
    @LogExecution
    @GetMapping("/{codigo}")
    public ResponseEntity<ResponseDto> recuperar(@ApiParam(required = true,
                    value = "Código do Pedido Kit Coleta.") @PathVariable("codigo") final Long codigoKitColeta) {

        return this.recuperarResponse(this.service.recuperar(codigoKitColeta));
    }

    @ApiOperation(produces = "application/json", value = "Exclusão de todos Pedidos do KitColeta",
                    notes = "Exclui todos Pedidos do KitColeta cadastrados.")
    @LogExecution
    @DeleteMapping
    public ResponseEntity<ResponseDto> deletar() {

        this.service.deletar();
        return this.sucessoResponse();
    }

    @ApiOperation(produces = "application/json", value = "Exclui um Pedido Kit Coleta",
                    notes = "Exclui o Pedido Kit Coleta através do seu código")
    @LogExecution
    @DeleteMapping("/{codigo}")
    public ResponseEntity<ResponseDto> deletar(@ApiParam(required = true,
                    value = "Código do Pedido Kit Coleta") @PathVariable("codigo") final Long codigoKitColeta) {

        this.service.deletar(codigoKitColeta);
        return this.sucessoResponse();
    }

    @ApiOperation(produces = "application/json", value = "Exclusão dos Pedidos Kit Coleta antigos",
                    notes = "Exclusão dos Pedidos Kit Coleta criados anteriormente à um período de meses")
    @LogExecution
    @DeleteMapping("/antigos/{meses}")
    public ResponseEntity<ResponseDto> deletarAntigosPeloMes(@ApiParam(required = true,
                    value = "Meses que serão subtraídos da data atual") @PathVariable("meses") final Integer meses) {

        this.service.deletarAntigos(meses);
        return this.sucessoResponse();
    }

}
