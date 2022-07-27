package com.codinglk.tutorials.junit.ui.controllers;


import com.codinglk.tutorials.junit.security.SecurityConstants;
import com.codinglk.tutorials.junit.ui.response.UserRest;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.TestPropertySource;

import java.util.Arrays;
import java.util.List;

//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, properties = {"server.port=8081"})
// It is recommended to use RANDOM_PORT to avoid the port conflicts, we can run multiple integration tests in one test environment
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@TestPropertySource(locations = "/application-test.properties", properties = "server.port=8081")
//@TestPropertySource(locations = "/application-test.properties")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
// By default, Instance is created per method, we need to use member variable in other methods so added this annotation to create instance per class
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UsersControllerIntegrationTest {

    @Value("${server.port}")
    private int serverPort;

    @LocalServerPort
    private int localServerPort;

    @Autowired
    private TestRestTemplate testRestTemplate;

    private String authorizationToken;

//    @Test
//    void contextLoads(){
//        System.out.println("Server Port - "+ serverPort);
//        System.out.println("Local Server Port - "+ localServerPort);
//    }

    @Test
    @DisplayName("User can be created")
    @Order(1)
    void testCreateUser_whenValidDetailsProvided_returnsUserDetails() throws JSONException {
        // Arrange
        JSONObject userDetailsRequestJson = new JSONObject();
        userDetailsRequestJson.put("firstName", "Lalit");
        userDetailsRequestJson.put("lastName", "Kumar");
        userDetailsRequestJson.put("email", "test@test.com");
        userDetailsRequestJson.put("password", "12345678");
        userDetailsRequestJson.put("repeatPassword", "12345678");

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

        HttpEntity<String> request = new HttpEntity<>(userDetailsRequestJson.toString(), httpHeaders);

        // Act
        ResponseEntity<UserRest> createdUserDetailsEntity = testRestTemplate.postForEntity("/users",
                request,
                UserRest.class);
        UserRest userRest = createdUserDetailsEntity.getBody();

        // Assert
        Assertions.assertEquals(HttpStatus.OK, createdUserDetailsEntity.getStatusCode());
        Assertions.assertEquals(userDetailsRequestJson.get("firstName"),
                userRest.getFirstName(),
                "Returned user's first name seems to be incorrect");
        Assertions.assertEquals(userDetailsRequestJson.get("lastName"),
                userRest.getLastName(),
                "Returned user's last name seems to be incorrect");
        Assertions.assertEquals(userDetailsRequestJson.get("email"),
                userRest.getEmail(),
                "Returned user's email seems to be incorrect");
        Assertions.assertFalse(userRest.getUserId().trim().isEmpty(), "User Id should not be empty");
    }

    @Test
    @DisplayName("GET /users requires JWT")
    @Order(2)
    void tetGetUsers_whenMissingJWT_returns403(){
        // Arrange
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Accept","application/json");

        HttpEntity requestEntity = new HttpEntity(null,httpHeaders);

        // Act
        ResponseEntity<List<UserRest>> responseEntity = testRestTemplate.exchange("/users",
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<List<UserRest>>() {
                });

        // Asserts
        Assertions.assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode(), "HTTP Status code 403 Forbidden should have been returned");
    }

    @Test
    @DisplayName("/login works")
    @Order(3)
    void testUserLogin_whenValidCredentialsProvided_returnsJWTinAuthorizationHeader() throws JSONException {
        // Arrange
//        String loginCredentialsJson = "{\n" +
//                "   \"email\":\"test@test.com\", \n " +
//                "   \"password\":\"12345678\", \n " +
//                "}";

        JSONObject loginCredentials = new JSONObject();
        loginCredentials.put("email", "test@test.com");
        loginCredentials.put("password", "12345678");

        HttpEntity<String> request = new HttpEntity<>(loginCredentials.toString());

        // Act
        ResponseEntity responseEntity = testRestTemplate.postForEntity("/users/login",
                request,
                null);

        authorizationToken = responseEntity.getHeaders().
                getValuesAsList(SecurityConstants.HEADER_STRING).get(0);

        // Assert
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode(),
                "HTTP Status code should be 200");
        Assertions.assertNotNull(authorizationToken,
                "Response should contain Authorization header with JWT");
        Assertions.assertNotNull(responseEntity.getHeaders().
                getValuesAsList("UserID").get(0),
                "Response should contain UserID in response header");
    }

    @Test
    @Order(4)
    @DisplayName("GET /users works")
    void testGetUsers_whenValidJWTProvided_returnsUsers(){
        // Arrange
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        httpHeaders.setBearerAuth(authorizationToken);

        HttpEntity httpEntity = new HttpEntity(httpHeaders);

        // Act
        ResponseEntity<List<UserRest>> responseEntity = testRestTemplate.exchange("/users",
                HttpMethod.GET,
                httpEntity,
                new ParameterizedTypeReference<List<UserRest>>() {
                });

        // Asserts
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode(),
                "HTTP Status code should be 200");
        Assertions.assertEquals(1,responseEntity.getBody().size(),
                "There should be exactly 1 user in the list");
    }
}
