package com.fiap.techchallenge3.integrados.reserva;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiap.techchallenge3.domain.reserva.StatusReservaEnum;
import com.fiap.techchallenge3.domain.restaurante.model.DiasEnum;
import com.fiap.techchallenge3.domain.restaurante.model.TipoCozinhaEnum;
import com.fiap.techchallenge3.infrastructure.reserva.controller.dto.ReservaDTO;
import com.fiap.techchallenge3.infrastructure.reserva.model.ReservaEntity;
import com.fiap.techchallenge3.infrastructure.reserva.repository.ReservaRepository;
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
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

import static com.fiap.techchallenge3.infrastructure.reserva.controller.ReservaController.URL_RESERVA;
import static com.fiap.techchallenge3.infrastructure.reserva.controller.ReservaController.URL_RESERVA_POR_CNPJ;
import static com.fiap.techchallenge3.infrastructure.restaurante.controller.RestauranteController.URL_RESTAURANTE;
import static com.fiap.techchallenge3.utils.RestauranteUtils.*;

@AutoConfigureMockMvc
@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
class ReservaControllerIT {

    @Autowired
    private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	RestauranteRepository repositoryRestaurante;

	@Autowired
	ReservaRepository repositoryReserva;

	@BeforeEach
	void inicializaLimpezaDoDatabase() {
		this.repositoryRestaurante.deleteAll();
		this.repositoryReserva.deleteAll();
	}

	@AfterAll
	void finalizaLimpezaDoDatabase() {
		this.repositoryRestaurante.deleteAll();
		this.repositoryReserva.deleteAll();
	}

	@Test
	public void reserva_deveRetornar201_salvaNaBaseDeDados() throws Exception {
		var mockData = Mockito.mockStatic(LocalDate.class, Mockito.CALLS_REAL_METHODS);
		var dataMock = LocalDate.of(2024, 04, 30);
		mockData.when(LocalDate::now)
				.thenReturn(dataMock);

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

		var request = new ReservaDTO(
				LocalDate.now(),
				"11:00",
				2,
				"12345678901"
		);
		var objectMapper = this.objectMapper
				.writer()
				.withDefaultPrettyPrinter();
		var jsonRequest = objectMapper.writeValueAsString(request);

		this.mockMvc
				.perform(MockMvcRequestBuilders.post(URL_RESERVA_POR_CNPJ.replace("{cnpj}", "49251058000123"))
						.content(jsonRequest)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers
						.status()
						.isCreated()
				);

		var reserva = this.repositoryReserva.findAll().get(0);

		Assertions.assertEquals(1, this.repositoryReserva.findAll().size());
		Assertions.assertEquals("49251058000123", reserva.getCnpjRestaurante());
		Assertions.assertEquals("12345678901", reserva.getCpfCnpjCliente());
		Assertions.assertEquals(LocalDate.of(2024, 04, 30), reserva.getDia());
		Assertions.assertEquals("11:00", reserva.getHorarioDeChegada());
		Assertions.assertEquals(2, reserva.getQuantidadeLugaresClienteDeseja());
		Assertions.assertEquals(StatusReservaEnum.PENDENTE, reserva.getStatusReserva());
		Assertions.assertNotNull(reserva.getHorarioDaReservaRealizada());

		mockData.close();
	}

	@Test
	public void reserva_deveRetornar500_restauranteNaoEncontrado_naoSalvaNaBaseDeDados() throws Exception {
		var mockData = Mockito.mockStatic(LocalDate.class, Mockito.CALLS_REAL_METHODS);
		var dataMock = LocalDate.of(2024, 04, 30);
		mockData.when(LocalDate::now)
				.thenReturn(dataMock);

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

		var request = new ReservaDTO(
				LocalDate.now(),
				"11:00",
				2,
				"12345678901"
		);
		var objectMapper = this.objectMapper
				.writer()
				.withDefaultPrettyPrinter();
		var jsonRequest = objectMapper.writeValueAsString(request);

		this.mockMvc
				.perform(MockMvcRequestBuilders.post(URL_RESERVA_POR_CNPJ.replace("{cnpj}", "49251058000555"))
						.content(jsonRequest)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers
						.status()
						.isInternalServerError()
				);

		Assertions.assertEquals(0, this.repositoryReserva.findAll().size());

		mockData.close();
	}

