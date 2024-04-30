package com.fiap.techchallenge3.integrados.restaurante;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiap.techchallenge3.domain.restaurante.model.DiasEnum;
import com.fiap.techchallenge3.domain.restaurante.model.TipoCozinhaEnum;
import com.fiap.techchallenge3.infrastructure.restaurante.controller.dto.CriaRestauranteDTO;
import com.fiap.techchallenge3.infrastructure.restaurante.controller.dto.EnderecoCompletoDTO;
import com.fiap.techchallenge3.infrastructure.restaurante.controller.dto.HorarioDeFuncionamentoDTO;
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

import static com.fiap.techchallenge3.infrastructure.restaurante.controller.RestauranteController.URL_RESTAURANTE;
import static com.fiap.techchallenge3.utils.RestauranteUtils.*;

@AutoConfigureMockMvc
@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
class RestauranteControllerIT {

    @Autowired
    private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	RestauranteRepository repositoryRestaurante;

	@BeforeEach
	void inicializaLimpezaDoDatabase() {
		this.repositoryRestaurante.deleteAll();
	}

	@AfterAll
	void finalizaLimpezaDoDatabase() {
		this.repositoryRestaurante.deleteAll();
	}


	@Test
	public void restaurante_deveRetornar201_salvaNaBaseDeDados() throws Exception {
		var request = new CriaRestauranteDTO(
				"49.251.058/0001-05",
				"Restaurante Teste",
				localizacaoDefault(),
				TipoCozinhaEnum.COMIDA_ARABE,
				horarioFuncionamentoDefault(),
				500
		);
		var objectMapper = this.objectMapper
				.writer()
				.withDefaultPrettyPrinter();
		var jsonRequest = objectMapper.writeValueAsString(request);

		this.mockMvc
				.perform(MockMvcRequestBuilders.post(URL_RESTAURANTE)
						.content(jsonRequest)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers
						.status()
						.isCreated()
				);

		var restaurante = this.repositoryRestaurante.findAll().get(0);

		Assertions.assertEquals(1, this.repositoryRestaurante.findAll().size());
		Assertions.assertEquals("rua teste", restaurante.getLogradouro());
		Assertions.assertEquals(10, restaurante.getNumeroEndereco());
		Assertions.assertEquals("14000000", restaurante.getCep());
		Assertions.assertEquals("bairro teste", restaurante.getBairro());
		Assertions.assertEquals("cidade teste", restaurante.getCidade());
		Assertions.assertEquals("SP", restaurante.getEstado());
		Assertions.assertEquals("ap1122", restaurante.getComplemento());
		Assertions.assertEquals("49251058000105", restaurante.getCnpj());
		Assertions.assertEquals(TipoCozinhaEnum.COMIDA_ARABE, restaurante.getTipoCozinha());
		Assertions.assertEquals(List.of(DiasEnum.TODOS).toString(), restaurante.getDiasFuncionamento());
		Assertions.assertEquals("18:00 ate 23:00", restaurante.getHorarioFuncionamento());
		Assertions.assertEquals(500, restaurante.getCapacidadeDePessoas());
	}

