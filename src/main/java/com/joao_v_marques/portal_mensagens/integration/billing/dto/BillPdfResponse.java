package com.joao_v_marques.portal_mensagens.integration.billing.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record BillPdfResponse(
        @JsonProperty("linhadigitavel")
        String digitableLine,

        @JsonProperty("boleto")
        byte[] pdf
) {
    @Override
    public String toString() {
        return "BillPdfResponse[digitableLine=" + digitableLine + ", pdf=" + (pdf != null ? pdf.length + " bytes" : "null") + "]";
    }
}
