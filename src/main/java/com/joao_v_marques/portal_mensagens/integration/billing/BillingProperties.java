package com.joao_v_marques.portal_mensagens.integration.billing;

import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app.billing")
public record BillingProperties(
        @NotBlank
        String baseUrl,

        @NotBlank
        String copyId, // ID da cooperativa

        @NotBlank
        String cnpj,

        @NotBlank
        String password
) {
        @Override
        public String toString() {
                return "BillingProperties[baseUrl=" + baseUrl + ", copyId=" + copyId + ", cnpj=" + cnpj + ", password=***]";
        }
}
