package com.fiap.techchallenge3.performance;

import com.fiap.techchallenge3.domain.reserva.StatusReservaEnum;
import io.gatling.javaapi.core.ActionBuilder;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import java.time.Duration;
import java.time.LocalDate;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class PerformanceTestSimulation extends Simulation {

    private final HttpProtocolBuilder httpProtocol = http
            .baseUrl("http://localhost:8080");

    ActionBuilder buscaLocalizacaoRequest = http("busca localizacao")
            .get("/localizacao/14025-710")
            .check(status().is(200));

    ActionBuilder cadastraRestauranteRequest = http("cadastra restaurante")
            .post("/restaurante")
            .header("Content-Type", "application/json")
            .body(StringBody("""
                              {
                                "cnpj": "49251058000101",
                                "nome": "Restaurante Teste",
                                "localizacao": {
                                  "logradouro": "Av. Teste",
                                  "numero": 1234,
                                  "cep": "14012-456",
                                  "bairro": "Bairro Teste",
                                  "cidade": "Cidade Teste",
                                  "estado": "SP",
                                  "complemento": "ap 01"
                                },
                                "tipoCozinha": "COMIDA_JAPONESA",
                                "horarioFuncionamento": {
                                  "diasAbertos": [
                                    "TODOS"
                                  ],
                                  "horarioFuncionamento": "18:00 ate 23:00"
                                },
                                "capacidadeDePessoas": 100
                              }
                    """))
            .check(status().is(201));

    ActionBuilder buscaRestauranteRequest = http("busca restaurante")
            .get("/restaurante")
            .header("Content-Type", "application/json")
            .check(status().is(200));

    LocalDate data = LocalDate.now();
    ActionBuilder realizaReservaRequest = http("realiza reserva no restaurante")
            .post("/reserva/49251058000101")
            .header("Content-Type", "application/json")
            .body(StringBody("""
                              {
                                "dia": \"""" + data + """
                                ",
                                "horarioDeChegada": "19:00",
                                "quantidadeLugares": 2,
                                "cpfCnpjCliente": "12345678901"
                              }
                    """))
            .check(status().is(201));

    ActionBuilder atualizaReservaRequest = http("atualiza reserva no restaurante")
            .put("/reserva/atualiza/1")
            .header("Content-Type", "application/json")
            .queryParam("status", StatusReservaEnum.RESERVADO)
            .check(status().is(200));

    ActionBuilder buscaReservaRequest = http("busca reserva no restaurante")
            .get("/reserva/49251058000101")
            .header("Content-Type", "application/json")
            .check(status().is(200));

    ScenarioBuilder cenarioBuscaLocalizacao = scenario("Buscar localizacao")
            .exec(buscaLocalizacaoRequest);

    ScenarioBuilder cenarioCadastraRestaurante = scenario("Cadastra restaurante")
            .exec(cadastraRestauranteRequest);

    ScenarioBuilder cenarioBuscaRestaurante = scenario("Busca restaurante")
            .exec(cadastraRestauranteRequest)
            .exec(buscaRestauranteRequest);

    ScenarioBuilder cenarioRealizaReserva = scenario("Realiza reserva no restaurante")
            .exec(cadastraRestauranteRequest)
            .exec(realizaReservaRequest);

    ScenarioBuilder cenarioAtualizaReserva = scenario("Atualiza reserva no restaurante")
            .exec(cadastraRestauranteRequest)
            .exec(realizaReservaRequest)
            .exec(atualizaReservaRequest);

    ScenarioBuilder cenarioBuscaReserva = scenario("Busca reserva no restaurante")
            .exec(cadastraRestauranteRequest)
            .exec(realizaReservaRequest)
            .exec(buscaReservaRequest);

    {
        setUp(
                //TODO: Comentado, pois este teste consulta a API pública VIA CEP
//                cenarioBuscaLocalizacao.injectOpen(
//                        rampUsersPerSec(1)
//                                .to(10)
//                                .during(Duration.ofSeconds(10)),
//                        constantUsersPerSec(10)
//                                .during(Duration.ofSeconds(20)),
//                        rampUsersPerSec(10)
//                                .to(1)
//                                .during(Duration.ofSeconds(10))),
                cenarioCadastraRestaurante.injectOpen(
                        rampUsersPerSec(1)
                                .to(10)
                                .during(Duration.ofSeconds(10)),
                        constantUsersPerSec(10)
                                .during(Duration.ofSeconds(20)),
                        rampUsersPerSec(10)
                                .to(1)
                                .during(Duration.ofSeconds(10))),
                cenarioBuscaRestaurante.injectOpen(
                        rampUsersPerSec(1)
                                .to(10)
                                .during(Duration.ofSeconds(10)),
                        constantUsersPerSec(10)
                                .during(Duration.ofSeconds(20)),
                        rampUsersPerSec(10)
                                .to(1)
                                .during(Duration.ofSeconds(10))),
                cenarioRealizaReserva.injectOpen(
                        rampUsersPerSec(1)
                                .to(10)
                                .during(Duration.ofSeconds(10)),
                        constantUsersPerSec(10)
                                .during(Duration.ofSeconds(20)),
                        rampUsersPerSec(10)
                                .to(1)
                                .during(Duration.ofSeconds(10))),
                cenarioAtualizaReserva.injectOpen(
                        rampUsersPerSec(1)
                                .to(10)
                                .during(Duration.ofSeconds(10)),
                        constantUsersPerSec(10)
                                .during(Duration.ofSeconds(20)),
                        rampUsersPerSec(10)
                                .to(1)
                                .during(Duration.ofSeconds(10))),
                cenarioBuscaReserva.injectOpen(
                        rampUsersPerSec(1)
                                .to(10)
                                .during(Duration.ofSeconds(10)),
                        constantUsersPerSec(10)
                                .during(Duration.ofSeconds(20)),
                        rampUsersPerSec(10)
                                .to(1)
                                .during(Duration.ofSeconds(10)))

        )
                .protocols(httpProtocol)
                .assertions(
                        global().responseTime().max().lt(600),
                        global().failedRequests().count().is(0L));
    }
}