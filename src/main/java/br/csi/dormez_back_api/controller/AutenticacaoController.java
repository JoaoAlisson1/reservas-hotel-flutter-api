package br.csi.dormez_back_api.controller;

import br.csi.dormez_back_api.Service.TokenService;
import br.csi.dormez_back_api.model.Usuario;
import br.csi.dormez_back_api.repository.UsuarioRepository;
import br.csi.dormez_back_api.dto.LoginDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/login")
public class AutenticacaoController {

    private final UsuarioRepository repository;
    private final TokenService tokenService;

    public AutenticacaoController(UsuarioRepository repository, TokenService tokenService) {
        this.repository = repository;
        this.tokenService = tokenService;
    }

    @PostMapping
    public ResponseEntity<?> efetuarLogin(@RequestBody @Valid LoginDTO dados) {
        try {
            Usuario usuario = repository.findByLoginAndSenha(dados.getLogin(), dados.getSenha())
                    .orElseThrow(() -> new IllegalArgumentException("Usuário ou senha incorretos."));

            String tokenJWT = tokenService.gerarToken(usuario);

            Map<String, Object> dadosResposta = new HashMap<>();
            dadosResposta.put("accessToken", tokenJWT);
            dadosResposta.put("id", usuario.getId());
            dadosResposta.put("login", usuario.getLogin());
            dadosResposta.put("permissao", usuario.getPermissao());

            return ResponseEntity.ok(dadosResposta);

        } catch (IllegalArgumentException e) {

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", e.getMessage()));
        }
    }
}