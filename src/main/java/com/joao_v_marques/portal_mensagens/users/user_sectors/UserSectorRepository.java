package com.joao_v_marques.portal_mensagens.users.user_sectors;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserSectorRepository extends JpaRepository<UserSector, Integer> {

    boolean existsByNameIgnoreCase(String name);
}
