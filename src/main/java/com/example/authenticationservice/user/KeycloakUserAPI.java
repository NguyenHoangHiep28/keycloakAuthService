package com.example.authenticationservice.user;

import com.example.authenticationservice.credential.KeycloakAccessToken;
import com.example.authenticationservice.credential.UserCredentials;
import com.example.authenticationservice.dto.request.AddUserRoleRequest;
import com.example.authenticationservice.dto.request.UserRegisterRequest;
import com.example.authenticationservice.dto.request.UserUpdateDTO;
import com.example.authenticationservice.dto.request.UserUpdateRequest;
import com.example.authenticationservice.util.Peggy;
import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin("*")
@RestController
@RequestMapping(path = "/api/v1/users")
@Log4j2
public class KeycloakUserAPI {
    private final KeycloakService keycloakService;

    public KeycloakUserAPI(KeycloakService keycloakService) {
        this.keycloakService = keycloakService;
    }

//    @RolesAllowed("app_admin")
    @RolesAllowed("user")
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Peggy<KeycloakUser>> index() throws IOException {
        return new ResponseEntity<>(
                keycloakService.findAll(null, null),
                HttpStatus.OK);
    }
//    @RolesAllowed("admin")
    @RolesAllowed("user")
    @RequestMapping(method = RequestMethod.GET, path = "/info")
    public ResponseEntity<KeycloakUser> findUserById(Authentication authentication) throws IOException {
        Optional<KeycloakUser> user = keycloakService.findById(authentication.getPrincipal().toString());
        if (user.isPresent()){
            return ResponseEntity.ok(user.get());
        }else {
            return ResponseEntity.notFound().build();
        }
    }
    @RolesAllowed("app_admin")
    @RequestMapping(method = RequestMethod.POST, path = "/add-role")
    public ResponseEntity<?> addRoleToUser(@RequestBody AddUserRoleRequest request) throws IOException {
        boolean success = keycloakService.addClientRoleToUser(request.getUserId(),request.getRoleName());
        if (success){
            return ResponseEntity.ok("Add role to user successfully!");
        }else {
            return ResponseEntity.badRequest().build();
        }
    }

    @RolesAllowed("user")
    @RequestMapping(method = RequestMethod.POST, path = "/update")
    public ResponseEntity<?> updateUser(@RequestBody UserUpdateRequest userUpdateRequest) throws IOException {
        UserUpdateDTO updateDTO = new UserUpdateDTO(userUpdateRequest.getUpdateAttributes());
        boolean success = keycloakService.update(userUpdateRequest.getUserId(), updateDTO);
        if (success){
            return ResponseEntity.ok().build();
        }else {
            return ResponseEntity.badRequest().build();
        }
    }
//    @RolesAllowed({"user", "admin"})
//    @RequestMapping(method = RequestMethod.GET, path = "/profile")
//    public ResponseEntity<?> viewProducts(){
//        return ResponseEntity.ok("Products show here!");
//    }
//    @RolesAllowed("admin")
//    @RequestMapping(method = RequestMethod.GET, path = "/orders")
//    public ResponseEntity<?> viewOrders(){
//        return ResponseEntity.ok("Orders show here!");
//    }

}
