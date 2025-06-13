package com.ipin.rest_api.service;

import com.ipin.rest_api.dto.request.UserEditionRequest;
import com.ipin.rest_api.dto.request.UserLoginRequest;
import com.ipin.rest_api.dto.request.UserRegistrationRequest;
import com.ipin.rest_api.dto.response.UserLoginResponse;
import com.ipin.rest_api.dto.response.UserResponse;
import com.ipin.rest_api.entity.User;
import com.ipin.rest_api.enums.ErrorCode;
import com.ipin.rest_api.exception.AppException;
import com.ipin.rest_api.repository.RoleRepository;
import com.ipin.rest_api.repository.UserRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class UserService {

    UserRepository userRepository;
    RoleRepository roleRepository;
    PasswordEncoder passwordEncoder;

    @NonFinal
    @Value("${jwt.valid_duration}")
    Long validDuration;

    @NonFinal
    @Value("${jwt.secret_key}")
    String signerKey;

    public UserResponse register(UserRegistrationRequest request) {

        if (userRepository.existsByUserName(request.getUserName())) {
            throw new AppException(ErrorCode.USERNAME_EXISTED);
        }

        var roles = roleRepository.findAllById(request.getRoles());

        User user = User.builder()
                .name(request.getName())
                .userName(request.getUserName())
                .passWord(passwordEncoder.encode(request.getPassWord()))
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .status(true)
                .roles(new HashSet<>(roles))
                .build();

        userRepository.save(user);

        return UserResponse.builder()
                .name(user.getName())
                .userName(user.getUserName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .status(user.isStatus())
                .deleted(user.isDeleted())
                .deletedAt(user.getDeletedAt())
                .roles(user.getRoles())
                .build();
    }

    public UserLoginResponse authenticate(UserLoginRequest request) throws JOSEException {

        User user = userRepository.findByUserName(request.getUserName())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        boolean matched_password = passwordEncoder.matches(request.getPassWord(), user.getPassWord());

        if (!matched_password) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        
        var token = generateToken(user);

        return UserLoginResponse.builder()
                .token(token)
                .build();
    }

    public String generateToken(User user) throws JOSEException {
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS512);

        Payload payload = new Payload(
                new JWTClaimsSet.Builder()
                        .subject(user.getUserName())
                        .issuer(user.getName())
                        .issueTime(new Date())
                        .expirationTime(
                                new Date(Instant.now().plus(validDuration, ChronoUnit.SECONDS).toEpochMilli())
                        )
                        .jwtID(UUID.randomUUID().toString())
                        .claim("role", extractRoles(user))
                        .build()
                        .toJSONObject()
        );

        JWSObject jwsObject = new JWSObject(jwsHeader, payload);

        jwsObject.sign(new MACSigner(signerKey.getBytes()));
        return jwsObject.serialize();
    }

    public List<String> extractRoles(User user) {
        var roles = user.getRoles();
        List<String> rolesList = null;

        if (!roles.isEmpty()) {
            rolesList = roles.stream().map(role -> "ROLE_" + role.getName()).toList();
        }
        return rolesList;
    }

    public UserResponse editMyInfo(UserEditionRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        var user = userRepository.findByUserName(auth.getName())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        user.setPassWord(passwordEncoder.encode(request.getPassWord()));
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPhoneNumber(request.getPhoneNumber());


        userRepository.save(user);

        return UserResponse.builder()
                .userName(user.getUserName())
                .name(user.getName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .status(user.isStatus())
                .deleted(user.isDeleted())
                .deletedAt(user.getDeletedAt())
                .roles(user.getRoles())
                .build();
    }

    public UserResponse getMyInfo() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        var user = userRepository.findByUserName(auth.getName())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        return UserResponse.builder()
                .name(user.getName())
                .userName(user.getUserName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .status(user.isStatus())
                .deleted(user.isDeleted())
                .deletedAt(user.getDeletedAt())
                .roles(user.getRoles())
                .build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> getUsers() {
        return userRepository.findAll().stream()
                .map(user -> UserResponse.builder()
                        .name(user.getName())
                        .userName(user.getUserName())
                        .email(user.getEmail())
                        .phoneNumber(user.getPhoneNumber())
                        .status(user.isStatus())
                        .deleted(user.isDeleted())
                        .deletedAt(user.getDeletedAt())
                        .roles(user.getRoles())
                        .build()
                ).toList();
    }

    public void deleteMyAccount() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        var user = userRepository.findByUserName(auth.getName())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        user.setDeleted(true);
        user.setDeletedAt(LocalDateTime.now());

        userRepository.save(user);
    }
}
