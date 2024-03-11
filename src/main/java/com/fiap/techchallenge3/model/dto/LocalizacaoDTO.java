package com.fiap.techchallenge3.model.dto;

import com.fasterxml.jackson.annotation.JsonAlias;

public record LocalizacaoDTO(

        String logradouro,
        String cep,
        String bairro,
        @JsonAlias("localidade")
        String cidade,
        @JsonAlias("uf")
        String estado

) {}
