package com.fiap.techchallenge3.unitario.avaliacao;

import com.fiap.techchallenge3.infrastructure.avaliacao.controller.AvaliacaoController;
import com.fiap.techchallenge3.infrastructure.avaliacao.controller.dto.AvaliacaoDTO;
import com.fiap.techchallenge3.useCase.avaliacao.impl.AvaliacaoUseCaseImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;

import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

public class AvaliacaoControllerTest {

    @Test
    public void avaliacao_deveRetornar201_salvaNaBaseDeDados() {
        // preparação
        var avaliacaoUseCaseImpl = Mockito.mock(AvaliacaoUseCaseImpl.class);

        var avaliacaoController = new AvaliacaoController(avaliacaoUseCaseImpl);

        // execução
        var restaurante = avaliacaoController.avalia(
                "12345678901234",
                "11122233344",
                new AvaliacaoDTO(
                        "Restaurante excelente",
                        10
                )
        );

        // avaliação
        Assertions.assertEquals(HttpStatus.CREATED, restaurante.getStatusCode());
    }

    @Test
    public void avaliacao_deveRetornar500_restauranteNaoEncontrado_naoSalvaNaBaseDeDados() {
        // preparação
        var avaliacaoUseCaseImpl = Mockito.mock(AvaliacaoUseCaseImpl.class);
        Mockito.doThrow(
                        new RuntimeException("Restaurante não encontrado!")
                )
                .when(avaliacaoUseCaseImpl)
                .avalia(anyString(), any(), any());

        var avaliacaoController = new AvaliacaoController(avaliacaoUseCaseImpl);

        // execução e avaliação
        var excecao = Assertions.assertThrows(RuntimeException.class, () -> {
            avaliacaoController.avalia(
                    "12345678901234",
                    "11122233344",
                    new AvaliacaoDTO(
                            "Restaurante excelente",
                            10
                    )
            );
        });
    }

    @ParameterizedTest
    @MethodSource("requestValidandoCampos")
    public void avaliacao_camposInvalidos_naoRealizaAvaliacao(String cnpjRestaurante,
                                                              String cpfCnpjCliente,
                                                              String comentario,
                                                              Integer nota) {
        // preparação
        var avaliacaoUseCaseImpl = Mockito.mock(AvaliacaoUseCaseImpl.class);
        Mockito.doThrow(
                new IllegalArgumentException("Campos inválidos!")
        )
                .when(avaliacaoUseCaseImpl)
                .avalia(any(), any(), any());

        var avaliacaoController = new AvaliacaoController(avaliacaoUseCaseImpl);

        // execução e avaliação
        var excecao = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            avaliacaoController.avalia(
                    cnpjRestaurante,
                    cpfCnpjCliente,
                    new AvaliacaoDTO(
                            comentario,
                            nota
                    )
            );
        });
    }

    private static Stream<Arguments> requestValidandoCampos() {
        return Stream.of(
                Arguments.of(null, "11122233344", "Restaurante top", 10),
                Arguments.of("", "11122233344", "Restaurante top", 10),
                Arguments.of("04623021000114", null, "Restaurante top", 10),
                Arguments.of("04623021000114", "", "Restaurante top", 10),
                Arguments.of("04623021000114", "11122233344", null, 10),
                Arguments.of("04623021000114", "11122233344", "", 10),
                Arguments.of("04623021000114", "11122233344", "Restaurante top", null),
                Arguments.of(null, null, null,null),
                Arguments.of("", "", "", null),

                Arguments.of("aa", "11122233344", "Restaurante top", 10),
                Arguments.of("04623021000114", "aa", "Restaurante top", 10),
                Arguments.of("04623021000114", "11122233344", "aa", 10),
                Arguments.of("04623021000114", "11122233344", "aaaaaaaaaabbbbbbbbbbaaaaaaaaaabbbbbbbbbbaaaaaaaaaabbbbbbbbbbaaaaaaaaaabbbbbbbbbbaaaaaaaaaabbbbbbbbbbcc", 10),
                Arguments.of("04623021000114", "11122233344", "Restaurante top", 0),
                Arguments.of("04623021000114", "11122233344", "Restaurante top", 11)
        );
    }

}
