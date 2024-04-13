package com.madhan65.emailverification.event;

import com.madhan65.emailverification.user.User;
import com.madhan65.emailverification.user.UserService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserRegistrationEventListener implements ApplicationListener<UserRegistrationEvent> {
    private final UserService userService;
    private final JavaMailSender mailSender;

    private User theUser;
    @Override
    public void onApplicationEvent(UserRegistrationEvent event) {
        //get newly register email
        theUser = event.getUser();
        //create token
        String verificationToken = UUID.randomUUID().toString();
        //save toke
        userService.saveVerificationToken(theUser,verificationToken);
        //build url
        String url = event.getApplicationUrl()+"/register/verifyEmail?token="+verificationToken;
        //sent email
        try {
            sendVerificationEmail(url);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        log.info("verify email: {}",url);
    }
    @Value("${spring.mail.username}") private String sender;
    public void sendVerificationEmail(String url) throws MessagingException, UnsupportedEncodingException {
        String subject="Email verification";
        String senderName="User Registration Portal Service";
        String content= "<p>Hi ," + theUser.getFirstName() + ",</p>"+
                "<p>Thankyou you for registration with us,"+""+
                "Plase verify the link below to complete registration"+
                "<a href=\""+url+"\"> Verify your email</a>";
        MimeMessage message = mailSender.createMimeMessage();
        var messageHelper = new MimeMessageHelper(message);
        messageHelper.setFrom(sender,senderName);
        messageHelper.setTo(theUser.getEmail());
        messageHelper.setSubject(subject);
        messageHelper.setText(content,true);
        mailSender.send(message);
    }
}
