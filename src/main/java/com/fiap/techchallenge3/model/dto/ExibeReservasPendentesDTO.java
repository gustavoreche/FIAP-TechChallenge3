package com.fiap.techchallenge3.model.dto;

import java.time.LocalDateTime;

public record ExibeReservasPendentesDTO(

		Long id,
		int quantidadeLugaresClienteDeseja,
		LocalDateTime horarioDaReservaRealizada
) {}
