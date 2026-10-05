package com.joao_v_marques.portal_mensagens.users.user_sectors.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserSectorRequest(
        @NotBlank(message = "Preencha o nome do setor")
        @Size(max = 255, message = "O nome do setor deve conter no máximo 255 caracteres")
        String name
) {
}
