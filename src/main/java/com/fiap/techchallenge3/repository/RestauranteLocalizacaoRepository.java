package com.fiap.techchallenge3.repository;

import com.fiap.techchallenge3.model.Restaurante;
import com.fiap.techchallenge3.model.RestauranteId;
import com.fiap.techchallenge3.model.RestauranteLocalizacao;
import com.fiap.techchallenge3.model.RestauranteLocalizacaoId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestauranteLocalizacaoRepository extends JpaRepository<RestauranteLocalizacao, RestauranteLocalizacaoId> {

}
