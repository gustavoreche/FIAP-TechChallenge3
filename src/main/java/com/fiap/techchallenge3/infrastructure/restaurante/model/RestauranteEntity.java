package com.fiap.techchallenge3.infrastructure.restaurante.model;

import com.fiap.techchallenge3.domain.restaurante.TipoCozinhaEnum;
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
public class RestauranteEntity {

    @Id
    private String cnpj;
    private String nome;
    @Enumerated(EnumType.STRING)
    private TipoCozinhaEnum tipoCozinha;
    private String diasFuncionamento;
    private String horarioFuncionamento;
    private int capacidadeDePessoas;
    private String cep;
    private String logradouro;
    private int numeroEndereco;
    private String bairro;
    private String cidade;
    private String estado;
    private String complemento;
}
