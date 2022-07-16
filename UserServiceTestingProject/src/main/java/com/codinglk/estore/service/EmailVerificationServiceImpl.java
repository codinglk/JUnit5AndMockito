package com.codinglk.estore.service;

import com.codinglk.estore.model.User;

public class EmailVerificationServiceImpl implements EmailVerificationService {
    @Override
    public void scheduleEmailConfirmation(User user) {
        // Put user details in email queue
        System.out.println("scheduleEmailConfirmation is called");
    }
}
