package com.joao_v_marques.portal_mensagens.users.user;

import com.joao_v_marques.portal_mensagens.users.user.dto.UserResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // GET de todos os usuários
    @Transactional(readOnly = true)
    public List<UserResponse> findAll() {
        return userRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
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
