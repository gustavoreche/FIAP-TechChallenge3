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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.stream.Stream;

import static com.fiap.techchallenge3.utils.RestauranteUtils.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
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
    public void restaurante_funcionamento24horas_salvaNaBaseDeDados() {
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
                        criaHorarioFuncionamento(List.of(DiasEnum.SEGUNDA_FEIRA), "24horas"),
                        500
                )
        );

        // avaliação
        verify(restauranteRepository, times(1)).save(Mockito.any());
    }

    @Test
    public void restaurante_semComplemento_salvaNaBaseDeDados() {
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
                        criaLocalizacao("rua teste", 10, "14000-000", "bairro teste", "cidade teste", "SP", null),
                        TipoCozinhaEnum.COMIDA_ARABE,
                        horarioFuncionamentoDefault(),
                        500
                )
        );

        // avaliação
        verify(restauranteRepository, times(1)).save(Mockito.any());
    }

    @Test
    public void restaurante_especificaUmDiaETambemColocaTodosDias_salvaNaBaseDeDados() {
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
                        criaHorarioFuncionamento(List.of(DiasEnum.SEGUNDA_FEIRA, DiasEnum.TODOS), "18:00 ate 23:00"),
                        500
                )
        );

        // avaliação
        verify(restauranteRepository, times(1)).save(Mockito.any());
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
                            cnpj,
                            nome,
                            localizacao,
                            tipoCozinha,
                            horarioFuncionamento,
                            capacidadeDePessoas
                    )
            );
        });
        verify(restauranteRepository, times(0)).save(Mockito.any());
    }

    @ParameterizedTest
    @MethodSource("requestHorariosInvalidos")
    public void restaurante_horarioInvalido_naoSalvaNaBaseDeDados(String horario) {
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
                            "49.251.058/0001-05",
                            "Restaurante Teste",
                            localizacaoDefault(),
                            TipoCozinhaEnum.COMIDA_ARABE,
                            criaHorarioFuncionamento(List.of(DiasEnum.SEGUNDA_FEIRA), horario),
                            500
                    )
            );
        });
        verify(restauranteRepository, times(0)).save(Mockito.any());
    }

    @Test
    public void restaurante_informandoTodosCampos_buscaNaBaseDeDados() {
        // preparação
        // preparação
        var restauranteRepository = Mockito.mock(RestauranteRepository.class);
        Mockito.when(restauranteRepository.findAll(Mockito.any(Specification.class)))
                .thenReturn(
                        List.of(
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
                        )
                );

        var restauranteUseCaseImpl = new RestauranteUseCaseImpl(restauranteRepository);

        // execução
        restauranteUseCaseImpl.busca(
                "nome teste",
                "14000-000",
                "bairro teste",
                "cidade teste",
                "SP",
                TipoCozinhaEnum.COMIDA_JAPONESA
        );

        // avaliação
        verify(restauranteRepository, times(1)).findAll(Mockito.any(Specification.class));
    }

    @ParameterizedTest
    @MethodSource("requestValidandoCamposDeBusca")
    public void restaurante_camposInvalidos_naoBuscaNaBaseDeDados(String nome,
                                                                  String cep,
                                                                  String bairro,
                                                                  String cidade,
                                                                  String estado,
                                                                  TipoCozinhaEnum tipoCozinha) {
        // preparação
        var restauranteRepository = Mockito.mock(RestauranteRepository.class);
        Mockito.when(restauranteRepository.findAll(Mockito.any(Specification.class)))
                .thenReturn(
                        List.of(
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
                        )
                );

        var restauranteUseCaseImpl = new RestauranteUseCaseImpl(restauranteRepository);

        // execução e avaliação
        var excecao = Assertions.assertThrows(RuntimeException.class, () -> {
            restauranteUseCaseImpl.busca(
                    nome,
                    cep,
                    bairro,
                    cidade,
                    estado,
                    tipoCozinha
            );
        });
        verify(restauranteRepository, times(0)).findAll(Mockito.any(Specification.class));
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

    private static Stream<Arguments> requestValidandoCamposDeBusca() {
        return Stream.of(
                Arguments.of(" ", "14000-000", "bairro teste", "cidade teste", "sp", TipoCozinhaEnum.COMIDA_JAPONESA),
                Arguments.of("aa", "14000-000", "bairro teste", "cidade teste", "sp", TipoCozinhaEnum.COMIDA_JAPONESA),
                Arguments.of("aaaaaaaaaabbbbbbbbbbccccccccccddddddddddeeeeeeeeeef", "14000-000", "bairro teste", "cidade teste", "sp", TipoCozinhaEnum.COMIDA_JAPONESA),
                Arguments.of("Nome de teste", " ", "bairro teste", "cidade teste", "sp", TipoCozinhaEnum.COMIDA_JAPONESA),
                Arguments.of("Nome de teste", "14000000", "bairro teste", "cidade teste", "sp", TipoCozinhaEnum.COMIDA_JAPONESA),
                Arguments.of("Nome de teste", "14000-000", " ", "cidade teste", "sp", TipoCozinhaEnum.COMIDA_JAPONESA),
                Arguments.of("Nome de teste", "14000-000", "aaaa", "cidade teste", "sp", TipoCozinhaEnum.COMIDA_JAPONESA),
                Arguments.of("Nome de teste", "14000-000", "aaaaaaaaaabbbbbbbbbbccccccccccddddddddddeeeeeeeeeef", "cidade teste", "sp", TipoCozinhaEnum.COMIDA_JAPONESA),
                Arguments.of("Nome de teste", "14000-000", "bairro teste", " ", "sp", TipoCozinhaEnum.COMIDA_JAPONESA),
                Arguments.of("Nome de teste", "14000-000", "bairro teste", "aaaa", "sp", TipoCozinhaEnum.COMIDA_JAPONESA),
                Arguments.of("Nome de teste", "14000-000", "bairro teste", "aaaaaaaaaabbbbbbbbbbccccccccccddddddddddeeeeeeeeeef", "sp", TipoCozinhaEnum.COMIDA_JAPONESA),
                Arguments.of("Nome de teste", "14000-000", "bairro teste", "cidade teste", " ", TipoCozinhaEnum.COMIDA_JAPONESA),
                Arguments.of("Nome de teste", "14000-000", "bairro teste", "cidade teste", "a", TipoCozinhaEnum.COMIDA_JAPONESA),
                Arguments.of("Nome de teste", "14000-000", "bairro teste", "cidade teste", "aaa", TipoCozinhaEnum.COMIDA_JAPONESA)
        );
    }

}
