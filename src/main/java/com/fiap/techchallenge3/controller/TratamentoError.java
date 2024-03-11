package com.fiap.techchallenge3.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class TratamentoError {
	
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public Map<String, String> trataErroNaValidacao(final MethodArgumentNotValidException erros) {
	    final var camposDeErro = new HashMap<String, String>();
		erros.getBindingResult()
				.getAllErrors()
				.forEach(error -> {
					var nomeCampo = ((FieldError) error).getField();
					var mensagemErro = error.getDefaultMessage();
					camposDeErro.put(nomeCampo, mensagemErro);
				});
		return camposDeErro;
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler({HttpMessageNotReadableException.class, HandlerMethodValidationException.class, MethodArgumentTypeMismatchException.class})
	public String trataErroNaValidacao(Exception ex) {
		return "Erro de valor nos campos... O campo espera um valor(texto, número...) e foi passado outro valor. Segue o valor errado: " + ex.getMessage();
	}

	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(Exception.class)
	public String trataErroNaoMapeado(Exception ex) {
		return ex.getMessage();
	}

}
