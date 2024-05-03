package com.fiap.techchallenge3.bdd.reserva;

import com.fiap.techchallenge3.domain.restaurante.TipoCozinhaEnum;
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

import static com.fiap.techchallenge3.infrastructure.reserva.controller.ReservaController.URL_RESERVA_POR_CNPJ;
import static com.fiap.techchallenge3.infrastructure.restaurante.controller.RestauranteController.URL_RESTAURANTE;
import static com.fiap.techchallenge3.utils.RestauranteUtils.*;
import static io.restassured.RestAssured.given;


public class ReservaSteps {

    private Response response;
    private String cnpj;
    private ReservaDTO request;

    @Dado("que existe restaurante cadastrado no sistema")
    public void existeRestauranteCadastradoNoSistema() {
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

    @E("informo os dados para realizar a reserva no restaurante")
    public void informoOsDadosParaRealizarAReservaNoRestaurante() {
        this.cnpj = "49251058000105";
        this.request = new ReservaDTO(
                LocalDate.now().plusDays(1),
                "18:15",
                2,
                "12345678901"
        );
    }

    @E("informo os dados para realizar a reserva de um restaurante que não esta cadastrado no sistema")
    public void informoOsDadosParaRealizarAReservaDeUmRestauranteQueNaoEstaCadastradoNoSistema() {
        this.cnpj = "49251058000333";
        this.request = new ReservaDTO(
                LocalDate.now().plusDays(1),
                "18:15",
                2,
                "12345678901"
        );
    }

    @E("informo os dados para realizar a reserva de um restaurante que esta fechado no horário informado")
    public void informoOsDadosParaRealizarAReservaDeUmRestauranteQueEstaFechadoNoHorarioInformado() {
        this.cnpj = "49251058000333";
        this.request = new ReservaDTO(
                LocalDate.now().plusDays(1),
                "23:30",
                2,
                "12345678901"
        );
    }

    @E("informo os dados para realizar a reserva de um restaurante que ainda não abriu no horário informado")
    public void informoOsDadosParaRealizarAReservaDeUmRestauranteQueAindaNaoAbriuNoHorarioInformado() {
        this.cnpj = "49251058000333";
        this.request = new ReservaDTO(
                LocalDate.now().plusDays(1),
                "17:15",
                2,
                "12345678901"
        );
    }

    @E("informo os dados para realizar a reserva de um restaurante que já esta lotado no horário informado")
    public void informoOsDadosParaRealizarAReservaDeUmRestauranteQueJaEstaLotadoNoHorarioInformado() {
        this.cnpj = "49251058000333";
        this.request = new ReservaDTO(
                LocalDate.now().plusDays(1),
                "19:15",
                50,
                "12345678901"
        );
    }

    @Quando("realizo a reserva")
    public void realizoAReserva() {
        RestAssured.baseURI = "http://localhost:8080";
        this.response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(this.request)
                .when()
                .post(URL_RESERVA_POR_CNPJ.replace("{cnpj}", this.cnpj));
    }

    @Entao("recebo uma resposta que a reserva será analisada")
    public void receboUmaRespostaQueAReservaSeraAnalisada() {
        this.response
                .prettyPeek()
                .then()
                .statusCode(HttpStatus.CREATED.value())
        ;
    }

    @Entao("recebo uma resposta que a reserva não foi realizada")
    public void receboUmaRespostaQueAReservaNaoFoiRealizada() {
        this.response
                .prettyPeek()
                .then()
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
        ;
    }

}
