package com.fiap.techchallenge3.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fiap.techchallenge3.model.RestauranteLocalizacao;
import com.fiap.techchallenge3.model.RestauranteLocalizacaoId;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import static com.fiap.techchallenge3.controller.LocalizacaoController.REGEX_CEP;

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
		@Pattern(regexp = "^\\w{2}$")
		@JsonInclude(JsonInclude.Include.NON_NULL)
		String estado,

		@Size(min = 2, max = 30, message = "A cidade deve ter no mínimo 2 letras e no máximo 30 letras")
		@JsonInclude(JsonInclude.Include.NON_NULL)
		String complemento
) {
    public RestauranteLocalizacao converte() {
		return RestauranteLocalizacao.builder()
				.id(criaId())
				.bairro(this.bairro)
				.cidade(this.cidade)
				.estado(this.estado)
				.complemento(this.complemento)
				.build();
    }

	private RestauranteLocalizacaoId criaId() {
		return RestauranteLocalizacaoId.builder()
				.logradouro(this.logradouro)
				.numero(this.numero)
				.cep(this.cep.replace("-", ""))
				.build();
	}
}
