package com.fiap.techchallenge3.model;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tb_restaurante_localizacao")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RestauranteLocalizacao {

    @EmbeddedId
    private RestauranteLocalizacaoId id;
    private String bairro;
    private String cidade;
    private String estado;
    private String complemento;
}
