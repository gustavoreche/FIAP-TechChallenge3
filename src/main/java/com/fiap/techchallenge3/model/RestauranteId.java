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
public class RestauranteId {

	private String cnpj;
	private int numeroEndereco;
	private String cep;

}
