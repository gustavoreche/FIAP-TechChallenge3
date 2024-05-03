package com.fiap.techchallenge3.bdd.restaurante;

import com.fiap.techchallenge3.domain.restaurante.DiasEnum;
import com.fiap.techchallenge3.domain.restaurante.TipoCozinhaEnum;
import com.fiap.techchallenge3.infrastructure.restaurante.controller.dto.CriaRestauranteDTO;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Entao;
import io.cucumber.java.pt.Quando;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;

import static com.fiap.techchallenge3.infrastructure.restaurante.controller.RestauranteController.URL_RESTAURANTE;
import static com.fiap.techchallenge3.utils.RestauranteUtils.*;
import static io.restassured.RestAssured.given;


public class RestauranteSteps {

    private Response response;
    private CriaRestauranteDTO request;

    @Dado("que tenho dados validos de um restaurante")
    public void tenhoDadosValidosDeUmRestaurante() {
        this.request = new CriaRestauranteDTO(
                "49.251.058/0001-05",
                "Restaurante Teste",
                localizacaoDefault(),
                TipoCozinhaEnum.COMIDA_ARABE,
                horarioFuncionamentoDefault(),
                500
        );
    }

    @Dado("que tenho dados validos de um restaurante que funciona 24 horas")
    public void tenhoDadosValidosDeUmRestauranteQueFunciona24Horas() {
        this.request = new CriaRestauranteDTO(
                "49.251.058/0001-05",
                "Restaurante Teste",
                localizacaoDefault(),
                TipoCozinhaEnum.COMIDA_ARABE,
                criaHorarioFuncionamento(List.of(DiasEnum.SEGUNDA_FEIRA), "24horas"),
                500
        );
    }

    @Dado("que tenho dados validos de um restaurante que não tem complemento no endereço")
    public void tenhoDadosValidosDeUmRestauranteQueNaoTemComplementoNoEndereco() {
        this.request = new CriaRestauranteDTO(
                "49251058000105",
                "Restaurante Teste",
                criaLocalizacao("rua teste", 10, "14000-000", "bairro teste", "cidade teste", "SP", null),
                TipoCozinhaEnum.COMIDA_ARABE,
                horarioFuncionamentoDefault(),
                500
        );
    }

    @Dado("que tenho dados validos de um restaurante que funciona todos os dias")
    public void tenhoDadosValidosDeUmRestauranteQueFuncionaTodosOsDias() {
        this.request = new CriaRestauranteDTO(
                "49.251.058/0001-05",
                "Restaurante Teste",
                localizacaoDefault(),
                TipoCozinhaEnum.COMIDA_ARABE,
                criaHorarioFuncionamento(List.of(DiasEnum.SEGUNDA_FEIRA, DiasEnum.TODOS), "18:00 ate 23:00"),
                500
        );
    }

    @Quando("cadastro esse restaurante")
    public void cadastroEsseRestaurante() {
        RestAssured.baseURI = "http://localhost:8080";
        this.response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(this.request)
                .when()
                .post(URL_RESTAURANTE);
    }

    @Entao("recebo uma resposta que o restaurante foi cadastrado com sucesso")
    public void receboUmaRespostaQueORestauranteFoiCadastradoComSucesso() {
        this.response
                .prettyPeek()
                .then()
                .statusCode(HttpStatus.CREATED.value())
        ;
    }

}
