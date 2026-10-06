package com.joao_v_marques.portal_mensagens.integration.gupshup.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record GupshupSendResponse(
        String status,
        String messageId
) {
}
