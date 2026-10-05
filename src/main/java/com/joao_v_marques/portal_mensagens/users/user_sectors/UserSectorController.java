package com.joao_v_marques.portal_mensagens.users.user_sectors;

import com.joao_v_marques.portal_mensagens.users.user_sectors.dto.UserSectorRequest;
import com.joao_v_marques.portal_mensagens.users.user_sectors.dto.UserSectorResponse;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/user-sectors")
public class UserSectorController {

    private final UserSectorService userSectorService;

    public UserSectorController(UserSectorService userSectorService) {
        this.userSectorService = userSectorService;
    }

    // GET de todos os setores cadastrados no sistema
    @GetMapping
    public List<UserSectorResponse> findAll() {
        return userSectorService.findAll();
    }

    // POST de um novo setor no sistema
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserSectorResponse> create(@Valid @RequestBody UserSectorRequest request) {
        UserSectorResponse created = userSectorService.create(request);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.id())
                .toUri();

        return ResponseEntity.created(location).body(created);
    }

    // PUT de um setor já existente
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserSectorResponse> update(@PathVariable Integer id, @Valid @RequestBody UserSectorRequest request) {
        UserSectorResponse updated = userSectorService.update(id, request);

        return ResponseEntity.ok(updated);
    }

    // PATCH para desativar um setor já existente
    @PatchMapping(value = "/{id}/deactivate")
    public ResponseEntity<UserSectorResponse> deactivate(@PathVariable Integer id) {
        UserSectorResponse deactivated = userSectorService.deactivate(id);

        return ResponseEntity.ok(deactivated);
    }

    // PATCH para reativar um setor já existente
    @PatchMapping(value = "/{id}/reactivate")
    public ResponseEntity<UserSectorResponse> reactivate(@PathVariable Integer id) {
        UserSectorResponse reactivated = userSectorService.reactivate(id);

        return ResponseEntity.ok(reactivated);
    }
}
