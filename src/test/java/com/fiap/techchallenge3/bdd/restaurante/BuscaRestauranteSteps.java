package com.fiap.techchallenge3.bdd.restaurante;

import com.fiap.techchallenge3.domain.restaurante.model.DiasEnum;
import com.fiap.techchallenge3.domain.restaurante.model.TipoCozinhaEnum;
import com.fiap.techchallenge3.infrastructure.restaurante.controller.dto.CriaRestauranteDTO;
import com.fiap.techchallenge3.infrastructure.restaurante.controller.dto.ExibeBuscaRestauranteDTO;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.E;
import io.cucumber.java.pt.Entao;
import io.cucumber.java.pt.Quando;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.Objects;

import static com.fiap.techchallenge3.infrastructure.restaurante.controller.RestauranteController.URL_RESTAURANTE;
import static com.fiap.techchallenge3.utils.RestauranteUtils.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;


public class BuscaRestauranteSteps {

    private Response response;
    private String nome;
    private String cep;
    private String bairro;
    private String cidade;
    private String estado;
    private String tipoCozinha;

    @Dado("que tenho restaurantes cadastrados no sistema")
    public void tenhoRestaurantesCadastradosNoSistema() {
        var request = new CriaRestauranteDTO(
                "49.251.058/0001-05",
                "Restaurante Cucumber",
                localizacaoDefault(),
                TipoCozinhaEnum.COMIDA_ARABE,
                horarioFuncionamentoDefault(),
                500
        );
        var request2 = new CriaRestauranteDTO(
                "49.251.058/0001-10",
                "Restaurante bla bla B",
                criaLocalizacao("avenida gustavo", 10, "14000-001", "bairro gustavo", "cidade gustavo", "RJ", "ap1122"),
                TipoCozinhaEnum.COMIDA_JAPONESA,
                horarioFuncionamentoDefault(),
                500
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
        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request2)
                .when()
                .post(URL_RESTAURANTE)
                .prettyPeek()
                .then()
                .statusCode(HttpStatus.CREATED.value());
    }

    @E("utilizo todos os filtros de busca")
    public void utilizoTodosOsFiltrosDeBusca() {
        this.nome = "Restaurante Cucumber";
        this.cep = "14000-000";
        this.bairro = "bairro teste";
        this.cidade = "cidade teste";
        this.estado = "SP";
        this.tipoCozinha = TipoCozinhaEnum.COMIDA_ARABE.name();
    }

    @E("filtro por nome")
    public void filtroPorNome() {
        this.nome = "Restaurante Cucumber";
    }

    @E("filtro por cep")
    public void filtroPorCep() {
        this.cep = "14000-000";
    }

    @E("filtro por bairro")
    public void filtroPorBairro() {
        this.bairro = "bairro teste";
    }

    @E("filtro por cidade")
    public void filtroPorCidade() {
        this.cidade = "cidade teste";
    }

    @E("filtro por estado")
    public void filtroPorEstado() {
        this.estado = "SP";
    }

    @E("filtro por tipo cozinha")
    public void filtroPorTipoCozinha() {
        this.tipoCozinha = TipoCozinhaEnum.COMIDA_ARABE.name();
    }

    @E("não utilizo filtros")
    public void naoUtilizoFiltros() {
        this.nome = null;
        this.cep = null;
        this.bairro = null;
        this.cidade = null;
        this.estado = null;
        this.tipoCozinha = null;
    }

    @E("os filtros não retornam resultados")
    public void osFiltroNaoRetornamResultados() {
        this.tipoCozinha = TipoCozinhaEnum.MASSAS.name();
    }

    @Quando("busco por um restaurante")
    public void buscoPorUmRestaurante() {
        var request = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE);
        if(Objects.nonNull(this.nome)) {
            request.queryParam("nome", this.nome);
        }
        if(Objects.nonNull(this.cep)) {
            request.queryParam("cep", this.cep);
        }
        if(Objects.nonNull(this.bairro)) {
            request.queryParam("bairro", this.bairro);
        }
        if(Objects.nonNull(this.cidade)) {
            request.queryParam("cidade", this.cidade);
        }
        if(Objects.nonNull(this.estado)) {
            request.queryParam("estado", this.estado);
        }
        if(Objects.nonNull(this.tipoCozinha)) {
            request.queryParam("tipoCozinha", this.tipoCozinha);
        }

