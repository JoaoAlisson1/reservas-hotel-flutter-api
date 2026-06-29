package br.csi.dormez_back_api.controller;

import br.csi.dormez_back_api.Service.ReservaService;
import br.csi.dormez_back_api.dto.ReservaRequestDTO;
import br.csi.dormez_back_api.dto.ReservaResponseDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/reservas")
public class ReservaController {
    private final ReservaService service;

    public ReservaController(ReservaService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<?> cadastrar(@RequestBody @Valid ReservaRequestDTO dto) {
        try {
            dto.setId(null);
            ReservaResponseDTO salva = service.salvar(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(salva);
        } catch (IllegalArgumentException | IllegalStateException e) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", e.getMessage()));
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> editar(@PathVariable Long id, @RequestBody @Valid ReservaRequestDTO dto) {
        try {
            dto.setId(id);
            ReservaResponseDTO atualizada = service.salvar(dto);
            return ResponseEntity.ok(atualizada);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", e.getMessage()));
        }
    }

    @PutMapping("/{id}/checkin")
    public ResponseEntity<?> realizarCheckIn(@PathVariable Long id) {
        try {
            service.realizarCheckIn(id);
            return ResponseEntity.ok(Map.of("message", "Check-in realizado com sucesso!"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", e.getMessage()));
        }
    }

    @PutMapping("/{id}/checkout")
    public ResponseEntity<?> realizarCheckOut(@PathVariable Long id) {
        try {
            service.realizarCheckOut(id);
            return ResponseEntity.ok(Map.of("message", "Check-out realizado com sucesso!"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<List<ReservaResponseDTO>> listar() {
        return ResponseEntity.ok(service.listarTodas());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
