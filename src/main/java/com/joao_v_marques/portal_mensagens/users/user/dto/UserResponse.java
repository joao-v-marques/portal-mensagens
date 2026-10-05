package com.joao_v_marques.portal_mensagens.users.user.dto;

import java.time.OffsetDateTime;

public record UserResponse(
        Integer id,
        String name,
        String username,
        Integer roleId,
        String roleName,
        Integer sectorId,
        String sectorName,
        OffsetDateTime createdAt,
        boolean isActive,
        String insertedByName
) {
}
