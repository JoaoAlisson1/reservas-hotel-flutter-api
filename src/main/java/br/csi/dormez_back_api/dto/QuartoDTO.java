package br.csi.dormez_back_api.dto;

import br.csi.dormez_back_api.model.enums.StatusQuarto;
import br.csi.dormez_back_api.model.enums.TipoQuarto;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuartoDTO {
    private Long id;
    private String uuid;

    @NotNull(message = "O número do quarto é obrigatório")
    private Integer numero;

    @NotNull(message = "O tipo do quarto é obrigatório")
    private TipoQuarto tipo;

    @NotNull(message = "O status do quarto é obrigatório")
    private StatusQuarto status;

    @NotNull(message = "O valor da diária é obrigatório")
    @Positive(message = "A diária deve ser maior que zero")
    private BigDecimal diaria;
}
