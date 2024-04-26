package com.fiap.techchallenge3.domain.localizacao.model;

import java.util.Objects;

public record Localizacao(
        String logradouro,
        String cep,
        String bairro,
        String cidade,
        String estado
) {
    public boolean naoExiste() {
        return Objects.isNull(this.logradouro());
    }
}
