package com.fiap.techchallenge3.repository;

import com.fiap.techchallenge3.model.Restaurante;
import com.fiap.techchallenge3.model.RestauranteId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface RestauranteRepository extends JpaRepository<Restaurante, RestauranteId>, JpaSpecificationExecutor<Restaurante> {

}
