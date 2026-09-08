package com.github.noeeekr.servicerized.identity.controller.request;

import com.github.noeeekr.servicerized.identity.services.user.request.UserSignInInterface;
import lombok.Builder;

@Builder  
public class SignInRequest implements UserSignInInterface {
    private String groupName;
    private String groupPassword;

    public String getGroupName() {
        return this.groupName;
    }
    public String getGroupPassword() {
        return this.groupPassword;
    }
}
