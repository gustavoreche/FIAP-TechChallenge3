package com.fiap.techchallenge3.integrados.restaurante;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiap.techchallenge3.domain.restaurante.model.DiasEnum;
import com.fiap.techchallenge3.domain.restaurante.model.TipoCozinhaEnum;
import com.fiap.techchallenge3.infrastructure.localizacao.client.ViaCepResponse;
import com.fiap.techchallenge3.infrastructure.restaurante.controller.dto.CriaRestauranteDTO;
import com.fiap.techchallenge3.infrastructure.restaurante.controller.dto.EnderecoCompletoDTO;
import com.fiap.techchallenge3.infrastructure.restaurante.controller.dto.ExibeBuscaRestauranteDTO;
import com.fiap.techchallenge3.infrastructure.restaurante.controller.dto.HorarioDeFuncionamentoDTO;
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

	@Test
	public void restaurante_deveRetornar200_informandoTodosCampos_buscaNaBaseDeDados() throws Exception {

		var restaurante1 = RestauranteEntity.builder()
				.cnpj("49251058000123")
				.nome("Restaurante Teste A")
				.tipoCozinha(TipoCozinhaEnum.COMIDA_ARABE)
				.diasFuncionamento(List.of(DiasEnum.TODOS).toString())
				.horarioFuncionamento("18:00 ate 23:00")
				.capacidadeDePessoas(500)
				.cep("14000000")
				.logradouro("rua teste")
				.numeroEndereco(10)
				.bairro("bairro teste A")
				.cidade("cidade teste A")
				.estado("SP")
				.build();
		var restaurante2 = RestauranteEntity.builder()
				.cnpj("49251058000105")
				.nome("Restaurante bla bla B")
				.tipoCozinha(TipoCozinhaEnum.COMIDA_ARABE)
				.diasFuncionamento(List.of(DiasEnum.TODOS).toString())
				.horarioFuncionamento("18:00 ate 23:00")
				.capacidadeDePessoas(500)
				.cep("14000000")
				.logradouro("rua teste")
				.numeroEndereco(10)
				.bairro("bairro teste A")
				.cidade("cidade teste A")
				.estado("SP")
				.build();
		this.repositoryRestaurante.saveAll(List.of(restaurante1, restaurante2));

		var response = this.mockMvc
				.perform(MockMvcRequestBuilders.get(URL_RESTAURANTE)
						.param("nome", "Restaurante Teste")
						.param("cep", "14000-000")
						.param("bairro", "bairro teste")
						.param("cidade", "cidade teste")
						.param("estado", "SP")
						.param("tipoCozinha", TipoCozinhaEnum.COMIDA_ARABE.name())
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers
						.status()
						.isOk()
				).andReturn();
		var responseAppString = response.getResponse().getContentAsString();
		var responseApp = this.objectMapper
				.readValue(responseAppString, new TypeReference<List<ExibeBuscaRestauranteDTO>>(){});

		Assertions.assertEquals(1, responseApp.size());
		Assertions.assertEquals("Restaurante Teste A", responseApp.get(0).nome());
		Assertions.assertEquals("rua teste", responseApp.get(0).logradouro());
		Assertions.assertEquals(10, responseApp.get(0).numero());
		Assertions.assertEquals("14000000", responseApp.get(0).cep());
		Assertions.assertEquals("bairro teste A", responseApp.get(0).bairro());
		Assertions.assertEquals("cidade teste A", responseApp.get(0).cidade());
		Assertions.assertEquals("SP", responseApp.get(0).estado());
		Assertions.assertNull(responseApp.get(0).complemento());
		Assertions.assertEquals(TipoCozinhaEnum.COMIDA_ARABE, responseApp.get(0).tipoCozinha());
		Assertions.assertEquals(List.of(DiasEnum.TODOS).toString(), responseApp.get(0).diasFuncionamento());
		Assertions.assertEquals("18:00 ate 23:00", responseApp.get(0).horarioFuncionamento());
		Assertions.assertEquals(500, responseApp.get(0).capacidadeDePessoas());
	}

	@Test
	public void restaurante_deveRetornar200_informandoApenasNome_buscaNaBaseDeDados() throws Exception {

		var restaurante1 = RestauranteEntity.builder()
				.cnpj("49251058000123")
				.nome("Restaurante Teste A")
				.tipoCozinha(TipoCozinhaEnum.COMIDA_ARABE)
				.diasFuncionamento(List.of(DiasEnum.TODOS).toString())
				.horarioFuncionamento("18:00 ate 23:00")
				.capacidadeDePessoas(500)
				.cep("14000000")
				.logradouro("rua teste")
				.numeroEndereco(10)
				.bairro("bairro teste A")
				.cidade("cidade teste A")
				.estado("SP")
				.build();
		var restaurante2 = RestauranteEntity.builder()
				.cnpj("49251058000105")
				.nome("Restaurante bla bla B")
				.tipoCozinha(TipoCozinhaEnum.COMIDA_ARABE)
				.diasFuncionamento(List.of(DiasEnum.TODOS).toString())
				.horarioFuncionamento("18:00 ate 23:00")
				.capacidadeDePessoas(500)
				.cep("14000000")
				.logradouro("rua teste")
				.numeroEndereco(10)
				.bairro("bairro teste A")
				.cidade("cidade teste A")
				.estado("SP")
				.build();
		this.repositoryRestaurante.saveAll(List.of(restaurante1, restaurante2));

		var response = this.mockMvc
				.perform(MockMvcRequestBuilders.get(URL_RESTAURANTE)
						.param("nome", "Restaurante Teste")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers
						.status()
						.isOk()
				).andReturn();
		var responseAppString = response.getResponse().getContentAsString();
		var responseApp = this.objectMapper
				.readValue(responseAppString, new TypeReference<List<ExibeBuscaRestauranteDTO>>(){});

		Assertions.assertEquals(1, responseApp.size());
		Assertions.assertEquals("Restaurante Teste A", responseApp.get(0).nome());
		Assertions.assertEquals("rua teste", responseApp.get(0).logradouro());
		Assertions.assertEquals(10, responseApp.get(0).numero());
		Assertions.assertEquals("14000000", responseApp.get(0).cep());
		Assertions.assertEquals("bairro teste A", responseApp.get(0).bairro());
		Assertions.assertEquals("cidade teste A", responseApp.get(0).cidade());
		Assertions.assertEquals("SP", responseApp.get(0).estado());
		Assertions.assertNull(responseApp.get(0).complemento());
		Assertions.assertEquals(TipoCozinhaEnum.COMIDA_ARABE, responseApp.get(0).tipoCozinha());
		Assertions.assertEquals(List.of(DiasEnum.TODOS).toString(), responseApp.get(0).diasFuncionamento());
		Assertions.assertEquals("18:00 ate 23:00", responseApp.get(0).horarioFuncionamento());
		Assertions.assertEquals(500, responseApp.get(0).capacidadeDePessoas());
	}

	@Test
	public void restaurante_deveRetornar200_informandoApenasCep_buscaNaBaseDeDados() throws Exception {

		var restaurante1 = RestauranteEntity.builder()
				.cnpj("49251058000123")
				.nome("Restaurante Teste A")
				.tipoCozinha(TipoCozinhaEnum.COMIDA_ARABE)
				.diasFuncionamento(List.of(DiasEnum.TODOS).toString())
				.horarioFuncionamento("18:00 ate 23:00")
				.capacidadeDePessoas(500)
				.cep("14000000")
				.logradouro("rua teste")
				.numeroEndereco(10)
				.bairro("bairro teste A")
				.cidade("cidade teste A")
				.estado("SP")
				.build();
		var restaurante2 = RestauranteEntity.builder()
				.cnpj("49251058000105")
				.nome("Restaurante bla bla B")
				.tipoCozinha(TipoCozinhaEnum.COMIDA_ARABE)
				.diasFuncionamento(List.of(DiasEnum.TODOS).toString())
				.horarioFuncionamento("18:00 ate 23:00")
				.capacidadeDePessoas(500)
				.cep("14000001")
				.logradouro("rua teste")
				.numeroEndereco(10)
				.bairro("bairro teste A")
				.cidade("cidade teste A")
				.estado("SP")
				.build();
		this.repositoryRestaurante.saveAll(List.of(restaurante1, restaurante2));

		var response = this.mockMvc
				.perform(MockMvcRequestBuilders.get(URL_RESTAURANTE)
						.param("cep", "14000-000")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers
						.status()
						.isOk()
				).andReturn();
		var responseAppString = response.getResponse().getContentAsString();
		var responseApp = this.objectMapper
				.readValue(responseAppString, new TypeReference<List<ExibeBuscaRestauranteDTO>>(){});

		Assertions.assertEquals(1, responseApp.size());
		Assertions.assertEquals("Restaurante Teste A", responseApp.get(0).nome());
		Assertions.assertEquals("rua teste", responseApp.get(0).logradouro());
		Assertions.assertEquals(10, responseApp.get(0).numero());
		Assertions.assertEquals("14000000", responseApp.get(0).cep());
		Assertions.assertEquals("bairro teste A", responseApp.get(0).bairro());
		Assertions.assertEquals("cidade teste A", responseApp.get(0).cidade());
		Assertions.assertEquals("SP", responseApp.get(0).estado());
		Assertions.assertNull(responseApp.get(0).complemento());
		Assertions.assertEquals(TipoCozinhaEnum.COMIDA_ARABE, responseApp.get(0).tipoCozinha());
		Assertions.assertEquals(List.of(DiasEnum.TODOS).toString(), responseApp.get(0).diasFuncionamento());
		Assertions.assertEquals("18:00 ate 23:00", responseApp.get(0).horarioFuncionamento());
		Assertions.assertEquals(500, responseApp.get(0).capacidadeDePessoas());
	}

	@Test
	public void restaurante_deveRetornar200_informandoApenasBairro_buscaNaBaseDeDados() throws Exception {

		var restaurante1 = RestauranteEntity.builder()
				.cnpj("49251058000123")
				.nome("Restaurante Teste A")
				.tipoCozinha(TipoCozinhaEnum.COMIDA_ARABE)
				.diasFuncionamento(List.of(DiasEnum.TODOS).toString())
				.horarioFuncionamento("18:00 ate 23:00")
				.capacidadeDePessoas(500)
				.cep("14000000")
				.logradouro("rua teste")
				.numeroEndereco(10)
				.bairro("bairro teste A")
				.cidade("cidade teste A")
				.estado("SP")
				.build();
		var restaurante2 = RestauranteEntity.builder()
				.cnpj("49251058000105")
				.nome("Restaurante bla bla B")
				.tipoCozinha(TipoCozinhaEnum.COMIDA_ARABE)
				.diasFuncionamento(List.of(DiasEnum.TODOS).toString())
				.horarioFuncionamento("18:00 ate 23:00")
				.capacidadeDePessoas(500)
				.cep("14000000")
				.logradouro("rua teste")
				.numeroEndereco(10)
				.bairro("bairro A")
				.cidade("cidade teste A")
				.estado("SP")
				.build();
		this.repositoryRestaurante.saveAll(List.of(restaurante1, restaurante2));

		var response = this.mockMvc
				.perform(MockMvcRequestBuilders.get(URL_RESTAURANTE)
						.param("bairro", "bairro teste")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers
						.status()
						.isOk()
				).andReturn();
		var responseAppString = response.getResponse().getContentAsString();
		var responseApp = this.objectMapper
				.readValue(responseAppString, new TypeReference<List<ExibeBuscaRestauranteDTO>>(){});

		Assertions.assertEquals(1, responseApp.size());
		Assertions.assertEquals("Restaurante Teste A", responseApp.get(0).nome());
		Assertions.assertEquals("rua teste", responseApp.get(0).logradouro());
		Assertions.assertEquals(10, responseApp.get(0).numero());
		Assertions.assertEquals("14000000", responseApp.get(0).cep());
		Assertions.assertEquals("bairro teste A", responseApp.get(0).bairro());
		Assertions.assertEquals("cidade teste A", responseApp.get(0).cidade());
		Assertions.assertEquals("SP", responseApp.get(0).estado());
		Assertions.assertNull(responseApp.get(0).complemento());
		Assertions.assertEquals(TipoCozinhaEnum.COMIDA_ARABE, responseApp.get(0).tipoCozinha());
		Assertions.assertEquals(List.of(DiasEnum.TODOS).toString(), responseApp.get(0).diasFuncionamento());
		Assertions.assertEquals("18:00 ate 23:00", responseApp.get(0).horarioFuncionamento());
		Assertions.assertEquals(500, responseApp.get(0).capacidadeDePessoas());
	}

	@Test
	public void restaurante_deveRetornar200_informandoApenasCidade_buscaNaBaseDeDados() throws Exception {

		var restaurante1 = RestauranteEntity.builder()
				.cnpj("49251058000123")
				.nome("Restaurante Teste A")
				.tipoCozinha(TipoCozinhaEnum.COMIDA_ARABE)
				.diasFuncionamento(List.of(DiasEnum.TODOS).toString())
				.horarioFuncionamento("18:00 ate 23:00")
				.capacidadeDePessoas(500)
				.cep("14000000")
				.logradouro("rua teste")
				.numeroEndereco(10)
				.bairro("bairro teste A")
				.cidade("cidade teste A")
				.estado("SP")
				.build();
		var restaurante2 = RestauranteEntity.builder()
				.cnpj("49251058000105")
				.nome("Restaurante bla bla B")
				.tipoCozinha(TipoCozinhaEnum.COMIDA_ARABE)
				.diasFuncionamento(List.of(DiasEnum.TODOS).toString())
				.horarioFuncionamento("18:00 ate 23:00")
				.capacidadeDePessoas(500)
				.cep("14000000")
				.logradouro("rua teste")
				.numeroEndereco(10)
				.bairro("bairro teste A")
				.cidade("cidade A")
				.estado("SP")
				.build();
		this.repositoryRestaurante.saveAll(List.of(restaurante1, restaurante2));

		var response = this.mockMvc
				.perform(MockMvcRequestBuilders.get(URL_RESTAURANTE)
						.param("cidade", "cidade teste")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers
						.status()
						.isOk()
				).andReturn();
		var responseAppString = response.getResponse().getContentAsString();
		var responseApp = this.objectMapper
				.readValue(responseAppString, new TypeReference<List<ExibeBuscaRestauranteDTO>>(){});

		Assertions.assertEquals(1, responseApp.size());
		Assertions.assertEquals("Restaurante Teste A", responseApp.get(0).nome());
		Assertions.assertEquals("rua teste", responseApp.get(0).logradouro());
		Assertions.assertEquals(10, responseApp.get(0).numero());
		Assertions.assertEquals("14000000", responseApp.get(0).cep());
		Assertions.assertEquals("bairro teste A", responseApp.get(0).bairro());
		Assertions.assertEquals("cidade teste A", responseApp.get(0).cidade());
		Assertions.assertEquals("SP", responseApp.get(0).estado());
		Assertions.assertNull(responseApp.get(0).complemento());
		Assertions.assertEquals(TipoCozinhaEnum.COMIDA_ARABE, responseApp.get(0).tipoCozinha());
		Assertions.assertEquals(List.of(DiasEnum.TODOS).toString(), responseApp.get(0).diasFuncionamento());
		Assertions.assertEquals("18:00 ate 23:00", responseApp.get(0).horarioFuncionamento());
		Assertions.assertEquals(500, responseApp.get(0).capacidadeDePessoas());
	}

	@Test
	public void restaurante_deveRetornar200_informandoApenasEstado_buscaNaBaseDeDados() throws Exception {

		var restaurante1 = RestauranteEntity.builder()
				.cnpj("49251058000123")
				.nome("Restaurante Teste A")
				.tipoCozinha(TipoCozinhaEnum.COMIDA_ARABE)
				.diasFuncionamento(List.of(DiasEnum.TODOS).toString())
				.horarioFuncionamento("18:00 ate 23:00")
				.capacidadeDePessoas(500)
				.cep("14000000")
				.logradouro("rua teste")
				.numeroEndereco(10)
				.bairro("bairro teste A")
				.cidade("cidade teste A")
				.estado("SP")
				.build();
		var restaurante2 = RestauranteEntity.builder()
				.cnpj("49251058000105")
				.nome("Restaurante bla bla B")
				.tipoCozinha(TipoCozinhaEnum.COMIDA_ARABE)
				.diasFuncionamento(List.of(DiasEnum.TODOS).toString())
				.horarioFuncionamento("18:00 ate 23:00")
				.capacidadeDePessoas(500)
				.cep("14000000")
				.logradouro("rua teste")
				.numeroEndereco(10)
				.bairro("bairro teste A")
				.cidade("cidade teste A")
				.estado("RJ")
				.build();
		this.repositoryRestaurante.saveAll(List.of(restaurante1, restaurante2));

		var response = this.mockMvc
				.perform(MockMvcRequestBuilders.get(URL_RESTAURANTE)
						.param("estado", "SP")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers
						.status()
						.isOk()
				).andReturn();
		var responseAppString = response.getResponse().getContentAsString();
		var responseApp = this.objectMapper
				.readValue(responseAppString, new TypeReference<List<ExibeBuscaRestauranteDTO>>(){});

		Assertions.assertEquals(1, responseApp.size());
		Assertions.assertEquals("Restaurante Teste A", responseApp.get(0).nome());
		Assertions.assertEquals("rua teste", responseApp.get(0).logradouro());
		Assertions.assertEquals(10, responseApp.get(0).numero());
		Assertions.assertEquals("14000000", responseApp.get(0).cep());
		Assertions.assertEquals("bairro teste A", responseApp.get(0).bairro());
		Assertions.assertEquals("cidade teste A", responseApp.get(0).cidade());
		Assertions.assertEquals("SP", responseApp.get(0).estado());
		Assertions.assertNull(responseApp.get(0).complemento());
		Assertions.assertEquals(TipoCozinhaEnum.COMIDA_ARABE, responseApp.get(0).tipoCozinha());
		Assertions.assertEquals(List.of(DiasEnum.TODOS).toString(), responseApp.get(0).diasFuncionamento());
		Assertions.assertEquals("18:00 ate 23:00", responseApp.get(0).horarioFuncionamento());
		Assertions.assertEquals(500, responseApp.get(0).capacidadeDePessoas());
	}

	@Test
	public void restaurante_deveRetornar200_informandoApenasTipoCozinha_buscaNaBaseDeDados() throws Exception {

		var restaurante1 = RestauranteEntity.builder()
				.cnpj("49251058000123")
				.nome("Restaurante Teste A")
				.tipoCozinha(TipoCozinhaEnum.COMIDA_ARABE)
				.diasFuncionamento(List.of(DiasEnum.TODOS).toString())
				.horarioFuncionamento("18:00 ate 23:00")
				.capacidadeDePessoas(500)
				.cep("14000000")
				.logradouro("rua teste")
				.numeroEndereco(10)
				.bairro("bairro teste A")
				.cidade("cidade teste A")
				.estado("SP")
				.build();
		var restaurante2 = RestauranteEntity.builder()
				.cnpj("49251058000105")
				.nome("Restaurante bla bla B")
				.tipoCozinha(TipoCozinhaEnum.COMIDA_JAPONESA)
				.diasFuncionamento(List.of(DiasEnum.TODOS).toString())
				.horarioFuncionamento("18:00 ate 23:00")
				.capacidadeDePessoas(500)
				.cep("14000000")
				.logradouro("rua teste")
				.numeroEndereco(10)
				.bairro("bairro teste A")
				.cidade("cidade teste A")
				.estado("SP")
				.build();
		this.repositoryRestaurante.saveAll(List.of(restaurante1, restaurante2));

		var response = this.mockMvc
				.perform(MockMvcRequestBuilders.get(URL_RESTAURANTE)
						.param("tipoCozinha", TipoCozinhaEnum.COMIDA_ARABE.name())
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers
						.status()
						.isOk()
				).andReturn();
		var responseAppString = response.getResponse().getContentAsString();
		var responseApp = this.objectMapper
				.readValue(responseAppString, new TypeReference<List<ExibeBuscaRestauranteDTO>>(){});

		Assertions.assertEquals(1, responseApp.size());
		Assertions.assertEquals("Restaurante Teste A", responseApp.get(0).nome());
		Assertions.assertEquals("rua teste", responseApp.get(0).logradouro());
		Assertions.assertEquals(10, responseApp.get(0).numero());
		Assertions.assertEquals("14000000", responseApp.get(0).cep());
		Assertions.assertEquals("bairro teste A", responseApp.get(0).bairro());
		Assertions.assertEquals("cidade teste A", responseApp.get(0).cidade());
		Assertions.assertEquals("SP", responseApp.get(0).estado());
		Assertions.assertNull(responseApp.get(0).complemento());
		Assertions.assertEquals(TipoCozinhaEnum.COMIDA_ARABE, responseApp.get(0).tipoCozinha());
		Assertions.assertEquals(List.of(DiasEnum.TODOS).toString(), responseApp.get(0).diasFuncionamento());
		Assertions.assertEquals("18:00 ate 23:00", responseApp.get(0).horarioFuncionamento());
		Assertions.assertEquals(500, responseApp.get(0).capacidadeDePessoas());
	}

	@Test
	public void restaurante_deveRetornar200_informandoNenhumCampos_buscaNaBaseDeDados() throws Exception {

		var restaurante1 = RestauranteEntity.builder()
				.cnpj("49251058000123")
				.nome("Restaurante Teste A")
				.tipoCozinha(TipoCozinhaEnum.COMIDA_ARABE)
				.diasFuncionamento(List.of(DiasEnum.TODOS).toString())
				.horarioFuncionamento("18:00 ate 23:00")
				.capacidadeDePessoas(500)
				.cep("14000000")
				.logradouro("rua teste")
				.numeroEndereco(10)
				.bairro("bairro teste A")
				.cidade("cidade teste A")
				.estado("SP")
				.build();
		var restaurante2 = RestauranteEntity.builder()
				.cnpj("49251058000105")
				.nome("Restaurante bla bla B")
				.tipoCozinha(TipoCozinhaEnum.COMIDA_ARABE)
				.diasFuncionamento(List.of(DiasEnum.TODOS).toString())
				.horarioFuncionamento("18:00 ate 23:00")
				.capacidadeDePessoas(500)
				.cep("14000000")
				.logradouro("rua teste")
				.numeroEndereco(10)
				.bairro("bairro teste A")
				.cidade("cidade teste A")
				.estado("SP")
				.build();
		this.repositoryRestaurante.saveAll(List.of(restaurante1, restaurante2));

		var response = this.mockMvc
				.perform(MockMvcRequestBuilders.get(URL_RESTAURANTE)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers
						.status()
						.isOk()
				).andReturn();
		var responseAppString = response.getResponse().getContentAsString();
		var responseApp = this.objectMapper
				.readValue(responseAppString, new TypeReference<List<ExibeBuscaRestauranteDTO>>(){});

		Assertions.assertEquals(2, responseApp.size());
		var restauranteResponse1 = responseApp.get(0).nome()
				.equalsIgnoreCase("Restaurante Teste A") ? responseApp.get(0) : responseApp.get(1);
		var restauranteResponse2 = responseApp.get(1).nome()
				.equalsIgnoreCase("Restaurante bla bla B") ? responseApp.get(1) : responseApp.get(0);
		Assertions.assertEquals("Restaurante Teste A", restauranteResponse1.nome());
		Assertions.assertEquals("rua teste", restauranteResponse1.logradouro());
		Assertions.assertEquals(10, restauranteResponse1.numero());
		Assertions.assertEquals("14000000", restauranteResponse1.cep());
		Assertions.assertEquals("bairro teste A", restauranteResponse1.bairro());
		Assertions.assertEquals("cidade teste A", restauranteResponse1.cidade());
		Assertions.assertEquals("SP", restauranteResponse1.estado());
		Assertions.assertNull(restauranteResponse1.complemento());
		Assertions.assertEquals(TipoCozinhaEnum.COMIDA_ARABE, restauranteResponse1.tipoCozinha());
		Assertions.assertEquals(List.of(DiasEnum.TODOS).toString(), restauranteResponse1.diasFuncionamento());
		Assertions.assertEquals("18:00 ate 23:00", restauranteResponse1.horarioFuncionamento());
		Assertions.assertEquals(500, restauranteResponse1.capacidadeDePessoas());

		Assertions.assertEquals("Restaurante bla bla B", restauranteResponse2.nome());
		Assertions.assertEquals("rua teste", restauranteResponse2.logradouro());
		Assertions.assertEquals(10, restauranteResponse2.numero());
		Assertions.assertEquals("14000000", restauranteResponse2.cep());
		Assertions.assertEquals("bairro teste A", restauranteResponse2.bairro());
		Assertions.assertEquals("cidade teste A", restauranteResponse2.cidade());
		Assertions.assertEquals("SP", restauranteResponse2.estado());
		Assertions.assertNull(restauranteResponse2.complemento());
		Assertions.assertEquals(TipoCozinhaEnum.COMIDA_ARABE, restauranteResponse2.tipoCozinha());
		Assertions.assertEquals(List.of(DiasEnum.TODOS).toString(), restauranteResponse2.diasFuncionamento());
		Assertions.assertEquals("18:00 ate 23:00", restauranteResponse2.horarioFuncionamento());
		Assertions.assertEquals(500, restauranteResponse2.capacidadeDePessoas());
	}

	@Test
	public void restaurante_deveRetornar204_informandoTodosCampos_buscaNaBaseDeDados() throws Exception {

		var restaurante1 = RestauranteEntity.builder()
				.cnpj("49251058000123")
				.nome("Restaurante Teste A")
				.tipoCozinha(TipoCozinhaEnum.COMIDA_ARABE)
				.diasFuncionamento(List.of(DiasEnum.TODOS).toString())
				.horarioFuncionamento("18:00 ate 23:00")
				.capacidadeDePessoas(500)
				.cep("14000000")
				.logradouro("rua teste")
				.numeroEndereco(10)
				.bairro("bairro teste A")
				.cidade("cidade teste A")
				.estado("SP")
				.build();
		var restaurante2 = RestauranteEntity.builder()
				.cnpj("49251058000105")
				.nome("Restaurante bla bla B")
				.tipoCozinha(TipoCozinhaEnum.COMIDA_ARABE)
				.diasFuncionamento(List.of(DiasEnum.TODOS).toString())
				.horarioFuncionamento("18:00 ate 23:00")
				.capacidadeDePessoas(500)
				.cep("14000000")
				.logradouro("rua teste")
				.numeroEndereco(10)
				.bairro("bairro teste A")
				.cidade("cidade teste A")
				.estado("SP")
				.build();
		this.repositoryRestaurante.saveAll(List.of(restaurante1, restaurante2));

		var response = this.mockMvc
				.perform(MockMvcRequestBuilders.get(URL_RESTAURANTE)
						.param("nome", "Restaurante Teste Ole")
						.param("cep", "14000-000")
						.param("bairro", "bairro teste")
						.param("cidade", "cidade teste")
						.param("estado", "SP")
						.param("tipoCozinha", TipoCozinhaEnum.COMIDA_ARABE.name())
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers
						.status()
						.isNoContent()
				).andReturn();
	}

	@ParameterizedTest
	@MethodSource("requestValidandoCamposDeBusca")
	public void restaurante_camposInvalidos_naoBuscaNaBaseDeDados(String nome,
																  String cep,
																  String bairro,
																  String cidade,
																  String estado,
																  TipoCozinhaEnum tipoCozinha) throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.get(URL_RESTAURANTE)
						.param("nome", nome)
						.param("cep", cep)
						.param("bairro", bairro)
						.param("cidade", cidade)
						.param("estado", estado)
						.param("tipoCozinha", tipoCozinha.name())
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers
						.status()
						.isBadRequest()
				);
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

	private static Stream<Arguments> requestValidandoCamposDeBusca() {
		return Stream.of(
				Arguments.of(" ", "14000-000", "bairro teste", "cidade teste", "sp", TipoCozinhaEnum.COMIDA_JAPONESA),
				Arguments.of("aa", "14000-000", "bairro teste", "cidade teste", "sp", TipoCozinhaEnum.COMIDA_JAPONESA),
				Arguments.of("aaaaaaaaaabbbbbbbbbbccccccccccddddddddddeeeeeeeeeef", "14000-000", "bairro teste", "cidade teste", "sp", TipoCozinhaEnum.COMIDA_JAPONESA),
				Arguments.of("Nome de teste", " ", "bairro teste", "cidade teste", "sp", TipoCozinhaEnum.COMIDA_JAPONESA),
				Arguments.of("Nome de teste", "14000000", "bairro teste", "cidade teste", "sp", TipoCozinhaEnum.COMIDA_JAPONESA),
				Arguments.of("Nome de teste", "14000-000", " ", "cidade teste", "sp", TipoCozinhaEnum.COMIDA_JAPONESA),
				Arguments.of("Nome de teste", "14000-000", "aaaa", "cidade teste", "sp", TipoCozinhaEnum.COMIDA_JAPONESA),
				Arguments.of("Nome de teste", "14000-000", "aaaaaaaaaabbbbbbbbbbccccccccccddddddddddeeeeeeeeeef", "cidade teste", "sp", TipoCozinhaEnum.COMIDA_JAPONESA),
				Arguments.of("Nome de teste", "14000-000", "bairro teste", " ", "sp", TipoCozinhaEnum.COMIDA_JAPONESA),
				Arguments.of("Nome de teste", "14000-000", "bairro teste", "aaaa", "sp", TipoCozinhaEnum.COMIDA_JAPONESA),
				Arguments.of("Nome de teste", "14000-000", "bairro teste", "aaaaaaaaaabbbbbbbbbbccccccccccddddddddddeeeeeeeeeef", "sp", TipoCozinhaEnum.COMIDA_JAPONESA),
				Arguments.of("Nome de teste", "14000-000", "bairro teste", "cidade teste", " ", TipoCozinhaEnum.COMIDA_JAPONESA),
				Arguments.of("Nome de teste", "14000-000", "bairro teste", "cidade teste", "a", TipoCozinhaEnum.COMIDA_JAPONESA),
				Arguments.of("Nome de teste", "14000-000", "bairro teste", "cidade teste", "aaa", TipoCozinhaEnum.COMIDA_JAPONESA)
		);
	}

}
