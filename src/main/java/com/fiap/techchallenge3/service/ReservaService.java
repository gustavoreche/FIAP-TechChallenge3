package com.fiap.techchallenge3.service;

import com.fiap.techchallenge3.model.StatusReservaEnum;
import com.fiap.techchallenge3.model.dto.ExibeReservasPendentesDTO;
import com.fiap.techchallenge3.model.dto.ReservaDTO;

public interface ReservaService {

    void reserva(final String cnpj,
                 final ReservaDTO dadosReserva);

    void atualizaReserva(final String cnpj,
                         final StatusReservaEnum status);

    ExibeReservasPendentesDTO buscaReservasPendentesDoDia(final String cnpj);

}
