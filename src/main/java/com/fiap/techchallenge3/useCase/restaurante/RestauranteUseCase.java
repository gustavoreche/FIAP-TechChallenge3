package com.fiap.techchallenge3.useCase.restaurante;

import com.fiap.techchallenge3.domain.restaurante.TipoCozinhaEnum;
import com.fiap.techchallenge3.infrastructure.restaurante.controller.dto.CriaRestauranteDTO;
import com.fiap.techchallenge3.infrastructure.restaurante.controller.dto.ExibeBuscaRestauranteDTO;

import java.util.List;

public interface RestauranteUseCase {

    void cadastra(CriaRestauranteDTO dadosRestaurante);
    List<ExibeBuscaRestauranteDTO> busca(String nome,
                                         String cep,
                                         String bairro,
                                         String cidade,
                                         String estado,
                                         TipoCozinhaEnum tipoCozinha);

}
