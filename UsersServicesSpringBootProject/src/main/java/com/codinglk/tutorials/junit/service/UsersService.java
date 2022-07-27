package com.codinglk.tutorials.junit.service;

import com.codinglk.tutorials.junit.shared.UserDto;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UsersService extends UserDetailsService {
    UserDto createUser(UserDto user);
    List<UserDto> getUsers(int page, int limit);
    UserDto getUser(String email);
}
