package br.csi.dormez_back_api.Service;

import br.csi.dormez_back_api.dto.FuncionarioDTO;
import br.csi.dormez_back_api.model.Funcionario;
import br.csi.dormez_back_api.repository.FuncionarioRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class FuncionarioService {
    private final FuncionarioRepository repository;

    public FuncionarioService(FuncionarioRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public FuncionarioDTO salvar(FuncionarioDTO dto) {

        if (dto.getId() == null) {
            if (repository.findByEmail(dto.getEmail()).isPresent()) {

                throw new IllegalArgumentException("Este e-mail já está cadastrado para outro funcionário.");
            }

            if (dto.getUuid() == null || dto.getUuid().trim().isEmpty()) {
                dto.setUuid(UUID.randomUUID().toString());
            }
        } else {

            Optional<Funcionario> existente = repository.findByEmail(dto.getEmail());
            if (existente.isPresent() && !existente.get().getId().equals(dto.getId())) {
                throw new IllegalArgumentException("Não foi possível atualizar: este e-mail já está em uso.");
            }
        }

        Funcionario funcionario = Funcionario.builder()
                .id(dto.getId())
                .uuid(dto.getUuid())
                .nome(dto.getNome())
                .email(dto.getEmail())
                .telefone(dto.getTelefone())
                .build();

        Funcionario salvo = repository.save(funcionario);
        return entityToDto(salvo);
    }

    public List<FuncionarioDTO> listarTodos() {
        return repository.findAll().stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deletar(Long id) {
        try {
            repository.deleteById(id);
            repository.flush();
        } catch (DataIntegrityViolationException e) {

            throw new IllegalStateException("Não é possível excluir este funcionário pois ele possui registros vinculados.");
        }
    }

    private FuncionarioDTO entityToDto(Funcionario f) {
        return new FuncionarioDTO(f.getId(), f.getUuid(), f.getNome(), f.getEmail(), f.getTelefone());
    }
}
