package com.fiap.techchallenge3.domain.localizacao.service;

import com.fiap.techchallenge3.infrastructure.localizacao.client.ViaCepResponse;

public interface LocalizacaoAdapter {

    ViaCepResponse pegaLocalizacao(String cep);

}
