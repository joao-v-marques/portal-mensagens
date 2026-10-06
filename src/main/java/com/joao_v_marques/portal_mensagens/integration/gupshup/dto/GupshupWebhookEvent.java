package com.joao_v_marques.portal_mensagens.integration.gupshup.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import tools.jackson.databind.JsonNode;

@JsonIgnoreProperties(ignoreUnknown = true)
public record GupshupWebhookEvent(
        String app,
        Long timestamp,
        Integer version,
        String type,
        JsonNode payload
) {
}
