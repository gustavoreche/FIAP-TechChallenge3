package com.fiap.techchallenge3.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fiap.techchallenge3.model.TipoCozinhaEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.br.CNPJ;

public record CriaRestauranteDTO(

		@NotBlank(message = "O CNPJ nao pode ser vazio")
		@CNPJ(message = "CNPJ inválido")
		@Schema(example = "49.251.058/0001-05 OU 49251058000105")
		@JsonInclude(JsonInclude.Include.NON_NULL)
		String cnpj,

		@NotBlank(message = "O nome nao pode ser vazio")
		@Size(min = 3, max = 50, message = "O nome deve ter no mínimo 3 letras e no máximo 50 letras")
		@JsonInclude(JsonInclude.Include.NON_NULL)
		String nome,

		@NotNull(message = "A localizacao nao pode ser vazia")
		@Valid
		@JsonInclude(JsonInclude.Include.NON_NULL)
		EnderecoCompletoDTO localizacao,

		@NotNull(message = "O tipoCozinha nao pode ser vazio")
		@JsonInclude(JsonInclude.Include.NON_NULL)
		TipoCozinhaEnum tipoCozinha,

		@NotNull(message = "O horarioFuncionamento nao pode ser vazia")
		@Valid
		@JsonInclude(JsonInclude.Include.NON_NULL)
		HorarioDeFuncionamentoDTO horarioFuncionamento,

		@NotNull(message = "A capacidadeDePessoas nao pode ser vazio")
		@Min(value = 1, message = "A capacidadeDePessoas tem que ser igual ou maior que 1")
		@Max(value = 5000, message = "A capacidadeDePessoas tem que ser igual ou menor que 5000")
		@JsonInclude(JsonInclude.Include.NON_NULL)
		Integer capacidadeDePessoas
) {}
