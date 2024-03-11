package com.fiap.techchallenge3;

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

import java.util.stream.Stream;

import static com.fiap.techchallenge3.controller.LocalizacaoController.URL_LOCALIZACAO_POR_CEP;

@AutoConfigureMockMvc
@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
class LocalizacaoPorCepTests {

    @Autowired
    private MockMvc mockMvc;

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
