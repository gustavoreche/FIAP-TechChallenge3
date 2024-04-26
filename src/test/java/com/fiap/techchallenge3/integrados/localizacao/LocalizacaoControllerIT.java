package com.fiap.techchallenge3.integrados.localizacao;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiap.techchallenge3.infrastructure.localizacao.client.ViaCepClient;
import com.fiap.techchallenge3.infrastructure.localizacao.client.ViaCepResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.stream.Stream;

import static com.fiap.techchallenge3.infrastructure.localizacao.controller.LocalizacaoController.URL_LOCALIZACAO_POR_CEP;

@AutoConfigureMockMvc
@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
class LocalizacaoControllerIT {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	@MockBean
	ViaCepClient client;

	@Test
	public void localizacao_existe_deveRetornarStatus200() throws Exception {
		// preparação
		Mockito.when(this.client.pegaLocalizacao("14000000"))
				.thenReturn(new ViaCepResponse(
						"Avenida dos testes",
						"14000-000",
						"Bairro teste",
						"Cidade do teste",
						"SP"
					)
				);

		// execução e avaliação
		var response = this.mockMvc
				.perform(MockMvcRequestBuilders.get(URL_LOCALIZACAO_POR_CEP.replace("{cep}", "14000-000"))
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers
						.status()
						.isOk()
				)
				.andReturn();
		var responseAppString = response.getResponse().getContentAsString();
		var responseApp = this.objectMapper
				.readValue(responseAppString, ViaCepResponse.class);
		Assertions.assertEquals("Avenida dos testes", responseApp.logradouro());
		Assertions.assertEquals("14000-000", responseApp.cep());
		Assertions.assertEquals("Bairro teste", responseApp.bairro());
		Assertions.assertEquals("Cidade do teste", responseApp.cidade());
		Assertions.assertEquals("SP", responseApp.estado());
	}

	@Test
	public void localizacao_naoExiste_deveRetornarStatus500() throws Exception {
		// preparação
		Mockito.when(this.client.pegaLocalizacao("14000001"))
				.thenReturn(new ViaCepResponse(
						null,
						null,
						null,
						null,
						null
						)
				);

		// execução e avaliação
		var response = this.mockMvc
				.perform(MockMvcRequestBuilders.get(URL_LOCALIZACAO_POR_CEP.replace("{cep}", "14000-001"))
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers
						.status()
						.isInternalServerError()
				)
				.andReturn();
		var responseAppString = response.getResponse().getContentAsString();
		Assertions.assertEquals("ENDEREÇO NAO EXISTE!", responseAppString);
	}

	@Test
	public void localizacao_apiRestIndisponivel_deveRetornarStatus500() throws Exception {
		// preparação
		Mockito.when(this.client.pegaLocalizacao("14000001"))
				.thenThrow(
						new RuntimeException("API REST Indisponível")
				);

		// execução e avaliação
		var response = this.mockMvc
				.perform(MockMvcRequestBuilders.get(URL_LOCALIZACAO_POR_CEP.replace("{cep}", "14000-001"))
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers
						.status()
						.isInternalServerError()
				)
				.andReturn();
		var responseAppString = response.getResponse().getContentAsString();
		Assertions.assertEquals("API REST Indisponível", responseAppString);
	}

	@ParameterizedTest
	@MethodSource("requestValidandoCampo")
	public void validacaoDoCampo_deveRetornarStatus400(String cep) throws Exception {
		// execução e avaliação
		this.mockMvc
				.perform(MockMvcRequestBuilders.get(URL_LOCALIZACAO_POR_CEP.replace("{cep}", cep))
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers
						.status()
						.isBadRequest()
				);
	}

	private static Stream<Arguments> requestValidandoCampo() {
		// preparação
		return Stream.of(
				Arguments.of("   "),
				Arguments.of("a"),
				Arguments.of("aaaaaaaaaa"),
				Arguments.of("1234"),
				Arguments.of("1"),
				Arguments.of("14025710"),
				Arguments.of("14025-7101"),
				Arguments.of("114025-710")
		);
	}

}
