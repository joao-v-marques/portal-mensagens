package com.joao_v_marques.portal_mensagens.users.roles;

import com.joao_v_marques.portal_mensagens.users.roles.dto.RoleRequest;
import com.joao_v_marques.portal_mensagens.users.roles.dto.RoleResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    // GET de todas as roles cadastradas
    @Transactional(readOnly = true)
    public List<RoleResponse> findAll() {
        return roleRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public RoleResponse create(RoleRequest request) {
        String name = request.name().trim();

        if (roleRepository.existsByNameIgnoreCase(name)) {
            throw new IllegalArgumentException("A role que está tentando cadastrar já existe");
        }

        // Montar entidade com base na dto
        Role userRole = new Role();
        userRole.setName(name);

        Role saved = roleRepository.save(userRole);

        return toResponse(saved);
    }

    @Transactional
    public RoleResponse update(Integer userRoleId, RoleRequest request) {
        String name = request.name().trim();

        // Valida se a role editada realmente existe
        Role userRole = roleRepository.findById(userRoleId)
                .orElseThrow(() -> new IllegalArgumentException("Não foi encontrado nenhuma função de usuário com o ID fornecido."));

        // Validações que a DTO não cobre
        if (!userRole.isActive()) {
            throw new IllegalArgumentException("Não é possível editar uma ocorrência inativa.");
        }

        // Atualizar a entidade já existente
        userRole.setName(name);

        return toResponse(userRole);
    }

    @Transactional
    public RoleResponse deactivate(Integer userRoleId) {
        // Valida se a role desativada realmente existe
        Role userRole = roleRepository.findById(userRoleId)
                .orElseThrow(() -> new IllegalArgumentException("Não foi encontrado nenhuma função de usuário com o ID fornecido."));

        if (!userRole.isActive()) {
            throw new IllegalArgumentException("Esta função já está inativa.");
        }

        userRole.setActive(false);

        return toResponse(userRole);
    }

    @Transactional
    public RoleResponse reactivate(Integer userRoleId) {
        // Valida se a role desativada realmente existe
        Role userRole = roleRepository.findById(userRoleId)
                .orElseThrow(() -> new IllegalArgumentException("Não foi encontrado nenhuma função de usuário com o ID fornecido."));

        if (userRole.isActive()) {
            throw new IllegalArgumentException("Esta função já está ativa.");
        }

        userRole.setActive(true);

        return toResponse(userRole);
    }

    private RoleResponse toResponse(Role userRole) {
        return new RoleResponse(
                userRole.getId(),
                userRole.getName(),
                userRole.isActive()
        );
    }
}
