package br.csi.dormez_back_api.controller;

import br.csi.dormez_back_api.Service.QuartoService;
import br.csi.dormez_back_api.dto.QuartoDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/quartos")
public class QuartoController {
    private final QuartoService service;

    public QuartoController(QuartoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<?> cadastrar(@RequestBody @Valid QuartoDTO dto) {
        try {
            dto.setId(null);
            QuartoDTO salvo = service.salvar(dto);

            return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
        } catch (IllegalArgumentException e) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editar(@PathVariable Long id, @RequestBody @Valid QuartoDTO dto) {
        try {
            dto.setId(id);
            QuartoDTO atualizado = service.salvar(dto);
            return ResponseEntity.ok(atualizado);
        } catch (IllegalArgumentException e) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<List<QuartoDTO>> listar() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluir(@PathVariable Long id) {
        try {
            service.deletar(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalStateException e) {

            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message", e.getMessage()));
        }
    }
}
