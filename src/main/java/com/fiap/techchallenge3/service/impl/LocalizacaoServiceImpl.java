package com.fiap.techchallenge3.service.impl;

import com.fiap.techchallenge3.client.ViaCepClient;
import com.fiap.techchallenge3.model.dto.LocalizacaoDTO;
import com.fiap.techchallenge3.repository.RestauranteRepository;
import com.fiap.techchallenge3.service.LocalizacaoService;
import com.fiap.techchallenge3.service.RestauranteService;
import org.springframework.stereotype.Service;

@Service
public class LocalizacaoServiceImpl implements LocalizacaoService {

    private final ViaCepClient client;

    public LocalizacaoServiceImpl(final ViaCepClient client) {
        this.client = client;
    }

    @Override
    public LocalizacaoDTO buscaPorCep(final String cep) {
        return this.client.getAddressByCep(cep.replace("-", ""));
    }
}
