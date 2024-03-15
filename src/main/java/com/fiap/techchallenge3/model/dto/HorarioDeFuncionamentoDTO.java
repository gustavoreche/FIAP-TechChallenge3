package com.fiap.techchallenge3.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fiap.techchallenge3.model.DiasEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.util.List;

public record HorarioDeFuncionamentoDTO(

		@NotNull(message = "O diasAbertos nao pode ser vazio")
		@NotEmpty(message = "O diasAbertos nao pode ser vazio")
		@JsonInclude(JsonInclude.Include.NON_NULL)
		List<DiasEnum> diasAbertos,

		@NotBlank(message = "O horarioFuncionamento nao pode ser vazio")
		@Pattern(regexp = "^\\d{2}:\\d{2}\\sate\\s\\d{2}:\\d{2}$|^24horas$")
		@JsonInclude(JsonInclude.Include.NON_NULL)
		String horarioFuncionamento
) {}
