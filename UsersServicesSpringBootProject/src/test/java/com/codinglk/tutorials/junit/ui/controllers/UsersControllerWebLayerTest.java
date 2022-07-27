package com.codinglk.tutorials.junit.ui.controllers;

import com.codinglk.tutorials.junit.service.UsersService;
import com.codinglk.tutorials.junit.shared.UserDto;
import com.codinglk.tutorials.junit.ui.request.UserDetailsRequestModel;
import com.codinglk.tutorials.junit.ui.response.UserRest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebMvcTest(controllers = UsersController.class,
excludeAutoConfiguration = SecurityAutoConfiguration.class)
//@AutoConfigureMockMvc(addFilters = false)
//@MockBean(UsersServiceImpl.class)
public class UsersControllerWebLayerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
//    @Autowired
    UsersService usersService;

    private UserDetailsRequestModel userDetailsRequestModel;

    @BeforeEach
    void setup(){
        userDetailsRequestModel = new UserDetailsRequestModel();
        userDetailsRequestModel.setFirstName("Lalit");
        userDetailsRequestModel.setLastName("Kumar");
        userDetailsRequestModel.setEmail("email@test.com");
        userDetailsRequestModel.setPassword("12345678");
        userDetailsRequestModel.setRepeatPassword("12345678");
    }

    @Test
    @DisplayName("User can be created")
    void testCreateUser_whenValidUserDetailsProvided_returnsCreatedUserDetails() throws Exception {
        // Arrange

//        UserDto userDto = new UserDto();
//        userDto.setFirstName("Lalit");
//        userDto.setLastName("Kumar");
//        userDto.setEmail("email");
//        userDto.setUserId(UUID.randomUUID().toString());

        UserDto userDto = new ModelMapper().map(userDetailsRequestModel, UserDto.class);
        userDto.setUserId(UUID.randomUUID().toString());
        when(usersService.createUser(any(UserDto.class))).thenReturn(userDto);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(userDetailsRequestModel));

        // Act
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
        String responseBodyAsString = mvcResult.getResponse().getContentAsString();
        UserRest createdUser = new ObjectMapper().readValue(responseBodyAsString, UserRest.class);

        // Assert
        Assertions.assertEquals(userDetailsRequestModel.getFirstName(), createdUser.getFirstName(),
                "First name is incorrect.");
        Assertions.assertEquals(userDetailsRequestModel.getLastName(), createdUser.getLastName(),
                "Last name is incorrect.");
        Assertions.assertEquals(userDetailsRequestModel.getEmail(), createdUser.getEmail(),
                "Email is incorrect.");
        Assertions.assertFalse(createdUser.getUserId().isEmpty(), "userId should not be empty");

    }

    @Test
    @DisplayName("First Name is not empty")
    void testCreateUser_whenFirstNameIsNotProvided_returns400StatusCode() throws Exception {
        // Arrange
        userDetailsRequestModel.setFirstName("");

        UserDto userDto = new ModelMapper().map(userDetailsRequestModel, UserDto.class);
        userDto.setUserId(UUID.randomUUID().toString());
        when(usersService.createUser(any(UserDto.class))).thenReturn(userDto);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(userDetailsRequestModel));

        // Act
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

        // Assert
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), mvcResult.getResponse().getStatus(), "Incorrect Status Code is returned");

    }

    @Test
    @DisplayName("First name must not be less than 2 characters")
    void testCreateUser_whenFirstNameLessThan2Characters_returns400StatusCode() throws Exception {
        // Arrange
        userDetailsRequestModel.setFirstName("L");

        UserDto userDto = new ModelMapper().map(userDetailsRequestModel, UserDto.class);
        userDto.setUserId(UUID.randomUUID().toString());
        when(usersService.createUser(any(UserDto.class))).thenReturn(userDto);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(userDetailsRequestModel));

        // Act
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

        // Assert
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), mvcResult.getResponse().getStatus(), "HTTP Status is not set to 400");

    }

}
