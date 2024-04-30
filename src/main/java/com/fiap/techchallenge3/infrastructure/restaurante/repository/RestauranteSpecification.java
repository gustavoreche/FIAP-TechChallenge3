package com.fiap.techchallenge3.infrastructure.restaurante.repository;

import com.fiap.techchallenge3.infrastructure.restaurante.model.RestauranteEntity;
import org.springframework.data.jpa.domain.Specification;

public class RestauranteSpecification {

    public static Specification<RestauranteEntity> nome(String nome) {
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.like(root.get("nome"), "%" + nome + "%");
    }

    public static Specification<RestauranteEntity> cep(String cep) {
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.like(root.get("cep"), "%" + cep + "%");
    }

    public static Specification<RestauranteEntity> bairro(String bairro) {
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.like(root.get("bairro"), "%" + bairro + "%");
    }

    public static Specification<RestauranteEntity> cidade(String cidade) {
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.like(root.get("cidade"), "%" + cidade + "%");
    }

    public static Specification<RestauranteEntity> estado(String estado) {
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.like(root.get("estado"), "%" + estado + "%");
    }

    public static Specification<RestauranteEntity> tipoCozinha(String tipoCozinha) {
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.like(root.get("tipoCozinha"), "%" + tipoCozinha + "%");
    }

}
