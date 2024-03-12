package com.fiap.techchallenge3.service.impl;

import com.fiap.techchallenge3.model.dto.CriaRestauranteDTO;
import com.fiap.techchallenge3.repository.RestauranteRepository;
import com.fiap.techchallenge3.service.RestauranteService;
import org.springframework.stereotype.Service;

@Service
public class RestauranteServiceImpl implements RestauranteService {

    private final RestauranteRepository repository;

    public RestauranteServiceImpl(final RestauranteRepository repository) {
        this.repository = repository;
    }


    @Override
    public String cadastra(final CriaRestauranteDTO dadosRestaurante) {
        return "HELLO WORLD COM DOCKER";
    }

}
