package com.madhan65.emailverification.user;

import com.madhan65.emailverification.exception.UserAlreadyExistException;
import com.madhan65.emailverification.registration.RegistrationRepo;
import com.madhan65.emailverification.registration.RegistrationRequest;
import com.madhan65.emailverification.registration.token.VerificationToken;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;
@Service
@RequiredArgsConstructor
public class UserService implements IUserService{
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final RegistrationRepo registrationRepo;
    @Override
    public List<User> getUsers() {
        return userRepo.findAll();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepo.findByEmail(email);
    }

    @Override
    public User registration(RegistrationRequest request) {
        Optional<User> user = this.findByEmail(request.email());
        if(user.isPresent()){
            throw new UserAlreadyExistException("Email already Exist");
        }
        var newUser = new User();
        newUser.setFirstName(request.firstName());
        newUser.setLastName(request.lastName());
        newUser.setEmail(request.email());
        newUser.setPassword(passwordEncoder.encode(request.password()));
        newUser.setRole(request.role());
        return userRepo.save(newUser);
    }

    @Override
    public void saveVerificationToken(User theUser, String verificationToken) {
        VerificationToken token = new VerificationToken(verificationToken,theUser);
        registrationRepo.save(token);
    }

    @Override
    public String validToken(String verifyToken) {
        VerificationToken token = registrationRepo.findByToken(verifyToken);
        if(token == null){
            return "Invalid token";
        }
        User user = token.getUser();
        Calendar calendar=Calendar.getInstance();
        if((token.getExpirationTime().getTime()- calendar.getTime().getTime())<=0){
            registrationRepo.delete(token);
            return "token expiry";
        }
        user.setEnabled(true);
        userRepo.save(user);
        return "valid";
    }
}
