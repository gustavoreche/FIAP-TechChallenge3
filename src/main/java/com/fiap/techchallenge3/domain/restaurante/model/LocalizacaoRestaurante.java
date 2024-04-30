package com.fiap.techchallenge3.domain.restaurante.model;

import com.fiap.techchallenge3.domain.localizacao.model.Localizacao;

import java.util.Objects;

public record LocalizacaoRestaurante(
        Localizacao localizacao,
        Integer numero,
        String complemento
) {

    public LocalizacaoRestaurante {
        if (Objects.isNull(numero)) {
            throw new IllegalArgumentException("NUMERO NAO PODE SER NULO OU VAZIO!");
        }
        if (numero < 1 || numero > 99999) {
            throw new IllegalArgumentException("O numero deve ter no mínimo 1 e no máximo 99999 letras");
        }

        if (Objects.nonNull(complemento) && (complemento.length() < 2 || complemento.length() > 30)) {
            throw new IllegalArgumentException("COMPLEMENTO INVÁLIDO!");
        }

    }

}
