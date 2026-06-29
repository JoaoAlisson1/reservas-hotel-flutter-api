package br.csi.dormez_back_api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginDTO {
    @NotBlank(message = "O login é obrigatório")
    private String login;
    @NotBlank(message = "A senha é obrigatória")
    private String senha;
}
