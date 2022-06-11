package com.example.authenticationservice.credential;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class KeycloakAccessToken {
    private String access_token;
    private int expires_in;
    private int refresh_expires_in;
    private String refresh_token;
    private String token_type;
    private String session_state;
    private String scope;
    private String userId;
}
