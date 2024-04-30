package com.fiap.techchallenge3.infrastructure.restaurante.controller.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

public record EnderecoCompletoDTO(

		@JsonInclude(JsonInclude.Include.NON_NULL)
		String logradouro,

		@JsonInclude(JsonInclude.Include.NON_NULL)
		Integer numero,

		@Schema(example = "14012-456")
		@JsonInclude(JsonInclude.Include.NON_NULL)
		String cep,

		@JsonInclude(JsonInclude.Include.NON_NULL)
		String bairro,

		@JsonInclude(JsonInclude.Include.NON_NULL)
		String cidade,

		@JsonInclude(JsonInclude.Include.NON_NULL)
		String estado,

		@JsonInclude(JsonInclude.Include.NON_NULL)
		String complemento
) {}
