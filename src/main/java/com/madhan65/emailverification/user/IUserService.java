package com.madhan65.emailverification.user;

import com.madhan65.emailverification.registration.RegistrationRequest;
import com.madhan65.emailverification.registration.token.VerificationToken;

import java.util.List;
import java.util.Optional;

public interface IUserService {

    List<User> getUsers();
    Optional<User> findByEmail(String  email);

    User registration(RegistrationRequest request);

    void saveVerificationToken(User theUser, String verificationToken);

    String validToken(String verifyToken);
}
