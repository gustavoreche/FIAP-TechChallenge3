package com.fiap.techchallenge3.useCase.localizacao;

import com.fiap.techchallenge3.domain.localizacao.model.Localizacao;

public interface LocalizacaoUseCase {

    Localizacao buscaPorCep(final String cep);

}
