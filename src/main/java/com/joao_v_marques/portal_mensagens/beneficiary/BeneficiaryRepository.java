package com.joao_v_marques.portal_mensagens.beneficiary;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
class BeneficiaryRepository {

    private final JdbcClient oracle;

    BeneficiaryRepository(@Qualifier("oracleJdbcClient") JdbcClient oracle) {
        this.oracle = oracle;
    }

    // GET de todos os beneficiários do banco de dados TOTVS
    public List<Beneficiary> findActive() {
        return oracle.sql("""
                SELECT u.NM_USUARIO, u.CD_CPF, u.DT_NASCIMENTO, u.NR_TELEFONE1, u.NR_TELEFONE2
                FROM SRCADGER.USUARIO u
                WHERE u.DT_EXCLUSAO_PLANO IS NULL
                AND u.CD_MODALIDADE IN ('10', '11', '20', '30', '31', '40')
                AND u.DT_INCLUSAO_PLANO <= SYSDATE
                AND u.NM_USUARIO != 'USUARIO EVENTUAL'
                ORDER BY u.NM_USUARIO
                """).query((rs, rowNum) -> new Beneficiary(
                        rs.getString("NM_USUARIO"),
                        rs.getString("CD_CPF"),
                        rs.getObject("DT_NASCIMENTO", LocalDateTime.class),
                        rs.getString("NR_TELEFONE1"),
                        rs.getString("NR_TELEFONE2")))
                .list();
    }
}
