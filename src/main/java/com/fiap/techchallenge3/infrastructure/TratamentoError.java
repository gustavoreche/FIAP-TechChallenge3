package com.fiap.techchallenge3.infrastructure;

import com.fiap.techchallenge3.domain.restaurante.exception.HorarioInvalidoException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class TratamentoError {

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(HorarioInvalidoException.class)
	public String trataErroDeHorarioInvalido(HorarioInvalidoException ex) {
		return "Erro na definição dos horarios... Exemplo de como deve ser: 22:10. Segue o valor errado: " + ex.getMessage();
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(IllegalArgumentException.class)
	public String trataParametroInvalido(IllegalArgumentException ex) {
		return ex.getMessage();
	}

	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(Exception.class)
	public String trataErroNaoMapeado(Exception ex) {
		return ex.getMessage();
	}

}
