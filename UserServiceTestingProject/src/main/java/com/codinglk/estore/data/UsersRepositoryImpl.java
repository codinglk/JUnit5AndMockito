package com.codinglk.estore.data;

import com.codinglk.estore.model.User;

import java.util.HashMap;
import java.util.Map;

public class UsersRepositoryImpl implements UsersRepository {

    Map<String, User> users = new HashMap<>();

    @Override
    public boolean save(User user) {

        boolean returnValue = false;

        if(!users.containsKey(user.getUserId())) {
            users.put(user.getUserId(), user);
            returnValue = true;
        }
        return returnValue;
    }
}
