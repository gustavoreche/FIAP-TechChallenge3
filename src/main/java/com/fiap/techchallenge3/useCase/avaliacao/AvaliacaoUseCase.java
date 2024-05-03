package com.fiap.techchallenge3.useCase.avaliacao;

import com.fiap.techchallenge3.infrastructure.avaliacao.controller.dto.AvaliacaoDTO;

public interface AvaliacaoUseCase {

    void avalia(final String cnpjRestaurante,
                final String cpfCnpjCliente,
                final AvaliacaoDTO avaliacaoDTO);

}
