package com.fiap.techchallenge3.unitario.localizacao;

import com.fiap.techchallenge3.domain.localizacao.service.LocalizacaoAdapter;
import com.fiap.techchallenge3.infrastructure.localizacao.client.ViaCepResponse;
import com.fiap.techchallenge3.useCase.localizacao.impl.LocalizacaoUseCaseImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class LocalizacaoUseCaseTest {

    @Test
    public void localizacao_existe() {
        // preparação
        var localizacaoAdapter = Mockito.mock(LocalizacaoAdapter.class);
        Mockito.when(localizacaoAdapter.pegaLocalizacao("12345678"))
                .thenReturn(new ViaCepResponse(
                        "Rua A",
                        "12345-678",
                        "Bairro A",
                        "Cidade A",
                        "SP"
                        )
                );

        var localizacaoUseCaseImpl = new LocalizacaoUseCaseImpl(localizacaoAdapter);

        // execução
        var localizacao = localizacaoUseCaseImpl.buscaPorCep("12345-678");

        // avaliação
        Assertions.assertEquals("Rua A", localizacao.logradouro());
        Assertions.assertEquals("12345-678", localizacao.cep());
        Assertions.assertEquals("Bairro A", localizacao.bairro());
        Assertions.assertEquals("Cidade A", localizacao.cidade());
        Assertions.assertEquals("SP", localizacao.estado());
    }

    @Test
    public void localizacao_naoExiste() {
        // preparação
        var localizacaoAdapter = Mockito.mock(LocalizacaoAdapter.class);
        Mockito.when(localizacaoAdapter.pegaLocalizacao("12345678"))
                .thenReturn(new ViaCepResponse(
                                null,
                                null,
                                null,
                                null,
                                null
                        )
                );

        var localizacaoUseCaseImpl = new LocalizacaoUseCaseImpl(localizacaoAdapter);

        // execução e avaliação
        var excecao = Assertions.assertThrows(RuntimeException.class, () -> {
            localizacaoUseCaseImpl.buscaPorCep("12345-678");
        });
        Assertions.assertEquals("ENDEREÇO NAO EXISTE!", excecao.getMessage());
    }

    @Test
    public void localizacao_apiRestIndisponivel() {
        // preparação
        var localizacaoAdapter = Mockito.mock(LocalizacaoAdapter.class);
        Mockito.when(localizacaoAdapter.pegaLocalizacao("12345678"))
                .thenThrow(
                        new RuntimeException("API REST Indisponível")
                );

        var localizacaoUseCaseImpl = new LocalizacaoUseCaseImpl(localizacaoAdapter);

        // execução e avaliação
        var excecao = Assertions.assertThrows(RuntimeException.class, () -> {
            localizacaoUseCaseImpl.buscaPorCep("12345-678");
        });
        Assertions.assertEquals("API REST Indisponível", excecao.getMessage());
    }

}
