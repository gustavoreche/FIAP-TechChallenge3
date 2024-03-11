package com.fiap.techchallenge3.model;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class RestauranteLocalizacaoId {
	
	private String logradouro;
	private String numero;
	private int cep;

}
