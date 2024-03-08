package com.fiap.techchallenge3.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fiap.techchallenge3.model.TipoCozinhaEnum;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.br.CNPJ;

public record CriaRestauranteDTO(

		@CNPJ(message = "Erro no CNPJ")
		@JsonInclude(JsonInclude.Include.NON_NULL)
		String cnpj,

		@NotBlank(message = "O nome nao pode ser vazio")
		@Size(min = 5, max = 50, message = "O nome deve ter no mínimo 3 letras e no máximo 50 letras")
		@JsonInclude(JsonInclude.Include.NON_NULL)
		String nome,

		@NotNull(message = "O tipoCozinha nao pode ser vazio")
		@JsonInclude(JsonInclude.Include.NON_NULL)
		TipoCozinhaEnum tipoCozinha,

		@Parameter(example = "2024-01-25T21:48:54")
		@NotNull(message = "O tipoCozinha nao pode ser vazio")
		@JsonInclude(JsonInclude.Include.NON_NULL)
		TipoCozinhaEnum horarioAbertura
) {}
