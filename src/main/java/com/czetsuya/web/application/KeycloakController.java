package com.czetsuya.web.application;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.czetsuya.keycloak.service.KeycloakAdminClientService;

/**
 * We will be using
 * https://www.keycloak.org/docs-api/9.0/javadocs/org/keycloak/admin/client/resource/RolesResource.html
 * API.
 * 
 * @author Edward P. Legaspi | czetsuya@gmail.com
 * 
 * @version 0.0.1
 * @since 0.0.1
 */
@RestController
@RequestMapping(path = "/keycloak", produces = MediaType.APPLICATION_JSON_VALUE)
public class KeycloakController {

    @Autowired
    private KeycloakAdminClientService keycloakAdminClientService;

    @GetMapping(path = "/hello")
    public String hello() {
        return "Hello World!";
    }

    @GetMapping(path = "/roles")
    public Collection<String> rolesOfCurrentUser() {
        return keycloakAdminClientService.getCurrentUserRoles();
    }

    @GetMapping(path = "/profile")
    public Object profileOfCurrentUser() {
        return keycloakAdminClientService.getUserProfileOfLoggedUser();
    }
}
