package com.fiap.techchallenge3.domain.restaurante.model;

import java.util.Objects;

public record Restaurante(
        String cnpj,
        String nome,
        LocalizacaoRestaurante localizacaoCompleta,
        TipoCozinhaEnum tipoCozinha,
        HorarioDeFuncionamento funcionamento,
        Integer capacidadeDePessoas
) {

    public static final String REGEX_CNPJ = "^\\d{2}\\.?\\d{3}\\.?\\d{3}\\/?\\d{4}\\-?\\d{2}$";

    public Restaurante {
        if (Objects.isNull(cnpj) || cnpj.isEmpty()) {
            throw new IllegalArgumentException("CNPJ NAO PODE SER NULO OU VAZIO!");
        }
        if (!cnpj.matches(REGEX_CNPJ)) {
            throw new IllegalArgumentException("CNPJ INVÁLIDO!");
        }

        if (Objects.isNull(nome) || nome.isEmpty()) {
            throw new IllegalArgumentException("CNPJ NAO PODE SER NULO OU VAZIO!");
        }
        if (nome.length() < 3 || nome.length() > 50) {
            throw new IllegalArgumentException("O nome deve ter no mínimo 3 letras e no máximo 50 letras");
        }

        if (Objects.isNull(tipoCozinha)) {
            throw new IllegalArgumentException("TIPO COZINHA NAO PODE SER NULO!");
        }

        if (Objects.isNull(capacidadeDePessoas)) {
            throw new IllegalArgumentException("CAPACIDADE DE PESSOAS NAO PODE SER NULO!");
        }
        if (capacidadeDePessoas < 1 || capacidadeDePessoas > 5000) {
            throw new IllegalArgumentException("A capacidade de pessoas deve ter no mínimo 1 ou no máximo 5000");
        }

    }

}
