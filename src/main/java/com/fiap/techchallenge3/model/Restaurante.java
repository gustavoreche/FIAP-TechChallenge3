package com.fiap.techchallenge3.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tb_restaurante")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Restaurante {

    @EmbeddedId
    private RestauranteId id;
    private String nome;
    @Enumerated(EnumType.STRING)
    private TipoCozinhaEnum tipoCozinha;
    private String diasFuncionamento;
    private String horarioFuncionamento;
    private int capacidadeDePessoas;
}
