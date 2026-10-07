package com.joao_v_marques.portal_mensagens.beneficiary;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BeneficiaryService {

    private final BeneficiaryRepository beneficiaryRepository;

    public BeneficiaryService(BeneficiaryRepository beneficiaryRepository) {
        this.beneficiaryRepository = beneficiaryRepository;
    }

    public List<Beneficiary> findActive() {
        List<Beneficiary> beneficiaries = beneficiaryRepository.findActive();

        // Tratar as informações antes de retornar
        beneficiaries = beneficiaries.stream()
                .map(e -> {
                    String phoneNumber = (e.phoneNumber() == null || e.phoneNumber().isBlank()) ? null : e.phoneNumber().trim();
                    String phoneNumber2 = (e.phoneNumber2() == null || e.phoneNumber2().isBlank()) ? null : e.phoneNumber2().trim();

                    return new Beneficiary(
                            e.name(),
                            e.cpf(),
                            e.birthDate(),
                            phoneNumber,
                            phoneNumber2
                    );
                 })
                .toList();

        return beneficiaries;
    }
}
