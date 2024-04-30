package com.fiap.techchallenge3.infrastructure.localizacao.controller;

import com.fiap.techchallenge3.infrastructure.localizacao.controller.dto.LocalizacaoDTO;
import com.fiap.techchallenge3.useCase.localizacao.LocalizacaoUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.fiap.techchallenge3.infrastructure.localizacao.controller.LocalizacaoController.URL_LOCALIZACAO;

@Tag(
		name = "Localização",
		description = "Serviço para buscar localização do restaurante"
)
@RestController
@RequestMapping(URL_LOCALIZACAO)
public class LocalizacaoController {

	public static final String REGEX_CEP = "^\\d{5}-\\d{3}$";

	public static final String URL_LOCALIZACAO = "/localizacao";
	public static final String URL_LOCALIZACAO_POR_CEP = URL_LOCALIZACAO.concat("/{cep}");

	private final LocalizacaoUseCase localizacaoUseCase;

	public LocalizacaoController(final LocalizacaoUseCase localizacaoUseCase) {
		this.localizacaoUseCase = localizacaoUseCase;
	}

	@Operation(
			summary = "Serviço para buscar dados do endereço do restaurante, baseado no CEP."
	)
	@GetMapping("/{cep}")
	public ResponseEntity<LocalizacaoDTO> buscaPorCep(@PathVariable final String cep) {
		return ResponseEntity
				.status(HttpStatus.OK)
				.body(
						new LocalizacaoDTO(this.localizacaoUseCase.buscaPorCep(cep))
				);
	}

}
