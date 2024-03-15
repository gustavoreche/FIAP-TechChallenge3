package com.fiap.techchallenge3.model;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RestauranteLocalizacaoId {
	
	private String logradouro;
	private int numero;
	private String cep;

}
