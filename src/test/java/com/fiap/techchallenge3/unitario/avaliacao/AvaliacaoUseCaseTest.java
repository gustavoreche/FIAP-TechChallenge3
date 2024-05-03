package com.fiap.techchallenge3.unitario.avaliacao;

import com.fiap.techchallenge3.domain.restaurante.DiasEnum;
import com.fiap.techchallenge3.domain.restaurante.TipoCozinhaEnum;
import com.fiap.techchallenge3.infrastructure.avaliacao.controller.dto.AvaliacaoDTO;
import com.fiap.techchallenge3.infrastructure.avaliacao.model.AvaliacaoEntity;
import com.fiap.techchallenge3.infrastructure.avaliacao.repository.AvaliacaoRepository;
import com.fiap.techchallenge3.infrastructure.restaurante.model.RestauranteEntity;
import com.fiap.techchallenge3.infrastructure.restaurante.repository.RestauranteRepository;
import com.fiap.techchallenge3.useCase.avaliacao.impl.AvaliacaoUseCaseImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;

import java.util.Optional;
import java.util.stream.Stream;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class AvaliacaoUseCaseTest {

    @Test
    public void avaliacao_salvaNaBaseDeDados() {
        // preparação
        var restauranteRepository = Mockito.mock(RestauranteRepository.class);
        Mockito.when(restauranteRepository.findById(Mockito.any()))
                .thenReturn(
                        Optional.of(new RestauranteEntity(
                                "12345678901234",
                                "Restaurante A",
                                TipoCozinhaEnum.COMIDA_ARABE,
                                DiasEnum.TODOS.toString(),
                                "24horas",
                                100,
                                "14025010",
                                "Rua teste",
                                123,
                                "Jardim Sumare",
                                "Cidade A",
                                "SP",
                                "Complemento A"
                        ))
                );
        var avaliacaoRepository = Mockito.mock(AvaliacaoRepository.class);
        Mockito.when(avaliacaoRepository.save(Mockito.any()))
                .thenReturn(
                        new AvaliacaoEntity(
                                1L,
                                "12345678901234",
                                "11122233344",
                                "Restaurante excelente",
                                10
                        )
                );

        var avaliacaoUseCaseImpl = new AvaliacaoUseCaseImpl(restauranteRepository, avaliacaoRepository);

        // execução
        avaliacaoUseCaseImpl.avalia(
                "12345678901234",
                "11122233344",
                new AvaliacaoDTO(
                        "Restaurante excelente",
                        10
                )
        );

        // avaliação
        verify(avaliacaoRepository, times(1)).save(Mockito.any());
    }

    @Test
    public void avaliacao_restauranteNaoEncontrado_naoSalvaNaBaseDeDados() {
        // preparação
        var restauranteRepository = Mockito.mock(RestauranteRepository.class);
        Mockito.when(restauranteRepository.findById(Mockito.any()))
                .thenReturn(
                        Optional.empty()
                );
        var avaliacaoRepository = Mockito.mock(AvaliacaoRepository.class);

        var avaliacaoUseCaseImpl = new AvaliacaoUseCaseImpl(restauranteRepository, avaliacaoRepository);

        // execução e avaliação
        var excecao = Assertions.assertThrows(RuntimeException.class, () -> {
            avaliacaoUseCaseImpl.avalia(
                    "12345678901234",
                    "11122233344",
                    new AvaliacaoDTO(
                            "Restaurante excelente",
                            10
                    )
            );
        });
        verify(avaliacaoRepository, times(0)).save(Mockito.any());
        Assertions.assertEquals("Restaurante não encontrado!", excecao.getMessage());
    }

    @ParameterizedTest
    @MethodSource("requestValidandoCampos")
    public void restaurante_camposInvalidos_naoRealizaAvaliacao(String cnpjRestaurante,
                                                                String cpfCnpjCliente,
                                                                String comentario,
                                                                Integer nota) {
        // preparação
        var restauranteRepository = Mockito.mock(RestauranteRepository.class);
        var avaliacaoRepository = Mockito.mock(AvaliacaoRepository.class);

        var avaliacaoUseCaseImpl = new AvaliacaoUseCaseImpl(restauranteRepository, avaliacaoRepository);

        // execução e avaliação
        var excecao = Assertions.assertThrows(RuntimeException.class, () -> {
            avaliacaoUseCaseImpl.avalia(
                    cnpjRestaurante,
                    cpfCnpjCliente,
                    new AvaliacaoDTO(
                            comentario,
                            nota
                    )
            );
        });
        verify(avaliacaoRepository, times(0)).save(Mockito.any());
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
