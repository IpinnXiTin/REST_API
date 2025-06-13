package com.ipin.rest_api.controller;

import com.ipin.rest_api.dto.request.UserEditionRequest;
import com.ipin.rest_api.dto.request.UserLoginRequest;
import com.ipin.rest_api.dto.request.UserRegistrationRequest;
import com.ipin.rest_api.dto.response.ApiResponse;
import com.ipin.rest_api.dto.response.UserLoginResponse;
import com.ipin.rest_api.dto.response.UserResponse;
import com.ipin.rest_api.service.UserService;
import com.nimbusds.jose.JOSEException;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    UserService userService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserResponse>> register(@Valid @RequestBody UserRegistrationRequest request) {
        UserResponse user = userService.register(request);

        ApiResponse<UserResponse> apiResponse = ApiResponse.<UserResponse>builder()
                .code(1000)
                .message("User registered successfully")
                .result(user)
                .build();

        return ResponseEntity.accepted().body(apiResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<UserLoginResponse>> authenticate(@RequestBody UserLoginRequest request)
            throws JOSEException {
        UserLoginResponse user = userService.authenticate(request);

        ApiResponse<UserLoginResponse> apiResponse = ApiResponse.<UserLoginResponse>builder()
                .code(1000)
                .message("User logged in successfully")
                .result(user)
                .build();

        return ResponseEntity.ok().body(apiResponse);
    }

    @GetMapping("/get-my-info")
    public ResponseEntity<ApiResponse<UserResponse>> getMyInfo() {
        UserResponse user = userService.getMyInfo();

        ApiResponse<UserResponse> apiResponse = ApiResponse.<UserResponse>builder()
                .code(1000)
                .message("User retrieved successfully")
                .result(user)
                .build();

        return ResponseEntity.ok().body(apiResponse);
    }

    @GetMapping("/get-all-users")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getUsers() {
        List<UserResponse> users = userService.getUsers();

        ApiResponse<List<UserResponse>> apiResponse = ApiResponse.<List<UserResponse>>builder()
                .code(1000)
                .message("Users list retrieved successfully")
                .result(users)
                .build();

        return ResponseEntity.ok().body(apiResponse);
    }

    @PutMapping("/edit-my-info")
    public ResponseEntity<ApiResponse<UserResponse>> editMyInfo(@Valid @RequestBody UserEditionRequest request) {
        UserResponse user = userService.editMyInfo(request);

        ApiResponse<UserResponse> apiResponse = ApiResponse.<UserResponse>builder()
                .code(1000)
                .message("User edited successfully")
                .result(user)
                .build();

        return ResponseEntity.ok().body(apiResponse);
    }

    @DeleteMapping("/delete-my-account")
    public ResponseEntity<ApiResponse<?>> deleteMyAccount() {
        userService.deleteMyAccount();

        ApiResponse<?> apiResponse = ApiResponse.builder()
                .code(1000)
                .message("User deleted successfully")
                .build();

        return ResponseEntity.ok().body(apiResponse);
    }
}
