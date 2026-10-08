package com.joao_v_marques.portal_mensagens.integration.billing;

import com.joao_v_marques.portal_mensagens.integration.billing.dto.BillResponse;
import com.joao_v_marques.portal_mensagens.integration.billing.dto.BillingApiResponse;
import com.joao_v_marques.portal_mensagens.shared.exceptions.BillingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.function.Function;

@Component
public class BillingClient {

    private static final Logger log = LoggerFactory.getLogger(BillingClient.class);

    private final RestClient restClient;
    private final BillingTokenProvider tokenProvider;
    private static final DateTimeFormatter BIRTH_DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public BillingClient(RestClient.Builder builder, BillingTokenProvider tokenProvider, BillingProperties billingProperties) {
        this.tokenProvider = tokenProvider;
        this.restClient = builder
                .baseUrl(billingProperties.baseUrl())
                .build();
    }

    // Fazer consulta dos boletos de um único beneficiário pelo CPF e Data de Nascimento
    public List<BillResponse> findBilling(String cpf, LocalDate birthDate) {
        BillingApiResponse<BillResponse> response;

        try {
            response = withAuth(token -> restClient.get()
                    .uri("/api/BuscaBoletos")
                    .header("X-TOKEN", token)
                    .header("X-IDFEDERAL", cpf)
                    .header("X-SENHA", "") // A API exixe o header de senha, mas deixa passar vazio
                    .header("X-DTNASC", birthDate.format(BIRTH_DATE_FORMAT))
                    .retrieve()
                    .body(new ParameterizedTypeReference<BillingApiResponse<BillResponse>>() {}));
        } catch (RestClientResponseException e) {
            throw new BillingException("Falha ao consultar boletos na API de cobrança: status " + e.getStatusCode(), e);
        } catch (RestClientException e) {
            throw new BillingException("Não foi possível se comunicar com a API de cobrança", e);
        }

        // Valida caso a API não retorne nada, simplesmente null
        if (response == null) {
            throw new BillingException("A API de cobrança retornou com uma resposta vazia", null);
        }
        // Caso não tenha boletos em aberto, lança vazia em vez de lançar com erro
        if (isNoOpenBills(response)) {
            return List.of();
        }
        // Valida caso tenha dado erro na API, retorna para o usuário a exception
        if (!response.isSuccess()) {
            throw new BillingException("A API de cobrança retornou erro ao consultar boletos: " + response.message(), null);
        }

        return response.data() == null ? List.of() : response.data().stream()
                .filter(bill -> bill != null && StringUtils.hasText(bill.titleId()))
                .toList();
    }

    private boolean isNoOpenBills(BillingApiResponse<?> response) {
        return response.result() != null && response.result() == 0 && response.message() != null && response.message().toLowerCase(Locale.ROOT).contains("não existe titulos em aberto");
    }

    private boolean isInvalidToken(BillingApiResponse<?> response) {
        return response != null && response.result() != null && response.result() == 0
                && response.message() != null
                && response.message().toLowerCase(Locale.ROOT).contains("token inválido");
    }

    private <T> BillingApiResponse<T> withAuth(Function<String, BillingApiResponse<T>> call) {
        String token = tokenProvider.getToken();
        BillingApiResponse<T> response;

        try {
            response = call.apply(token);
        } catch (HttpClientErrorException.Unauthorized e) {
            tokenProvider.invalidate(token);
            return call.apply(tokenProvider.getToken());
        }

        if (isInvalidToken(response)) {
            log.info("Billing: token recusado pela API, obtendo um novo e tentando novamente");
            tokenProvider.invalidate(token);
            return call.apply(tokenProvider.getToken());
        }

        return response;
    }
}
