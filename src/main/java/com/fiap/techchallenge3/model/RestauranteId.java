package com.fiap.techchallenge3.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Embeddable;
import jakarta.persistence.OneToOne;
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
	@OneToOne(cascade = CascadeType.ALL)
	private RestauranteLocalizacao localizacao;

}
