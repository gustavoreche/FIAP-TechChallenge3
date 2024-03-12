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
		@NotBlank(message = "O estado nao pode ser vazio")
		@Pattern(regexp = "^\\w{2}$")
		@JsonInclude(JsonInclude.Include.NON_NULL)
		String estado
) {}
