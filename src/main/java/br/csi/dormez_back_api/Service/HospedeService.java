package br.csi.dormez_back_api.Service;

import br.csi.dormez_back_api.dto.HospedeDTO;
import br.csi.dormez_back_api.model.Hospede;
import br.csi.dormez_back_api.repository.HospedeRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class HospedeService {

    private final HospedeRepository repository;

    public HospedeService(HospedeRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public HospedeDTO salvar(HospedeDTO dto) {

        if (dto.getId() == null) {
            if (repository.findByCpf(dto.getCpf()).isPresent()) {

                throw new IllegalArgumentException("Este CPF já está cadastrado.");
            }
            if (repository.findByEmail(dto.getEmail()).isPresent()) {
                throw new IllegalArgumentException("Este e-mail já está em uso.");
            }

            if (dto.getUuid() == null || dto.getUuid().trim().isEmpty()) {
                dto.setUuid(UUID.randomUUID().toString());
            }
        } else {

            Optional<Hospede> hospedeCpf = repository.findByCpf(dto.getCpf());
            if (hospedeCpf.isPresent() && !hospedeCpf.get().getId().equals(dto.getId())) {
                throw new IllegalArgumentException("Conflito de dados: Este CPF já pertence a outro hóspede.");
            }

            Optional<Hospede> hospedeEmail = repository.findByEmail(dto.getEmail());
            if (hospedeEmail.isPresent() && !hospedeEmail.get().getId().equals(dto.getId())) {
                throw new IllegalArgumentException("Conflito de dados: Este e-mail já pertence a outro hóspede.");
            }
        }

        Hospede hospede = Hospede.builder()
                .id(dto.getId())
                .uuid(dto.getUuid())
                .nome(dto.getNome())
                .email(dto.getEmail())
                .telefone(dto.getTelefone())
                .cpf(dto.getCpf())
                .build();

        Hospede salvo = repository.save(hospede);
        return entityToDto(salvo);
    }

    public List<HospedeDTO> listarTodos() {

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

            throw new IllegalStateException("Não é possível excluir este hóspede pois ele possui reservas registradas.");
        }
    }

    private HospedeDTO entityToDto(Hospede h) {
        return new HospedeDTO(h.getId(), h.getUuid(), h.getNome(), h.getEmail(), h.getTelefone(), h.getCpf());
    }
}
