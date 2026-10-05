package com.joao_v_marques.portal_mensagens.users.roles;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Integer> {

    boolean existsByNameIgnoreCase(String name);
}
