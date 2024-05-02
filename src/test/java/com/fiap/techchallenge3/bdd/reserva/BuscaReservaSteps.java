package com.fiap.techchallenge3.bdd.reserva;

import com.fiap.techchallenge3.domain.restaurante.model.TipoCozinhaEnum;
import com.fiap.techchallenge3.infrastructure.reserva.controller.dto.ReservaDTO;
import com.fiap.techchallenge3.infrastructure.restaurante.controller.dto.CriaRestauranteDTO;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.E;
import io.cucumber.java.pt.Entao;
import io.cucumber.java.pt.Quando;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.time.LocalDate;

import static com.fiap.techchallenge3.infrastructure.reserva.controller.ReservaController.URL_RESERVA_POR_CNPJ;
import static com.fiap.techchallenge3.infrastructure.restaurante.controller.RestauranteController.URL_RESTAURANTE;
import static com.fiap.techchallenge3.utils.RestauranteUtils.horarioFuncionamentoDefault;
import static com.fiap.techchallenge3.utils.RestauranteUtils.localizacaoDefault;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;


public class BuscaReservaSteps {

    private Response response;
    private String cnpj;

    @Dado("que existe determinado restaurante cadastrado no sistema")
    public void existeDeterminadoRestauranteCadastradoNoSistema() {
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

    @E("um cliente efetuou uma reserva nesse determinado restaurante")
    public void umClienteEfetuouUmaReservaNesseDeterminadoRestaurante() {
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

    @E("desejo buscar as reservas pendentes do dia")
    public void desejoBuscarAsReservasPendentesDoDia() {
        this.cnpj = "49251058000105";
    }

    @E("desejo buscar as reservas pendentes do dia informando um CNPJ inválido")
    public void desejoBuscarAsReservasPendentesDoDiaInformandoUmCnpjInvalido() {
        this.cnpj = "aa";
    }

    @E("desejo buscar as reservas pendentes do dia de outro restaurante")
    public void desejoBuscarAsReservasPendentesDoDiaDeOutroRestaurante() {
        this.cnpj = "49251058000777";
    }

    @Quando("realizo a busca das reservas pendentes do dia atual")
    public void realizoABuscaDasReservasPendentesDoDiaAtual() {
        RestAssured.baseURI = "http://localhost:8080";
        this.response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get(URL_RESERVA_POR_CNPJ.replace("{cnpj}", this.cnpj));
    }

    @Entao("recebo uma resposta das reservas pendentes do dia atual")
    public void receboUmaRespostaDasReservasPendentesDoDiaAtual() {
        this.response
                .prettyPeek()
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("[0].id", Matchers.notNullValue())
                .body("[0].quantidadeLugaresClienteDeseja", equalTo(2))
                .body("[0].horarioDaReservaRealizada", Matchers.notNullValue())
        ;
    }

    @Entao("recebo uma resposta que o CNPJ esta inválido")
    public void receboUmaRespostaQueOCnpjEstaInvalido() {
        this.response
                .prettyPeek()
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
        ;
    }

    @Entao("recebo uma resposta que não existe reserva no dia atual")
    public void receboUmaRespostaQueNaoExisteReservaNoDiaAtual() {
        this.response
                .prettyPeek()
                .then()
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
        ;
    }

}