	@Test
	public void restaurante_deveRetornar201_funcionamento24horas_salvaNaBaseDeDados() throws Exception {
		var request = new CriaRestauranteDTO(
				"49.251.058/0001-05",
				"Restaurante Teste",
				localizacaoDefault(),
				TipoCozinhaEnum.COMIDA_ARABE,
				criaHorarioFuncionamento(List.of(DiasEnum.SEGUNDA_FEIRA), "24horas"),
				500
		);
		var objectMapper = this.objectMapper
				.writer()
				.withDefaultPrettyPrinter();
		var jsonRequest = objectMapper.writeValueAsString(request);

		this.mockMvc
				.perform(MockMvcRequestBuilders.post(URL_RESTAURANTE)
						.content(jsonRequest)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers
						.status()
						.isCreated()
				);

		var restaurante = this.repositoryRestaurante.findAll().get(0);

		Assertions.assertEquals(1, this.repositoryRestaurante.findAll().size());
		Assertions.assertEquals("rua teste", restaurante.getLogradouro());
		Assertions.assertEquals(10, restaurante.getNumeroEndereco());
		Assertions.assertEquals("14000000", restaurante.getCep());
		Assertions.assertEquals("bairro teste", restaurante.getBairro());
		Assertions.assertEquals("cidade teste", restaurante.getCidade());
		Assertions.assertEquals("SP", restaurante.getEstado());
		Assertions.assertEquals("ap1122", restaurante.getComplemento());
		Assertions.assertEquals("49251058000105", restaurante.getCnpj());
		Assertions.assertEquals(TipoCozinhaEnum.COMIDA_ARABE, restaurante.getTipoCozinha());
		Assertions.assertEquals(List.of(DiasEnum.SEGUNDA_FEIRA).toString(), restaurante.getDiasFuncionamento());
		Assertions.assertEquals("24horas", restaurante.getHorarioFuncionamento());
		Assertions.assertEquals(500, restaurante.getCapacidadeDePessoas());
	}

	@Test
	public void restaurante_deveRetornar201_semComplemento_salvaNaBaseDeDados() throws Exception {
		var request = new CriaRestauranteDTO(
				"49251058000105",
				"Restaurante Teste",
				criaLocalizacao("rua teste", 10, "14000-000", "bairro teste", "cidade teste", "SP", null),
				TipoCozinhaEnum.COMIDA_ARABE,
				horarioFuncionamentoDefault(),
				500
		);
		var objectMapper = this.objectMapper
				.writer()
				.withDefaultPrettyPrinter();
		var jsonRequest = objectMapper.writeValueAsString(request);

		this.mockMvc
				.perform(MockMvcRequestBuilders.post(URL_RESTAURANTE)
						.content(jsonRequest)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers
						.status()
						.isCreated()
				);

		var restaurante = this.repositoryRestaurante.findAll().get(0);

		Assertions.assertEquals(1, this.repositoryRestaurante.findAll().size());
		Assertions.assertEquals("rua teste", restaurante.getLogradouro());
		Assertions.assertEquals(10, restaurante.getNumeroEndereco());
		Assertions.assertEquals("14000000", restaurante.getCep());
		Assertions.assertEquals("bairro teste", restaurante.getBairro());
		Assertions.assertEquals("cidade teste", restaurante.getCidade());
		Assertions.assertEquals("SP", restaurante.getEstado());
		Assertions.assertNull(restaurante.getComplemento());
		Assertions.assertEquals("49251058000105", restaurante.getCnpj());
		Assertions.assertEquals(TipoCozinhaEnum.COMIDA_ARABE, restaurante.getTipoCozinha());
		Assertions.assertEquals(List.of(DiasEnum.TODOS).toString(), restaurante.getDiasFuncionamento());
		Assertions.assertEquals("18:00 ate 23:00", restaurante.getHorarioFuncionamento());
		Assertions.assertEquals(500, restaurante.getCapacidadeDePessoas());
	}

