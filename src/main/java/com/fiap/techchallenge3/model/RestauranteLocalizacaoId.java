package com.fiap.techchallenge3.model;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class RestauranteLocalizacaoId {
	
	private String nome;
	private String email;

}
