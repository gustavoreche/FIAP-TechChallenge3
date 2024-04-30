package com.fiap.techchallenge3.infrastructure.restaurante.controller.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fiap.techchallenge3.domain.restaurante.model.DiasEnum;

import java.util.List;

public record HorarioDeFuncionamentoDTO(

		@JsonInclude(JsonInclude.Include.NON_NULL)
		List<DiasEnum> diasAbertos,

		@JsonInclude(JsonInclude.Include.NON_NULL)
		String horarioFuncionamento
) {}
