package com.fiap.techchallenge3.useCase.localizacao.impl;

import com.fiap.techchallenge3.domain.localizacao.model.Cep;
import com.fiap.techchallenge3.domain.localizacao.model.Localizacao;
import com.fiap.techchallenge3.domain.localizacao.service.LocalizacaoAdapter;
import com.fiap.techchallenge3.useCase.localizacao.LocalizacaoUseCase;
import org.springframework.stereotype.Service;

@Service
public class LocalizacaoUseCaseImpl implements LocalizacaoUseCase {

    private final LocalizacaoAdapter adapter;

    public LocalizacaoUseCaseImpl(final LocalizacaoAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public Localizacao buscaPorCep(final String cep) {
        var cepObject = new Cep(cep);
        var viaCepResponse = this.adapter.pegaLocalizacao(cep.replace("-", ""));
        return new Localizacao(
                viaCepResponse.logradouro(),
                cepObject,
                viaCepResponse.bairro(),
                viaCepResponse.cidade(),
                viaCepResponse.estado()
        );
    }
}
