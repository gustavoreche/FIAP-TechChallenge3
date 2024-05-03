package com.fiap.techchallenge3.infrastructure.restaurante.controller.dto;

import com.fiap.techchallenge3.domain.restaurante.TipoCozinhaEnum;

public record ExibeBuscaRestauranteDTO(

		String nome,
		String logradouro,
		Integer numero,
		String cep,
		String bairro,
		String cidade,
		String estado,
		String complemento,
		TipoCozinhaEnum tipoCozinha,
		String diasFuncionamento,
		String horarioFuncionamento,
		Integer capacidadeDePessoas
) {}
