package com.madhan65.emailverification.registration;

import com.madhan65.emailverification.event.UserRegistrationEvent;
import com.madhan65.emailverification.registration.token.VerificationToken;
import com.madhan65.emailverification.user.User;
import com.madhan65.emailverification.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/register")
@RequiredArgsConstructor
public class RegistrationController {

    private final UserService userService;
    private final ApplicationEventPublisher publisher;
    private final RegistrationRepo registrationRepo;
    @PostMapping
    public String registration(@RequestBody RegistrationRequest registrationRequest, final HttpServletRequest request){
        User user = userService.registration(registrationRequest);
        //publish register event
        publisher.publishEvent(new UserRegistrationEvent(user,applicationUrl(request)));
        return "Succesfully register. Please check your email";
    }
    @GetMapping("/verifyEmail")
    public String verifyEmail(@RequestParam("token") String token){
        VerificationToken verifyToken = registrationRepo.findByToken(token);
        if(verifyToken.getUser().isEnabled()){
            return "Already verify.Please Login";
        }
        String verifyResult = userService.validToken(token);
        if(verifyResult.equalsIgnoreCase("valid")){
            return "Successfully verify email!!";
        }
        return "Invalid verification";
    }

    private String applicationUrl(HttpServletRequest request) {
        return "http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath();
    }
    @GetMapping
    private String registration(){
        return "Hello";
    }
}
