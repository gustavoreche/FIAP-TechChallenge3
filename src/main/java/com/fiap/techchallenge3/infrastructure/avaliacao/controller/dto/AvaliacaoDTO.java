package com.fiap.techchallenge3.infrastructure.avaliacao.controller.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

public record AvaliacaoDTO(

		@JsonInclude(JsonInclude.Include.NON_NULL)
		String comentario,

		@JsonInclude(JsonInclude.Include.NON_NULL)
		Integer nota
) {}
