package com.ipin.rest_api.controller;

import com.ipin.rest_api.dto.request.RoleCreationRequest;
import com.ipin.rest_api.dto.response.*;
import com.ipin.rest_api.dto.response.RoleEditionRequest;
import com.ipin.rest_api.service.RoleService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@RequestMapping("/role")
public class RoleController {

    RoleService roleService;

    @PostMapping("/create-role")
    public ResponseEntity<ApiResponse<RoleResponse>> createRole(@RequestBody RoleCreationRequest request) {
        RoleResponse role = roleService.createRole(request);

        ApiResponse<RoleResponse> apiResponse = ApiResponse.<RoleResponse>builder()
                .code(1000)
                .message("Role created successfully")
                .result(role)
                .build();

        return ResponseEntity.ok().body(apiResponse);
    }

    @GetMapping("/get-roles")
    public ResponseEntity<ApiResponse<List<RoleResponse>>> getRoles() {
        List<RoleResponse> roles = roleService.getRoles();

        ApiResponse<List<RoleResponse>> apiResponse = ApiResponse.<List<RoleResponse>>builder()
                .code(1000)
                .message("Roles list retrieved successfully")
                .result(roles)
                .build();

        return ResponseEntity.ok().body(apiResponse);
    }

    @PutMapping("/edit-role/{roleName}")
    public ResponseEntity<ApiResponse<RoleResponse>> editUser(@PathVariable String roleName, @RequestBody RoleEditionRequest request) {
        RoleResponse role = roleService.editRole(roleName, request);

        ApiResponse<RoleResponse> apiResponse = ApiResponse.<RoleResponse>builder()
                .code(1000)
                .message("Role edited successfully")
                .result(role)
                .build();

        return ResponseEntity.ok().body(apiResponse);
    }

    @DeleteMapping("/delete-role/{roleName}")
    public ResponseEntity<ApiResponse<?>> deleteUSer(@PathVariable String roleName) {
        roleService.deleteRole(roleName);

        ApiResponse<?> apiResponse = ApiResponse.builder()
                .code(1000)
                .message("Role deleted successfully")
                .build();

        return ResponseEntity.ok().body(apiResponse);
    }
}
