package com.fiap.techchallenge3.unitario.restaurante;

import com.fiap.techchallenge3.domain.localizacao.model.Cep;
import com.fiap.techchallenge3.domain.localizacao.model.Localizacao;
import com.fiap.techchallenge3.infrastructure.localizacao.controller.LocalizacaoController;
import com.fiap.techchallenge3.useCase.localizacao.LocalizacaoUseCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;

public class RestauranteControllerTest {

    @Test
    public void localizacao_existe() {
        // preparação
        var localizacaoUseCase = Mockito.mock(LocalizacaoUseCase.class);
        Mockito.when(localizacaoUseCase.buscaPorCep("12345-678"))
                .thenReturn(new Localizacao(
                                "Rua A",
                                new Cep("12345-678"),
                                "Bairro A",
                                "Cidade A",
                                "SP"
                        )
                );

        var localizacaoController = new LocalizacaoController(localizacaoUseCase);

        // execução
        var localizacao = localizacaoController.buscaPorCep("12345-678");

        // avaliação
        Assertions.assertEquals(HttpStatus.OK, localizacao.getStatusCode());
        Assertions.assertEquals("Rua A", localizacao.getBody().logradouro());
        Assertions.assertEquals("12345-678", localizacao.getBody().cep());
        Assertions.assertEquals("Bairro A", localizacao.getBody().bairro());
        Assertions.assertEquals("Cidade A", localizacao.getBody().cidade());
        Assertions.assertEquals("SP", localizacao.getBody().estado());
    }

    @Test
    public void localizacao_naoExiste() {
        // preparação
        var localizacaoUseCase = Mockito.mock(LocalizacaoUseCase.class);
        Mockito.when(localizacaoUseCase.buscaPorCep("12345-678"))
                .thenThrow(
                        new RuntimeException("ENDEREÇO NAO EXISTE!")
                );

        var localizacaoController = new LocalizacaoController(localizacaoUseCase);

        // execução e avaliação
        var excecao = Assertions.assertThrows(RuntimeException.class, () -> {
            localizacaoController.buscaPorCep("12345-678");
        });
        Assertions.assertEquals("ENDEREÇO NAO EXISTE!", excecao.getMessage());
    }

    @Test
    public void localizacao_apiRestIndisponivel() {
        // preparação
        var localizacaoUseCase = Mockito.mock(LocalizacaoUseCase.class);
        Mockito.when(localizacaoUseCase.buscaPorCep("12345-678"))
                .thenThrow(
                        new RuntimeException("API REST Indisponível")
                );

        var localizacaoController = new LocalizacaoController(localizacaoUseCase);

        // execução e avaliação
        var excecao = Assertions.assertThrows(RuntimeException.class, () -> {
            localizacaoController.buscaPorCep("12345-678");
        });
        Assertions.assertEquals("API REST Indisponível", excecao.getMessage());
    }

}
