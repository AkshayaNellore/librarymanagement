package com.cts.lms.dto;

import java.util.Set;

import com.cts.lms.enums.Membership;
import com.cts.lms.enums.Role;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private String memberId;
    private String name;
    @NotNull(message = "user name should not be null")
    private String username;
    @Email(message = "email format is incorrect ")
    private String email;
    @Pattern(regexp = "\\d{10}", message = "Phone number must contain exactly 10 digits")
    private String phone;
    private String address;
    private Membership membershipStatus;
    private String userType;
    @NotNull(message = "password should not be null")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
}