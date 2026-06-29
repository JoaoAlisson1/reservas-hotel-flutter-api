package br.csi.dormez_back_api.repository;

import br.csi.dormez_back_api.model.Hospede;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HospedeRepository extends JpaRepository<Hospede, Long> {
    Optional<Hospede> findByUuid(String uuid);
    Optional<Hospede> findByCpf(String cpf);
    Optional<Hospede> findByEmail(String email);
}
