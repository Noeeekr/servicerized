package com.github.noeeekr.servicerized.identity.repository.interfaces;

import java.util.UUID;

public interface UserInterface extends CreateUserInterface {
    public UUID getUserId();
}
