package com.ipin.rest_api.dto.response;

import com.ipin.rest_api.entity.Role;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.Set;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
    String name;
    String userName;
    String email;
    String phoneNumber;
    boolean status;
    boolean deleted;
    LocalDateTime deletedAt;

    Set<Role> roles;
}
