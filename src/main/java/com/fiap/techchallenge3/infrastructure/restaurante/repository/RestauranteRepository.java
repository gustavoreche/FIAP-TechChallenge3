package com.fiap.techchallenge3.infrastructure.restaurante.repository;

import com.fiap.techchallenge3.infrastructure.restaurante.model.Restaurante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface RestauranteRepository extends JpaRepository<Restaurante, String>, JpaSpecificationExecutor<Restaurante> {

}
