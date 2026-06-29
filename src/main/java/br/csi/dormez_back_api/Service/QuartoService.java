package br.csi.dormez_back_api.Service;

import br.csi.dormez_back_api.dto.QuartoDTO;
import br.csi.dormez_back_api.model.Quarto;
import br.csi.dormez_back_api.repository.QuartoRepository;
import br.csi.dormez_back_api.repository.ReservaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class QuartoService {

    private final QuartoRepository repository;
    private final ReservaRepository reservaRepository;

    public QuartoService(QuartoRepository repository, ReservaRepository reservaRepository) {
        this.repository = repository;
        this.reservaRepository = reservaRepository;
    }

    @Transactional
    public QuartoDTO salvar(QuartoDTO dto) {

        if (dto.getId() == null) {
            if (repository.findByNumero(dto.getNumero()).isPresent()) {

                throw new IllegalArgumentException("Já existe um quarto cadastrado com o número " + dto.getNumero());
            }

            if (dto.getUuid() == null || dto.getUuid().trim().isEmpty()) {
                dto.setUuid(java.util.UUID.randomUUID().toString());
            }
        } else {

            Optional<Quarto> quartoExistente = repository.findByNumero(dto.getNumero());
            if (quartoExistente.isPresent() && !quartoExistente.get().getId().equals(dto.getId())) {
                throw new IllegalArgumentException("O número " + dto.getNumero() + " já está sendo usado por outro quarto.");
            }
        }

        Quarto quarto = Quarto.builder()
                .id(dto.getId())
                .uuid(dto.getUuid())
                .numero(dto.getNumero())
                .tipo(dto.getTipo())
                .status(dto.getStatus())
                .diaria(dto.getDiaria())
                .build();

        Quarto salvo = repository.save(quarto);
        return entityToDto(salvo);
    }

    public List<QuartoDTO> listarTodos() {
        return repository.findAll().stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deletar(Long id) {

        boolean possuiReservas = reservaRepository.existsByQuartoId(id);

        if (possuiReservas) {
            throw new IllegalStateException(
                    "Não é possível excluir este quarto pois ele possui reservas registradas no histórico."
            );
        }

        repository.deleteById(id);
    }

    private QuartoDTO entityToDto(Quarto q) {
        return new QuartoDTO(q.getId(), q.getUuid(), q.getNumero(), q.getTipo(), q.getStatus(), q.getDiaria());
    }
}
