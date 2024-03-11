package com.fiap.techchallenge3.service.impl;

import com.fiap.techchallenge3.client.ViaCepClient;
import com.fiap.techchallenge3.model.dto.LocalizacaoDTO;
import com.fiap.techchallenge3.service.LocalizacaoService;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class LocalizacaoServiceImpl implements LocalizacaoService {

    private final ViaCepClient client;

    public LocalizacaoServiceImpl(final ViaCepClient client) {
        this.client = client;
    }

    @Override
    public LocalizacaoDTO buscaPorCep(final String cep) {
        var endereco = this.client.getAddressByCep(cep.replace("-", ""));
        if(Objects.isNull(endereco.logradouro())) {
            throw new RuntimeException("ENDEREÇO NAO EXISTE!");
        }
        return endereco;
    }
}
