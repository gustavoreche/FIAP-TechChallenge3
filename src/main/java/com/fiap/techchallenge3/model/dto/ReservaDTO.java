package com.fiap.techchallenge3.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fiap.techchallenge3.domain.restaurante.model.DiasEnum;
import com.fiap.techchallenge3.model.Reserva;
import com.fiap.techchallenge3.model.StatusReservaEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record ReservaDTO(

		@NotNull(message = "O dia nao pode ser vazio")
		@JsonInclude(JsonInclude.Include.NON_NULL)
		DiasEnum dia,

		@NotBlank(message = "O horarioDeChegada nao pode ser vazio")
		@JsonInclude(JsonInclude.Include.NON_NULL)
		String horarioDeChegada,

		@JsonInclude(JsonInclude.Include.NON_NULL)
		int quantidadeLugares,

		@NotBlank(message = "A quantidadeLugares nao pode ser vazio")
		@JsonInclude(JsonInclude.Include.NON_NULL)
		String cpfCnpjCliente
) {
	public Reserva converte(String cnpj) {
		return Reserva.builder()
				.cnpjRestaurante(cnpj)
				.cpfCnpjCliente(this.cpfCnpjCliente)
				.dia(this.dia)
				.horarioDeChegada(this.horarioDeChegada)
				.quantidadeLugaresClienteDeseja(this.quantidadeLugares)
				.horarioDaReservaRealizada(LocalDateTime.now())
				.statusReserva(StatusReservaEnum.PENDENTE)
				.build();
	}
}
