package com.fiap.techchallenge3.service;

import com.fiap.techchallenge3.model.dto.LocalizacaoDTO;

public interface LocalizacaoService {

    LocalizacaoDTO buscaPorCep(final String cep);

}
