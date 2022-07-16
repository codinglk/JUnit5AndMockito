package com.codinglk.estore.service;

import com.codinglk.estore.data.UsersRepository;
import com.codinglk.estore.model.User;

import java.util.UUID;

/**
 * @author codinglk
 */
public class UserServiceImpl implements UserService {

    UsersRepository usersRepository;
    EmailVerificationService emailVerificationService;

    public UserServiceImpl(UsersRepository usersRepository, EmailVerificationService emailVerificationService) {
        this.usersRepository = usersRepository;
        this.emailVerificationService = emailVerificationService;
    }


    @Override
    public User createUser(String firstName,
                           String lastName,
                           String email,
                           String password,
                           String repeatPassword) {

        if(firstName == null || firstName.trim().length() == 0){
            throw new IllegalArgumentException("User's first name is empty");
        }

        if(lastName == null || lastName.trim().length() == 0){
            throw new IllegalArgumentException("User's last name is empty");
        }

        User user = new User(firstName, lastName, email, UUID.randomUUID().toString());
        boolean isUserCreated;
        try {
            isUserCreated = usersRepository.save(user);
        }catch(RuntimeException ex){
            throw new UserServiceException(ex.getMessage());
        }

        if(!isUserCreated) {
            throw new UserServiceException("Could not create user");
        }

        try {
            emailVerificationService.scheduleEmailConfirmation(user);
        }catch(RuntimeException ex){
            throw new UserServiceException(ex.getMessage());
        }

        return user;

    }

    void demoMethod(){
        System.out.println("Demo method");
    }
}
