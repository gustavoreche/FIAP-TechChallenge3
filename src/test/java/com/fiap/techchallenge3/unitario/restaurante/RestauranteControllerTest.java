package com.fiap.techchallenge3.unitario.restaurante;

import com.fiap.techchallenge3.domain.restaurante.model.DiasEnum;
import com.fiap.techchallenge3.domain.restaurante.model.TipoCozinhaEnum;
import com.fiap.techchallenge3.infrastructure.restaurante.controller.RestauranteController;
import com.fiap.techchallenge3.infrastructure.restaurante.controller.dto.CriaRestauranteDTO;
import com.fiap.techchallenge3.infrastructure.restaurante.controller.dto.EnderecoCompletoDTO;
import com.fiap.techchallenge3.infrastructure.restaurante.controller.dto.HorarioDeFuncionamentoDTO;
import com.fiap.techchallenge3.useCase.restaurante.impl.RestauranteUseCaseImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.stream.Stream;

import static com.fiap.techchallenge3.utils.RestauranteUtils.*;
import static org.mockito.ArgumentMatchers.any;

public class RestauranteControllerTest {

    @Test
    public void restaurante_deveRetornar201_salvaNaBaseDeDados() {
        // preparação
        var restauranteUseCaseImpl = Mockito.mock(RestauranteUseCaseImpl.class);

        var restauranteController = new RestauranteController(restauranteUseCaseImpl);

        // execução
        var restaurante = restauranteController.cadastra(
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
        Assertions.assertEquals(HttpStatus.CREATED, restaurante.getStatusCode());
    }

    @Test
    public void restaurante_deveRetornar201_funcionamento24horas_salvaNaBaseDeDados() {
        // preparação
        var restauranteUseCaseImpl = Mockito.mock(RestauranteUseCaseImpl.class);

        var restauranteController = new RestauranteController(restauranteUseCaseImpl);

        // execução
        var restaurante = restauranteController.cadastra(
                new CriaRestauranteDTO(
                        "49.251.058/0001-05",
                        "Restaurante Teste",
                        localizacaoDefault(),
                        TipoCozinhaEnum.COMIDA_ARABE,
                        criaHorarioFuncionamento(List.of(DiasEnum.SEGUNDA_FEIRA), "24horas"),
                        500
                )
        );

        // avaliação
        Assertions.assertEquals(HttpStatus.CREATED, restaurante.getStatusCode());
    }

    @Test
    public void restaurante_deveRetornar201_semComplemento_salvaNaBaseDeDados() {
        // preparação
        var restauranteUseCaseImpl = Mockito.mock(RestauranteUseCaseImpl.class);

        var restauranteController = new RestauranteController(restauranteUseCaseImpl);

        // execução
        var restaurante = restauranteController.cadastra(
                new CriaRestauranteDTO(
                        "49.251.058/0001-05",
                        "Restaurante Teste",
                        criaLocalizacao("rua teste", 10, "14000-000", "bairro teste", "cidade teste", "SP", null),
                        TipoCozinhaEnum.COMIDA_ARABE,
                        horarioFuncionamentoDefault(),
                        500
                )
        );

        // avaliação
        Assertions.assertEquals(HttpStatus.CREATED, restaurante.getStatusCode());
    }

    @Test
    public void restaurante_deveRetornar201_especificaUmDiaETambemColocaTodosDias_salvaNaBaseDeDados() {
        // preparação
        var restauranteUseCaseImpl = Mockito.mock(RestauranteUseCaseImpl.class);

        var restauranteController = new RestauranteController(restauranteUseCaseImpl);

        // execução
        var restaurante = restauranteController.cadastra(
                new CriaRestauranteDTO(
                        "49.251.058/0001-05",
                        "Restaurante Teste",
                        localizacaoDefault(),
                        TipoCozinhaEnum.COMIDA_ARABE,
                        criaHorarioFuncionamento(List.of(DiasEnum.SEGUNDA_FEIRA, DiasEnum.TODOS), "18:00 ate 23:00"),
                        500
                )
        );

        // avaliação
        Assertions.assertEquals(HttpStatus.CREATED, restaurante.getStatusCode());
    }

    @ParameterizedTest
    @MethodSource("requestValidandoCampos")
    public void restaurante_camposInvalidos_naoSalvaNaBaseDeDados(String cnpj,
                                                                                  String nome,
                                                                                  EnderecoCompletoDTO localizacao,
                                                                                  TipoCozinhaEnum tipoCozinha,
                                                                                  HorarioDeFuncionamentoDTO horarioFuncionamento,
                                                                                  Integer capacidadeDePessoas) {
        // preparação
        var restauranteUseCaseImpl = Mockito.mock(RestauranteUseCaseImpl.class);
        Mockito.doThrow(
                new IllegalArgumentException("Campos inválidos!")
        )
                .when(restauranteUseCaseImpl)
                .cadastra(any(CriaRestauranteDTO.class));

        var restauranteController = new RestauranteController(restauranteUseCaseImpl);

        // execução e avaliação
        var excecao = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            restauranteController.cadastra(
                    new CriaRestauranteDTO(
                            cnpj,
                            nome,
                            localizacao,
                            tipoCozinha,
                            horarioFuncionamento,
                            capacidadeDePessoas
                    )
            );
        });
    }

    @ParameterizedTest
    @MethodSource("requestHorariosInvalidos")
    public void restaurante_horarioInvalido_naoSalvaNaBaseDeDados(String horario) {
        // preparação
        var restauranteUseCaseImpl = Mockito.mock(RestauranteUseCaseImpl.class);
        Mockito.doThrow(
                        new IllegalArgumentException("Campos inválidos!")
                )
                .when(restauranteUseCaseImpl)
                .cadastra(any(CriaRestauranteDTO.class));

        var restauranteController = new RestauranteController(restauranteUseCaseImpl);

        // execução e avaliação
        var excecao = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            restauranteController.cadastra(
                    new CriaRestauranteDTO(
                            "49.251.058/0001-05",
                            "Restaurante Teste",
                            localizacaoDefault(),
                            TipoCozinhaEnum.COMIDA_ARABE,
                            criaHorarioFuncionamento(List.of(DiasEnum.SEGUNDA_FEIRA), horario),
                            500
                    )
            );
        });
    }

    private static Stream<Arguments> requestValidandoCampos() {
        return Stream.of(
                Arguments.of(null, "Nome de teste", localizacaoDefault(),
                        TipoCozinhaEnum.COMIDA_JAPONESA, horarioFuncionamentoDefault(), 10),
                Arguments.of("", "Nome de teste", localizacaoDefault(),
                        TipoCozinhaEnum.COMIDA_JAPONESA, horarioFuncionamentoDefault(), 10),
                Arguments.of("04623021000114", null, localizacaoDefault(),
                        TipoCozinhaEnum.COMIDA_JAPONESA, horarioFuncionamentoDefault(), 10),
                Arguments.of("04623021000114", "", localizacaoDefault(),
                        TipoCozinhaEnum.COMIDA_JAPONESA, horarioFuncionamentoDefault(), 10),
                Arguments.of("04623021000114", "Nome de teste", null,
                        TipoCozinhaEnum.COMIDA_JAPONESA, horarioFuncionamentoDefault(), 10),
                Arguments.of("04623021000114", "Nome de teste", criaLocalizacao(null, 1010, "14000-000", "bairro teste", "cidade teste", "SP", "ap1122"),
                        TipoCozinhaEnum.COMIDA_JAPONESA, horarioFuncionamentoDefault(), 10),
                Arguments.of("04623021000114", "Nome de teste", criaLocalizacao("rua dos testes", null, "14000-000", "bairro teste", "cidade teste", "SP", "ap1122"),
                        TipoCozinhaEnum.COMIDA_JAPONESA, horarioFuncionamentoDefault(), 10),
                Arguments.of("04623021000114", "Nome de teste", criaLocalizacao("rua dos testes", 1010, null, "bairro teste", "cidade teste", "SP", "ap1122"),
                        TipoCozinhaEnum.COMIDA_JAPONESA, horarioFuncionamentoDefault(), 10),
                Arguments.of("04623021000114", "Nome de teste", criaLocalizacao("rua dos testes", 1010, "14000-000", null, "cidade teste", "SP", "ap1122"),
                        TipoCozinhaEnum.COMIDA_JAPONESA, horarioFuncionamentoDefault(), 10),
                Arguments.of("04623021000114", "Nome de teste", criaLocalizacao("rua dos testes", 1010, "14000-000", "bairro teste", null, "SP", "ap1122"),
                        TipoCozinhaEnum.COMIDA_JAPONESA, horarioFuncionamentoDefault(), 10),
                Arguments.of("04623021000114", "Nome de teste", criaLocalizacao("rua dos testes", 1010, "14000-000", "bairro teste", "cidade teste", null, "ap1122"),
                        TipoCozinhaEnum.COMIDA_JAPONESA, horarioFuncionamentoDefault(), 10),
                Arguments.of("04623021000114", "Nome de teste", localizacaoDefault(),
                        null, horarioFuncionamentoDefault(), 10),
                Arguments.of("04623021000114", "Nome de teste", localizacaoDefault(),
                        TipoCozinhaEnum.COMIDA_JAPONESA, null, 10),
                Arguments.of("04623021000114", "Nome de teste", localizacaoDefault(),
                        TipoCozinhaEnum.COMIDA_JAPONESA, criaHorarioFuncionamento(null, "aa"), 10),
                Arguments.of("04623021000114", "Nome de teste", localizacaoDefault(),
                        TipoCozinhaEnum.COMIDA_JAPONESA, criaHorarioFuncionamento(List.of(DiasEnum.SEGUNDA_FEIRA), null), 10),
                Arguments.of("04623021000114", "Nome de teste", localizacaoDefault(),
                        TipoCozinhaEnum.COMIDA_JAPONESA, horarioFuncionamentoDefault(), null),
                Arguments.of(null, null, null, null, null, null),
                Arguments.of("", "", localizacaoDefault(),
                        TipoCozinhaEnum.COMIDA_JAPONESA, horarioFuncionamentoDefault(), 10),

                Arguments.of(" ", "Nome de teste", localizacaoDefault(),
                        TipoCozinhaEnum.COMIDA_JAPONESA, horarioFuncionamentoDefault(), 10),
                Arguments.of("aa", "Nome de teste", localizacaoDefault(),
                        TipoCozinhaEnum.COMIDA_JAPONESA, horarioFuncionamentoDefault(), 10),
                Arguments.of("376.082.600-84", "Nome de teste", localizacaoDefault(),
                        TipoCozinhaEnum.COMIDA_JAPONESA, horarioFuncionamentoDefault(), 10),
                Arguments.of("04623021000114", " ", localizacaoDefault(),
                        TipoCozinhaEnum.COMIDA_JAPONESA, horarioFuncionamentoDefault(), 10),
                Arguments.of("04623021000114", "aa", localizacaoDefault(),
                        TipoCozinhaEnum.COMIDA_JAPONESA, horarioFuncionamentoDefault(), 10),
                Arguments.of("04623021000114", "aaaaaaaaaabbbbbbbbbbccccccccccddddddddddeeeeeeeeeef", localizacaoDefault(),
                        TipoCozinhaEnum.COMIDA_JAPONESA, horarioFuncionamentoDefault(), 10),
                Arguments.of("04623021000114", "Nome de teste", criaLocalizacao(" ", 1010, "14000-000", "bairro teste", "cidade teste", "SP", "ap1122"),
                        TipoCozinhaEnum.COMIDA_JAPONESA, horarioFuncionamentoDefault(), 10),
                Arguments.of("04623021000114", "Nome de teste", criaLocalizacao("aaaa", 1010, "14000-000", "bairro teste", "cidade teste", "SP", "ap1122"),
                        TipoCozinhaEnum.COMIDA_JAPONESA, horarioFuncionamentoDefault(), 10),
                Arguments.of("04623021000114", "Nome de teste", criaLocalizacao("aaaaaaaaaabbbbbbbbbbccccccccccddddddddddeeeeeeeeeef", 1010, "14000-000", "bairro teste", "cidade teste", "SP", "ap1122"),
                        TipoCozinhaEnum.COMIDA_JAPONESA, horarioFuncionamentoDefault(), 10),
                Arguments.of("04623021000114", "Nome de teste", criaLocalizacao("rua dos testes", 0, "14000-000", "bairro teste", "cidade teste", "SP", "ap1122"),
                        TipoCozinhaEnum.COMIDA_JAPONESA, horarioFuncionamentoDefault(), 10),
                Arguments.of("04623021000114", "Nome de teste", criaLocalizacao("rua dos testes", 1_000_000, "14000-000", "bairro teste", "cidade teste", "SP", "ap1122"),
                        TipoCozinhaEnum.COMIDA_JAPONESA, horarioFuncionamentoDefault(), 10),
                Arguments.of("04623021000114", "Nome de teste", criaLocalizacao("rua dos testes", 1010, " ", "bairro teste", "cidade teste", "SP", "ap1122"),
                        TipoCozinhaEnum.COMIDA_JAPONESA, horarioFuncionamentoDefault(), 10),
                Arguments.of("04623021000114", "Nome de teste", criaLocalizacao("rua dos testes", 1010, "14000000", "bairro teste", "cidade teste", "SP", "ap1122"),
                        TipoCozinhaEnum.COMIDA_JAPONESA, horarioFuncionamentoDefault(), 10),
                Arguments.of("04623021000114", "Nome de teste", criaLocalizacao("rua dos testes", 1010, "14000-000", " ", "cidade teste", "SP", "ap1122"),
                        TipoCozinhaEnum.COMIDA_JAPONESA, horarioFuncionamentoDefault(), 10),
                Arguments.of("04623021000114", "Nome de teste", criaLocalizacao("rua dos testes", 1010, "14000-000", "aaaa", "cidade teste", "SP", "ap1122"),
                        TipoCozinhaEnum.COMIDA_JAPONESA, horarioFuncionamentoDefault(), 10),
                Arguments.of("04623021000114", "Nome de teste", criaLocalizacao("rua dos testes", 1010, "14000-000", "aaaaaaaaaabbbbbbbbbbccccccccccddddddddddeeeeeeeeeef", "cidade teste", "SP", "ap1122"),
                        TipoCozinhaEnum.COMIDA_JAPONESA, horarioFuncionamentoDefault(), 10),
                Arguments.of("04623021000114", "Nome de teste", criaLocalizacao("rua dos testes", 1010, "14000-000", "bairro teste", " ", "SP", "ap1122"),
                        TipoCozinhaEnum.COMIDA_JAPONESA, horarioFuncionamentoDefault(), 10),
                Arguments.of("04623021000114", "Nome de teste", criaLocalizacao("rua dos testes", 1010, "14000-000", "bairro teste", "aaaa", "SP", "ap1122"),
                        TipoCozinhaEnum.COMIDA_JAPONESA, horarioFuncionamentoDefault(), 10),
                Arguments.of("04623021000114", "Nome de teste", criaLocalizacao("rua dos testes", 1010, "14000-000", "bairro teste", "aaaaaaaaaabbbbbbbbbbccccccccccddddddddddeeeeeeeeeef", "SP", "ap1122"),
                        TipoCozinhaEnum.COMIDA_JAPONESA, horarioFuncionamentoDefault(), 10),
                Arguments.of("04623021000114", "Nome de teste", criaLocalizacao("rua dos testes", 1010, "14000-000", "bairro teste", "cidade teste", " ", "ap1122"),
                        TipoCozinhaEnum.COMIDA_JAPONESA, horarioFuncionamentoDefault(), 10),
                Arguments.of("04623021000114", "Nome de teste", criaLocalizacao("rua dos testes", 1010, "14000-000", "bairro teste", "cidade teste", "a", "ap1122"),
                        TipoCozinhaEnum.COMIDA_JAPONESA, horarioFuncionamentoDefault(), 10),
                Arguments.of("04623021000114", "Nome de teste", criaLocalizacao("rua dos testes", 1010, "14000-000", "bairro teste", "cidade teste", "aaa", "ap1122"),
                        TipoCozinhaEnum.COMIDA_JAPONESA, horarioFuncionamentoDefault(), 10),
                Arguments.of("04623021000114", "Nome de teste", criaLocalizacao("rua dos testes", 1010, "14000-000", "bairro teste", "cidade teste", "SP", " "),
                        TipoCozinhaEnum.COMIDA_JAPONESA, horarioFuncionamentoDefault(), 10),
                Arguments.of("04623021000114", "Nome de teste", criaLocalizacao("rua dos testes", 1010, "14000-000", "bairro teste", "cidade teste", "SP", "a"),
                        TipoCozinhaEnum.COMIDA_JAPONESA, horarioFuncionamentoDefault(), 10),
                Arguments.of("04623021000114", "Nome de teste", criaLocalizacao("rua dos testes", 1010, "14000-000", "bairro teste", "cidade teste", "SP", "aaaaaaaaaabbbbbbbbbbccccccccccd"),
                        TipoCozinhaEnum.COMIDA_JAPONESA, horarioFuncionamentoDefault(), 10),
                Arguments.of("04623021000114", "Nome de teste", localizacaoDefault(),
                        TipoCozinhaEnum.COMIDA_JAPONESA, criaHorarioFuncionamento(List.of(DiasEnum.SEGUNDA_FEIRA), " "), 10),
                Arguments.of("04623021000114", "Nome de teste", localizacaoDefault(),
                        TipoCozinhaEnum.COMIDA_JAPONESA, criaHorarioFuncionamento(List.of(DiasEnum.SEGUNDA_FEIRA), "aaaa"), 10),
                Arguments.of("04623021000114", "Nome de teste", localizacaoDefault(),
                        TipoCozinhaEnum.COMIDA_JAPONESA, criaHorarioFuncionamento(List.of(), "18:00 ate 23:00"), 10),
                Arguments.of("04623021000114", "Nome de teste", localizacaoDefault(),
                        TipoCozinhaEnum.COMIDA_JAPONESA, horarioFuncionamentoDefault(), 0),
                Arguments.of("04623021000114", "Nome de teste", localizacaoDefault(),
                        TipoCozinhaEnum.COMIDA_JAPONESA, horarioFuncionamentoDefault(), 5001)
        );
    }

    private static Stream<Arguments> requestHorariosInvalidos() {
        return Stream.of(
                Arguments.of("25:00 ate 23:00"),
                Arguments.of("22:00 ate 25:00"),
                Arguments.of("22:00 ate 22:00"),
                Arguments.of("22:00 ate 21:00")
        );
    }

}
