package com.joao_v_marques.portal_mensagens.integration.billing.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.LocalDate;

public record BillResponse(
        @JsonProperty("titulo")
        String titleNumber,

        @JsonProperty("idtitulo")
        String titleId,

        @JsonProperty("data")
        @JsonFormat(pattern = "dd/MM/yyyy")

        LocalDate dueDate,
        @JsonProperty("valor")
        String amount,

        @JsonProperty("modter")
        String modter
) {
    // converte o valor no formato BR ("1.711,32") para BigDecimal
    public BigDecimal amountAsBigDecimal() {
        return new BigDecimal(amount.replace(".", "").replace(",", "."));
    }
}
