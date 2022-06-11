package com.example.authenticationservice.util;

public class KeycloakConstant {
    public static String KEYCLOAK_BASEURL = "http://localhost:8080";
    public static String KEYCLOAK_REALM = "master";
    public static String KEYCLOAK_CLIENT_ID = "test";
    public static String KEYCLOAK_ADMIN_USERNAME = "admin";
    public static String KEYCLOAK_ADMIN_PASSWORD = "hiep28802";
    public static String KEYCLOAK_USERS_BASEURL = String.format("%s/auth/admin/realms/%s/users", KEYCLOAK_BASEURL, KEYCLOAK_REALM);
    public static String KEYCLOAK_TOKEN_BASEURL = String.format("%s/auth/realms/%s/protocol/openid-connect/token", KEYCLOAK_BASEURL, KEYCLOAK_REALM);
    public static String KEYCLOAK_AUTHENTICATION_HEADER = "Authorization";
    public static String KEYCLOAK_AUTHENTICATION_BEARER = "Bearer";
    public static String KEYCLOAK_CREDENTIAL_GRANT_TYPE = "password";
}
