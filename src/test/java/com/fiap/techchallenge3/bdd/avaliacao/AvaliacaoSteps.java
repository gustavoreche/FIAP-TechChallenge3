package com.fiap.techchallenge3.bdd.avaliacao;

import com.fiap.techchallenge3.domain.reserva.StatusReservaEnum;
import com.fiap.techchallenge3.domain.restaurante.TipoCozinhaEnum;
import com.fiap.techchallenge3.infrastructure.avaliacao.controller.dto.AvaliacaoDTO;
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

import static com.fiap.techchallenge3.infrastructure.avaliacao.controller.AvaliacaoController.URL_AVALIACAO_POR_CNPJ;
import static com.fiap.techchallenge3.infrastructure.reserva.controller.ReservaController.URL_ATUALIZA_RESERVA;
import static com.fiap.techchallenge3.infrastructure.reserva.controller.ReservaController.URL_RESERVA_POR_CNPJ;
import static com.fiap.techchallenge3.infrastructure.restaurante.controller.RestauranteController.URL_RESTAURANTE;
import static com.fiap.techchallenge3.utils.RestauranteUtils.horarioFuncionamentoDefault;
import static com.fiap.techchallenge3.utils.RestauranteUtils.localizacaoDefault;
import static io.restassured.RestAssured.given;


public class AvaliacaoSteps {

    private Response response;
    private String cnpjRestaurante;
    private String cpfCnpjCliente;
    private AvaliacaoDTO request;

    @Dado("que um restaurante cadastrado no sistema")
    public void umRestauranteCadastradoNoSistema() {
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

    @E("desejo realizar a avaliação da visita a esse restaurante")
    public void desejoRealizarAAvaliacaoDaVisitaAEsseRestaurante() {
        this.cnpjRestaurante = "49251058000105";
        this.cpfCnpjCliente = "11122233344";
        this.request = new AvaliacaoDTO(
                "Restaurante excelente",
                10
        );
    }

    @E("desejo realizar a avaliação da visita de um restaurante não cadastrado no sistema")
    public void desejoRealizarAAvaliacaoDaVisitaDeUmRestauranteNaoCadastradoNoSistema() {
        this.cnpjRestaurante = "49251058000333";
        this.cpfCnpjCliente = "11122233344";
        this.request = new AvaliacaoDTO(
                "Restaurante excelente",
                10
        );
    }

    @Quando("realizo a avaliação do restaurante")
    public void realizoAAvaliacaoDoRestaurante() {
        RestAssured.baseURI = "http://localhost:8080";
        this.response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(this.request)
                .when()
                .post(URL_AVALIACAO_POR_CNPJ.replace("{cnpjRestaurante}", this.cnpjRestaurante).replace("{cpfCnpjCliente}", this.cpfCnpjCliente));
    }

    @Entao("recebo uma resposta que a avaliação foi realizada com sucesso")
    public void receboUmaRespostaQueAAvaliacaoFoiRealizadaComSucesso() {
        this.response
                .prettyPeek()
                .then()
                .statusCode(HttpStatus.CREATED.value())
        ;
    }

    @Entao("recebo uma resposta que a avaliação não foi realizada com sucesso")
    public void receboUmaRespostaQueAAvaliacaoNaoFoiRealizadaComSucesso() {
        this.response
                .prettyPeek()
                .then()
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
        ;
    }

}
