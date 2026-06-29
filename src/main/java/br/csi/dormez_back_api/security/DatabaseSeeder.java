package br.csi.dormez_back_api.security;

import br.csi.dormez_back_api.model.Usuario;
import br.csi.dormez_back_api.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DatabaseSeeder implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;

    public DatabaseSeeder(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (usuarioRepository.count() == 0) {
            System.out.println("Banco de dados de usuários vazio! Criando administrador padrão...");

            Usuario adminPadrao = Usuario.builder()
                    .login("admin@hotel.com")
                    .senha("123")
                    .permissao("ADMIN")
                    .build();

            usuarioRepository.save(adminPadrao);

            System.out.println("Usuário ADMIN padrão (admin@hotel.com / 123) criado com sucesso!");
        } else {
            System.out.println("Usuários já existentes no banco. Inicialização padrão pulada.");
        }
    }
}