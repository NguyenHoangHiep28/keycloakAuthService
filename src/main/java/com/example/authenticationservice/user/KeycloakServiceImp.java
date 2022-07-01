package com.example.authenticationservice.user;

import com.example.authenticationservice.credential.KeyCloakClient;
import com.example.authenticationservice.credential.KeycloakAccessToken;
import com.example.authenticationservice.credential.KeycloakRole;
import com.example.authenticationservice.dto.request.UserUpdateDTO;
import com.example.authenticationservice.dto.request.UserUpdateRequest;
import com.example.authenticationservice.retrofiet.RetrofietServiceGenerator;
import com.example.authenticationservice.retrofiet.RetrofietUserService;
import com.example.authenticationservice.util.KeycloakConstant;
import com.example.authenticationservice.util.Peggable;
import com.example.authenticationservice.util.Peggy;
import com.example.authenticationservice.util.Specifearcation;
import netscape.security.Principal;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import retrofit2.Response;

import java.io.IOException;
import java.util.*;

@Service
public class KeycloakServiceImp implements KeycloakService {

    public static final String LOGIN_FORM_CLIENT_ID_KEY = "client_id";
    public static final String LOGIN_FORM_USERNAME_KEY = "username";
    public static final String LOGIN_FORM_PASSWORD_KEY = "password";
    public static final String LOGIN_FORM_GRANT_TYPE_KEY = "grant_type";
    public static final String FORM_REFRESH_TOKEN_KEY = "refresh_token";
    public static final String TRUE_EXACT = "true";
    private static String adminToken;

    @Override
    public KeycloakAccessToken login(String username, String password) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put(LOGIN_FORM_CLIENT_ID_KEY, KeycloakConstant.KEYCLOAK_CLIENT_ID);
        params.put(LOGIN_FORM_USERNAME_KEY, username);
        params.put(LOGIN_FORM_PASSWORD_KEY, password);
        params.put(LOGIN_FORM_GRANT_TYPE_KEY, KeycloakConstant.KEYCLOAK_CREDENTIAL_GRANT_TYPE);
        RetrofietUserService service
                = RetrofietServiceGenerator.createService(RetrofietUserService.class);
        Response<KeycloakAccessToken> response = service.login(params).execute();
        if (response.isSuccessful()) {
            String userToken = response.body().getAccess_token();
        }
        KeycloakAccessToken finalResponse = response.body();
        KeycloakUser user = getUserByUsername(username);
        if (user != null){
            finalResponse.setUserId(user.getId());
            return finalResponse;
        }
        throw new IOException();
    }

    private KeycloakAccessToken loginAdmin(String username, String password) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put(LOGIN_FORM_CLIENT_ID_KEY, KeycloakConstant.KEYCLOAK_CLIENT_ID);
        params.put(LOGIN_FORM_USERNAME_KEY, username);
        params.put(LOGIN_FORM_PASSWORD_KEY, password);
        params.put(LOGIN_FORM_GRANT_TYPE_KEY, KeycloakConstant.KEYCLOAK_CREDENTIAL_GRANT_TYPE);
        RetrofietUserService service
                = RetrofietServiceGenerator.createService(RetrofietUserService.class);
        Response<KeycloakAccessToken> response = service.login(params).execute();
        if (response.isSuccessful()) {
            adminToken = response.body().getAccess_token();
            return response.body();
        }
        throw new IOException();
    }

    @Override
    public ResponseCookie getRefreshTokenCookie(String path, Long maxAge,String refreshToken) {
        return ResponseCookie.from(FORM_REFRESH_TOKEN_KEY, refreshToken)
                .maxAge(maxAge) // 7 days
                .path(path)
                .httpOnly(true)
                .build();
    }

    @Override
    public KeycloakAccessToken refreshToken(String refreshToken) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put(LOGIN_FORM_CLIENT_ID_KEY, KeycloakConstant.KEYCLOAK_CLIENT_ID);
        params.put(LOGIN_FORM_GRANT_TYPE_KEY, KeycloakConstant.KEYCLOAK_REFRESH_TOKEN_GRANT_TYPE);
        params.put(FORM_REFRESH_TOKEN_KEY, refreshToken);
        RetrofietUserService service
                = RetrofietServiceGenerator.createService(RetrofietUserService.class);
        Response<KeycloakAccessToken> response = service.refreshToken(params).execute();
        if (response.isSuccessful()) {
//            adminToken = response.body().getAccess_token();
            return response.body();
        }
        return null;
    }
    private KeycloakUser getUserByUsername(String username) throws IOException {
        prepareAdminToken();
        RetrofietUserService service
                = RetrofietServiceGenerator.createService(RetrofietUserService.class, adminToken);
        Response<List<KeycloakUser>> response = service.findUserByNameEct(username, TRUE_EXACT).execute();
        if (response.isSuccessful()){
            return response.body().get(0);
        }
        return null;
    }
    @Override
    public boolean logout(String refreshToken) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put(LOGIN_FORM_CLIENT_ID_KEY, KeycloakConstant.KEYCLOAK_CLIENT_ID);
        params.put(FORM_REFRESH_TOKEN_KEY, refreshToken);
        RetrofietUserService service
                = RetrofietServiceGenerator.createService(RetrofietUserService.class);
        Response<Void> response = service.logout(params).execute();
        return response.isSuccessful();
    }

    @Override
    public boolean save(KeycloakUser keycloakUser) throws IOException {
        prepareAdminToken();
        RetrofietUserService service
                = RetrofietServiceGenerator.createService(RetrofietUserService.class, adminToken);
        HashMap<String, Object> attributes = new HashMap<>();
        attributes.put("FirstName", "");
        attributes.put("LastName", "");
        attributes.put("Dob", "");
        attributes.put("Email", "");
        attributes.put("Phone", "");
        attributes.put("Avatar", "");
        attributes.put("Address", "");
        keycloakUser.setAttributes(attributes);
        Response<Void> response
                = service.save(keycloakUser).execute();
        if (!response.isSuccessful()) {
            if (response.code() == HttpStatus.UNAUTHORIZED.value()
                    || response.code() == HttpStatus.FORBIDDEN.value()) {
                adminToken = null;
            }
            throw new IOException(response.message());
        }
        return true;
    }

    @Override
    public Peggy<KeycloakUser> findAll(Specifearcation specifearcation, Peggable pageable) throws IOException {
        prepareAdminToken();
        RetrofietUserService service
                = RetrofietServiceGenerator.createService(RetrofietUserService.class, adminToken);
        Response<List<KeycloakUser>> response
                = service.findAll().execute();
        if (!response.isSuccessful()) {
            if (response.code() == HttpStatus.UNAUTHORIZED.value()
                    || response.code() == HttpStatus.FORBIDDEN.value()) {
                adminToken = null;
            }
            throw new IOException(response.message());
        }
        return Peggy.<KeycloakUser>builder().content(response.body()).limit(10).page(1).build();
    }

    @Override
    public Optional<KeycloakUser> findById(String id) throws IOException {
        prepareAdminToken();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if ((authentication instanceof KeycloakAuthenticationToken)) {
            Object currentUserName = authentication.getPrincipal();
            System.out.println(currentUserName);
        }
        RetrofietUserService service
                = RetrofietServiceGenerator.createService(RetrofietUserService.class, adminToken);
        Response<KeycloakUser> response
                = service.findById(id).execute();
        if (!response.isSuccessful()) {
            if (response.code() == HttpStatus.UNAUTHORIZED.value()
                    || response.code() == HttpStatus.FORBIDDEN.value()) {
                adminToken = null;
            }
            throw new IOException(response.message());
        }
        return Optional.ofNullable(response.body());
    }

    @Override
    public boolean update(String id, UserUpdateDTO updateDTO) throws IOException {
        prepareAdminToken();
        RetrofietUserService service
                = RetrofietServiceGenerator.createService(RetrofietUserService.class, adminToken);
        Optional<KeycloakUser> user = findById(id);
        if (user.isPresent()) {
            Map<String, Object> attributes = user.get().getAttributes();
            for (String key:
                    updateDTO.getAttributes().keySet()) {
                if (attributes.containsKey(key)){
                    attributes.put(key, updateDTO.getAttributes().get(key));
                }
            }
            UserUpdateDTO update = new UserUpdateDTO();
            update.setAttributes(attributes);
            Response<Void> response = service.update(id, update).execute();
            if (!response.isSuccessful()) {
                if (response.code() == HttpStatus.UNAUTHORIZED.value()
                        || response.code() == HttpStatus.FORBIDDEN.value()) {
                    adminToken = null;
                }
                throw new IOException(response.message());
            }
        }else {
            return false;
        }
        return true;
    }

    @Override
    public boolean delete(String id) throws IOException {
        prepareAdminToken();
        RetrofietUserService service
                = RetrofietServiceGenerator.createService(RetrofietUserService.class, adminToken);
        Response<Void> response
                = service.delete(id).execute();
        if (!response.isSuccessful()) {
            if (response.code() == HttpStatus.UNAUTHORIZED.value()
                    || response.code() == HttpStatus.FORBIDDEN.value()) {
                adminToken = null;
            }
            throw new IOException(response.message());
        }
        return true;
    }

