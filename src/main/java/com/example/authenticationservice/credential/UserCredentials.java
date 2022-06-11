package com.example.authenticationservice.credential;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserCredentials {
    private String username;
    private String password;
}
