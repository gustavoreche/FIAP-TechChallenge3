package com.fiap.techchallenge3.service.impl;

import com.fiap.techchallenge3.model.StatusReservaEnum;
import com.fiap.techchallenge3.model.dto.ExibeReservasPendentesDTO;
import com.fiap.techchallenge3.model.dto.ReservaDTO;
import com.fiap.techchallenge3.repository.ReservaRepository;
import com.fiap.techchallenge3.service.ReservaService;
import org.springframework.stereotype.Service;

@Service
public class ReservaServiceImpl implements ReservaService {

    private final ReservaRepository repositoryReserva;

    public ReservaServiceImpl(final ReservaRepository repositoryReserva) {
        this.repositoryReserva = repositoryReserva;
    }

    @Override
    public void reserva(final String cnpj,
                        final ReservaDTO dadosReserva) {
        this.repositoryReserva.save(dadosReserva.converte(cnpj));
    }

    @Override
    public void atualizaReserva(final String cnpj,
                                final StatusReservaEnum status) {
//        this.repositoryReserva.save(dadosReserva.converte(cnpj));
    }

    @Override
    public ExibeReservasPendentesDTO buscaReservasPendentesDoDia(final String cnpj) {
//        this.repositoryReserva.save(dadosReserva.converte(cnpj));
        return null;
    }

}