//    private List<KeyCloakClient> findClient(String clientId) throws IOException {
//        prepareAdminToken();
//        RetrofietUserService service
//                = RetrofietServiceGenerator.createService(RetrofietUserService.class, adminToken);
//        Response<List<KeyCloakClient>> response = service.findClientByClientID(clientId).execute();
//        if (!response.isSuccessful()) {
//            if (response.code() == HttpStatus.UNAUTHORIZED.value()

//                    || response.code() == HttpStatus.FORBIDDEN.value()) {
//                adminToken = null;
//            }
//            throw new IOException(response.message());
//        }
//        return response.body();
//    }

    @Override
    public boolean addClientRoleToUser(String userId, String roleName) throws IOException {
        prepareAdminToken();
        RetrofietUserService service
                = RetrofietServiceGenerator.createService(RetrofietUserService.class, adminToken);
        Response<List<KeyCloakClient>> clientResponse = service.findClientByClientID(KeycloakConstant.KEYCLOAK_CLIENT_ID).execute();
        if (clientResponse.isSuccessful()){
            String idClient = clientResponse.body().get(0).getId();
            List<KeycloakRole> keycloakRoles = new ArrayList<>();
            KeycloakRole keycloakRole = service.findClientRoleByRoleName(idClient,roleName).execute().body();
            keycloakRoles.add(keycloakRole);
            Response<Void> response = service.addClientRoleToUser(userId, idClient, keycloakRoles).execute();
            if (!response.isSuccessful()) {
                if (response.code() == HttpStatus.UNAUTHORIZED.value()
                        || response.code() == HttpStatus.FORBIDDEN.value()) {
                    adminToken = null;
                }
                throw new IOException(response.message());
            }
            return true;
        }else {
            throw new IOException(clientResponse.message());
        }
    }

    @Override
    public void prepareAdminToken() throws IOException {
//        if (adminToken != null && adminToken.length() > 0) {
//            return;
//        }
        KeycloakAccessToken token
                = loginAdmin(KeycloakConstant.KEYCLOAK_ADMIN_USERNAME, KeycloakConstant.KEYCLOAK_ADMIN_PASSWORD);
        if (token == null) {
            throw new IOException();
        }
        adminToken = token.getAccess_token();
    }
}
