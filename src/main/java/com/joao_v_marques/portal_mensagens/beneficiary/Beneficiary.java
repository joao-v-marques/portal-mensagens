package com.joao_v_marques.portal_mensagens.beneficiary;

import java.time.LocalDateTime;

public record Beneficiary(
    String name,
    String cpf,
    LocalDateTime birthDate,
    String phoneNumber
) {}
