package br.com.viavarejo.integration.web;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.viavarejo.Application;
import br.com.viavarejo.utils.IntegrationTest;
import io.restassured.RestAssured;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = { Application.class })
public abstract class EndPointConfigurationTest implements IntegrationTest {

    @Value("${local.server.port}")
    protected int porta;

    @Before
    public void setUp() {
        RestAssured.port = this.porta;
    }
}
