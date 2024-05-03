package com.fiap.techchallenge3.domain.restaurante;

import java.util.Objects;

public record Cnpj(
        String numero
) {

    public static final String REGEX_CNPJ = "^\\d{2}\\.?\\d{3}\\.?\\d{3}\\/?\\d{4}\\-?\\d{2}$";

    public Cnpj {
        if (Objects.isNull(numero) || numero.isEmpty()) {
            throw new IllegalArgumentException("CNPJ NAO PODE SER NULO OU VAZIO!");
        }
        if (!numero.matches(REGEX_CNPJ)) {
            throw new IllegalArgumentException("CNPJ INVÁLIDO!");
        }

    }

}
