package com.joao_v_marques.portal_mensagens.integration.gupshup;

import com.joao_v_marques.portal_mensagens.integration.gupshup.dto.GupshupWebhookEvent;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import org.slf4j.Logger;

@RestController
@RequestMapping("/api/integrations/gupshup/webhook")
public class GupshupWebhookController {

    private static final Logger log = LoggerFactory.getLogger(GupshupWebhookController.class);

    private final GupshupProperties gupshupProperties;

    public GupshupWebhookController(GupshupProperties gupshupProperties) {
        this.gupshupProperties = gupshupProperties;
    }

    // POST chamado pela Gupshup a cada evento (status do envio, mensagem recebida, opt-in/out)
    @PostMapping("/{token}")
    public ResponseEntity<Void> receive(@PathVariable String token, @RequestBody(required = false) GupshupWebhookEvent event) {
        if (!MessageDigest.isEqual(token.getBytes(StandardCharsets.UTF_8),
        gupshupProperties.webhookToken().getBytes(StandardCharsets.UTF_8))) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // Ao cadastrar a URL a Gupshup manda uma requisição de validação sem corpo útil
        if (event == null || event.type() == null) {
            return ResponseEntity.ok().build();
        }

        switch (event.type()) {
            case "message-event" -> log.info("Gupshup status: {} id={} gsId={}",
                    event.payload().path("type").asString(),
                    event.payload().path("id").asString(),
                    event.payload().path("gsId").asString());
            case "message" -> log.info("Gupshup mensagem recebida de {}: {}",
                    event.payload().path("source").asString(), event.payload());
            default -> log.info("Gupshup evento {}: {}", event.type(), event.payload());
        }

        return ResponseEntity.ok().build();
    }
}
