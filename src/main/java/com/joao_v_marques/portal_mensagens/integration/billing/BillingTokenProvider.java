package com.joao_v_marques.portal_mensagens.integration.billing;

import com.joao_v_marques.portal_mensagens.integration.billing.dto.BillingApiResponse;
import com.joao_v_marques.portal_mensagens.integration.billing.dto.BillingToken;
import com.joao_v_marques.portal_mensagens.shared.exceptions.BillingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;

import java.time.Duration;
import java.time.Instant;

@Component
public class BillingTokenProvider {

    private static final Logger log = LoggerFactory.getLogger(BillingTokenProvider.class);

    private static final Duration SAFETY_MARGIN = Duration.ofMinutes(1);
    private static final Duration DEFAULT_TTL = Duration.ofMinutes(30);

    private final RestClient restClient;
    private final BillingProperties properties;

    private volatile CachedToken cached;

    BillingTokenProvider(RestClient.Builder builder, BillingProperties properties) {
        this.properties = properties;
        this.restClient = builder
                .baseUrl(properties.baseUrl())
                .build();
    }

    String getToken() {
        CachedToken current = cached;
        if (current != null && current.isValid()) {
            return current.value();
        }

        // só uma thread faz login por vez; as outras esperam e reaproveitam o token novo
        synchronized (this) {
            if (cached == null || !cached.isValid()) {
                cached = login();
            }
            return cached.value();
        }
    }

    // descarta o token só se ainda for o mesmo que deu 401 (outra thread pode já ter renovado)
    void invalidate(String staleToken) {
        CachedToken current = cached;
        if (current != null && current.value().equals(staleToken)) {
            cached = null;
        }
    }

    private CachedToken login() {
        BillingApiResponse<BillingToken> response;

        try {
            response = restClient.get()
                    .uri("/api/Autenticacao")
                    .header("X-COPYID", properties.copyId())
                    .header("X-CNPJ", properties.cnpj())
                    .header("X-SENHA", properties.password())
                    .retrieve()
                    .body(new ParameterizedTypeReference<BillingApiResponse<BillingToken>>() {});
        } catch (RestClientResponseException e) {
            // não loga o corpo: pode conter dados da requisição de login
            throw new BillingException("Falha ao autenticar na API de cobrança: status " + e.getStatusCode(), e);
        } catch (RestClientException e) {
            throw new BillingException("Não foi possível se comunicar com a API de cobrança", e);
        }

        // a API pode responder HTTP 200 com erro no Result (ex.: senha incorreta)
        if (response == null || !response.isSuccess()) {
            String message = response != null ? response.message() : "resposta vazia";
            throw new BillingException("A API de cobrança recusou a autenticação: " + message, null);
        }

        if (response.data() == null || response.data().isEmpty() || !StringUtils.hasText(response.data().getFirst().token())) {
            throw new BillingException("A API de cobrança não retornou o token de acesso", null);
        }

        log.info("Billing: novo token obtido");

        return new CachedToken(response.data().getFirst().token(), Instant.now().plus(DEFAULT_TTL).minus(SAFETY_MARGIN));
    }

    private record CachedToken(String value, Instant expiresAt) {
        boolean isValid() {
            return Instant.now().isBefore(expiresAt);
        }
    }
}
