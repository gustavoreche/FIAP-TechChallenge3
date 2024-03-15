package com.fiap.techchallenge3.repository;

import com.fiap.techchallenge3.model.Restaurante;
import com.fiap.techchallenge3.model.RestauranteId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestauranteRepository extends JpaRepository<Restaurante, RestauranteId> {

}
