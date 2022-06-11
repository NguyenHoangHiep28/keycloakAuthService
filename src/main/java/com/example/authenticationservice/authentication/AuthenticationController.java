package com.example.authenticationservice.authentication;

import com.example.authenticationservice.credential.UserCredentials;
import com.example.authenticationservice.dto.request.LogoutRequest;
import com.example.authenticationservice.dto.request.UserRegisterRequest;
import com.example.authenticationservice.user.KeycloakPassword;
import com.example.authenticationservice.user.KeycloakService;
import com.example.authenticationservice.user.KeycloakUser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1")
public class AuthenticationController {
    private final KeycloakService keycloakService;

    public AuthenticationController(KeycloakService keycloakService) {
        this.keycloakService = keycloakService;
    }

    @RequestMapping(method = RequestMethod.POST, path = "/login")
    public ResponseEntity<?> login(@RequestBody UserCredentials userCredentials) throws IOException {
        return ResponseEntity.ok(keycloakService.login(userCredentials.getUsername(), userCredentials.getPassword()));
    }
    @RequestMapping(path = "/register",method = RequestMethod.POST)
    public ResponseEntity<?> register(@RequestBody UserRegisterRequest userRegisterRequest) throws IOException {
        KeycloakUser newUser = new KeycloakUser();
        newUser.setUsername(userRegisterRequest.getUsername());
        List<KeycloakPassword> credentials = new ArrayList<>();
        credentials.add(new KeycloakPassword(false, "password", userRegisterRequest.getPassword()));
        newUser.setCredentials(credentials);
        newUser.setEnabled(true);
        if (keycloakService.save(newUser)) {
            return ResponseEntity.ok(keycloakService.login(userRegisterRequest.getUsername(),userRegisterRequest.getPassword()));
        } else {
            throw new IOException();
        }
    }
    @RequestMapping(method = RequestMethod.POST, path = "/logout")
    public ResponseEntity<?> logout(@RequestBody LogoutRequest logoutRequest) throws IOException {
        if (keycloakService.logout(logoutRequest.getRefreshToken())){
            return ResponseEntity.ok().build();
        }else {
            return ResponseEntity.badRequest().build();
        }
    }

}
