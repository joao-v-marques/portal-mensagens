package com.joao_v_marques.portal_mensagens.users.user.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record UserRequest(
        @Size(max = 255, message = "O nome do usuário deve conter no máximo 255 caracteres")
        String name,

        @NotBlank(message = "Preencha o campo de usuário")
        @Size(max = 255, message = "O usuário deve conter no máximo 255 caracteres")
        String username,

        @NotBlank(message = "Informe a senha do usuário")
        @Size(min = 4, max = 72, message = "A senha deve ter entre 4 e 72 caracteres")
        String password,

        @NotNull(message = "Informe a função do usuário")
        @Positive(message = "Perfil de acesso inválido")
        Integer roleId,

        @Positive(message = "Setor inválido")
        Integer sectorId
) {
}
