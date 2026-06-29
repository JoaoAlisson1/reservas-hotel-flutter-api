package br.csi.dormez_back_api.model;

import br.csi.dormez_back_api.model.enums.StatusReserva;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "reservas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reserva {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, columnDefinition = "VARCHAR(255) DEFAULT gen_random_uuid()")
    private String uuid;

    @Column(nullable = false)
    private LocalDateTime checkIn;

    @Column(nullable = false)
    private LocalDateTime checkOut;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal valorTotal;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusReserva status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quarto_id", nullable = false)
    private Quarto quarto;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "reserva_hospedes",
            joinColumns = @JoinColumn(name = "reserva_id"),
            inverseJoinColumns = @JoinColumn(name = "hospede_id")
    )
    @Builder.Default
    private List<Hospede> hospedes = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        if (this.uuid == null || this.uuid.isEmpty()) {
            this.uuid = UUID.randomUUID().toString();
        }
    }
}