	@Test
	public void restaurante_deveRetornar201_especificaUmDiaETambemColocaTodosDias_salvaNaBaseDeDados() throws Exception {
		var request = new CriaRestauranteDTO(
				"49.251.058/0001-05",
				"Restaurante Teste",
				localizacaoDefault(),
				TipoCozinhaEnum.COMIDA_ARABE,
				criaHorarioFuncionamento(List.of(DiasEnum.SEGUNDA_FEIRA, DiasEnum.TODOS), "18:00 ate 23:00"),
				500
		);
		var objectMapper = this.objectMapper
				.writer()
				.withDefaultPrettyPrinter();
		var jsonRequest = objectMapper.writeValueAsString(request);

		this.mockMvc
				.perform(MockMvcRequestBuilders.post(URL_RESTAURANTE)
						.content(jsonRequest)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers
						.status()
						.isCreated()
				);

		var restaurante = this.repositoryRestaurante.findAll().get(0);

		Assertions.assertEquals(1, this.repositoryRestaurante.findAll().size());
		Assertions.assertEquals("rua teste", restaurante.getLogradouro());
		Assertions.assertEquals(10, restaurante.getNumeroEndereco());
		Assertions.assertEquals("14000000", restaurante.getCep());
		Assertions.assertEquals("bairro teste", restaurante.getBairro());
		Assertions.assertEquals("cidade teste", restaurante.getCidade());
		Assertions.assertEquals("SP", restaurante.getEstado());
		Assertions.assertEquals("ap1122", restaurante.getComplemento());
		Assertions.assertEquals("49251058000105", restaurante.getCnpj());
		Assertions.assertEquals(TipoCozinhaEnum.COMIDA_ARABE, restaurante.getTipoCozinha());
		Assertions.assertEquals(List.of(DiasEnum.TODOS).toString(), restaurante.getDiasFuncionamento());
		Assertions.assertEquals("18:00 ate 23:00", restaurante.getHorarioFuncionamento());
		Assertions.assertEquals(500, restaurante.getCapacidadeDePessoas());
	}

	@ParameterizedTest
	@MethodSource("requestValidandoCampos")
	public void restaurante_deveRetornar400_camposInvalidos_naoSalvaNaBaseDeDados(String cnpj,
																				  String nome,
																				  EnderecoCompletoDTO localizacao,
																				  TipoCozinhaEnum tipoCozinha,
																				  HorarioDeFuncionamentoDTO horarioFuncionamento,
																				  Integer capacidadeDePessoas) throws Exception {
		var request = new CriaRestauranteDTO(cnpj, nome, localizacao, tipoCozinha, horarioFuncionamento, capacidadeDePessoas);
		var objectMapper = this.objectMapper
				.writer()
				.withDefaultPrettyPrinter();
		var jsonRequest = objectMapper.writeValueAsString(request);

		this.mockMvc
				.perform(MockMvcRequestBuilders.post(URL_RESTAURANTE)
						.content(jsonRequest)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers
						.status()
						.isBadRequest()
				);
		Assertions.assertEquals(0, this.repositoryRestaurante.findAll().size());
	}

	@ParameterizedTest
	@MethodSource("requestHorariosInvalidos")
	public void restaurante_horarioInvalido_naoSalvaNaBaseDeDados(String horario) throws Exception {
		var request = new CriaRestauranteDTO(
				"49.251.058/0001-05",
				"Restaurante Teste",
				localizacaoDefault(),
				TipoCozinhaEnum.COMIDA_ARABE,
				criaHorarioFuncionamento(List.of(DiasEnum.SEGUNDA_FEIRA), horario),
				500
		);
		var objectMapper = this.objectMapper
				.writer()
				.withDefaultPrettyPrinter();
		var jsonRequest = objectMapper.writeValueAsString(request);

		var response = this.mockMvc
				.perform(MockMvcRequestBuilders.post(URL_RESTAURANTE)
						.content(jsonRequest)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers
						.status()
						.isBadRequest()
				).andReturn();

		Assertions.assertEquals(0, this.repositoryRestaurante.findAll().size());
		Assertions.assertTrue(response.getResponse().getContentAsString().contains("Erro na definição dos horarios... Exemplo de como deve ser: 22:10. Segue o valor errado"));
	}

