package com.fiap.techchallenge3.useCase.reserva.impl;

import com.fiap.techchallenge3.domain.reserva.CpfCnpj;
import com.fiap.techchallenge3.domain.reserva.Reserva;
import com.fiap.techchallenge3.domain.reserva.StatusReservaEnum;
import com.fiap.techchallenge3.domain.restaurante.Cnpj;
import com.fiap.techchallenge3.infrastructure.reserva.controller.dto.ExibeReservasPendentesDTO;
import com.fiap.techchallenge3.infrastructure.reserva.controller.dto.ReservaDTO;
import com.fiap.techchallenge3.infrastructure.reserva.model.ReservaEntity;
import com.fiap.techchallenge3.infrastructure.reserva.repository.ReservaRepository;
import com.fiap.techchallenge3.infrastructure.restaurante.repository.RestauranteRepository;
import com.fiap.techchallenge3.useCase.reserva.ReservaUseCase;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class ReservaUseCaseImpl implements ReservaUseCase {

    private final ReservaRepository repositoryReserva;
    private final RestauranteRepository repositoryRestaurante;

    public ReservaUseCaseImpl(final ReservaRepository repositoryReserva,
                              final RestauranteRepository repositoryRestaurante) {
        this.repositoryReserva = repositoryReserva;
        this.repositoryRestaurante = repositoryRestaurante;
    }

    @Override
    public void reserva(final String cnpj,
                        final ReservaDTO dadosReserva) {
        var cnpjObject = new Cnpj(cnpj);
        var cpfCnpjObject = new CpfCnpj(dadosReserva.cpfCnpjCliente());
        var reserva = new Reserva(
                dadosReserva.dia(),
                dadosReserva.horarioDeChegada(),
                dadosReserva.quantidadeLugares(),
                cpfCnpjObject.numero()
        );

        var restaurante = this.repositoryRestaurante.findById(cnpjObject.numero());
        if (restaurante.isEmpty()) {
            throw new RuntimeException("Restaurante não encontrado");
        }
        var restauranteEntity = restaurante.get();
        if(reserva.restauranteEstaFechado(restauranteEntity.getDiasFuncionamento(), restauranteEntity.getHorarioFuncionamento())) {
            throw new RuntimeException("Restaurante não está aberto no período da reserva");
        }

        var quantidadeLugaresJaReservados = this.repositoryReserva.sumByDiaAndStatusReserva(reserva.dia(), StatusReservaEnum.RESERVADO);
        if(Objects.nonNull(quantidadeLugaresJaReservados) &&
                reserva.restauranteEstaLotado(quantidadeLugaresJaReservados, restauranteEntity.getCapacidadeDePessoas())) {
            throw new RuntimeException("Restaurante não tem reservas disponíveis para o dia e horário solicitado");
        }

        var reservaEntity = ReservaEntity.builder()
                .cnpjRestaurante(cnpjObject.numero())
                .cpfCnpjCliente(reserva.cpfCnpjCliente())
                .dia(reserva.dia())
                .horarioDeChegada(reserva.horarioDeChegada())
                .quantidadeLugaresClienteDeseja(reserva.quantidadeLugares())
                .statusReserva(StatusReservaEnum.PENDENTE)
                .horarioDaReservaRealizada(LocalDateTime.now())
                .build();
        this.repositoryReserva.save(reservaEntity);
    }

    @Override
    public void atualizaReserva(final Long idDaReserva,
                                final StatusReservaEnum status) {
        var reserva = this.repositoryReserva.findById(idDaReserva);
        if (reserva.isEmpty()) {
            throw new RuntimeException("Reserva não encontrada");
        }
        var reservaEntity = reserva.get();
        reservaEntity.setStatusReserva(status);
        this.repositoryReserva.save(reservaEntity);
    }

    @Override
    public List<ExibeReservasPendentesDTO> buscaReservasPendentesDoDia(final String cnpj) {
        var cnpjObject = new Cnpj(cnpj);
        var reservasDoDia = this.repositoryReserva.findByCnpjRestauranteAndDiaAndStatusReserva(
                cnpjObject.numero(),
                LocalDate.now(),
                StatusReservaEnum.PENDENTE
        );
        if (reservasDoDia.isEmpty()) {
            throw new RuntimeException("Sem reservas para o restaurante no dia de hoje");
        }
        return reservasDoDia
                .stream()
                .map(registro -> new ExibeReservasPendentesDTO(
                        registro.getId(),
                        registro.getQuantidadeLugaresClienteDeseja(),
                        registro.getHorarioDaReservaRealizada()
                    )
                )
                .toList();
    }

}
