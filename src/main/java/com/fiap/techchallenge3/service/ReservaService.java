package com.fiap.techchallenge3.service;

import com.fiap.techchallenge3.model.dto.ReservaDTO;

public interface ReservaService {

    void reserva(String cnpj,
                 ReservaDTO dadosReserva);

}
