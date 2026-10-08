package com.joao_v_marques.portal_mensagens.integration.billing.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record BillingApiResponse<T>(
    @JsonProperty("Result")
    Integer result,

    @JsonProperty("Message")
    String message,

    @JsonProperty("Data")
    List<T> data,

    @JsonProperty("DebugMessage")
    @JsonAlias("Debug")
    String debugMessage
) {
    public boolean isSuccess() {
        return result != null && result == 1;
    }
}
