package com.ipin.rest_api.service;

import com.ipin.rest_api.dto.request.RoleCreationRequest;
import com.ipin.rest_api.dto.response.RoleEditionRequest;
import com.ipin.rest_api.dto.response.RoleResponse;
import com.ipin.rest_api.entity.Role;
import com.ipin.rest_api.enums.ErrorCode;
import com.ipin.rest_api.exception.AppException;
import com.ipin.rest_api.repository.RoleRepository;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class RoleService {

    RoleRepository roleRepository;

    @PreAuthorize("hasRole('ADMIN')")
    public RoleResponse createRole(RoleCreationRequest request) {
        if (roleRepository.existsByName(request.getName())) {
            throw new AppException(ErrorCode.ROLE_EXISTED);
        }

        var role = Role.builder()
                .name(request.getName())
                .description(request.getDescription())
                .build();

        roleRepository.save(role);

        return RoleResponse.builder()
                .name(role.getName())
                .description(role.getDescription())
                .build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    public RoleResponse editRole(String roleName, RoleEditionRequest request) {

        var role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));

        role.setDescription(request.getDescription());

        roleRepository.save(role);

        return RoleResponse.builder()
                .name(role.getName())
                .description(role.getDescription())
                .build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void deleteRole(String roleName) {

        roleRepository.deleteByName(roleName);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<RoleResponse> getRoles() {
        return roleRepository.findAll()
                .stream()
                .map(role -> RoleResponse.builder()
                        .name(role.getName())
                        .description(role.getDescription())
                        .build()
                ).toList();
    }
}
