package com.ipin.rest_api.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Getter
public enum ErrorCode {

    USERNAME_EXISTED(1001, "Username existed", HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND(1002, "User not found", HttpStatus.FORBIDDEN),
    UNAUTHENTICATED(1003, "Password is incorrect", HttpStatus.FORBIDDEN),
    ROLE_EXISTED(1004, "Role existed", HttpStatus.BAD_REQUEST),
    ROLE_NOT_FOUND(1005, "Role not found", HttpStatus.FORBIDDEN);

    int code;
    String message;
    HttpStatus httpStatus;
}
