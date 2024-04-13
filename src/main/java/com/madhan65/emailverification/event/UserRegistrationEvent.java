package com.madhan65.emailverification.event;

import com.madhan65.emailverification.user.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class UserRegistrationEvent extends ApplicationEvent {

    private User user;
    private String applicationUrl;

    public UserRegistrationEvent(User user, String applicationUrl) {
        super(user);
        this.user = user;
        this.applicationUrl = applicationUrl;
    }
}
