package com.codinglk.estore.service;

import com.codinglk.estore.model.User;

public interface EmailVerificationService {

    void scheduleEmailConfirmation(User user);
}
