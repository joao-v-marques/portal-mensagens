package com.joao_v_marques.portal_mensagens.users.auth.dto;

import jakarta.validation.constraints.NotBlank;

public record AuthRequest(

        @NotBlank(message = "Informe o usuário.")
        String username,

        @NotBlank(message = "Informe a senha.")
        String password
) {
}
