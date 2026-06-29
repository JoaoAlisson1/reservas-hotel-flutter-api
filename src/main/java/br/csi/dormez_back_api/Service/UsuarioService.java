package br.csi.dormez_back_api.Service;

import br.csi.dormez_back_api.dto.LoginDTO;
import br.csi.dormez_back_api.dto.UsuarioDTO;
import br.csi.dormez_back_api.model.Usuario;
import br.csi.dormez_back_api.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UsuarioService {
    private final UsuarioRepository repository;

    public UsuarioService(UsuarioRepository repository) {
        this.repository = repository;
    }

    public UsuarioDTO autenticar(LoginDTO dados) {
        Usuario usuario = repository.findByLoginAndSenha(dados.getLogin(), dados.getSenha())
                .orElseThrow(() -> new IllegalArgumentException("Usuário ou senha incorretos."));
        return entityToDto(usuario);
    }

    @Transactional
    public UsuarioDTO salvar(UsuarioDTO dto) {
        String senhaFinal = dto.getSenha();

        if (dto.getId() == null) {

            if (repository.findByLogin(dto.getLogin()).isPresent()) {
                throw new IllegalArgumentException("O e-mail " + dto.getLogin() + " já está cadastrado.");
            }
            if (senhaFinal == null || senhaFinal.trim().isEmpty()) {
                throw new IllegalArgumentException("A senha é obrigatória para novos usuários.");
            }
        } else {

            Optional<Usuario> existente = repository.findByLogin(dto.getLogin());
            if (existente.isPresent() && !existente.get().getId().equals(dto.getId())) {
                throw new IllegalArgumentException("Este e-mail já está sendo usado por outro funcionário.");
            }

            if (senhaFinal == null || senhaFinal.trim().isEmpty()) {
                Usuario usuarioBanco = repository.findById(dto.getId())
                        .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado."));
                senhaFinal = usuarioBanco.getSenha();
            }
        }

        Usuario usuario = Usuario.builder()
                .id(dto.getId())
                .login(dto.getLogin())
                .senha(senhaFinal)
                .permissao(dto.getPermissao().toUpperCase())
                .build();

        Usuario salvo = repository.save(usuario);
        return entityToDto(salvo);
    }

    public List<UsuarioDTO> listarTodos() {
        return repository.findAll().stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deletar(Long id) {

        Usuario usuario = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado para exclusão."));

        if ("admin@hotel.com".equalsIgnoreCase(usuario.getLogin())) {
            throw new IllegalArgumentException("O administrador padrão do sistema não pode ser excluído.");
        }

        repository.deleteById(id);
    }

    private UsuarioDTO entityToDto(Usuario u) {

        return new UsuarioDTO(u.getId(), u.getLogin(), u.getPermissao());
    }
}
