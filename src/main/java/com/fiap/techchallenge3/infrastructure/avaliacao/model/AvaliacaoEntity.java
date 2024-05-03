package com.fiap.techchallenge3.infrastructure.avaliacao.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tb_avaliacao")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AvaliacaoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String cnpjRestaurante;
    private String cpfCnpjCliente;
    private String comentario;
    private int nota;

}
