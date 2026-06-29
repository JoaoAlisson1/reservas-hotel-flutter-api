package br.csi.dormez_back_api.dto;

import br.csi.dormez_back_api.model.enums.StatusReserva;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReservaRequestDTO {
    private Long id;
    private String uuid;

    @NotNull(message = "Data de check-in é obrigatória")
    private LocalDateTime checkIn;

    @NotNull(message = "Data de check-out é obrigatória")
    private LocalDateTime checkOut;

    @NotNull(message = "O status é obrigatório")
    private StatusReserva status;

    @NotNull(message = "O ID do usuário é obrigatório")
    @JsonProperty("usuario_id")
    private Long usuarioId;

    @NotNull(message = "O ID do quarto é obrigatório")
    @JsonProperty("quarto_id")
    private Long quartoId;

    @JsonProperty("hospedes_ids")
    private List<Long> hospedesIds;
}
