package com.joao_v_marques.portal_mensagens.users.roles.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RoleRequest(
        @NotBlank(message = "Preencha o nome da role")
        @Size(max = 255, message = "O campo deve ter no máximo 255 caracteres")
        String name
) {
}
