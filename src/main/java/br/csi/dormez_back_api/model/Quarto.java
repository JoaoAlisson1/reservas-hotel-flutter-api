package br.csi.dormez_back_api.model;

import br.csi.dormez_back_api.model.enums.StatusQuarto;
import br.csi.dormez_back_api.model.enums.TipoQuarto;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "quartos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Quarto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, columnDefinition = "VARCHAR(255) DEFAULT gen_random_uuid()")
    private String uuid;

    @Column(nullable = false, unique = true)
    private Integer numero;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoQuarto tipo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusQuarto status;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal diaria;

    @PrePersist
    public void prePersist() {
        if (this.uuid == null || this.uuid.trim().isEmpty()) {
            this.uuid = UUID.randomUUID().toString();
        }
    }
}
