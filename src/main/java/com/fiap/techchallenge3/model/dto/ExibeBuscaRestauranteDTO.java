package com.fiap.techchallenge3.model.dto;

import com.fiap.techchallenge3.model.TipoCozinhaEnum;

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
