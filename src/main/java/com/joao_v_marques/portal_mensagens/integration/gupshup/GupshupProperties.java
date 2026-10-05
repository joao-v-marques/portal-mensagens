package com.joao_v_marques.portal_mensagens.integration.gupshup;

import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app.gupshop")
public record GupshupProperties(
        @NotBlank
        String baseUrl,

        @NotBlank
        String apiKey,

        @NotBlank
        String appName,

        @NotBlank
        String sourceNumber,

        @NotBlank
        String webhookToken
) {
}
