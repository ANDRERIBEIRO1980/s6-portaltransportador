package br.com.viavarejo.unit.application.service;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import br.com.viavarejo.application.exception.NaoEncontradoException;
import br.com.viavarejo.application.exception.ValidacaoException;
import br.com.viavarejo.application.service.LoteCobrancaService;
import br.com.viavarejo.application.service.impl.LoteCobrancaServiceImpl;
import br.com.viavarejo.domain.model.builder.LoteCobrancaBuilder;
import br.com.viavarejo.domain.model.enumerator.StatusAprovacaoLoteCobrancaEnum;
import br.com.viavarejo.domain.model.mongodb.cobranca.LoteCobranca;
import br.com.viavarejo.domain.repository.LoteCobrancaRepository;
import br.com.viavarejo.utils.UnitTest;

public class LoteCobrancaServiceTest implements UnitTest {

    private static final Integer MESES_ATRAS = 1;
    private static final Integer NUMERO_FATURA = 123456789;
    private static final String ID_LOTE_COBRANCA = "5b2a6ef6927d5026bc2746da";

    @InjectMocks
    private final LoteCobrancaService service = new LoteCobrancaServiceImpl();

    @Mock
    private LoteCobrancaRepository repository;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    // CRIAR

    @Test
    public void criar_sucesso() {
        final LoteCobranca loteCobranca = this.getMockEntidade();
        when(this.repository.insert(any(LoteCobranca.class))).thenReturn(loteCobranca);

        final LoteCobranca loteRetorno = this.service.criar(loteCobranca);

        assertThat(loteRetorno, notNullValue());
        assertThat(loteRetorno.getDataCriacaoAPI(), notNullValue());
        verify(this.repository, times(1)).insert(any(LoteCobranca.class));
    }

    @Test(expected = ValidacaoException.class)
    public void criar_insucesso() {
        this.service.criar(null);
    }


    // RECUPERAR

    @Test
    public void recuperar_pelo_boleto_sucesso() {
        final LoteCobranca loteCobranca = this.getMockEntidade();

        when(this.repository.findByNumeroFatura(any())).thenReturn(loteCobranca);

        final LoteCobranca loteRetorno = this.service.recuperarNumeroFatura(NUMERO_FATURA);

        assertThat(loteRetorno, notNullValue());
        verify(this.repository, times(1)).findByNumeroFatura(any());
    }

    @Test(expected = NaoEncontradoException.class)
    public void recuperar_pelo_boleto_nao_existente() {

        when(this.repository.findByNumeroFatura(any())).thenReturn(null);
        this.service.recuperarNumeroFatura(NUMERO_FATURA);
    }

    @Test(expected = ValidacaoException.class)
    public void recuperar_pelo_boleto_insucesso() {
        this.service.recuperarNumeroFatura(null);
    }

    @Test
    public void recuperar_pelo_id_sucesso() {
        final Optional<LoteCobranca> loteCobranca = Optional.ofNullable(this.getMockEntidade());

        when(this.repository.findById(any(ObjectId.class))).thenReturn(loteCobranca);

        final LoteCobranca loteRetornoEsperado = this.service.recuperarId(ID_LOTE_COBRANCA);

        assertThat(loteRetornoEsperado, notNullValue());
        verify(this.repository, times(1)).findById(any(ObjectId.class));
    }

    @Test(expected = NaoEncontradoException.class)
    public void recuperar_pelo_id_nao_existente() {
        final Optional<LoteCobranca> loteCobranca = Optional.ofNullable(null);
        when(this.repository.findById(any(ObjectId.class))).thenReturn(loteCobranca);
        this.service.recuperarId(ID_LOTE_COBRANCA);
    }

    @Test(expected = ValidacaoException.class)
    public void recuperar_pelo_id_insucesso() {
        this.service.recuperarId(null);
    }

    // DELETAR

    @Test(expected = ValidacaoException.class)
    public void deletarAntigos_parametroNulo() {
        this.service.deletarAntigos(null);
    }

    @Test
    public void deletarAntigos_sucesso() {
        this.service.deletarAntigos(MESES_ATRAS);
        verify(this.repository, times(1)).deleteByObjectIdOld(any());
    }

    private LoteCobranca getMockEntidade() {
        return LoteCobrancaBuilder.init()
                        .formaPagamento("Boleto")
                        .numeroFatura(NUMERO_FATURA)
                        .valorDebito(new BigDecimal("50.00"))
                        .codigoContasReceber(5000)
                        .codigoContasPagar(1000)
                        .statusAprovacao(StatusAprovacaoLoteCobrancaEnum.VAZIO)
                        .observacao("Não possui")
                        .build();
    }
}