	@Test
	public void reserva_deveRetornar500_restauranteFechadoNoDia_naoSalvaNaBaseDeDados() throws Exception {
		var mockData = Mockito.mockStatic(LocalDate.class, Mockito.CALLS_REAL_METHODS);
		var dataMock = LocalDate.of(2024, 04, 30);
		mockData.when(LocalDate::now)
				.thenReturn(dataMock);

		var restaurante1 = RestauranteEntity.builder()
				.cnpj("49251058000123")
				.nome("Restaurante Teste A")
				.tipoCozinha(TipoCozinhaEnum.COMIDA_ARABE)
				.diasFuncionamento(List.of(DiasEnum.SEGUNDA_FEIRA, DiasEnum.QUARTA_FEIRA).toString())
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

		var request = new ReservaDTO(
				LocalDate.now(),
				"11:00",
				2,
				"12345678901"
		);
		var objectMapper = this.objectMapper
				.writer()
				.withDefaultPrettyPrinter();
		var jsonRequest = objectMapper.writeValueAsString(request);

		this.mockMvc
				.perform(MockMvcRequestBuilders.post(URL_RESERVA_POR_CNPJ.replace("{cnpj}", "49251058000123"))
						.content(jsonRequest)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers
						.status()
						.isInternalServerError()
				);

		Assertions.assertEquals(0, this.repositoryReserva.findAll().size());

		mockData.close();
	}

	@Test
	public void reserva_deveRetornar500_restauranteFechadoForaDoHorario_naoSalvaNaBaseDeDados() throws Exception {
		var mockData = Mockito.mockStatic(LocalDate.class, Mockito.CALLS_REAL_METHODS);
		var dataMock = LocalDate.of(2024, 04, 29);
		mockData.when(LocalDate::now)
				.thenReturn(dataMock);

		var restaurante1 = RestauranteEntity.builder()
				.cnpj("49251058000123")
				.nome("Restaurante Teste A")
				.tipoCozinha(TipoCozinhaEnum.COMIDA_ARABE)
				.diasFuncionamento(List.of(DiasEnum.SEGUNDA_FEIRA, DiasEnum.QUARTA_FEIRA).toString())
				.horarioFuncionamento("10:00 ate 12:00")
				.capacidadeDePessoas(500)
				.cep("14000000")
				.logradouro("rua teste")
				.numeroEndereco(10)
				.bairro("bairro teste A")
				.cidade("cidade teste A")
				.estado("SP")
				.build();
		this.repositoryRestaurante.save(restaurante1);

		var request = new ReservaDTO(
				LocalDate.now(),
				"12:01",
				2,
				"12345678901"
		);
		var objectMapper = this.objectMapper
				.writer()
				.withDefaultPrettyPrinter();
		var jsonRequest = objectMapper.writeValueAsString(request);

		this.mockMvc
				.perform(MockMvcRequestBuilders.post(URL_RESERVA_POR_CNPJ.replace("{cnpj}", "49251058000123"))
						.content(jsonRequest)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers
						.status()
						.isInternalServerError()
				);

		Assertions.assertEquals(0, this.repositoryReserva.findAll().size());

		mockData.close();
	}

	@Test
	public void reserva_deveRetornar500_restauranteAindaNaoAbriuHorario_naoSalvaNaBaseDeDados() throws Exception {
		var mockData = Mockito.mockStatic(LocalDate.class, Mockito.CALLS_REAL_METHODS);
		var dataMock = LocalDate.of(2024, 04, 29);
		mockData.when(LocalDate::now)
				.thenReturn(dataMock);

		var restaurante1 = RestauranteEntity.builder()
				.cnpj("49251058000123")
				.nome("Restaurante Teste A")
				.tipoCozinha(TipoCozinhaEnum.COMIDA_ARABE)
				.diasFuncionamento(List.of(DiasEnum.SEGUNDA_FEIRA, DiasEnum.QUARTA_FEIRA).toString())
				.horarioFuncionamento("10:00 ate 12:00")
				.capacidadeDePessoas(500)
				.cep("14000000")
				.logradouro("rua teste")
				.numeroEndereco(10)
				.bairro("bairro teste A")
				.cidade("cidade teste A")
				.estado("SP")
				.build();
		this.repositoryRestaurante.save(restaurante1);

		var request = new ReservaDTO(
				LocalDate.now(),
				"09:59",
				2,
				"12345678901"
		);
		var objectMapper = this.objectMapper
				.writer()
				.withDefaultPrettyPrinter();
		var jsonRequest = objectMapper.writeValueAsString(request);

		this.mockMvc
				.perform(MockMvcRequestBuilders.post(URL_RESERVA_POR_CNPJ.replace("{cnpj}", "49251058000123"))
						.content(jsonRequest)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers
						.status()
						.isInternalServerError()
				);

		Assertions.assertEquals(0, this.repositoryReserva.findAll().size());

		mockData.close();
	}

