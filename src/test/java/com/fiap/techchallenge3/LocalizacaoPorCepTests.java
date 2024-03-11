package com.fiap.techchallenge3;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiap.techchallenge3.client.ViaCepClient;
import com.fiap.techchallenge3.model.dto.LocalizacaoDTO;
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

import static com.fiap.techchallenge3.controller.LocalizacaoController.URL_LOCALIZACAO_POR_CEP;

@AutoConfigureMockMvc
@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
class LocalizacaoPorCepTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	@MockBean
	ViaCepClient client;

	@Test
	public void deveRetornarStatus200_cepExistente() throws Exception {
		Mockito.when(this.client.getAddressByCep("14000000"))
				.thenReturn(new LocalizacaoDTO(
						"Avenida dos testes",
						"14000-000",
						"Bairro teste",
						"Cidade do teste",
						"SP"
					)
				);

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
				.readValue(responseAppString, LocalizacaoDTO.class);

		Assertions.assertEquals("Avenida dos testes", responseApp.logradouro());
		Assertions.assertEquals("14000-000", responseApp.cep());
		Assertions.assertEquals("Bairro teste", responseApp.bairro());
		Assertions.assertEquals("Cidade do teste", responseApp.cidade());
		Assertions.assertEquals("SP", responseApp.estado());
	}

	@Test
	public void deveRetornarStatus500_cepNaoExistente() throws Exception {
		Mockito.when(this.client.getAddressByCep("14000001"))
				.thenReturn(new LocalizacaoDTO(
						null,
						null,
						null,
						null,
						null
						)
				);

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

	@ParameterizedTest
	@MethodSource("requestValidandoCampo")
	public void deveRetornarStatus400_validacaoDoCampo(String cep) throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.get(URL_LOCALIZACAO_POR_CEP.replace("{cep}", cep))
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers
						.status()
						.isBadRequest()
				);
	}

	private static Stream<Arguments> requestValidandoCampo() {
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
