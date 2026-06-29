package br.csi.dormez_back_api.repository;

import br.csi.dormez_back_api.model.Funcionario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FuncionarioRepository extends JpaRepository<Funcionario, Long> {
    Optional<Funcionario> findByUuid(String uuid);
    Optional<Funcionario> findByEmail(String email);
}