	@Test
	public void reserva_deveRetornar500_restauranteAtingiuCapacidadeMaxima_naoSalvaNaBaseDeDados() throws Exception {
		var mockData = Mockito.mockStatic(LocalDate.class, Mockito.CALLS_REAL_METHODS);
		var dataMock = LocalDate.of(2024, 04, 29);
		mockData.when(LocalDate::now)
				.thenReturn(dataMock);

		var restaurante1 = RestauranteEntity.builder()
				.cnpj("49251058000123")
				.nome("Restaurante Teste A")
				.tipoCozinha(TipoCozinhaEnum.COMIDA_ARABE)
				.diasFuncionamento(List.of(DiasEnum.SEGUNDA_FEIRA, DiasEnum.QUARTA_FEIRA).toString())
				.horarioFuncionamento("10:00 ate 12:00")
				.capacidadeDePessoas(10)
				.cep("14000000")
				.logradouro("rua teste")
				.numeroEndereco(10)
				.bairro("bairro teste A")
				.cidade("cidade teste A")
				.estado("SP")
				.build();
		this.repositoryRestaurante.save(restaurante1);
		var reserva1 = ReservaEntity.builder()
				.cnpjRestaurante("49251058000123")
				.cpfCnpjCliente("11122233344")
				.dia(LocalDate.now())
				.horarioDeChegada("10:01")
				.quantidadeLugaresClienteDeseja(9)
				.statusReserva(StatusReservaEnum.RESERVADO)
				.build();
		this.repositoryRestaurante.save(restaurante1);
		this.repositoryReserva.save(reserva1);

		var request = new ReservaDTO(
				LocalDate.now(),
				"11:00",
				2,
				"12345678901"
		);
		var objectMapper = this.objectMapper
				.writer()
				.withDefaultPrettyPrinter();
		var jsonRequest = objectMapper.writeValueAsString(request);

		this.mockMvc
				.perform(MockMvcRequestBuilders.post(URL_RESERVA_POR_CNPJ.replace("{cnpj}", "49251058000123"))
						.content(jsonRequest)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers
						.status()
						.isInternalServerError()
				);

		Assertions.assertEquals(1, this.repositoryReserva.findAll().size());

		mockData.close();
	}