        this.response = request
                .when()
                .get(URL_RESTAURANTE);
    }

    @Entao("recebo as informações do restaurante")
    public void receboAsInformacoesDoRestaurante() {
        this.response
                .prettyPeek()
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("[0].nome", equalTo("Restaurante Cucumber"))
                .body("[0].logradouro", equalTo("rua teste"))
                .body("[0].numero", equalTo(10))
                .body("[0].cep", equalTo("14000000"))
                .body("[0].bairro", equalTo("bairro teste"))
                .body("[0].cidade", equalTo("cidade teste"))
                .body("[0].estado", equalTo("SP"))
                .body("[0].complemento", equalTo("ap1122"))
                .body("[0].tipoCozinha", equalTo(TipoCozinhaEnum.COMIDA_ARABE.name()))
                .body("[0].diasFuncionamento", equalTo(List.of(DiasEnum.TODOS.name()).toString()))
                .body("[0].horarioFuncionamento", equalTo("18:00 ate 23:00"))
                .body("[0].capacidadeDePessoas", equalTo(500))
        ;
    }

    @Entao("recebo as informações de todos os restaurante")
    public void receboAsInformacoesDeTodosOsRestaurante() {
        List<ExibeBuscaRestauranteDTO> responses = this.response
                .prettyPeek()
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract().jsonPath().getList(".", ExibeBuscaRestauranteDTO.class);

        var restauranteResponse1 = responses.get(0).nome()
                .equalsIgnoreCase("Restaurante Cucumber") ? responses.get(0) : responses.get(1);
        var restauranteResponse2 = responses.get(1).nome()
                .equalsIgnoreCase("Restaurante bla bla B") ? responses.get(1) : responses.get(0);

        Assertions.assertEquals("Restaurante Cucumber", restauranteResponse1.nome());
        Assertions.assertEquals("rua teste", restauranteResponse1.logradouro());
        Assertions.assertEquals(10, restauranteResponse1.numero());
        Assertions.assertEquals("14000000", restauranteResponse1.cep());
        Assertions.assertEquals("bairro teste", restauranteResponse1.bairro());
        Assertions.assertEquals("cidade teste", restauranteResponse1.cidade());
        Assertions.assertEquals("SP", restauranteResponse1.estado());
        Assertions.assertEquals("ap1122", restauranteResponse1.complemento());
        Assertions.assertEquals(TipoCozinhaEnum.COMIDA_ARABE, restauranteResponse1.tipoCozinha());
        Assertions.assertEquals(List.of(DiasEnum.TODOS).toString(), restauranteResponse1.diasFuncionamento());
        Assertions.assertEquals("18:00 ate 23:00", restauranteResponse1.horarioFuncionamento());
        Assertions.assertEquals(500, restauranteResponse1.capacidadeDePessoas());

        Assertions.assertEquals("Restaurante bla bla B", restauranteResponse2.nome());
        Assertions.assertEquals("avenida gustavo", restauranteResponse2.logradouro());
        Assertions.assertEquals(10, restauranteResponse2.numero());
        Assertions.assertEquals("14000001", restauranteResponse2.cep());
        Assertions.assertEquals("bairro gustavo", restauranteResponse2.bairro());
        Assertions.assertEquals("cidade gustavo", restauranteResponse2.cidade());
        Assertions.assertEquals("RJ", restauranteResponse2.estado());
        Assertions.assertEquals("ap1122", restauranteResponse2.complemento());
        Assertions.assertEquals(TipoCozinhaEnum.COMIDA_JAPONESA, restauranteResponse2.tipoCozinha());
        Assertions.assertEquals(List.of(DiasEnum.TODOS).toString(), restauranteResponse2.diasFuncionamento());
        Assertions.assertEquals("18:00 ate 23:00", restauranteResponse2.horarioFuncionamento());
        Assertions.assertEquals(500, restauranteResponse2.capacidadeDePessoas());
    }

    @Entao("recebo as informações que não foi encontrado nenhum restaurante")
    public void receboAsInformacoesQueNaoFoiEncontradoNenhumRestaurante() {
        this.response
                .prettyPeek()
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

}
