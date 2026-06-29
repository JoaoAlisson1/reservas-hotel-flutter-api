package br.csi.dormez_back_api.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "hospedes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Hospede {

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

    @Column(nullable = false, unique = true)
    private String cpf;

    @PrePersist
    public void prePersist() {
        if (this.uuid == null || this.uuid.trim().isEmpty()) {
            this.uuid = UUID.randomUUID().toString();
        }
    }
}
