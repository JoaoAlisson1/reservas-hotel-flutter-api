package br.csi.dormez_back_api.Service;

import br.csi.dormez_back_api.dto.ReservaRequestDTO;
import br.csi.dormez_back_api.dto.ReservaResponseDTO;
import br.csi.dormez_back_api.model.Hospede;
import br.csi.dormez_back_api.model.Quarto;
import br.csi.dormez_back_api.model.Reserva;
import br.csi.dormez_back_api.model.Usuario;
import br.csi.dormez_back_api.model.enums.StatusQuarto;
import br.csi.dormez_back_api.model.enums.StatusReserva;
import br.csi.dormez_back_api.repository.HospedeRepository;
import br.csi.dormez_back_api.repository.QuartoRepository;
import br.csi.dormez_back_api.repository.ReservaRepository;
import br.csi.dormez_back_api.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ReservaService {
    private final ReservaRepository repository;
    private final UsuarioRepository usuarioRepository;
    private final QuartoRepository quartoRepository;
    private final HospedeRepository hospedeRepository;

    public ReservaService(ReservaRepository repository, UsuarioRepository usuarioRepository, QuartoRepository quartoRepository, HospedeRepository hospedeRepository) {
        this.repository = repository;
        this.usuarioRepository = usuarioRepository;
        this.quartoRepository = quartoRepository;
        this.hospedeRepository = hospedeRepository;
    }

    @Transactional
    public ReservaResponseDTO salvar(ReservaRequestDTO dto) {
        Usuario usuario = usuarioRepository.findById(dto.getUsuarioId())
                .orElseThrow(() -> new IllegalArgumentException("Usuário responsável não encontrado."));
        Quarto quarto = quartoRepository.findById(dto.getQuartoId())
                .orElseThrow(() -> new IllegalArgumentException("Quarto especificado não encontrado."));

        if (dto.getCheckOut().isBefore(dto.getCheckIn())) {
            throw new IllegalArgumentException("A data de check-out não pode ser anterior à data de check-in.");
        }

        if (dto.getHospedesIds() == null || dto.getHospedesIds().isEmpty()) {
            throw new IllegalArgumentException("Nenhum hóspede selecionado para a reserva.");
        }

        Long quartoAntigoId = null;

        if (dto.getId() == null) {

            if (quarto.getStatus() != StatusQuarto.Disponivel) {
                throw new IllegalStateException("O quarto " + quarto.getNumero() + " está indisponível para reservas no momento. Status: " + quarto.getStatus());
            }

            if (dto.getUuid() == null || dto.getUuid().trim().isEmpty()) {
                dto.setUuid(UUID.randomUUID().toString());
            }

            if (dto.getStatus() == null) {
                dto.setStatus(StatusReserva.Reservada);
            }

            quarto.setStatus(StatusQuarto.Reservado);
            quartoRepository.save(quarto);
        } else {
            Reserva reservaExistente = repository.findById(dto.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Reserva não encontrada."));
            quartoAntigoId = reservaExistente.getQuarto().getId();

            if (!quartoAntigoId.equals(quarto.getId())) {

                if (quarto.getStatus() != StatusQuarto.Disponivel) {
                    throw new IllegalStateException("O novo quarto " + quarto.getNumero() + " não está disponível.");
                }

                Quarto quartoAntigo = quartoRepository.findById(quartoAntigoId).get();
                quartoAntigo.setStatus(StatusQuarto.Disponivel);
                quartoRepository.save(quartoAntigo);

                quarto.setStatus(StatusQuarto.Reservado);
                quartoRepository.save(quarto);
            }
        }

        long dias = Duration.between(dto.getCheckIn(), dto.getCheckOut()).toDays();
        if (dias <= 0) dias = 1;
        BigDecimal valorCalculado = quarto.getDiaria().multiply(BigDecimal.valueOf(dias));

        Reserva reserva = Reserva.builder()
                .id(dto.getId())
                .uuid(dto.getUuid())
                .checkIn(dto.getCheckIn())
                .checkOut(dto.getCheckOut())
                .status(dto.getStatus())
                .valorTotal(valorCalculado)
                .usuario(usuario)
                .quarto(quarto)
                .build();

        List<Hospede> hospedesCarregados = hospedeRepository.findAllById(dto.getHospedesIds());
        reserva.setHospedes(hospedesCarregados);

        Reserva salva = repository.save(reserva);
        return entityToResponseDto(salva);
    }

    @Transactional
    public void realizarCheckIn(Long id) {
        Reserva reserva = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Reserva não encontrada para Check-in."));

        reserva.setStatus(StatusReserva.Check_in);

        Quarto quarto = reserva.getQuarto();
        quarto.setStatus(StatusQuarto.Ocupado);

        quartoRepository.save(quarto);
        repository.save(reserva);
    }

    @Transactional
    public void realizarCheckOut(Long id) {
        Reserva reserva = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Reserva não encontrada para Check-out."));

        reserva.setStatus(StatusReserva.Check_out);

        Quarto quarto = reserva.getQuarto();
        quarto.setStatus(StatusQuarto.Limpeza);

        quartoRepository.save(quarto);
        repository.save(reserva);
    }

    public List<ReservaResponseDTO> listarTodas() {
        return repository.findAll().stream()
                .map(this::entityToResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deletar(Long id) {
        Reserva reserva = repository.findById(id).orElse(null);
        if (reserva != null) {

            if (reserva.getStatus() == StatusReserva.Reservada || reserva.getStatus() == StatusReserva.Check_in) {
                Quarto quarto = reserva.getQuarto();
                quarto.setStatus(StatusQuarto.Disponivel);
                quartoRepository.save(quarto);
            }
            repository.deleteById(id);
        }
    }

    private ReservaResponseDTO entityToResponseDto(Reserva r) {
        String nomeHospede = "Nenhum cadastrado";
        if (r.getHospedes() != null && !r.getHospedes().isEmpty()) {
            nomeHospede = r.getHospedes().get(0).getNome();
        }

        return ReservaResponseDTO.builder()
                .id(r.getId())
                .uuid(r.getUuid())
                .checkIn(r.getCheckIn())
                .checkOut(r.getCheckOut())
                .valorTotal(r.getValorTotal())
                .status(r.getStatus())
                .usuarioId(r.getUsuario().getId())
                .quartoId(r.getQuarto().getId())
                .nomeHospedePrincipal(nomeHospede)
                .numeroQuarto(r.getQuarto().getNumero())
                .loginUsuarioResponsavel(r.getUsuario().getLogin())
                .build();
    }
}
