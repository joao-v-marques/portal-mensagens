package com.joao_v_marques.portal_mensagens.users.user;

import com.joao_v_marques.portal_mensagens.users.roles.Role;
import com.joao_v_marques.portal_mensagens.users.roles.RoleRepository;
import com.joao_v_marques.portal_mensagens.users.user.dto.UserRequest;
import com.joao_v_marques.portal_mensagens.users.user.dto.UserResponse;
import com.joao_v_marques.portal_mensagens.users.user_sectors.UserSector;
import com.joao_v_marques.portal_mensagens.users.user_sectors.UserSectorRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserSectorRepository userSectorRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, UserSectorRepository userSectorRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userSectorRepository = userSectorRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // GET de todos os usuários
    @Transactional(readOnly = true)
    public List<UserResponse> findAll() {
        return userRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public UserResponse create(UserRequest request, Integer currentUserId) {
        String name = StringUtils.hasText(request.name()) ? request.name().trim() : null;
        String username = request.username().trim();
        UserSector userSector = null;

        // Validando as FK's
        Role role = roleRepository.findById(request.roleId())
                .orElseThrow(() -> new IllegalArgumentException("A função informada não existe."));
        if (!role.isActive()) {
            throw new IllegalArgumentException("Não é possível cadastrar um usuário com a função inativa.");
        }
        if (request.sectorId() != null) {
            userSector = userSectorRepository.findById(request.sectorId())
                    .orElseThrow(() -> new IllegalArgumentException("O setor informado não existe"));
            if (!userSector.isActive()) {
                throw new IllegalArgumentException("Não é possível cadastrar um usuário com o setor inativo.");
            }
        }
        User insertedBy = userRepository.findById(currentUserId)
                .orElseThrow(() -> new IllegalArgumentException("Não foi encontrado usuário com o ID informado"));

        // Verifica se o usuário já existe antes de criar (nunca pode ter 2 iguais)
        if (userRepository.existsByUsernameIgnoreCase(username)) {
            throw new IllegalArgumentException("O usuário que está tentando cadastrar já existe.");
        }

        // Criar a entidade com base na request
        User user = new User();
        user.setName(name);
        user.setUsername(username);
        user.setPasswordHash(passwordEncoder.encode(request.password()));
        user.setRole(role);
        user.setSector(userSector);
        user.setInsertedBy(insertedBy);

        User created = userRepository.save(user);

        return toResponse(created);
    }

    private UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getUsername(),
                user.getRole().getId(),
                user.getRole().getName(),
                user.getSector() != null ? user.getSector().getId() : null,
                user.getSector() != null ? user.getSector().getName() : null,
                user.getCreatedAt(),
                user.isActive(),
                user.getInsertedBy() != null ? user.getInsertedBy().getName() : null
        );
    }
}
