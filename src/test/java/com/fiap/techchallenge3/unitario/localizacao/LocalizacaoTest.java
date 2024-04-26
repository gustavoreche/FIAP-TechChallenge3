package com.fiap.techchallenge3.unitario.localizacao;

import com.fiap.techchallenge3.domain.localizacao.model.Localizacao;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class LocalizacaoTest {

    @Test
    public void localizacao_existe() {
        // preparação
        var localizacao = new Localizacao(
                "Rua A",
                "12345-678",
                "Bairro A",
                "Cidade A",
                "SP"
        );

        // execução
        var naoExiste = localizacao.naoExiste();

        // avaliação
        Assertions.assertFalse(naoExiste);
    }

    @Test
    public void localizacao_naoExiste() {
        // preparação
        var localizacao = new Localizacao(
                null,
                null,
                null,
                null,
                null
        );

        // execução
        var naoExiste = localizacao.naoExiste();

        // avaliação
        Assertions.assertTrue(naoExiste);
    }

}
