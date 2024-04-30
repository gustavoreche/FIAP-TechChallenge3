package com.fiap.techchallenge3.utils;

import com.fiap.techchallenge3.domain.restaurante.model.DiasEnum;
import com.fiap.techchallenge3.infrastructure.restaurante.controller.dto.EnderecoCompletoDTO;
import com.fiap.techchallenge3.infrastructure.restaurante.controller.dto.HorarioDeFuncionamentoDTO;

import java.util.List;

public class RestauranteUtils {

    public static EnderecoCompletoDTO localizacaoDefault() {
        return new EnderecoCompletoDTO(
                "rua teste",
                10,
                "14000-000",
                "bairro teste",
                "cidade teste",
                "SP",
                "ap1122"
        );
    }

    public static EnderecoCompletoDTO criaLocalizacao(String logradouro,
                                                       Integer numero,
                                                       String cep,
                                                       String bairro,
                                                       String cidade,
                                                       String estado,
                                                       String complemento) {
        return new EnderecoCompletoDTO(
                logradouro,
                numero,
                cep,
                bairro,
                cidade,
                estado,
                complemento
        );
    }

    public static HorarioDeFuncionamentoDTO horarioFuncionamentoDefault() {
        return new HorarioDeFuncionamentoDTO(
                List.of(DiasEnum.TODOS),
                "18:00 ate 23:00"
        );
    }

    public static HorarioDeFuncionamentoDTO criaHorarioFuncionamento(List<DiasEnum> diasAbertos,
                                                                      String horarioFuncionamento) {
        return new HorarioDeFuncionamentoDTO(
                diasAbertos,
                horarioFuncionamento
        );
    }

}
