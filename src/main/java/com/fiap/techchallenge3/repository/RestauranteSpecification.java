package com.fiap.techchallenge3.repository;

import com.fiap.techchallenge3.model.Restaurante;
import org.springframework.data.jpa.domain.Specification;

public class RestauranteSpecification {

    public static Specification<Restaurante> nome(String nome) {
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.like(root.get("nome"), "%" + nome + "%");
    }

    public static Specification<Restaurante> cep(String cep) {
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.like(root.get("id").get("cep"), "%" + cep + "%");
    }

    public static Specification<Restaurante> bairro(String bairro) {
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.like(root.get("bairro"), "%" + bairro + "%");
    }

    public static Specification<Restaurante> cidade(String cidade) {
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.like(root.get("cidade"), "%" + cidade + "%");
    }

    public static Specification<Restaurante> estado(String estado) {
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.like(root.get("estado"), "%" + estado + "%");
    }

    public static Specification<Restaurante> tipoCozinha(String tipoCozinha) {
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.like(root.get("tipoCozinha"), "%" + tipoCozinha + "%");
    }

}
