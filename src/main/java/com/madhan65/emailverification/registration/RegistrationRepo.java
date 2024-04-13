package com.madhan65.emailverification.registration;

import com.madhan65.emailverification.registration.token.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegistrationRepo extends JpaRepository<VerificationToken,Long> {
    VerificationToken findByToken(String token);
}
