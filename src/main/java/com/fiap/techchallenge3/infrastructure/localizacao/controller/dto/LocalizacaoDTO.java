package com.fiap.techchallenge3.infrastructure.localizacao.controller.dto;

import com.fiap.techchallenge3.domain.localizacao.model.Localizacao;

public record LocalizacaoDTO(
        String logradouro,
        String cep,
        String bairro,
        String cidade,
        String estado
) {
    public LocalizacaoDTO(Localizacao localizacao) {
        this(
                localizacao.logradouro(),
                localizacao.cep(),
                localizacao.bairro(),
                localizacao.cidade(),
                localizacao.estado()
        );
    }
}
