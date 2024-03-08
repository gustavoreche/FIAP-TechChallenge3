package com.fiap.techchallenge3.controller;

import com.fiap.techchallenge3.service.RestauranteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.fiap.techchallenge3.controller.RestauranteController.URL_RESTAURANTE;

@Tag(
		name = "Restaurante",
		description = "Serviço para cadastrar e buscar restaurantes no sistema"
)
@RestController
@RequestMapping(URL_RESTAURANTE)
public class RestauranteController {

	public static final String URL_RESTAURANTE = "/restaurante";

	private final RestauranteService service;

	public RestauranteController(final RestauranteService service) {
		this.service = service;
	}

	@Operation(
			summary = "Serviço para cadastrar restaurante."
	)
	@PostMapping
	public ResponseEntity<String> cadastra(@RequestBody @Valid final String teste) {
		return ResponseEntity
				.status(HttpStatus.CREATED)
				.body(this.service.cadastra());
	}

}
