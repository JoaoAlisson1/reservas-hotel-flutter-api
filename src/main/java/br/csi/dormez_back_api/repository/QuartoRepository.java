package br.csi.dormez_back_api.repository;

import br.csi.dormez_back_api.model.Quarto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface QuartoRepository extends JpaRepository<Quarto, Long> {
    Optional<Quarto> findByNumero(Integer numero);
    Optional<Quarto> findByUuid(String uuid);
}
