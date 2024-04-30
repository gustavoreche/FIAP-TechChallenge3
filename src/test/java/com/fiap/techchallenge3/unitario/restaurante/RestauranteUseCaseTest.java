package com.fiap.techchallenge3.unitario.restaurante;

import com.fiap.techchallenge3.domain.restaurante.model.DiasEnum;
import com.fiap.techchallenge3.domain.restaurante.model.TipoCozinhaEnum;
import com.fiap.techchallenge3.infrastructure.restaurante.controller.dto.CriaRestauranteDTO;
import com.fiap.techchallenge3.infrastructure.restaurante.controller.dto.EnderecoCompletoDTO;
import com.fiap.techchallenge3.infrastructure.restaurante.controller.dto.HorarioDeFuncionamentoDTO;
import com.fiap.techchallenge3.infrastructure.restaurante.model.RestauranteEntity;
import com.fiap.techchallenge3.infrastructure.restaurante.repository.RestauranteRepository;
import com.fiap.techchallenge3.useCase.restaurante.impl.RestauranteUseCaseImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class RestauranteUseCaseTest {

    @Test
    public void restaurante_salvaNaBaseDeDados() {
        // preparação
        var restauranteRepository = Mockito.mock(RestauranteRepository.class);
        Mockito.when(restauranteRepository.save(Mockito.any()))
                .thenReturn(
                        new RestauranteEntity(
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
                        )
                );

        var restauranteUseCaseImpl = new RestauranteUseCaseImpl(restauranteRepository);

        // execução
        restauranteUseCaseImpl.cadastra(
                new CriaRestauranteDTO(
                        "49.251.058/0001-05",
                        "Restaurante Teste",
                        localizacaoDefault(),
                        TipoCozinhaEnum.COMIDA_ARABE,
                        horarioFuncionamentoDefault(),
                        500
                )
        );

        // avaliação
        verify(restauranteRepository, times(1)).save(Mockito.any());
    }

    @Test
    public void restaurante_naoSalvaNaBaseDeDados() {
        // preparação
        var restauranteRepository = Mockito.mock(RestauranteRepository.class);
        Mockito.when(restauranteRepository.save(Mockito.any()))
                .thenReturn(
                        new RestauranteEntity(
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
                        )
                );

        var restauranteUseCaseImpl = new RestauranteUseCaseImpl(restauranteRepository);

        // execução e avaliação
        var excecao = Assertions.assertThrows(RuntimeException.class, () -> {
            restauranteUseCaseImpl.cadastra(
                    new CriaRestauranteDTO(
                            "123",
                            "Restaurante Teste",
                            localizacaoDefault(),
                            TipoCozinhaEnum.COMIDA_ARABE,
                            horarioFuncionamentoDefault(),
                            500
                    )
            );
        });
        Assertions.assertEquals("CNPJ INVÁLIDO!", excecao.getMessage());
        verify(restauranteRepository, times(0)).save(Mockito.any());
    }

    private static EnderecoCompletoDTO localizacaoDefault() {
        return new EnderecoCompletoDTO(
                "rua teste",
                10,
                "14000-000",
                "bairro teste",
                "cidade teste",
                "SP",
                "ap1122"
        );
    }

    private static HorarioDeFuncionamentoDTO horarioFuncionamentoDefault() {
        return new HorarioDeFuncionamentoDTO(
                List.of(DiasEnum.TODOS),
                "18:00 ate 23:00"
        );
    }

}
