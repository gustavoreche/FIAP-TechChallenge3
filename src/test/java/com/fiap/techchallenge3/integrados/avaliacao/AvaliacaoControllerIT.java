package com.fiap.techchallenge3.integrados.avaliacao;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiap.techchallenge3.domain.restaurante.DiasEnum;
import com.fiap.techchallenge3.domain.restaurante.TipoCozinhaEnum;
import com.fiap.techchallenge3.infrastructure.avaliacao.controller.dto.AvaliacaoDTO;
import com.fiap.techchallenge3.infrastructure.avaliacao.repository.AvaliacaoRepository;
import com.fiap.techchallenge3.infrastructure.restaurante.model.RestauranteEntity;
import com.fiap.techchallenge3.infrastructure.restaurante.repository.RestauranteRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.stream.Stream;

import static com.fiap.techchallenge3.infrastructure.avaliacao.controller.AvaliacaoController.URL_AVALIACAO_POR_CNPJ;

@AutoConfigureMockMvc
@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
class AvaliacaoControllerIT {

    @Autowired
    private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	RestauranteRepository repositoryRestaurante;

	@Autowired
	AvaliacaoRepository repositoryAvaliacao;

	@BeforeEach
	void inicializaLimpezaDoDatabase() {
		this.repositoryRestaurante.deleteAll();
		this.repositoryAvaliacao.deleteAll();
	}

	@AfterAll
	void finalizaLimpezaDoDatabase() {
		this.repositoryRestaurante.deleteAll();
		this.repositoryAvaliacao.deleteAll();
	}

	@Test
	public void avaliacao_deveRetornar201_salvaNaBaseDeDados() throws Exception {
		var restaurante1 = RestauranteEntity.builder()
				.cnpj("49251058000123")
				.nome("Restaurante Teste A")
				.tipoCozinha(TipoCozinhaEnum.COMIDA_ARABE)
				.diasFuncionamento(List.of(DiasEnum.TODOS).toString())
				.horarioFuncionamento("24horas")
				.capacidadeDePessoas(500)
				.cep("14000000")
				.logradouro("rua teste")
				.numeroEndereco(10)
				.bairro("bairro teste A")
				.cidade("cidade teste A")
				.estado("SP")
				.build();
		this.repositoryRestaurante.save(restaurante1);

		var request = new AvaliacaoDTO(
				"Restaurante excelente",
				10
		);
		var objectMapper = this.objectMapper
				.writer()
				.withDefaultPrettyPrinter();
		var jsonRequest = objectMapper.writeValueAsString(request);

		this.mockMvc
				.perform(MockMvcRequestBuilders.post(URL_AVALIACAO_POR_CNPJ.replace("{cnpjRestaurante}", "49251058000123").replace("{cpfCnpjCliente}", "12345678901"))
						.content(jsonRequest)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers
						.status()
						.isCreated()
				);

		var avaliacao = this.repositoryAvaliacao.findAll().get(0);

		Assertions.assertEquals(1, this.repositoryAvaliacao.findAll().size());
		Assertions.assertEquals("49251058000123", avaliacao.getCnpjRestaurante());
		Assertions.assertEquals("12345678901", avaliacao.getCpfCnpjCliente());
		Assertions.assertEquals("Restaurante excelente", avaliacao.getComentario());
		Assertions.assertEquals(10, avaliacao.getNota());
	}

	@Test
	public void atualizacao_deveRetornar500_restauranteNaoEncontrado_naoSalvaNaBaseDeDados() throws Exception {
		var restaurante1 = RestauranteEntity.builder()
				.cnpj("49251058000123")
				.nome("Restaurante Teste A")
				.tipoCozinha(TipoCozinhaEnum.COMIDA_ARABE)
				.diasFuncionamento(List.of(DiasEnum.TODOS).toString())
				.horarioFuncionamento("24horas")
				.capacidadeDePessoas(500)
				.cep("14000000")
				.logradouro("rua teste")
				.numeroEndereco(10)
				.bairro("bairro teste A")
				.cidade("cidade teste A")
				.estado("SP")
				.build();
		this.repositoryRestaurante.save(restaurante1);

		var request = new AvaliacaoDTO(
				"Restaurante excelente",
				10
		);
		var objectMapper = this.objectMapper
				.writer()
				.withDefaultPrettyPrinter();
		var jsonRequest = objectMapper.writeValueAsString(request);

		this.mockMvc
				.perform(MockMvcRequestBuilders.post(URL_AVALIACAO_POR_CNPJ.replace("{cnpjRestaurante}", "49251058000555").replace("{cpfCnpjCliente}", "12345678901"))
						.content(jsonRequest)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers
						.status()
						.isInternalServerError()
				);

		Assertions.assertEquals(0, this.repositoryAvaliacao.findAll().size());
	}

	@ParameterizedTest
	@MethodSource("requestValidandoCampos")
	public void avaliacao_deveRetornar400_camposInvalidos_naoSalvaNaBaseDeDados(String cnpjRestaurante,
																				String cpfCnpjCliente,
																				String comentario,
																				Integer nota) throws Exception {
		var request = new AvaliacaoDTO(
				comentario,
				nota
		);
		var objectMapper = this.objectMapper
				.writer()
				.withDefaultPrettyPrinter();
		var jsonRequest = objectMapper.writeValueAsString(request);

		this.mockMvc
				.perform(MockMvcRequestBuilders.post(URL_AVALIACAO_POR_CNPJ.replace("{cnpjRestaurante}", cnpjRestaurante).replace("{cpfCnpjCliente}", cpfCnpjCliente))
						.content(jsonRequest)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers
						.status()
						.isBadRequest()
				);
		Assertions.assertEquals(0, this.repositoryAvaliacao.findAll().size());
	}

	private static Stream<Arguments> requestValidandoCampos() {
		return Stream.of(
				Arguments.of("04623021000114", "11122233344", null, 10),
				Arguments.of("04623021000114", "11122233344", "", 10),
				Arguments.of("04623021000114", "11122233344", "Restaurante top", null),

				Arguments.of("aa", "11122233344", "Restaurante top", 10),
				Arguments.of("04623021000114", "aa", "Restaurante top", 10),
				Arguments.of("04623021000114", "11122233344", "aa", 10),
				Arguments.of("04623021000114", "11122233344", "aaaaaaaaaabbbbbbbbbbaaaaaaaaaabbbbbbbbbbaaaaaaaaaabbbbbbbbbbaaaaaaaaaabbbbbbbbbbaaaaaaaaaabbbbbbbbbbcc", 10),
				Arguments.of("04623021000114", "11122233344", "Restaurante top", 0),
				Arguments.of("04623021000114", "11122233344", "Restaurante top", 11)
		);
	}

}
