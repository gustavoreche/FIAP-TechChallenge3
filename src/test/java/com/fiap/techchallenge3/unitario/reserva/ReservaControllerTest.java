package com.fiap.techchallenge3.unitario.reserva;

import com.fiap.techchallenge3.infrastructure.reserva.controller.ReservaController;
import com.fiap.techchallenge3.infrastructure.reserva.controller.dto.ReservaDTO;
import com.fiap.techchallenge3.infrastructure.restaurante.controller.RestauranteController;
import com.fiap.techchallenge3.infrastructure.restaurante.controller.dto.CriaRestauranteDTO;
import com.fiap.techchallenge3.useCase.reserva.impl.ReservaUseCaseImpl;
import com.fiap.techchallenge3.useCase.restaurante.impl.RestauranteUseCaseImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

public class ReservaControllerTest {

    @Test
    public void reserva_deveRetornar201_salvaNaBaseDeDados() {
        // preparação
        var reservaUseCaseImpl = Mockito.mock(ReservaUseCaseImpl.class);

        var reservaController = new ReservaController(reservaUseCaseImpl);

        // execução
        var restaurante = reservaController.reserva(
                "12345678901234",
                new ReservaDTO(
                        LocalDate.of(2023, 12, 31),
                        "18:00",
                        10,
                        "11122233344"
                )
        );

        // avaliação
        Assertions.assertEquals(HttpStatus.CREATED, restaurante.getStatusCode());
    }

    @Test
    public void reserva_deveRetornar500_restauranteNaoEncontrado_naoSalvaNaBaseDeDados() {
        // preparação
        var reservaUseCaseImpl = Mockito.mock(ReservaUseCaseImpl.class);
        Mockito.doThrow(
                        new RuntimeException("Restaurante não encontrado")
                )
                .when(reservaUseCaseImpl)
                .reserva(anyString(), any(ReservaDTO.class));

        var reservaController = new ReservaController(reservaUseCaseImpl);

        // execução e avaliação
        var excecao = Assertions.assertThrows(RuntimeException.class, () -> {
            reservaController.reserva(
                    "12345678901234",
                    new ReservaDTO(
                            LocalDate.of(2023, 12, 31),
                            "18:00",
                            10,
                            "11122233344"
                    )
            );
        });
    }

    @Test
    public void reserva_deveRetornar500_restauranteFechadoNoDia_naoSalvaNaBaseDeDados() {
        // preparação
        var reservaUseCaseImpl = Mockito.mock(ReservaUseCaseImpl.class);
        Mockito.doThrow(
                        new RuntimeException("Restaurante não está aberto no período da reserva")
                )
                .when(reservaUseCaseImpl)
                .reserva(anyString(), any(ReservaDTO.class));

        var reservaController = new ReservaController(reservaUseCaseImpl);

        // execução e avaliação
        var excecao = Assertions.assertThrows(RuntimeException.class, () -> {
            reservaController.reserva(
                    "12345678901234",
                    new ReservaDTO(
                            LocalDate.of(2023, 12, 31),
                            "18:00",
                            10,
                            "11122233344"
                    )
            );
        });
    }

    @Test
    public void reserva_deveRetornar500_restauranteAtingiuCapacidadeMaxima_naoSalvaNaBaseDeDados() {
        // preparação
        var reservaUseCaseImpl = Mockito.mock(ReservaUseCaseImpl.class);
        Mockito.doThrow(
                        new RuntimeException("Restaurante não tem reservas disponíveis para o dia e horário solicitado")
                )
                .when(reservaUseCaseImpl)
                .reserva(anyString(), any(ReservaDTO.class));

        var reservaController = new ReservaController(reservaUseCaseImpl);

        // execução e avaliação
        var excecao = Assertions.assertThrows(RuntimeException.class, () -> {
            reservaController.reserva(
                    "12345678901234",
                    new ReservaDTO(
                            LocalDate.of(2023, 12, 31),
                            "18:00",
                            10,
                            "11122233344"
                    )
            );
        });
    }

    @ParameterizedTest
    @MethodSource("requestValidandoCampos")
    public void restaurante_camposInvalidos_naoRealizaReserva(String cnpj,
                                                              LocalDate dia,
                                                              String horarioDeChegada,
                                                              int quantidadeLugares,
                                                              String cpfCnpjCliente) {
        // preparação
        var reservaUseCaseImpl = Mockito.mock(ReservaUseCaseImpl.class);
        Mockito.doThrow(
                new IllegalArgumentException("Campos inválidos!")
        )
                .when(reservaUseCaseImpl)
                .reserva(any(), any(ReservaDTO.class));

        var reservaController = new ReservaController(reservaUseCaseImpl);

        // execução e avaliação
        var excecao = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            reservaController.reserva(
                    cnpj,
                    new ReservaDTO(
                            dia,
                            horarioDeChegada,
                            quantidadeLugares,
                            cpfCnpjCliente
                    )
            );
        });
    }

    private static Stream<Arguments> requestValidandoCampos() {
        return Stream.of(
                Arguments.of(null, LocalDate.now(), "11:15", 5, "11122233344"),
                Arguments.of("", LocalDate.now(), "11:15", 5, "11122233344"),
                Arguments.of("04623021000114", null, "11:15", 5, "11122233344"),
                Arguments.of("04623021000114", LocalDate.now(), null, 5, "11122233344"),
                Arguments.of("04623021000114", LocalDate.now(), "", 5, "11122233344"),
                Arguments.of("04623021000114", LocalDate.now(), "11:15", 5, null),
                Arguments.of("04623021000114", LocalDate.now(), "11:15", 5, ""),
                Arguments.of(null, null, null, 5, null, null),
                Arguments.of("", LocalDate.now(), "11:15", 5, "", ""),

                Arguments.of("aa", LocalDate.now(), "11:15", 5, "11122233344"),
                Arguments.of("04623021000114", LocalDate.now().minusDays(10), "11:15", 5, "11122233344"),
                Arguments.of("04623021000114", LocalDate.now(), "11", 5, "11122233344"),
                Arguments.of("04623021000114", LocalDate.now(), "11:15", 0, "11122233344"),
                Arguments.of("04623021000114", LocalDate.now(), "11:15", 51, "11122233344"),
                Arguments.of("04623021000114", LocalDate.now(), "11:15", 1, "aa")
        );
    }

}
