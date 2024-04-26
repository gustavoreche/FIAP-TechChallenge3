package com.fiap.techchallenge3.bdd.localizacao;

import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Entao;
import io.cucumber.java.pt.Quando;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static com.fiap.techchallenge3.infrastructure.localizacao.controller.LocalizacaoController.URL_LOCALIZACAO_POR_CEP;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;


public class LocalizacaoSteps {

    private Response response;
    private String cep;

    @Dado("que tenho um CEP válido")
    public void tenhoUmCepValido() {
        this.cep = "14025-710";
    }

    @Dado("que tenho um CEP inválido")
    public void tenhoUmCepInvalido() {
        this.cep = "14000-000";
    }

    @Dado("que informo um CEP inválido {string}")
    public void informoUmCepInvalido(String cep) {
        this.cep = cep;
    }

    @Quando("busco por este CEP")
    public void buscoPorEsteCep() {
        RestAssured.baseURI = "http://localhost:8080";

        this.response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get(URL_LOCALIZACAO_POR_CEP.replace("{cep}", this.cep));
    }

    @Entao("recebo uma resposta com a localização correspondente")
    public void receboUmaRespostaComALocalizacaoCorrespondente() {
        this.response
                .prettyPeek()
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("logradouro", equalTo("Avenida Caramuru"))
                .body("cep", equalTo(this.cep))
                .body("bairro", equalTo("Alto da Boa Vista"))
                .body("cidade", equalTo("Ribeirão Preto"))
                .body("estado", equalTo("SP"))
        ;
    }

    @Entao("recebo uma resposta que o endereço não existe")
    public void receboUmaRespostaQueOEnderecoNaoExiste() {
        this.response
                .prettyPeek()
                .then()
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
        ;
    }

    @Entao("recebo uma resposta que o CEP informado é inválido")
    public void receboUmaRespostaQueOCepInformadoEhInvalido() {
        this.response
                .prettyPeek()
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
        ;
    }

}
