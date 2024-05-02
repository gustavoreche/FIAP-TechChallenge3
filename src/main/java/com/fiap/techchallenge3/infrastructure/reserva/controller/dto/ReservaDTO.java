package com.fiap.techchallenge3.infrastructure.reserva.controller.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

public record ReservaDTO(

		@Schema(example = "2024-12-31")
		@JsonInclude(JsonInclude.Include.NON_NULL)
		LocalDate dia,

		@Schema(example = "11:15")
		@JsonInclude(JsonInclude.Include.NON_NULL)
		String horarioDeChegada,

		@JsonInclude(JsonInclude.Include.NON_NULL)
		int quantidadeLugares,

		@JsonInclude(JsonInclude.Include.NON_NULL)
		String cpfCnpjCliente
) {}
