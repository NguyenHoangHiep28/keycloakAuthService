package com.example.authenticationservice.user;

import lombok.*;

import java.util.HashMap;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
@AllArgsConstructor
@Builder
public class KeycloakUser {
    private String id;
    private String username;
    private List<KeycloakPassword> credentials;
    private boolean enabled;
    private List<String> realmRoles;
    private HashMap<String, Object> attributes;

    public KeycloakUser(String username, List<KeycloakPassword> credentials, boolean enabled) {
        this.username = username;
        this.credentials = credentials;
        this.enabled = enabled;
    }
}
