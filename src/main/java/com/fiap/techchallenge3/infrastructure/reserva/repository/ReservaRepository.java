package com.fiap.techchallenge3.infrastructure.reserva.repository;

import com.fiap.techchallenge3.domain.reserva.StatusReservaEnum;
import com.fiap.techchallenge3.infrastructure.reserva.model.ReservaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;

public interface ReservaRepository extends JpaRepository<ReservaEntity, Long> {

    @Query("SELECT SUM(r.quantidadeLugaresClienteDeseja) FROM ReservaEntity r WHERE r.dia = :dia AND r.statusReserva = :statusReserva")
    Integer sumByDiaAndStatusReserva(final LocalDate dia,
                                     final StatusReservaEnum statusReserva);

}