	@Test
	public void reserva_deveRetornar201_passouPorTodosRequisitos_salvaNaBaseDeDados() throws Exception {
		var mockData = Mockito.mockStatic(LocalDate.class, Mockito.CALLS_REAL_METHODS);
		var dataMock = LocalDate.of(2024, 04, 29);
		mockData.when(LocalDate::now)
				.thenReturn(dataMock);

		var restaurante1 = RestauranteEntity.builder()
				.cnpj("49251058000123")
				.nome("Restaurante Teste A")
				.tipoCozinha(TipoCozinhaEnum.COMIDA_ARABE)
				.diasFuncionamento(List.of(DiasEnum.SEGUNDA_FEIRA, DiasEnum.QUARTA_FEIRA).toString())
				.horarioFuncionamento("10:00 ate 12:00")
				.capacidadeDePessoas(100)
				.cep("14000000")
				.logradouro("rua teste")
				.numeroEndereco(10)
				.bairro("bairro teste A")
				.cidade("cidade teste A")
				.estado("SP")
				.build();
		this.repositoryRestaurante.save(restaurante1);
		var reserva1 = ReservaEntity.builder()
				.cnpjRestaurante("49251058000123")
				.cpfCnpjCliente("11122233344")
				.dia(LocalDate.now())
				.horarioDeChegada("10:01")
				.quantidadeLugaresClienteDeseja(9)
				.statusReserva(StatusReservaEnum.RESERVADO)
				.build();
		this.repositoryRestaurante.save(restaurante1);
		this.repositoryReserva.save(reserva1);

		var request = new ReservaDTO(
				LocalDate.now(),
				"11:00",
				2,
				"12345678901"
		);
		var objectMapper = this.objectMapper
				.writer()
				.withDefaultPrettyPrinter();
		var jsonRequest = objectMapper.writeValueAsString(request);

		this.mockMvc
				.perform(MockMvcRequestBuilders.post(URL_RESERVA_POR_CNPJ.replace("{cnpj}", "49251058000123"))
						.content(jsonRequest)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers
						.status()
						.isCreated()
				);

		var reservas = this.repositoryReserva.findAll();
		var reservaResponse1 = reservas.get(0).getCpfCnpjCliente()
				.equalsIgnoreCase("11122233344") ? reservas.get(0) : reservas.get(1);
		var reservaResponse2 = reservas.get(1).getCpfCnpjCliente()
				.equalsIgnoreCase("12345678901") ? reservas.get(1) : reservas.get(0);

		Assertions.assertEquals(2, this.repositoryReserva.findAll().size());
		Assertions.assertEquals("49251058000123", reservaResponse1.getCnpjRestaurante());
		Assertions.assertEquals("11122233344", reservaResponse1.getCpfCnpjCliente());
		Assertions.assertEquals(LocalDate.of(2024, 04, 29), reservaResponse1.getDia());
		Assertions.assertEquals("10:01", reservaResponse1.getHorarioDeChegada());
		Assertions.assertEquals(9, reservaResponse1.getQuantidadeLugaresClienteDeseja());
		Assertions.assertEquals(StatusReservaEnum.RESERVADO, reservaResponse1.getStatusReserva());
		Assertions.assertNotNull(reservaResponse1.getHorarioDaReservaRealizada());

		Assertions.assertEquals("49251058000123", reservaResponse2.getCnpjRestaurante());
		Assertions.assertEquals("12345678901", reservaResponse2.getCpfCnpjCliente());
		Assertions.assertEquals(LocalDate.of(2024, 04, 29), reservaResponse2.getDia());
		Assertions.assertEquals("11:00", reservaResponse2.getHorarioDeChegada());
		Assertions.assertEquals(2, reservaResponse2.getQuantidadeLugaresClienteDeseja());
		Assertions.assertEquals(StatusReservaEnum.PENDENTE, reservaResponse2.getStatusReserva());
		Assertions.assertNotNull(reservaResponse2.getHorarioDaReservaRealizada());

		mockData.close();
	}

	@ParameterizedTest
	@MethodSource("requestValidandoCampos")
	public void restaurante_deveRetornar400_camposInvalidos_naoSalvaNaBaseDeDados(String cnpj,
																				  LocalDate dia,
																				  String horarioDeChegada,
																				  int quantidadeLugares,
																				  String cpfCnpjCliente) throws Exception {
		var request = new ReservaDTO(
				dia,
				horarioDeChegada,
				quantidadeLugares,
				cpfCnpjCliente
		);
		var objectMapper = this.objectMapper
				.writer()
				.withDefaultPrettyPrinter();
		var jsonRequest = objectMapper.writeValueAsString(request);

		this.mockMvc
				.perform(MockMvcRequestBuilders.post(URL_RESERVA_POR_CNPJ.replace("{cnpj}", cnpj))
						.content(jsonRequest)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers
						.status()
						.isBadRequest()
				);
		Assertions.assertEquals(0, this.repositoryReserva.findAll().size());
	}

	private static Stream<Arguments> requestValidandoCampos() {
		return Stream.of(
				Arguments.of("04623021000114", null, "11:15", 5, "11122233344"),
				Arguments.of("04623021000114", LocalDate.now(), null, 5, "11122233344"),
				Arguments.of("04623021000114", LocalDate.now(), "", 5, "11122233344"),
				Arguments.of("04623021000114", LocalDate.now(), "11:15", 5, null),
				Arguments.of("04623021000114", LocalDate.now(), "11:15", 5, ""),

				Arguments.of("aa", LocalDate.now(), "11:15", 5, "11122233344"),
				Arguments.of("04623021000114", LocalDate.now().minusDays(10), "11:15", 5, "11122233344"),
				Arguments.of("04623021000114", LocalDate.now(), "11", 5, "11122233344"),
				Arguments.of("04623021000114", LocalDate.now(), "11:15", 0, "11122233344"),
				Arguments.of("04623021000114", LocalDate.now(), "11:15", 51, "11122233344"),
				Arguments.of("04623021000114", LocalDate.now(), "11:15", 1, "aa")
		);
	}

}
