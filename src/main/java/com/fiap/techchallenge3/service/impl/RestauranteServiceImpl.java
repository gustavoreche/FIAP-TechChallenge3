package com.fiap.techchallenge3.service.impl;

import com.fiap.techchallenge3.model.dto.CriaRestauranteDTO;
import com.fiap.techchallenge3.repository.RestauranteLocalizacaoRepository;
import com.fiap.techchallenge3.repository.RestauranteRepository;
import com.fiap.techchallenge3.service.RestauranteService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class RestauranteServiceImpl implements RestauranteService {

    private final RestauranteRepository repositoryRestaurante;
    private final RestauranteLocalizacaoRepository repositoryRestauranteLocalizacao;

    public RestauranteServiceImpl(final RestauranteRepository repositoryRestaurante,
                                  final RestauranteLocalizacaoRepository repositoryRestauranteLocalizacao) {
        this.repositoryRestaurante = repositoryRestaurante;
        this.repositoryRestauranteLocalizacao = repositoryRestauranteLocalizacao;
    }


    @Override
    @Transactional
    public void cadastra(final CriaRestauranteDTO dadosRestaurante) {
        this.repositoryRestauranteLocalizacao.save(dadosRestaurante.converteLocalizacao());
        this.repositoryRestaurante.save(dadosRestaurante.converte());
    }

}
