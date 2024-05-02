package com.fiap.techchallenge3.useCase.reserva;

import com.fiap.techchallenge3.domain.reserva.StatusReservaEnum;
import com.fiap.techchallenge3.infrastructure.reserva.controller.dto.ExibeReservasPendentesDTO;
import com.fiap.techchallenge3.infrastructure.reserva.controller.dto.ReservaDTO;

public interface ReservaUseCase {

    void reserva(final String cnpj,
                 final ReservaDTO dadosReserva);

    void atualizaReserva(final Long idDaReserva,
                         final StatusReservaEnum status);

    ExibeReservasPendentesDTO buscaReservasPendentesDoDia(final String cnpj);

}
