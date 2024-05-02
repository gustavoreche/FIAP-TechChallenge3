package com.fiap.techchallenge3.infrastructure.reserva.controller.dto;

import java.time.LocalDateTime;

public record ExibeReservasPendentesDTO(

		Long id,
		int quantidadeLugaresClienteDeseja,
		LocalDateTime horarioDaReservaRealizada
) {}
