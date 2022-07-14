package com.codinglk.service;

import com.codinglk.io.UsersDatabase;

import java.util.Map;
import java.util.UUID;

public class UserServiceImpl implements UserService {

    UsersDatabase usersDatabase;

    public UserServiceImpl(UsersDatabase usersDatabase) {
        this.usersDatabase = usersDatabase;
    }

    @Override
    public String createUser(Map userDetails) {
        String userId = UUID.randomUUID().toString();
        userDetails.put("userId", userId);
        usersDatabase.save(userId, userDetails);
        return userId;
    }

    @Override
    public Map updateUser(String userId, Map userDetails) {
        Map existingUser = usersDatabase.find(userId);
        if(existingUser == null) throw new IllegalArgumentException("User not found");

        existingUser.put("firstName", userDetails.get("firstName"));
        existingUser.put("lastName", userDetails.get("lastName"));

        return usersDatabase.update(userId, existingUser);
    }

    @Override
    public Map getUserDetails(String userId) {
        return usersDatabase.find(userId);
    }

    @Override
    public void deleteUser(String userId) {
        Map existingUser = usersDatabase.find(userId);
        if(existingUser == null) throw new IllegalArgumentException("User not found");

        usersDatabase.delete(userId);
    }
}
