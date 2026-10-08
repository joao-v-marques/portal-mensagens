package com.joao_v_marques.portal_mensagens.integration.billing.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record BillingToken(
        String token
) {
}
