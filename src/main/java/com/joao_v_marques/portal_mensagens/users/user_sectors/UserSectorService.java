package com.joao_v_marques.portal_mensagens.users.user_sectors;

import com.joao_v_marques.portal_mensagens.users.user_sectors.dto.UserSectorRequest;
import com.joao_v_marques.portal_mensagens.users.user_sectors.dto.UserSectorResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserSectorService {

    private final UserSectorRepository userSectorRepository;

    public UserSectorService(UserSectorRepository userSectorRepository) {
        this.userSectorRepository = userSectorRepository;
    }

    // GET de todos os setores cadastrados
    @Transactional(readOnly = true)
    public List<UserSectorResponse> findAll() {
        return userSectorRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public UserSectorResponse create(UserSectorRequest request) {
        String name = request.name().trim();

        if (userSectorRepository.existsByNameIgnoreCase(name)) {
            throw new IllegalArgumentException("O setor que está tentando cadastrar já existe");
        }

        // Montar entidade com base na dto
        UserSector userSector = new UserSector();
        userSector.setName(name);

        UserSector saved = userSectorRepository.save(userSector);

        return toResponse(saved);
    }

    @Transactional
    public UserSectorResponse update(Integer userSectorId, UserSectorRequest request) {
        String name = request.name().trim();

        // Valida se o setor editado realmente existe
        UserSector userSector = userSectorRepository.findById(userSectorId)
                .orElseThrow(() -> new IllegalArgumentException("Não foi encontrado nenhum setor com o ID fornecido."));

        // Validações que a DTO não cobre
        if (!userSector.isActive()) {
            throw new IllegalArgumentException("Não é possível editar uma ocorrência inativa.");
        }

        // Atualizar a entidade já existente
        userSector.setName(name);

        return toResponse(userSector);
    }

    @Transactional
    public UserSectorResponse deactivate(Integer userSectorId) {
        // Valida se o setor desativado realmente existe
        UserSector userSector = userSectorRepository.findById(userSectorId)
                .orElseThrow(() -> new IllegalArgumentException("Não foi encontrado nenhum setor com o ID fornecido."));

        if (!userSector.isActive()) {
            throw new IllegalArgumentException("Este setor já está inativo.");
        }

        userSector.setActive(false);

        return toResponse(userSector);
    }

    @Transactional
    public UserSectorResponse reactivate(Integer userSectorId) {
        // Valida se o setor reativado realmente existe
        UserSector userSector = userSectorRepository.findById(userSectorId)
                .orElseThrow(() -> new IllegalArgumentException("Não foi encontrado nenhum setor com o ID fornecido."));

        if (userSector.isActive()) {
            throw new IllegalArgumentException("Este setor já está ativo.");
        }

        userSector.setActive(true);

        return toResponse(userSector);
    }

    private UserSectorResponse toResponse(UserSector userSector) {
        return new UserSectorResponse(
                userSector.getId(),
                userSector.getName(),
                userSector.isActive()
        );
    }
}
