package com.fiap.techchallenge3.bdd.reserva;

import com.fiap.techchallenge3.domain.reserva.StatusReservaEnum;
import com.fiap.techchallenge3.domain.restaurante.model.TipoCozinhaEnum;
import com.fiap.techchallenge3.infrastructure.reserva.controller.dto.ReservaDTO;
import com.fiap.techchallenge3.infrastructure.restaurante.controller.dto.CriaRestauranteDTO;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.E;
import io.cucumber.java.pt.Entao;
import io.cucumber.java.pt.Quando;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.time.LocalDate;

import static com.fiap.techchallenge3.infrastructure.reserva.controller.ReservaController.URL_ATUALIZA_RESERVA;
import static com.fiap.techchallenge3.infrastructure.reserva.controller.ReservaController.URL_RESERVA_POR_CNPJ;
import static com.fiap.techchallenge3.infrastructure.restaurante.controller.RestauranteController.URL_RESTAURANTE;
import static com.fiap.techchallenge3.utils.RestauranteUtils.horarioFuncionamentoDefault;
import static com.fiap.techchallenge3.utils.RestauranteUtils.localizacaoDefault;
import static io.restassured.RestAssured.given;


public class AtualizaReservaSteps {

    private Response response;
    private String idDaReserva;

    @Dado("que existe um restaurante cadastrado no sistema")
    public void existeUmRestauranteCadastradoNoSistema() {
        var request = new CriaRestauranteDTO(
                "49.251.058/0001-05",
                "Restaurante Cucumber",
                localizacaoDefault(),
                TipoCozinhaEnum.COMIDA_ARABE,
                horarioFuncionamentoDefault(),
                50
        );

        RestAssured.baseURI = "http://localhost:8080";
        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when()
                .post(URL_RESTAURANTE)
                .prettyPeek()
                .then()
                .statusCode(HttpStatus.CREATED.value());
    }

    @E("um cliente efetuou uma reserva nesse restaurante")
    public void umClienteEfetuouUmaReservaNesseRestaurante() {
        var request = new ReservaDTO(
                LocalDate.now(),
                "18:15",
                2,
                "12345678901"
        );
        RestAssured.baseURI = "http://localhost:8080";
        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when()
                .post(URL_RESERVA_POR_CNPJ.replace("{cnpj}", "49251058000105"))
                .prettyPeek()
                .then()
                .statusCode(HttpStatus.CREATED.value());
    }

    @E("desejo efetivar a reserva do cliente")
    public void desejoEfetivarAReservaDoCliente() {
        this.idDaReserva = "1";
    }

    @E("desejo efetivar uma reserva que não existe")
    public void desejoEfetivarUmaReservaQueNaoExiste() {
        this.idDaReserva = "1000000000000";
    }

    @Quando("realizo a atualização da reserva")
    public void realizoAAtualizacaoDaReserva() {
        RestAssured.baseURI = "http://localhost:8080";
        this.response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .queryParam("status", StatusReservaEnum.RESERVADO)
                .when()
                .put(URL_ATUALIZA_RESERVA.replace("{idDaReserva}", this.idDaReserva));
    }

    @Entao("recebo uma resposta que a reserva foi atualizada com sucesso")
    public void receboUmaRespostaQueAReservaFoiAtualizadaComSucesso() {
        this.response
                .prettyPeek()
                .then()
                .statusCode(HttpStatus.OK.value())
        ;
    }

    @Entao("recebo uma resposta que a reserva não foi atualizada com sucesso")
    public void receboUmaRespostaQueAReservaNaoFoiAtualizadaComSucesso() {
        this.response
                .prettyPeek()
                .then()
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
        ;
    }

}
