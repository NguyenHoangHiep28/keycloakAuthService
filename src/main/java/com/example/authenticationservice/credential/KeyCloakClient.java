package com.example.authenticationservice.credential;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class KeyCloakClient {
    private String id;
    private String clientId;
}
