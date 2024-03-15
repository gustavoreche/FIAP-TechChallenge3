package com.fiap.techchallenge3.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fiap.techchallenge3.controller.exception.HorarioInvalidoException;
import com.fiap.techchallenge3.model.*;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.br.CNPJ;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

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
) {
    public Restaurante converte() {
		return Restaurante.builder()
				.id(criaId())
				.nome(this.nome)
				.tipoCozinha(this.tipoCozinha)
				.diasFuncionamento(verificaDiasDeFuncionamento())
				.horarioFuncionamento(verificaHorarioDeFuncionamento())
				.capacidadeDePessoas(this.capacidadeDePessoas)
				.build();
    }

	private RestauranteId criaId() {
		return RestauranteId.builder()
				.cnpj(this.cnpj
						.replace(".", "")
						.replace("/", "")
						.replace("-", "")
				)
				.localizacao(this.localizacao.converte())
				.build();
	}

	private String verificaDiasDeFuncionamento() {
		if(this.horarioFuncionamento.diasAbertos().contains(DiasEnum.TODOS)) {
			return List.of(DiasEnum.TODOS).toString();
		}
		return this.horarioFuncionamento.diasAbertos().toString();
	}

	private String verificaHorarioDeFuncionamento() {
		var horarios = this.horarioFuncionamento.horarioFuncionamento().split("ate");
		if(horarios.length == 2) {
			var horarioAbertura = horarios[0].trim();
			var horarioFechamento = horarios[1].trim();
			try {
				var abertura = LocalTime.parse(horarioAbertura);
				var fechamento = LocalTime.parse(horarioFechamento);
				if(abertura.isAfter(fechamento) || abertura.equals(fechamento)) {
					throw new RuntimeException("Horario abertura maior ou igual que horario fechamento");
				}
			} catch (Exception error) {
				throw new HorarioInvalidoException();
			}
		}
		return this.horarioFuncionamento.horarioFuncionamento();
	}

	public RestauranteLocalizacao converteLocalizacao() {
		return this.localizacao.converte();
	}
}
