package com.fiap.techchallenge3.domain.localizacao.model;

import java.util.Objects;

public record Localizacao(
        String logradouro,
        Cep cep,
        String bairro,
        String cidade,
        String estado
) {

    public static final String REGEX_ESTADO = "^\\w{2}$";

    public Localizacao {
        if (Objects.isNull(logradouro) || logradouro.isEmpty()) {
            throw new IllegalArgumentException("LOGRADOURO NAO PODE SER NULO OU VAZIO!");
        }
        if (logradouro.length() < 5 || logradouro.length() > 50) {
            throw new IllegalArgumentException("O logradouro deve ter no mínimo 5 letras e no máximo 50 letras");
        }

        if (Objects.isNull(bairro) || bairro.isEmpty()) {
            throw new IllegalArgumentException("BAIRRO NAO PODE SER NULO OU VAZIO!");
        }
        if (bairro.length() < 5 || bairro.length() > 50) {
            throw new IllegalArgumentException("O bairro deve ter no mínimo 5 letras e no máximo 50 letras");
        }

        if (Objects.isNull(cidade) || cidade.isEmpty()) {
            throw new IllegalArgumentException("CIDADE NAO PODE SER NULO OU VAZIO!");
        }
        if (cidade.length() < 5 || cidade.length() > 50) {
            throw new IllegalArgumentException("A cidade deve ter no mínimo 5 letras e no máximo 50 letras");
        }

        if (Objects.isNull(estado) || estado.isEmpty()) {
            throw new IllegalArgumentException("ESTADO NAO PODE SER NULO OU VAZIO!");
        }
        if (!estado.matches(REGEX_ESTADO)) {
            throw new IllegalArgumentException("ESTADO INVÁLIDO!");
        }

    }

}
