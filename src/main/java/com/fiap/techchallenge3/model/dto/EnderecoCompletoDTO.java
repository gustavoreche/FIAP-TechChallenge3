package com.fiap.techchallenge3.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import static com.fiap.techchallenge3.infrastructure.localizacao.controller.LocalizacaoController.REGEX_CEP;
import static com.fiap.techchallenge3.infrastructure.restaurante.controller.RestauranteController.REGEX_ESTADO;

public record EnderecoCompletoDTO(

		@NotBlank(message = "O logradouro nao pode ser vazio")
		@Size(min = 5, max = 50, message = "O logradouro deve ter no mínimo 5 letras e no máximo 50 letras")
		@JsonInclude(JsonInclude.Include.NON_NULL)
		String logradouro,

		@NotNull(message = "O numero nao pode ser vazio")
		@JsonInclude(JsonInclude.Include.NON_NULL)
		@Min(value = 1, message = "O numero tem que ser igual ou maior que 1")
		@Max(value = 99999, message = "O numero tem que ser igual ou menor que 99999")
		Integer numero,

		@NotBlank(message = "O cep nao pode ser vazio")
		@Pattern(regexp = REGEX_CEP)
		@Schema(example = "14012-456")
		@JsonInclude(JsonInclude.Include.NON_NULL)
		String cep,

		@NotBlank(message = "O bairro nao pode ser vazio")
		@Size(min = 5, max = 50, message = "O bairro deve ter no mínimo 5 letras e no máximo 50 letras")
		@JsonInclude(JsonInclude.Include.NON_NULL)
		String bairro,

		@NotBlank(message = "A cidade nao pode ser vazia")
		@Size(min = 5, max = 50, message = "A cidade deve ter no mínimo 5 letras e no máximo 50 letras")
		@JsonInclude(JsonInclude.Include.NON_NULL)
		String cidade,

		@NotBlank(message = "O estado nao pode ser vazio")
		@Pattern(regexp = REGEX_ESTADO)
		@JsonInclude(JsonInclude.Include.NON_NULL)
		String estado,

		@Size(min = 2, max = 30, message = "A cidade deve ter no mínimo 2 letras e no máximo 30 letras")
		@JsonInclude(JsonInclude.Include.NON_NULL)
		String complemento
) {}
