package br.com.viavarejo;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import br.com.viavarejo.integration.web.FilialEndpointTest;
import br.com.viavarejo.integration.web.LogAplicacaoEndpointTest;
import br.com.viavarejo.integration.web.LoteCobrancaEndpointTest;
import br.com.viavarejo.integration.web.OcorrenciaPedidoEndpointTest;
import br.com.viavarejo.integration.web.PedidoEndpointTest;
import br.com.viavarejo.integration.web.PedidoKitColetaEndpointTest;
import br.com.viavarejo.integration.web.RessarcimentoEndpointTest;
import br.com.viavarejo.integration.web.SincronizacaoEndpointTest;
import br.com.viavarejo.integration.web.TransportadorEndpointTest;
import br.com.viavarejo.unit.application.service.FilialServiceTest;
import br.com.viavarejo.unit.application.service.LogAplicacaoServiceTest;
import br.com.viavarejo.unit.application.service.LoteCobrancaServiceTest;
import br.com.viavarejo.unit.application.service.OcorrenciaPedidoServiceTest;
import br.com.viavarejo.unit.application.service.PedidoKitColetaServiceTest;
import br.com.viavarejo.unit.application.service.PedidoServiceTest;
import br.com.viavarejo.unit.application.service.RessarcimentoServiceTest;
import br.com.viavarejo.unit.application.service.SincronizacaoServiceTest;
import br.com.viavarejo.unit.application.service.TransportadorServiceTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({

                     FilialEndpointTest.class, PedidoEndpointTest.class, PedidoKitColetaEndpointTest.class, RessarcimentoEndpointTest.class,
                     SincronizacaoEndpointTest.class, TransportadorEndpointTest.class, OcorrenciaPedidoEndpointTest.class,
                     LoteCobrancaEndpointTest.class, LogAplicacaoEndpointTest.class,

                     FilialServiceTest.class, PedidoServiceTest.class, PedidoKitColetaServiceTest.class, TransportadorServiceTest.class,
                     SincronizacaoServiceTest.class, RessarcimentoServiceTest.class, OcorrenciaPedidoServiceTest.class,
                     LoteCobrancaServiceTest.class, LogAplicacaoServiceTest.class

})
public class S6PortalTransportadorSuite {

}
