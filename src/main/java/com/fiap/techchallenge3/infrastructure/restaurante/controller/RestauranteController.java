package com.fiap.techchallenge3.infrastructure.restaurante.controller;

import com.fiap.techchallenge3.infrastructure.restaurante.model.TipoCozinhaEnum;
import com.fiap.techchallenge3.model.dto.CriaRestauranteDTO;
import com.fiap.techchallenge3.model.dto.ExibeBuscaRestauranteDTO;
import com.fiap.techchallenge3.useCase.restaurante.RestauranteUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.fiap.techchallenge3.infrastructure.localizacao.controller.LocalizacaoController.REGEX_CEP;
import static com.fiap.techchallenge3.infrastructure.restaurante.controller.RestauranteController.URL_RESTAURANTE;

@Tag(
		name = "Restaurante",
		description = "Serviço para cadastrar e buscar restaurantes no sistema"
)
@RestController
@RequestMapping(URL_RESTAURANTE)
public class RestauranteController {

	public static final String REGEX_ESTADO = "^\\w{2}$";
	public static final String URL_RESTAURANTE = "/restaurante";

	private final RestauranteUseCase service;

	public RestauranteController(final RestauranteUseCase service) {
		this.service = service;
	}

	@Operation(
			summary = "Serviço para cadastrar restaurante.",
			description = """
					DICA: Para não digitar toda a LOCALIZAÇÃO completa, o endpoint "/localizacao/{cep}"
					fornece a maioria dos dados que são obrigatórios, tendo que ser preenchido somente
					os campos abaixo:
					
						- número (número do endereço)
						- complemento (opcional, se não tiver, não preencher).
					"""
	)
	@PostMapping
	public ResponseEntity<Void> cadastra(@RequestBody @Valid final CriaRestauranteDTO dadosRestaurante) {
		this.service.cadastra(dadosRestaurante);
		return ResponseEntity
				.status(HttpStatus.CREATED)
				.build();
	}

	@Operation(
			summary = """
					Serviço que busca os restaurantes. Veja os detalhes:
					""",
			description = """
					Busque as informações pelos seguintes parâmetros:
					
						- nome (se não passar um nome, pesquisa por todos os restaurantes)
						- localização (esse parâmetro contêm 4 itens, podendo ser informado, apenas um, dois, todos, ou nenhum. Se não passar algum parâmetro da localização, esse filtro não será levado em consideração)
						- tipo de cozinha (se não passar um tipo de cozinha, pesquisa por todos os tipos de cozinha)
						
					Observação: Esse serviço irá trazer somente os 50 primeiros restaurantes encontrados, baseado nos filtros informados.
					"""
	)
	@GetMapping
	public ResponseEntity<List<ExibeBuscaRestauranteDTO>> busca(@RequestParam(required = false) final String nome,
																@RequestParam(required = false) @Schema(example = "14012-456") @Pattern(regexp = REGEX_CEP) final String cep,
																@RequestParam(required = false) final String bairro,
																@RequestParam(required = false) final String cidade,
																@RequestParam(required = false) @Pattern(regexp = REGEX_ESTADO) final String estado,
																@RequestParam(required = false) final TipoCozinhaEnum tipoCozinha) {
		var busca = this.service.busca(
				nome,
				cep,
				bairro,
				cidade,
				estado,
				tipoCozinha
		);
		var status = busca.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK;
		return ResponseEntity
				.status(status)
				.body(busca);
	}

}
