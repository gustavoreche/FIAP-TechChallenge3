package com.fiap.techchallenge3.model;

import com.fiap.techchallenge3.domain.restaurante.model.DiasEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_reserva")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String cnpjRestaurante;
    private String cpfCnpjCliente;
    @Enumerated(EnumType.STRING)
    private DiasEnum dia;
    private String horarioDeChegada;
    private int quantidadeLugaresClienteDeseja;
    private LocalDateTime horarioDaReservaRealizada;
    @Enumerated(EnumType.STRING)
    private StatusReservaEnum statusReserva;

}