	private static Stream<Arguments> requestValidandoCampos() {
		return Stream.of(
				Arguments.of(null, "Nome de teste", localizacaoDefault(),
						TipoCozinhaEnum.COMIDA_JAPONESA, horarioFuncionamentoDefault(), 10),
				Arguments.of("", "Nome de teste", localizacaoDefault(),
						TipoCozinhaEnum.COMIDA_JAPONESA, horarioFuncionamentoDefault(), 10),
				Arguments.of("04623021000114", null, localizacaoDefault(),
						TipoCozinhaEnum.COMIDA_JAPONESA, horarioFuncionamentoDefault(), 10),
				Arguments.of("04623021000114", "", localizacaoDefault(),
						TipoCozinhaEnum.COMIDA_JAPONESA, horarioFuncionamentoDefault(), 10),
				Arguments.of("04623021000114", "Nome de teste", null,
						TipoCozinhaEnum.COMIDA_JAPONESA, horarioFuncionamentoDefault(), 10),
				Arguments.of("04623021000114", "Nome de teste", criaLocalizacao(null, 1010, "14000-000", "bairro teste", "cidade teste", "SP", "ap1122"),
						TipoCozinhaEnum.COMIDA_JAPONESA, horarioFuncionamentoDefault(), 10),
				Arguments.of("04623021000114", "Nome de teste", criaLocalizacao("rua dos testes", null, "14000-000", "bairro teste", "cidade teste", "SP", "ap1122"),
						TipoCozinhaEnum.COMIDA_JAPONESA, horarioFuncionamentoDefault(), 10),
				Arguments.of("04623021000114", "Nome de teste", criaLocalizacao("rua dos testes", 1010, null, "bairro teste", "cidade teste", "SP", "ap1122"),
						TipoCozinhaEnum.COMIDA_JAPONESA, horarioFuncionamentoDefault(), 10),
				Arguments.of("04623021000114", "Nome de teste", criaLocalizacao("rua dos testes", 1010, "14000-000", null, "cidade teste", "SP", "ap1122"),
						TipoCozinhaEnum.COMIDA_JAPONESA, horarioFuncionamentoDefault(), 10),
				Arguments.of("04623021000114", "Nome de teste", criaLocalizacao("rua dos testes", 1010, "14000-000", "bairro teste", null, "SP", "ap1122"),
						TipoCozinhaEnum.COMIDA_JAPONESA, horarioFuncionamentoDefault(), 10),
				Arguments.of("04623021000114", "Nome de teste", criaLocalizacao("rua dos testes", 1010, "14000-000", "bairro teste", "cidade teste", null, "ap1122"),
						TipoCozinhaEnum.COMIDA_JAPONESA, horarioFuncionamentoDefault(), 10),
				Arguments.of("04623021000114", "Nome de teste", localizacaoDefault(),
						null, horarioFuncionamentoDefault(), 10),
				Arguments.of("04623021000114", "Nome de teste", localizacaoDefault(),
						TipoCozinhaEnum.COMIDA_JAPONESA, null, 10),
				Arguments.of("04623021000114", "Nome de teste", localizacaoDefault(),
						TipoCozinhaEnum.COMIDA_JAPONESA, criaHorarioFuncionamento(null, "aa"), 10),
				Arguments.of("04623021000114", "Nome de teste", localizacaoDefault(),
						TipoCozinhaEnum.COMIDA_JAPONESA, criaHorarioFuncionamento(List.of(DiasEnum.SEGUNDA_FEIRA), null), 10),
				Arguments.of("04623021000114", "Nome de teste", localizacaoDefault(),
						TipoCozinhaEnum.COMIDA_JAPONESA, horarioFuncionamentoDefault(), null),
				Arguments.of(null, null, null, null, null, null),
				Arguments.of("", "", localizacaoDefault(),
						TipoCozinhaEnum.COMIDA_JAPONESA, horarioFuncionamentoDefault(), 10),

				Arguments.of(" ", "Nome de teste", localizacaoDefault(),
						TipoCozinhaEnum.COMIDA_JAPONESA, horarioFuncionamentoDefault(), 10),
				Arguments.of("aa", "Nome de teste", localizacaoDefault(),
						TipoCozinhaEnum.COMIDA_JAPONESA, horarioFuncionamentoDefault(), 10),
				Arguments.of("376.082.600-84", "Nome de teste", localizacaoDefault(),
						TipoCozinhaEnum.COMIDA_JAPONESA, horarioFuncionamentoDefault(), 10),
				Arguments.of("04623021000114", " ", localizacaoDefault(),
						TipoCozinhaEnum.COMIDA_JAPONESA, horarioFuncionamentoDefault(), 10),
				Arguments.of("04623021000114", "aa", localizacaoDefault(),
						TipoCozinhaEnum.COMIDA_JAPONESA, horarioFuncionamentoDefault(), 10),
				Arguments.of("04623021000114", "aaaaaaaaaabbbbbbbbbbccccccccccddddddddddeeeeeeeeeef", localizacaoDefault(),
						TipoCozinhaEnum.COMIDA_JAPONESA, horarioFuncionamentoDefault(), 10),
				Arguments.of("04623021000114", "Nome de teste", criaLocalizacao(" ", 1010, "14000-000", "bairro teste", "cidade teste", "SP", "ap1122"),
						TipoCozinhaEnum.COMIDA_JAPONESA, horarioFuncionamentoDefault(), 10),
				Arguments.of("04623021000114", "Nome de teste", criaLocalizacao("aaaa", 1010, "14000-000", "bairro teste", "cidade teste", "SP", "ap1122"),
						TipoCozinhaEnum.COMIDA_JAPONESA, horarioFuncionamentoDefault(), 10),
				Arguments.of("04623021000114", "Nome de teste", criaLocalizacao("aaaaaaaaaabbbbbbbbbbccccccccccddddddddddeeeeeeeeeef", 1010, "14000-000", "bairro teste", "cidade teste", "SP", "ap1122"),
						TipoCozinhaEnum.COMIDA_JAPONESA, horarioFuncionamentoDefault(), 10),
				Arguments.of("04623021000114", "Nome de teste", criaLocalizacao("rua dos testes", 0, "14000-000", "bairro teste", "cidade teste", "SP", "ap1122"),
						TipoCozinhaEnum.COMIDA_JAPONESA, horarioFuncionamentoDefault(), 10),
				Arguments.of("04623021000114", "Nome de teste", criaLocalizacao("rua dos testes", 1_000_000, "14000-000", "bairro teste", "cidade teste", "SP", "ap1122"),
						TipoCozinhaEnum.COMIDA_JAPONESA, horarioFuncionamentoDefault(), 10),
				Arguments.of("04623021000114", "Nome de teste", criaLocalizacao("rua dos testes", 1010, " ", "bairro teste", "cidade teste", "SP", "ap1122"),
						TipoCozinhaEnum.COMIDA_JAPONESA, horarioFuncionamentoDefault(), 10),
				Arguments.of("04623021000114", "Nome de teste", criaLocalizacao("rua dos testes", 1010, "14000000", "bairro teste", "cidade teste", "SP", "ap1122"),
						TipoCozinhaEnum.COMIDA_JAPONESA, horarioFuncionamentoDefault(), 10),
				Arguments.of("04623021000114", "Nome de teste", criaLocalizacao("rua dos testes", 1010, "14000-000", " ", "cidade teste", "SP", "ap1122"),
						TipoCozinhaEnum.COMIDA_JAPONESA, horarioFuncionamentoDefault(), 10),
				Arguments.of("04623021000114", "Nome de teste", criaLocalizacao("rua dos testes", 1010, "14000-000", "aaaa", "cidade teste", "SP", "ap1122"),
						TipoCozinhaEnum.COMIDA_JAPONESA, horarioFuncionamentoDefault(), 10),
				Arguments.of("04623021000114", "Nome de teste", criaLocalizacao("rua dos testes", 1010, "14000-000", "aaaaaaaaaabbbbbbbbbbccccccccccddddddddddeeeeeeeeeef", "cidade teste", "SP", "ap1122"),
						TipoCozinhaEnum.COMIDA_JAPONESA, horarioFuncionamentoDefault(), 10),
				Arguments.of("04623021000114", "Nome de teste", criaLocalizacao("rua dos testes", 1010, "14000-000", "bairro teste", " ", "SP", "ap1122"),
						TipoCozinhaEnum.COMIDA_JAPONESA, horarioFuncionamentoDefault(), 10),
				Arguments.of("04623021000114", "Nome de teste", criaLocalizacao("rua dos testes", 1010, "14000-000", "bairro teste", "aaaa", "SP", "ap1122"),
						TipoCozinhaEnum.COMIDA_JAPONESA, horarioFuncionamentoDefault(), 10),
				Arguments.of("04623021000114", "Nome de teste", criaLocalizacao("rua dos testes", 1010, "14000-000", "bairro teste", "aaaaaaaaaabbbbbbbbbbccccccccccddddddddddeeeeeeeeeef", "SP", "ap1122"),
						TipoCozinhaEnum.COMIDA_JAPONESA, horarioFuncionamentoDefault(), 10),
				Arguments.of("04623021000114", "Nome de teste", criaLocalizacao("rua dos testes", 1010, "14000-000", "bairro teste", "cidade teste", " ", "ap1122"),
						TipoCozinhaEnum.COMIDA_JAPONESA, horarioFuncionamentoDefault(), 10),
				Arguments.of("04623021000114", "Nome de teste", criaLocalizacao("rua dos testes", 1010, "14000-000", "bairro teste", "cidade teste", "a", "ap1122"),
						TipoCozinhaEnum.COMIDA_JAPONESA, horarioFuncionamentoDefault(), 10),
				Arguments.of("04623021000114", "Nome de teste", criaLocalizacao("rua dos testes", 1010, "14000-000", "bairro teste", "cidade teste", "aaa", "ap1122"),
						TipoCozinhaEnum.COMIDA_JAPONESA, horarioFuncionamentoDefault(), 10),
				Arguments.of("04623021000114", "Nome de teste", criaLocalizacao("rua dos testes", 1010, "14000-000", "bairro teste", "cidade teste", "SP", " "),
						TipoCozinhaEnum.COMIDA_JAPONESA, horarioFuncionamentoDefault(), 10),
				Arguments.of("04623021000114", "Nome de teste", criaLocalizacao("rua dos testes", 1010, "14000-000", "bairro teste", "cidade teste", "SP", "a"),
						TipoCozinhaEnum.COMIDA_JAPONESA, horarioFuncionamentoDefault(), 10),
				Arguments.of("04623021000114", "Nome de teste", criaLocalizacao("rua dos testes", 1010, "14000-000", "bairro teste", "cidade teste", "SP", "aaaaaaaaaabbbbbbbbbbccccccccccd"),
						TipoCozinhaEnum.COMIDA_JAPONESA, horarioFuncionamentoDefault(), 10),
				Arguments.of("04623021000114", "Nome de teste", localizacaoDefault(),
						TipoCozinhaEnum.COMIDA_JAPONESA, criaHorarioFuncionamento(List.of(DiasEnum.SEGUNDA_FEIRA), " "), 10),
				Arguments.of("04623021000114", "Nome de teste", localizacaoDefault(),
						TipoCozinhaEnum.COMIDA_JAPONESA, criaHorarioFuncionamento(List.of(DiasEnum.SEGUNDA_FEIRA), "aaaa"), 10),
				Arguments.of("04623021000114", "Nome de teste", localizacaoDefault(),
						TipoCozinhaEnum.COMIDA_JAPONESA, criaHorarioFuncionamento(List.of(), "18:00 ate 23:00"), 10),
				Arguments.of("04623021000114", "Nome de teste", localizacaoDefault(),
						TipoCozinhaEnum.COMIDA_JAPONESA, horarioFuncionamentoDefault(), 0),
				Arguments.of("04623021000114", "Nome de teste", localizacaoDefault(),
						TipoCozinhaEnum.COMIDA_JAPONESA, horarioFuncionamentoDefault(), 5001)
		);
	}

	private static Stream<Arguments> requestHorariosInvalidos() {
		return Stream.of(
				Arguments.of("25:00 ate 23:00"),
				Arguments.of("22:00 ate 25:00"),
				Arguments.of("22:00 ate 22:00"),
				Arguments.of("22:00 ate 21:00")
		);
	}

}