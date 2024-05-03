package com.fiap.techchallenge3.domain.reserva;

import java.util.Objects;

import static com.fiap.techchallenge3.domain.restaurante.Cnpj.REGEX_CNPJ;

public record CpfCnpj(
        String numero
) {

    public static final String REGEX_CPF_OU_CNPJ = "(^\\d{3}\\.?\\d{3}\\.?\\d{3}-?\\d{2}$|"
            .concat(REGEX_CNPJ)
            .concat(")");

    public CpfCnpj {
        if (Objects.isNull(numero) || numero.isEmpty()) {
            throw new IllegalArgumentException("CPF OU CNPJ DO CLIENTE NAO PODE SER NULO OU VAZIO!");
        }
        if (!numero.matches(REGEX_CPF_OU_CNPJ)) {
            throw new IllegalArgumentException("CPF OU CNPJ DO CLIENTE INVÁLIDO!");
        }

    }

}
