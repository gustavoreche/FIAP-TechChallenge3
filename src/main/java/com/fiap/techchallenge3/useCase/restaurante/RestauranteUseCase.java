package com.fiap.techchallenge3.useCase.restaurante;

import com.fiap.techchallenge3.infrastructure.restaurante.model.TipoCozinhaEnum;
import com.fiap.techchallenge3.model.dto.CriaRestauranteDTO;
import com.fiap.techchallenge3.model.dto.ExibeBuscaRestauranteDTO;

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