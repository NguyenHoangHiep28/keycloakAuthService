package com.example.authenticationservice.authentication;

import com.example.authenticationservice.credential.KeycloakAccessToken;
import com.example.authenticationservice.credential.UserCredentials;
import com.example.authenticationservice.dto.request.LogoutRequest;
import com.example.authenticationservice.dto.request.RefreshTokenRequest;
import com.example.authenticationservice.dto.request.UserRegisterRequest;
import com.example.authenticationservice.user.KeycloakPassword;
import com.example.authenticationservice.user.KeycloakService;
import com.example.authenticationservice.user.KeycloakUser;
import org.keycloak.authorization.client.util.Http;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

//@CrossOrigin("*")
@CrossOrigin(origins = "http://127.0.0.1:4200", allowCredentials = "true")
@RestController
@RequestMapping("/api/v1")
public class AuthenticationController {
    private final KeycloakService keycloakService;

    public AuthenticationController(KeycloakService keycloakService) {
        this.keycloakService = keycloakService;
    }

    @RequestMapping(method = RequestMethod.POST, path = "/login")
    public ResponseEntity<?> login(@RequestBody UserCredentials userCredentials) throws IOException {
        KeycloakAccessToken result = keycloakService.login(userCredentials.getUsername(), userCredentials.getPassword());
        if (result != null) {
//            ResponseCookie responseCookie = keycloakService.getRefreshTokenCookie("/", (long) (7 * 24 * 60 * 60), result.getRefresh_token());
            return ResponseEntity.ok()
//                    .header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.SET_COOKIE)
//                    .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                    .body(result);
        }
        return ResponseEntity.notFound().build();
    }

    @RequestMapping(path = "/register", method = RequestMethod.POST)
    public ResponseEntity<?> register(@RequestBody UserRegisterRequest userRegisterRequest) throws IOException {
        KeycloakUser newUser = new KeycloakUser();
        newUser.setUsername(userRegisterRequest.getUsername());
        List<KeycloakPassword> credentials = new ArrayList<>();
        credentials.add(new KeycloakPassword(false, "password", userRegisterRequest.getPassword()));
        newUser.setCredentials(credentials);
        newUser.setEnabled(true);
        if (keycloakService.save(newUser)) {
            KeycloakAccessToken result = keycloakService.login(userRegisterRequest.getUsername(), userRegisterRequest.getPassword());
//            ResponseCookie responseCookie = keycloakService.getRefreshTokenCookie("/", (long) (7 * 24 * 60 * 60), result.getRefresh_token());
            return ResponseEntity.ok()
//                    .header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.SET_COOKIE)
//                    .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                    .body(result);
        } else {
            throw new IOException();
        }
    }

    @RequestMapping(path = "/refresh-token", method = RequestMethod.GET)
    public ResponseEntity<?> refreshToken(@RequestParam(name = "refreshToken") String refreshToken) throws IOException {
//        String refresh_token = null;
//        Cookie[] cookies = request.getCookies();
//        if (cookies != null) {
//            for (Cookie cookie : cookies) {
//                if (cookie.getName().equals("refresh_token")) {
//                    refresh_token = cookie.getValue();
//                    break;
//                }
//            }
//        }
//        if (refresh_token != null) {
            KeycloakAccessToken token = keycloakService.refreshToken(refreshToken);
            if (token != null) {
                return ResponseEntity.ok(token);
            } else {
                return ResponseEntity.badRequest().build();
            }
//        } else {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//        }
    }

    @RequestMapping(method = RequestMethod.GET, path = "/logout")
    public ResponseEntity<?> logout(@RequestParam(name = "refreshToken") String refreshToken) throws IOException {
//        String refresh_token = null;
//        Cookie[] cookies = request.getCookies();
//        if (cookies != null) {
//            for (Cookie cookie : cookies) {
//                if (cookie.getName().equals("refresh_token")) {
//                    refresh_token = cookie.getValue();
//                    break;
//                }
//            }
//        }
//        if (refresh_token != null) {
            if (keycloakService.logout(refreshToken)) {
//                ResponseCookie responseCookie = keycloakService.getRefreshTokenCookie("/", (long) 0, "");
                return ResponseEntity.ok().build();
//                        .header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.SET_COOKIE)
//                        .header(HttpHeaders.SET_COOKIE, responseCookie.toString()).build();
            } else {
                return ResponseEntity.badRequest().build();
            }
//        } else {
//            return ResponseEntity.badRequest().build();
//        }
    }

}
