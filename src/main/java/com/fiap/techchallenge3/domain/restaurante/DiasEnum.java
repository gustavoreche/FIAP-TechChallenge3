package com.fiap.techchallenge3.domain.restaurante;

import lombok.Getter;

import java.time.DayOfWeek;

@Getter
public enum DiasEnum {

    DOMINGO(DayOfWeek.SUNDAY),
    SEGUNDA_FEIRA(DayOfWeek.MONDAY),
    TERCA_FEIRA(DayOfWeek.TUESDAY),
    QUARTA_FEIRA(DayOfWeek.WEDNESDAY),
    QUINTA_FEIRA(DayOfWeek.THURSDAY),
    SEXTA_FEIRA(DayOfWeek.FRIDAY),
    SABADO(DayOfWeek.SATURDAY),
    TODOS(null)
    ;

    private final DayOfWeek dia;

    DiasEnum(DayOfWeek dia) {
        this.dia = dia;
    }
}
