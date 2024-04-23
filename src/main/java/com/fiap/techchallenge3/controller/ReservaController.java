package com.fiap.techchallenge3.controller;

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
			summary = "Serviço para realizar reserva no restaurante.",
			description = """
					DICA: Para não digitar toda a LOCALIZAÇÃO completa, o endpoint "/localizacao/{cep}"
					fornece a maioria dos dados que são obrigatórios, tendo que ser preenchido somente
					os campos abaixo:
					
						- número (número do endereço)
						- complemento (opcional, se não tiver, não preencher).
					"""
	)
	@PostMapping("/{cnpj}")
	public ResponseEntity<Void> reserva(@PathVariable("cnpj") final String cnpj, @RequestBody @Valid final ReservaDTO dadosReserva) {
		this.service.reserva(cnpj, dadosReserva);
		return ResponseEntity
				.status(HttpStatus.CREATED)
				.build();
	}

//	@Operation(
//			summary = """
//					Serviço que busca os restaurantes. Veja os detalhes:
//					""",
//			description = """
//					Busque as informações pelos seguintes parâmetros:
//
//						- nome (se não passar um nome, pesquisa por todos os restaurantes)
//						- localização (esse parâmetro contêm 4 itens, podendo ser informado, apenas um, dois, todos, ou nenhum. Se não passar algum parâmetro da localização, esse filtro não será levado em consideração)
//						- tipo de cozinha (se não passar um tipo de cozinha, pesquisa por todos os tipos de cozinha)
//
//					Observação: Esse serviço irá trazer somente os 50 primeiros restaurantes encontrados, baseado nos filtros informados.
//					"""
//	)
//	@GetMapping
//	public ResponseEntity<List<ExibeBuscaRestauranteDTO>> busca(@RequestParam(required = false) final String nome,
//																@RequestParam(required = false) @Schema(example = "14012-456") @Pattern(regexp = REGEX_CEP) final String cep,
//																@RequestParam(required = false) final String bairro,
//																@RequestParam(required = false) final String cidade,
//																@RequestParam(required = false) @Pattern(regexp = REGEX_ESTADO) final String estado,
//																@RequestParam(required = false) final TipoCozinhaEnum tipoCozinha) {
//		var busca = this.service.busca(
//				nome,
//				cep,
//				bairro,
//				cidade,
//				estado,
//				tipoCozinha
//		);
//		var status = busca.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK;
//		return ResponseEntity
//				.status(status)
//				.body(busca);
//	}

}
