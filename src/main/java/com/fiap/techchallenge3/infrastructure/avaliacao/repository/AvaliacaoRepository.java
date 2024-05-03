package com.fiap.techchallenge3.infrastructure.avaliacao.repository;

import com.fiap.techchallenge3.infrastructure.avaliacao.model.AvaliacaoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AvaliacaoRepository extends JpaRepository<AvaliacaoEntity, Long> {

}
