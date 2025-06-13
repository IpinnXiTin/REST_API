package com.ipin.rest_api.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    String name;
    String userName;
    String passWord;
    String email;
    String phoneNumber;
    boolean status;
    boolean deleted = false;
    LocalDateTime deletedAt = null;

    @ManyToMany
    Set<Role> roles;
}
