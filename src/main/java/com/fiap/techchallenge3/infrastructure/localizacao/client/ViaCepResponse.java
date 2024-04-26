package com.fiap.techchallenge3.infrastructure.localizacao.client;

import com.fasterxml.jackson.annotation.JsonAlias;

public record ViaCepResponse(

        String logradouro,
        String cep,
        String bairro,
        @JsonAlias("localidade")
        String cidade,
        @JsonAlias("uf")
        String estado

) {}
