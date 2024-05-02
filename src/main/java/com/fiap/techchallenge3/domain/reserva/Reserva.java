package com.fiap.techchallenge3.domain.reserva;

import com.fiap.techchallenge3.domain.restaurante.model.DiasEnum;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Objects;

import static com.fiap.techchallenge3.domain.restaurante.model.Cnpj.REGEX_CNPJ;

public record Reserva(
        LocalDate dia,
        String horarioDeChegada,
        int quantidadeLugares,
        String cpfCnpjCliente
)
{
    public static final String REGEX_HORARIO_CHEGADA = "^\\d{2}:\\d{2}$";
    public static final String REGEX_CPF_OU_CNPJ = "(^\\d{3}\\.?\\d{3}\\.?\\d{3}-?\\d{2}$|"
            .concat(REGEX_CNPJ)
            .concat(")");

    public Reserva {
        if (Objects.isNull(dia)) {
            throw new IllegalArgumentException("DIA NAO PODE SER NULO OU VAZIO!");
        }
        if (dia.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("DIA NAO PODE SER ANTERIOR AO DIA ATUAL!");
        }

        if (Objects.isNull(horarioDeChegada) || horarioDeChegada.isEmpty()) {
            throw new IllegalArgumentException("HORARIO DE CHEGADA NAO PODE SER NULO OU VAZIO!");
        }
        if (!horarioDeChegada.matches(REGEX_HORARIO_CHEGADA)) {
            throw new IllegalArgumentException("HORARIO DE CHEGADA INVÁLIDO!");
        }

        if (quantidadeLugares < 1 || quantidadeLugares > 50) {
            throw new IllegalArgumentException("A quantidade de lugares deve ter no mínimo 1 ou no máximo 50");
        }

        if (Objects.isNull(cpfCnpjCliente) || cpfCnpjCliente.isEmpty()) {
            throw new IllegalArgumentException("CPF OU CNPJ DO CLIENTE NAO PODE SER NULO OU VAZIO!");
        }
        if (!cpfCnpjCliente.matches(REGEX_CPF_OU_CNPJ)) {
            throw new IllegalArgumentException("CPF OU CNPJ DO CLIENTE INVÁLIDO!");
        }

    }

    public boolean restauranteEstaFechado(String diasFuncionamento,
                                         String horarioFuncionamento) {
        return this.estaFechadoNoDia(diasFuncionamento) || this.estaFechadoNoHorario(horarioFuncionamento);
    }

    private boolean estaFechadoNoDia(String diasFuncionamento) {
        if(diasFuncionamento.contains("TODOS")) {
            return false;
        }
        var reservaFeitaNoDiaAberto = Arrays
                .stream(diasFuncionamento.split(","))
                .map(dia -> {
                    return DiasEnum
                            .valueOf(dia
                                    .trim()
                                    .replace("[", "")
                                    .replace("]", "")
                            );
                })
                .filter(diasEnum -> this.dia.getDayOfWeek().equals(diasEnum.getDia()))
                .toList();
        return reservaFeitaNoDiaAberto.isEmpty();
    }

    private boolean estaFechadoNoHorario(String horarioFuncionamento) {
        var horarios = horarioFuncionamento.split("ate");
        if(horarios.length == 2) {
            var horarioAbertura = horarios[0].trim();
            var horarioFechamento = horarios[1].trim();
            var abertura = LocalTime.parse(horarioAbertura);
            var fechamento = LocalTime.parse(horarioFechamento);
            var horarioReserva = LocalTime.parse(this.horarioDeChegada);
            return (horarioReserva.isBefore(abertura) || horarioReserva.isAfter(fechamento));
        }
        return false;
    }

    public boolean restauranteEstaLotado(int quantidadeLugaresJaReservados,
                                         int capacidadeDePessoas) {
        return quantidadeLugaresJaReservados + this.quantidadeLugares > capacidadeDePessoas;
    }
}
