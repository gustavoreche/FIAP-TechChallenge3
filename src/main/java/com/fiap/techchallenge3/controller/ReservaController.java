package com.fiap.techchallenge3.controller;

import com.fiap.techchallenge3.model.StatusReservaEnum;
import com.fiap.techchallenge3.model.dto.ExibeReservasPendentesDTO;
import com.fiap.techchallenge3.model.dto.ReservaDTO;
import com.fiap.techchallenge3.service.ReservaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.fiap.techchallenge3.controller.ReservaController.URL_RESERVA;

@Tag(
		name = "Reserva",
		description = "Serviço para reservar mesas no restaurante desejado"
)
@RestController
@RequestMapping(URL_RESERVA)
public class ReservaController {

	public static final String URL_RESERVA = "/reserva";

	private final ReservaService service;

	public ReservaController(final ReservaService service) {
		this.service = service;
	}

	@Operation(
			summary = "Serviço para atualizar o status da reserva no restaurante."
	)
	@PostMapping("/{cnpj}")
	public ResponseEntity<Void> reserva(@PathVariable("cnpj") final String cnpj, @RequestBody @Valid final ReservaDTO dadosReserva) {
		this.service.reserva(cnpj, dadosReserva);
		return ResponseEntity
				.status(HttpStatus.CREATED)
				.build();
	}

	@Operation(
			summary = "Serviço para realizar reserva no restaurante."
	)
	@PutMapping("/atualiza/{cnpj}")
	public ResponseEntity<Void> atualizaReserva(@PathVariable("cnpj") final String cnpj, @RequestParam @Valid final StatusReservaEnum status) {
		this.service.atualizaReserva(cnpj, status);
		return ResponseEntity
				.status(HttpStatus.OK)
				.build();
	}

//	@Operation(
//			summary = "Serviço para listar reservas PENDENTES do dia no restaurante."
//	)
//	@GetMapping("/{cnpj}")
//	public ResponseEntity<ExibeReservasPendentesDTO> buscaReservasPendentesDoDia(@PathVariable("cnpj") final String cnpj) {
//		return ResponseEntity
//				.status(HttpStatus.OK)
//				.build(this.service.buscaReservasPendentesDoDia(cnpj));
//	}

}
