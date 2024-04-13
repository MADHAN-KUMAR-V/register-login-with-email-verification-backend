package com.madhan65.emailverification.registration;

public record RegistrationRequest(
        String firstName,
        String lastName,
        String email,
        String password,
        String role
) {
}
