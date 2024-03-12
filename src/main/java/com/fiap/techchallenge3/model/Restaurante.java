package com.fiap.techchallenge3.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_restaurante")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Restaurante {

    @Id
    private String cnpj;
    private String nome;
    @OneToOne
    private RestauranteLocalizacao localizacao;
    private TipoCozinhaEnum tipoCozinha;
    private LocalDateTime horarioAbertura;
    private LocalDateTime horarioFechamento;
    private int capacidadeDePessoas;
}
