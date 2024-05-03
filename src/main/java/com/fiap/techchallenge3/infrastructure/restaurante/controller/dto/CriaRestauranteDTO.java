package com.fiap.techchallenge3.infrastructure.restaurante.controller.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fiap.techchallenge3.domain.restaurante.TipoCozinhaEnum;
import io.swagger.v3.oas.annotations.media.Schema;

public record CriaRestauranteDTO(

		@Schema(example = "49.251.058/0001-05 OU 49251058000105")
		@JsonInclude(JsonInclude.Include.NON_NULL)
		String cnpj,

		@JsonInclude(JsonInclude.Include.NON_NULL)
		String nome,

		@JsonInclude(JsonInclude.Include.NON_NULL)
		EnderecoCompletoDTO localizacao,

		@JsonInclude(JsonInclude.Include.NON_NULL)
		TipoCozinhaEnum tipoCozinha,

		@JsonInclude(JsonInclude.Include.NON_NULL)
		HorarioDeFuncionamentoDTO horarioFuncionamento,

		@JsonInclude(JsonInclude.Include.NON_NULL)
		Integer capacidadeDePessoas
) {}
