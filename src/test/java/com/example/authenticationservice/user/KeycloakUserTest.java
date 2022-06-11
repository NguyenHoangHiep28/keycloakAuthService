package com.example.authenticationservice.user;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class KeycloakUserTest {

    @Test
    void testBuilder(){
        HashMap<String, Object> attributes = new HashMap<>();
        attributes.put("atr1", "Attribute 01");
        attributes.put("atr2", "Attribute 02");
        attributes.put("atr3", "Attribute 03");
        attributes.put("atr4", "Attribute 04");
        List<KeycloakPassword> credentials = new ArrayList<>();
        credentials.add(new KeycloakPassword(false, "password", "hongluyen01"));
        KeycloakUser keycloakUser = KeycloakUser.builder()
                .username("hongluyen01")
                .enabled(true)
                .attributes(attributes)
                .credentials(credentials)
                .build();
        System.out.println(keycloakUser.toString());
    }
}