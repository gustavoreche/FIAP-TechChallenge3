package com.fiap.techchallenge3.domain.restaurante;

import java.util.Objects;

import static com.fiap.techchallenge3.domain.localizacao.Cep.REGEX_CEP;
import static com.fiap.techchallenge3.domain.localizacao.Localizacao.REGEX_ESTADO;

public record BuscaRestaurante(
        String nome,
        String cep,
        String bairro,
        String cidade,
        String estado,
        TipoCozinhaEnum tipoCozinha
) {

    public BuscaRestaurante {
        if (Objects.nonNull(nome) && (nome.trim().length() < 3 || nome.trim().length() > 50)) {
            throw new IllegalArgumentException("O nome deve ter no mínimo 3 letras e no máximo 50 letras");
        }

        if (Objects.nonNull(cep) && !cep.matches(REGEX_CEP)) {
            throw new IllegalArgumentException("CEP INVÁLIDO!");
        }

        if (Objects.nonNull(bairro) && (bairro.trim().length() < 5 || bairro.trim().length() > 50)) {
            throw new IllegalArgumentException("O bairro deve ter no mínimo 5 letras e no máximo 50 letras");
        }

        if (Objects.nonNull(cidade) && (cidade.trim().length() < 5 || cidade.trim().length() > 50)) {
            throw new IllegalArgumentException("A cidade deve ter no mínimo 5 letras e no máximo 50 letras");
        }

        if (Objects.nonNull(estado) && !estado.matches(REGEX_ESTADO)) {
            throw new IllegalArgumentException("ESTADO INVÁLIDO!");
        }

    }

}
