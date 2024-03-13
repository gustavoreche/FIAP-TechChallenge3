package com.fiap.techchallenge3;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiap.techchallenge3.model.DiasEnum;
import com.fiap.techchallenge3.model.TipoCozinhaEnum;
import com.fiap.techchallenge3.model.dto.CriaRestauranteDTO;
import com.fiap.techchallenge3.model.dto.EnderecoCompletoDTO;
import com.fiap.techchallenge3.model.dto.HorarioDeFuncionamentoDTO;
import com.fiap.techchallenge3.repository.RestauranteRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
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

import static com.fiap.techchallenge3.controller.RestauranteController.URL_RESTAURANTE;

@AutoConfigureMockMvc
@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
class RestauranteCadastraTests {

    @Autowired
    private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	RestauranteRepository repository;

	@BeforeEach
	void inicializaLimpezaDoDatabase() {
		this.repository.deleteAll();
	}

	@AfterAll
	void finalizaLimpezaDoDatabase() {
		this.repository.deleteAll();
	}

	@ParameterizedTest
	@MethodSource("requestValidandoCamposNullsOuVazios")
	public void deveRetornarStatus400_validacoesDosCamposNullsOuVazios(String cnpj,
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
		Assertions.assertEquals(0, this.repository.findAll().size());
	}

	@ParameterizedTest
	@MethodSource("requestValidandoCampos")
	public void deveRetornarStatus400_validacoesDosCampos(String cnpj,
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
		Assertions.assertEquals(0, this.repository.findAll().size());
	}

	private static Stream<Arguments> requestValidandoCamposNullsOuVazios() {
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
						TipoCozinhaEnum.COMIDA_JAPONESA, horarioFuncionamentoDefault(), 10)
		);
	}

	private static EnderecoCompletoDTO localizacaoDefault() {
		return new EnderecoCompletoDTO(
				"rua teste",
				10,
				"14000-000",
				"bairro teste",
				"cidade teste",
				"SP",
				"ap1122"
		);
	}

	private static EnderecoCompletoDTO criaLocalizacao(String logradouro,
													   Integer numero,
													   String cep,
													   String bairro,
													   String cidade,
													   String estado,
													   String complemento) {
		return new EnderecoCompletoDTO(
				logradouro,
				numero,
				cep,
				bairro,
				cidade,
				estado,
				complemento
		);
	}

	private static HorarioDeFuncionamentoDTO horarioFuncionamentoDefault() {
		return new HorarioDeFuncionamentoDTO(
				List.of(DiasEnum.TODOS),
				"18:00 ate 23:00"
		);
	}

	private static HorarioDeFuncionamentoDTO criaHorarioFuncionamento(List<DiasEnum> diasAbertos,
																	  String horarioFuncionamento) {
		return new HorarioDeFuncionamentoDTO(
				diasAbertos,
				horarioFuncionamento
		);
	}

	private static Stream<Arguments> requestValidandoCampos() {
		return Stream.of(
				Arguments.of(" ", "Nome de teste", localizacaoDefault(),
						TipoCozinhaEnum.COMIDA_JAPONESA, horarioFuncionamentoDefault(), 10),
				Arguments.of("aa", "Nome de teste", localizacaoDefault(),
						TipoCozinhaEnum.COMIDA_JAPONESA, horarioFuncionamentoDefault(), 10),
				Arguments.of("04.623021/0001-14", "Nome de teste", localizacaoDefault(),
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

}
