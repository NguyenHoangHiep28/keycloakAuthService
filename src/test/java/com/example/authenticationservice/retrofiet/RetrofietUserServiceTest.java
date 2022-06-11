package com.example.authenticationservice.retrofiet;

import com.example.authenticationservice.credential.KeyCloakClient;
import com.example.authenticationservice.credential.KeycloakAccessToken;
import com.example.authenticationservice.credential.KeycloakRole;
import com.example.authenticationservice.user.KeycloakPassword;
import com.example.authenticationservice.user.KeycloakUser;
import com.example.authenticationservice.util.KeycloakConstant;
import org.junit.jupiter.api.Test;
import retrofit2.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class RetrofietUserServiceTest {

    private String token = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJEY0E0dU5OV2QwaXhMa3FEeUVudUVvdWVhNVo0RVhyOFh5UDVJbEttY2FjIn0.eyJleHAiOjE2NTQwNDk4MjMsImlhdCI6MTY1NDA0OTUyMywianRpIjoiMTEzMTBiNDUtZTVhMC00NTdiLWEzMmEtZDY3YmMyNzFlMWNjIiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo4MDgwL2F1dGgvcmVhbG1zL21hc3RlciIsImF1ZCI6WyJtYXN0ZXItcmVhbG0iLCJhY2NvdW50Il0sInN1YiI6ImY1NjQzODRmLTY0MWEtNDFmNy1iYTRjLWY2YTU2NzhiNjZjMyIsInR5cCI6IkJlYXJlciIsImF6cCI6InRlc3QiLCJzZXNzaW9uX3N0YXRlIjoiM2U4ZmZkODAtZjgwNy00MWMxLTk2YWEtMWNjZDYwYjJiN2VkIiwiYWNyIjoiMSIsInJlYWxtX2FjY2VzcyI6eyJyb2xlcyI6WyJkZWZhdWx0LXJvbGVzLW1hc3RlciIsIm9mZmxpbmVfYWNjZXNzIiwidW1hX2F1dGhvcml6YXRpb24iXX0sInJlc291cmNlX2FjY2VzcyI6eyJtYXN0ZXItcmVhbG0iOnsicm9sZXMiOlsibWFuYWdlLXVzZXJzIiwidmlldy11c2VycyIsInF1ZXJ5LWNsaWVudHMiLCJxdWVyeS1ncm91cHMiLCJxdWVyeS11c2VycyJdfSwiYWNjb3VudCI6eyJyb2xlcyI6WyJtYW5hZ2UtYWNjb3VudCIsIm1hbmFnZS1hY2NvdW50LWxpbmtzIiwidmlldy1wcm9maWxlIl19fSwic2NvcGUiOiJwcm9maWxlIGVtYWlsIiwic2lkIjoiM2U4ZmZkODAtZjgwNy00MWMxLTk2YWEtMWNjZDYwYjJiN2VkIiwiZW1haWxfdmVyaWZpZWQiOmZhbHNlLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJob2FuZ2hpZXBhZG1pbiJ9.AKhxegPof6GBVM5LKiT_rNIUdgaPCSNzpOLCfnf0Nzhe-X_Fue_FGJsxwyVAOFgbiF_BQsa9E6KGLLKmJx2ZDqCOHVgxC5mHFAIaRThidrGsScHVuJdv4S2eX71mj9l1g-4e6GYk0WZIXhhDi3vlEMAS1p1YWIhVZQdV2bhdL-bdVGkBbLGF7Up-kGxBT1vU6_85QaDPxILwWysyzLkCbG1YZRFl1zyeq7x01XDqduBUNSiQfB4NDGchknyoTYqBgt9pI7UbF4WvovSgXSEYJg0QEUFbibKx-P0jW_-parC3pWAPr8OeSNrWF58YKkxvjWhUAYg9lgbODa3DLTeTjA";
    private String currentId = "2e9c6a3e-53b0-42c7-9bff-39086351b9a3";
    @Test
    void login() {
        Map<String,String> params = new HashMap<>();
        params.put("client_id", KeycloakConstant.KEYCLOAK_CLIENT_ID);
        params.put("username", KeycloakConstant.KEYCLOAK_ADMIN_USERNAME);
        params.put("password", KeycloakConstant.KEYCLOAK_ADMIN_PASSWORD);
        params.put("grant_type", KeycloakConstant.KEYCLOAK_CREDENTIAL_GRANT_TYPE);
        RetrofietUserService service
                = RetrofietServiceGenerator.createService(RetrofietUserService.class);
        try {
            KeycloakAccessToken accessToken = service.login(params).execute().body();
            System.out.println(accessToken.getAccess_token());
            System.out.println(accessToken.getExpires_in());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Test
    void findClient() throws IOException {
        RetrofietUserService service
                = RetrofietServiceGenerator.createService(RetrofietUserService.class, token);
        List<KeyCloakClient> keyCloakClient = service.findClientByClientID(KeycloakConstant.KEYCLOAK_CLIENT_ID).execute().body();
        System.out.println(keyCloakClient.get(0).toString());
    }
    @Test
    void addRoleToUser() throws IOException {
        RetrofietUserService service
                = RetrofietServiceGenerator.createService(RetrofietUserService.class, token);
        List<KeyCloakClient> keyCloakClients = service.findClientByClientID(KeycloakConstant.KEYCLOAK_CLIENT_ID).execute().body();
        System.out.println(keyCloakClients.get(0).getId());
        List<KeycloakRole> keycloakRoles = new ArrayList<>();
//        keycloakClientRoles.add(new KeycloakClientRole("e155ba9f-6fc9-405a-ac71-f3245e753a99","admin"));
        KeycloakRole keycloakRole = service.findClientRoleByRoleName(keyCloakClients.get(0).getId(), "admin").execute().body();
        System.out.println(keycloakRole.toString());
        keycloakRoles.add(keycloakRole);
        Response<Void> response = service.addClientRoleToUser(currentId, keyCloakClients.get(0).getId(), keycloakRoles).execute();
        if(response.isSuccessful()){
            System.out.println(response.body());
            System.out.println(response.toString());
        }else{
            System.out.println(response.errorBody().string());;
        }
    }
    @Test
    void save() throws IOException {
        RetrofietUserService service
                = RetrofietServiceGenerator.createService(RetrofietUserService.class, token);
        List<KeycloakPassword> credentials = new ArrayList<>();
        credentials.add(new KeycloakPassword(false, "password", "hoanghiepadmin"));
        List<String> realmRoles = new ArrayList<>();
        realmRoles.add("admin");
         Response<Void> response
                 = service.save(KeycloakUser.builder()
                 .username("hoanghiepadmin")
                 .credentials(credentials)
                 .realmRoles(realmRoles)
                 .enabled(true).build()).execute();
         if(response.isSuccessful()){
             System.out.println(response.body());
             System.out.println(response.toString());
         }else{
             System.out.println(response.errorBody().string());;
         }
    }

    @Test
    void findAll() throws IOException {
        RetrofietUserService service
                = RetrofietServiceGenerator.createService(RetrofietUserService.class, token);
        Response<List<KeycloakUser>> response
                = service.findAll().execute();
        if(response.isSuccessful()){
            List<KeycloakUser> list = response.body();
            System.out.println(list.size());
            for (KeycloakUser user : list){
                System.out.println(user.toString());
            }
        }else{
            System.out.println("Error");
            System.out.println(response.errorBody().string());;
        }
    }

    @Test
    void findById() throws IOException {
        RetrofietUserService service
                = RetrofietServiceGenerator.createService(RetrofietUserService.class, token);
        Response<KeycloakUser> response
                = service.findById(currentId).execute();
        if(response.isSuccessful()){
            KeycloakUser obj = response.body();
            System.out.println(obj.toString());
        }else{
            System.out.println(response.errorBody().string());;
        }
    }

//    @Test
//    void update() throws IOException {
//        RetrofietUserService service
//                = RetrofietServiceGenerator.createService(RetrofietUserService.class, token);
//        HashMap<String, Object> atts = new HashMap<>();
//        atts.put("hello", "update here 01");
//        Response<Void> response
////                = service.update(currentId, atts).execute();
//        if(response.isSuccessful()){
//            System.out.println("Okie");
//        }else{
//            System.out.println(response.errorBody().string());;
//        }
//    }

    @Test
    void delete() throws IOException {
        RetrofietUserService service
                = RetrofietServiceGenerator.createService(RetrofietUserService.class, token);
        Response<Void> response
                = service.delete(currentId).execute();
        if(response.isSuccessful()){
            System.out.println("Okie");
        }else{
            System.out.println(response.errorBody().string());;
        }
    }
}