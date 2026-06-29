package br.csi.dormez_back_api.controller;

import br.csi.dormez_back_api.Service.HospedeService;
import br.csi.dormez_back_api.dto.HospedeDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/hospedes")
public class HospedeController {

    private final HospedeService service;

    public HospedeController(HospedeService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<?> cadastrar(@RequestBody @Valid HospedeDTO dto) {
        try {
            dto.setId(null);
            HospedeDTO salvo = service.salvar(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
        } catch (IllegalArgumentException e) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editar(@PathVariable Long id, @RequestBody @Valid HospedeDTO dto) {
        try {
            dto.setId(id);
            HospedeDTO atualizado = service.salvar(dto);
            return ResponseEntity.ok(atualizado);
        } catch (IllegalArgumentException e) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<List<HospedeDTO>> listar() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluir(@PathVariable Long id) {
        try {
            service.deletar(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalStateException e) {

            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message", e.getMessage())); // Retorna HTTP 409
        }
    }
}