package com.ipin.rest_api.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserEditionRequest {

    @NotBlank(message = "Name is mandatory")
    String name;

    @NotBlank(message = "Password is mandatory")
    @Size(min = 8, message = "Password must be at least 8 characters")
    String passWord;

    @NotBlank(message = "Name is mandatory")
    @Email(message = "Email format is incorrect")
    String email;

    @NotBlank(message = "Phone number is mandatory")
    @Size(min = 10, message = "Phone number must be at least 10 characters")
    String phoneNumber;
}
