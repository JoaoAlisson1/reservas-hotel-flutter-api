package br.csi.dormez_back_api.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "funcionarios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Funcionario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, columnDefinition = "VARCHAR(255) DEFAULT gen_random_uuid()")
    private String uuid;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String telefone;

    @PrePersist
    public void prePersist() {
        if (this.uuid == null || this.uuid.isEmpty()) {
            this.uuid = UUID.randomUUID().toString();
        }
    }
}
