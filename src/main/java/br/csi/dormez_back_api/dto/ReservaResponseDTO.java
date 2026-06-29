package br.csi.dormez_back_api.dto;

import br.csi.dormez_back_api.model.enums.StatusReserva;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservaResponseDTO {
    private Long id;
    private String uuid;
    private LocalDateTime checkIn;
    private LocalDateTime checkOut;
    private BigDecimal valorTotal;
    private StatusReserva status;

    @JsonProperty("usuario_id")
    private Long usuarioId;

    @JsonProperty("quarto_id")
    private Long quartoId;

    @JsonProperty("nome_hospede")
    private String nomeHospedePrincipal;

    @JsonProperty("numero_quarto")
    private Integer numeroQuarto;

    @JsonProperty("login_usuario")
    private String loginUsuarioResponsavel;
}
