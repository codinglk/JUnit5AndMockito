package com.codinglk.estore.service;

import com.codinglk.estore.model.User;

/**
 * @author codinglk
 */
public interface UserService {
    User createUser(String firstName,
                    String lastName,
                    String email,
                    String password,
                    String repeatPassword);
}
