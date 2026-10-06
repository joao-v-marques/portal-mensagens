package com.joao_v_marques.portal_mensagens.integration.gupshup;

import com.joao_v_marques.portal_mensagens.integration.gupshup.dto.GupshupSendResponse;
import com.joao_v_marques.portal_mensagens.shared.exceptions.GupshupException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;

@Component
public class GupshupClient {

    private static final Logger log = LoggerFactory.getLogger(GupshupClient.class);

    private final RestClient restClient;
    private final GupshupProperties properties;
    private final ObjectMapper objectMapper;

    public GupshupClient(RestClient.Builder builder, GupshupProperties properties, ObjectMapper objectMapper) {
        this.properties = properties;
        this.objectMapper = objectMapper;
        this.restClient = builder
                .baseUrl(properties.baseUrl())
                .defaultHeader("apiKey", properties.apiKey())
                .build();
    }

    public GupshupSendResponse sendText(String destination, String text) {
        MultiValueMap<String, String> form = baseForm(destination);
        form.add("message", toJson(Map.of("type", "text", "text", text)));
        return post("/wa/api/v1/msg", form);
    }

    // Template aprovado (permite iniciar a conversa)
    public GupshupSendResponse sendTemplate(String destination, String templateId, List<String> params) {
        MultiValueMap<String, String> form = baseForm(destination);
        form.add("template", toJson(Map.of("id", templateId, "params", params)));
        return post("/wa/api/v1/template/msg", form);
    }

    private MultiValueMap<String, String> baseForm(String destination) {
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("channel", "whatsapp");
        form.add("source", properties.sourceNumber());
        form.add("destination", destination);
        form.add("src.name", properties.appName());
        return form;
    }

    private GupshupSendResponse post(String uri, MultiValueMap<String, String> form) {
        try {
            GupshupSendResponse response = restClient.post()
                    .uri(uri)
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(form)
                    .retrieve()
                    .body(GupshupSendResponse.class);

            log.info("Gupshup: mensagem enviada para {} - messageId={}", form.getFirst("destination"), response.messageId());
            return response;
        } catch (RestClientResponseException e) {
            log.error("Gupshup: erro {} ao enviar - {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new GupshupException("Falha ao enviar mensagem pela Gupshup: " + e.getResponseBodyAsString(), e);
        } catch (RestClientException e) {
            throw new GupshupException("Não foi possível se comunicar com a Gupshup", e);
        }
    }

    private String toJson(Object value) {
        return objectMapper.writeValueAsString(value); // Jackson 3: exceção unchecked
    }
}
