package com.fiap.techchallenge3.domain.localizacao.model;

import java.util.Objects;

public record Cep(
        String numero
) {

    public static final String REGEX_CEP = "^\\d{5}-\\d{3}$";
    public Cep {
        if (Objects.isNull(numero) || numero.isEmpty()) {
            throw new IllegalArgumentException("CEP NAO PODE SER NULO OU VAZIO!");
        }
        if (!numero.matches(REGEX_CEP)) {
            throw new IllegalArgumentException("CEP INVÁLIDO!");
        }
    }
}
