package com.example.authenticationservice.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// to create user password, not access token.
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class KeycloakPassword {
    private boolean temporary;
    private String type;
    private String value;
}
