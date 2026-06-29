package br.csi.dormez_back_api.repository;

import br.csi.dormez_back_api.model.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {
    boolean existsByQuartoId(Long quartoId);
}
