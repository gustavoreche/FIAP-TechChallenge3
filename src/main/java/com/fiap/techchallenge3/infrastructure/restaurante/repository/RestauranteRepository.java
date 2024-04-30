package com.fiap.techchallenge3.infrastructure.restaurante.repository;

import com.fiap.techchallenge3.infrastructure.restaurante.model.RestauranteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface RestauranteRepository extends JpaRepository<RestauranteEntity, String>, JpaSpecificationExecutor<RestauranteEntity>{

}
