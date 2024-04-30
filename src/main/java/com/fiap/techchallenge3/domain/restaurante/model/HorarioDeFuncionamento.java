package com.fiap.techchallenge3.domain.restaurante.model;

import com.fiap.techchallenge3.controller.exception.HorarioInvalidoException;

import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

public record HorarioDeFuncionamento(
		List<DiasEnum> diasAbertos,
		String horarioFuncionamento
) {

	public static final String REGEX_HORARIO_FUNCIONAMENTO = "^\\d{2}:\\d{2}\\sate\\s\\d{2}:\\d{2}$|^24horas$";

	public HorarioDeFuncionamento {
		if (Objects.isNull(diasAbertos) || diasAbertos.isEmpty()) {
			throw new IllegalArgumentException("DIAS ABERTOS NAO PODE SER NULO OU VAZIO!");
		}

		if (Objects.isNull(horarioFuncionamento) || horarioFuncionamento.isEmpty()) {
			throw new IllegalArgumentException("HORARIO DE FUNCIONAMENTO NAO PODE SER NULO OU VAZIO!");
		}
		if (!horarioFuncionamento.matches(REGEX_HORARIO_FUNCIONAMENTO)) {
			throw new IllegalArgumentException("HORARIO DE FUNCIONAMENTO INVÁLIDO!");
		}

		var horarios = horarioFuncionamento.split("ate");
		if(horarios.length == 2) {
			var horarioAbertura = horarios[0].trim();
			var horarioFechamento = horarios[1].trim();
			try {
				var abertura = LocalTime.parse(horarioAbertura);
				var fechamento = LocalTime.parse(horarioFechamento);
				if (abertura.isAfter(fechamento) || abertura.equals(fechamento)) {
					throw new RuntimeException("Horario abertura maior ou igual que horario fechamento");
				}
			} catch (Exception error) {
				throw new HorarioInvalidoException();
			}
		}

		if(diasAbertos.contains(DiasEnum.TODOS)) {
			diasAbertos = List.of(DiasEnum.TODOS);
		}

	}

}
