package com.example.authenticationservice.retrofiet;

import com.example.authenticationservice.credential.KeyCloakClient;
import com.example.authenticationservice.credential.KeycloakAccessToken;
import com.example.authenticationservice.credential.KeycloakRole;
import com.example.authenticationservice.dto.request.UserUpdateDTO;
import com.example.authenticationservice.user.KeycloakUser;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;
import java.util.Map;

public interface RetrofietUserService {

    @FormUrlEncoded
    @POST("/auth/realms/master/protocol/openid-connect/token")
    Call<KeycloakAccessToken> login(@FieldMap Map<String,String> params);

    @FormUrlEncoded
    @POST("/auth/realms/master/protocol/openid-connect/logout")
    Call<Void> logout(@FieldMap Map<String, String> params);

    @POST("/auth/admin/realms/master/users")
    Call<Void> save(@Body KeycloakUser keycloakUser);

    @GET("auth/admin/realms/master/users")
    Call<List<KeycloakUser>> findUserByNameEct(@Query("username") String username, @Query("exact") String extract);

    @GET("/auth/admin/realms/master/users")
    Call<List<KeycloakUser>> findAll();

    @GET("/auth/admin/realms/master/users/{id}")
    Call<KeycloakUser> findById(@Path("id") String id);

    @PUT("/auth/admin/realms/master/users/{id}")
    Call<Void> update(@Path("id") String id, @Body UserUpdateDTO updateDTO);

    @DELETE("/auth/admin/realms/master/users/{id}")
    Call<Void> delete(@Path("id") String id);

    @POST("/auth/admin/realms/master/users/{userId}/role-mappings/clients/{clientId}")
    Call<Void> addClientRoleToUser(@Path("userId") String userId, @Path("clientId") String clientId, @Body List<KeycloakRole> keycloakRoles);

    @GET("/auth/admin/realms/master/clients")
    Call<List<KeyCloakClient>> findClientByClientID(@Query("clientId") String clientId);

    @GET("/auth/admin/realms/master/clients/{clientId}/roles/{roleName}")
    Call<KeycloakRole> findClientRoleByRoleName(@Path("clientId") String clientId, @Path("roleName") String roleName);

    @GET("auth/admin/realms/master/roles/{roleName}")
    Call<KeycloakRole> findRealmRoleByName(@Path("roleName") String roleName);

    @POST("/auth/admin/realms/master/users/{userId}/role-mappings/realm")
    Call<KeycloakRole> addRealmRoleToUser(@Path("userId") String userId);



}
