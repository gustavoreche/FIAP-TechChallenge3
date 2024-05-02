package com.fiap.techchallenge3.unitario.reserva;

import com.fiap.techchallenge3.domain.reserva.StatusReservaEnum;
import com.fiap.techchallenge3.domain.restaurante.model.DiasEnum;
import com.fiap.techchallenge3.domain.restaurante.model.TipoCozinhaEnum;
import com.fiap.techchallenge3.infrastructure.reserva.controller.dto.ReservaDTO;
import com.fiap.techchallenge3.infrastructure.reserva.model.ReservaEntity;
import com.fiap.techchallenge3.infrastructure.reserva.repository.ReservaRepository;
import com.fiap.techchallenge3.infrastructure.restaurante.controller.dto.CriaRestauranteDTO;
import com.fiap.techchallenge3.infrastructure.restaurante.model.RestauranteEntity;
import com.fiap.techchallenge3.infrastructure.restaurante.repository.RestauranteRepository;
import com.fiap.techchallenge3.useCase.reserva.impl.ReservaUseCaseImpl;
import com.fiap.techchallenge3.useCase.restaurante.impl.RestauranteUseCaseImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static com.fiap.techchallenge3.utils.RestauranteUtils.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class ReservaUseCaseTest {

    @Test
    public void reserva_salvaNaBaseDeDados() {
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
        var reservaRepository = Mockito.mock(ReservaRepository.class);
        Mockito.when(reservaRepository.save(Mockito.any()))
                .thenReturn(
                        new ReservaEntity(
                                1L,
                                "12345678901234",
                                "11122233344",
                                LocalDate.now(),
                                "18:00",
                                10,
                                null,
                                null
                        )
                );
        Mockito.when(reservaRepository.sumByDiaAndStatusReserva(Mockito.any(), Mockito.any()))
                .thenReturn(
                        5
                );

        var reservaUseCaseImpl = new ReservaUseCaseImpl(reservaRepository, restauranteRepository);

        // execução
        reservaUseCaseImpl.reserva(
                "12345678901234",
                new ReservaDTO(
                        LocalDate.now(),
                        "11:00",
                        2,
                        "11122233344"
                )
        );

        // avaliação
        verify(reservaRepository, times(1)).save(Mockito.any());
    }

    @Test
    public void reserva_restauranteNaoEncontrado_naoSalvaNaBaseDeDados() {
        // preparação
        var restauranteRepository = Mockito.mock(RestauranteRepository.class);
        Mockito.when(restauranteRepository.findById(Mockito.any()))
                .thenReturn(
                        Optional.empty()
                );
        var reservaRepository = Mockito.mock(ReservaRepository.class);

        var reservaUseCaseImpl = new ReservaUseCaseImpl(reservaRepository, restauranteRepository);

        // execução e avaliação
        var excecao = Assertions.assertThrows(RuntimeException.class, () -> {
            reservaUseCaseImpl.reserva(
                    "12345678901234",
                    new ReservaDTO(
                            LocalDate.now(),
                            "11:00",
                            2,
                            "11122233344"
                    )
            );
        });
        verify(reservaRepository, times(0)).save(Mockito.any());
        Assertions.assertEquals("Restaurante não encontrado", excecao.getMessage());
    }

    @Test
    public void reserva_restauranteFechadoNoDia_naoSalvaNaBaseDeDados() {
        // preparação
        var restauranteRepository = Mockito.mock(RestauranteRepository.class);
        Mockito.when(restauranteRepository.findById(Mockito.any()))
                .thenReturn(
                        Optional.of(new RestauranteEntity(
                                "12345678901234",
                                "Restaurante A",
                                TipoCozinhaEnum.COMIDA_ARABE,
                                List.of(DiasEnum.SEGUNDA_FEIRA, DiasEnum.QUARTA_FEIRA).toString(),
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
        var reservaRepository = Mockito.mock(ReservaRepository.class);

        var mockData = Mockito.mockStatic(LocalDate.class, Mockito.CALLS_REAL_METHODS);
        var dataMock = LocalDate.of(2024, 04, 30);
        mockData.when(LocalDate::now)
                .thenReturn(dataMock);

        var reservaUseCaseImpl = new ReservaUseCaseImpl(reservaRepository, restauranteRepository);

        // execução e avaliação
        var excecao = Assertions.assertThrows(RuntimeException.class, () -> {
            reservaUseCaseImpl.reserva(
                    "12345678901234",
                    new ReservaDTO(
                            LocalDate.now(),
                            "11:00",
                            2,
                            "11122233344"
                    )
            );
        });
        verify(reservaRepository, times(0)).save(Mockito.any());
        Assertions.assertEquals("Restaurante não está aberto no período da reserva", excecao.getMessage());

        mockData.close();
    }

    @Test
    public void reserva_restauranteFechadoForaDoHorario_naoSalvaNaBaseDeDados() {
        // preparação
        var restauranteRepository = Mockito.mock(RestauranteRepository.class);
        Mockito.when(restauranteRepository.findById(Mockito.any()))
                .thenReturn(
                        Optional.of(new RestauranteEntity(
                                "12345678901234",
                                "Restaurante A",
                                TipoCozinhaEnum.COMIDA_ARABE,
                                List.of(DiasEnum.SEGUNDA_FEIRA, DiasEnum.QUARTA_FEIRA).toString(),
                                "10:00 ate 12:00",
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
        var reservaRepository = Mockito.mock(ReservaRepository.class);

        var mockData = Mockito.mockStatic(LocalDate.class, Mockito.CALLS_REAL_METHODS);
        var dataMock = LocalDate.of(2024, 04, 29);
        mockData.when(LocalDate::now)
                .thenReturn(dataMock);

        var reservaUseCaseImpl = new ReservaUseCaseImpl(reservaRepository, restauranteRepository);

        // execução e avaliação
        var excecao = Assertions.assertThrows(RuntimeException.class, () -> {
            reservaUseCaseImpl.reserva(
                    "12345678901234",
                    new ReservaDTO(
                            LocalDate.now(),
                            "12:01",
                            2,
                            "11122233344"
                    )
            );
        });
        verify(reservaRepository, times(0)).save(Mockito.any());
        Assertions.assertEquals("Restaurante não está aberto no período da reserva", excecao.getMessage());

        mockData.close();
    }

    @Test
    public void reserva_restauranteAindaNaoAbriuHorario_naoSalvaNaBaseDeDados() {
        // preparação
        var restauranteRepository = Mockito.mock(RestauranteRepository.class);
        Mockito.when(restauranteRepository.findById(Mockito.any()))
                .thenReturn(
                        Optional.of(new RestauranteEntity(
                                "12345678901234",
                                "Restaurante A",
                                TipoCozinhaEnum.COMIDA_ARABE,
                                List.of(DiasEnum.SEGUNDA_FEIRA, DiasEnum.QUARTA_FEIRA).toString(),
                                "10:00 ate 12:00",
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
        var reservaRepository = Mockito.mock(ReservaRepository.class);

        var mockData = Mockito.mockStatic(LocalDate.class, Mockito.CALLS_REAL_METHODS);
        var dataMock = LocalDate.of(2024, 04, 29);
        mockData.when(LocalDate::now)
                .thenReturn(dataMock);

        var reservaUseCaseImpl = new ReservaUseCaseImpl(reservaRepository, restauranteRepository);

        // execução e avaliação
        var excecao = Assertions.assertThrows(RuntimeException.class, () -> {
            reservaUseCaseImpl.reserva(
                    "12345678901234",
                    new ReservaDTO(
                            LocalDate.now(),
                            "09:59",
                            2,
                            "11122233344"
                    )
            );
        });
        verify(reservaRepository, times(0)).save(Mockito.any());
        Assertions.assertEquals("Restaurante não está aberto no período da reserva", excecao.getMessage());

        mockData.close();
    }

    @Test
    public void reserva_restauranteAtingiuCapacidadeMaxima_naoSalvaNaBaseDeDados() {
        // preparação
        var restauranteRepository = Mockito.mock(RestauranteRepository.class);
        Mockito.when(restauranteRepository.findById(Mockito.any()))
                .thenReturn(
                        Optional.of(new RestauranteEntity(
                                "12345678901234",
                                "Restaurante A",
                                TipoCozinhaEnum.COMIDA_ARABE,
                                List.of(DiasEnum.SEGUNDA_FEIRA, DiasEnum.QUARTA_FEIRA).toString(),
                                "10:00 ate 12:00",
                                10,
                                "14025010",
                                "Rua teste",
                                123,
                                "Jardim Sumare",
                                "Cidade A",
                                "SP",
                                "Complemento A"
                        ))
                );
        var reservaRepository = Mockito.mock(ReservaRepository.class);
        Mockito.when(reservaRepository.sumByDiaAndStatusReserva(Mockito.any(), Mockito.any()))
                .thenReturn(
                        9
                );

        var mockData = Mockito.mockStatic(LocalDate.class, Mockito.CALLS_REAL_METHODS);
        var dataMock = LocalDate.of(2024, 04, 29);
        mockData.when(LocalDate::now)
                .thenReturn(dataMock);

        var reservaUseCaseImpl = new ReservaUseCaseImpl(reservaRepository, restauranteRepository);

        // execução e avaliação
        var excecao = Assertions.assertThrows(RuntimeException.class, () -> {
            reservaUseCaseImpl.reserva(
                    "12345678901234",
                    new ReservaDTO(
                            LocalDate.now(),
                            "10:05",
                            2,
                            "11122233344"
                    )
            );
        });
        verify(reservaRepository, times(0)).save(Mockito.any());
        Assertions.assertEquals("Restaurante não tem reservas disponíveis para o dia e horário solicitado", excecao.getMessage());

        mockData.close();
    }

    @Test
    public void reserva_passouPorTodosRequisitos_salvaNaBaseDeDados() {
        // preparação
        var restauranteRepository = Mockito.mock(RestauranteRepository.class);
        Mockito.when(restauranteRepository.findById(Mockito.any()))
                .thenReturn(
                        Optional.of(new RestauranteEntity(
                                "12345678901234",
                                "Restaurante A",
                                TipoCozinhaEnum.COMIDA_ARABE,
                                List.of(DiasEnum.SEGUNDA_FEIRA, DiasEnum.QUARTA_FEIRA).toString(),
                                "10:00 ate 12:00",
                                10,
                                "14025010",
                                "Rua teste",
                                123,
                                "Jardim Sumare",
                                "Cidade A",
                                "SP",
                                "Complemento A"
                        ))
                );
        var reservaRepository = Mockito.mock(ReservaRepository.class);
        Mockito.when(reservaRepository.sumByDiaAndStatusReserva(Mockito.any(), Mockito.any()))
                .thenReturn(
                        7
                );

        var mockData = Mockito.mockStatic(LocalDate.class, Mockito.CALLS_REAL_METHODS);
        var dataMock = LocalDate.of(2024, 04, 29);
        mockData.when(LocalDate::now)
                .thenReturn(dataMock);

        var reservaUseCaseImpl = new ReservaUseCaseImpl(reservaRepository, restauranteRepository);

        // execução
        reservaUseCaseImpl.reserva(
                "12345678901234",
                new ReservaDTO(
                        LocalDate.now(),
                        "11:00",
                        2,
                        "11122233344"
                )
        );

        // avaliação
        verify(reservaRepository, times(1)).save(Mockito.any());

        mockData.close();
    }

    @ParameterizedTest
    @MethodSource("requestValidandoCampos")
    public void restaurante_camposInvalidos_naoRealizaReserva(String cnpj,
                                                              LocalDate dia,
                                                              String horarioDeChegada,
                                                              int quantidadeLugares,
                                                              String cpfCnpjCliente) {
        // preparação
        var restauranteRepository = Mockito.mock(RestauranteRepository.class);
        var reservaRepository = Mockito.mock(ReservaRepository.class);
        Mockito.when(reservaRepository.save(Mockito.any()))
                .thenReturn(
                        new ReservaEntity(
                                1L,
                                "12345678901234",
                                "12345678901234",
                                LocalDate.now(),
                                "18:00",
                                10,
                                null,
                                null
                        )
                );

        var reservaUseCaseImpl = new ReservaUseCaseImpl(reservaRepository, restauranteRepository);

        // execução e avaliação
        var excecao = Assertions.assertThrows(RuntimeException.class, () -> {
            reservaUseCaseImpl.reserva(
                    cnpj,
                    new ReservaDTO(
                            dia,
                            horarioDeChegada,
                            quantidadeLugares,
                            cpfCnpjCliente
                    )
            );
        });
        verify(reservaRepository, times(0)).save(Mockito.any());
    }

    @Test
    public void reserva_atualizaNaBaseDeDados() {
        // preparação
        var restauranteRepository = Mockito.mock(RestauranteRepository.class);

        var reservaRepository = Mockito.mock(ReservaRepository.class);
        Mockito.when(reservaRepository.findById(Mockito.any()))
                .thenReturn(
                        Optional.of(new ReservaEntity(
                                1L,
                                "12345678901234",
                                "11122233344",
                                LocalDate.now(),
                                "18:00",
                                10,
                                LocalDateTime.now(),
                                StatusReservaEnum.PENDENTE
                        ))
                );

        var reservaUseCaseImpl = new ReservaUseCaseImpl(reservaRepository, restauranteRepository);

        // execução
        reservaUseCaseImpl.atualizaReserva(
                1L,
                StatusReservaEnum.RESERVADO
        );

        // avaliação
        verify(reservaRepository, times(1)).save(Mockito.any());
    }

    @Test
    public void reserva_reservaNaoEncontrada_naoAtualizaNaBaseDeDados() {
        // preparação
        var restauranteRepository = Mockito.mock(RestauranteRepository.class);

        var reservaRepository = Mockito.mock(ReservaRepository.class);
        Mockito.when(reservaRepository.findById(Mockito.any()))
                .thenReturn(
                        Optional.empty()
                );

        var reservaUseCaseImpl = new ReservaUseCaseImpl(reservaRepository, restauranteRepository);

        // execução e avaliação
        var excecao = Assertions.assertThrows(RuntimeException.class, () -> {
            reservaUseCaseImpl.atualizaReserva(
                    1L,
                    StatusReservaEnum.RESERVADO
            );
        });
        verify(reservaRepository, times(0)).save(Mockito.any());
        Assertions.assertEquals("Reserva não encontrada", excecao.getMessage());
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
