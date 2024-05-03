package com.fiap.techchallenge3.infrastructure.avaliacao.controller;

import com.fiap.techchallenge3.infrastructure.avaliacao.controller.dto.AvaliacaoDTO;
import com.fiap.techchallenge3.useCase.avaliacao.AvaliacaoUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.fiap.techchallenge3.infrastructure.avaliacao.controller.AvaliacaoController.URL_AVALIACAO;

@Tag(
		name = "Avaliação",
		description = "Serviço para realizar a avaliação da visita ao restaurante"
)
@RestController
@RequestMapping(URL_AVALIACAO)
public class AvaliacaoController {

	public static final String URL_AVALIACAO = "/avaliacao";
	public static final String URL_AVALIACAO_POR_CNPJ = URL_AVALIACAO.concat("/{cnpjRestaurante}/{cpfCnpjCliente}");
	private final AvaliacaoUseCase service;

	public AvaliacaoController(final AvaliacaoUseCase service) {
		this.service = service;
	}

	@Operation(
			summary = "Serviço para realizar a avaliação do restaurante."
	)
	@PostMapping("/{cnpjRestaurante}/{cpfCnpjCliente}")
	public ResponseEntity<Void> avalia(@PathVariable("cnpjRestaurante") final String cnpjRestaurante,
									   @PathVariable("cpfCnpjCliente") final String cpfCnpjCliente,
									   @RequestBody @Valid final AvaliacaoDTO avaliacaoDTO) {
		this.service.avalia(cnpjRestaurante, cpfCnpjCliente, avaliacaoDTO);
		return ResponseEntity
				.status(HttpStatus.CREATED)
				.build();
	}

}
