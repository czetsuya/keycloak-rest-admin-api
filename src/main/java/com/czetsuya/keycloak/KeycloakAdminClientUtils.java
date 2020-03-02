package com.czetsuya.keycloak;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.ws.rs.NotFoundException;

import org.apache.commons.lang3.StringUtils;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RoleResource;
import org.keycloak.admin.client.resource.RolesResource;
import org.keycloak.representations.idm.RoleRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Edward P. Legaspi | czetsuya@gmail.com
 * 
 * @version 0.0.1
 * @since 0.0.1
 */
public class KeycloakAdminClientUtils {

    private static Logger log = LoggerFactory.getLogger(KeycloakAdminClientUtils.class);

    /**
     * Loads the keycloak configuration from system property.
     * 
     * @return keycloak configuration
     * @see KeycloakAdminClientConfig
     */
    public static KeycloakAdminClientConfig loadConfig(KeycloakPropertyReader keycloakPropertyReader) {

        KeycloakAdminClientConfig.KeycloakAdminClientConfigBuilder builder = KeycloakAdminClientConfig.builder();

        try {
            String keycloakServer = System.getProperty("keycloak.url");
            if (!StringUtils.isBlank(keycloakServer)) {
                builder = builder.serverUrl(keycloakServer);

            } else {
                builder = builder.serverUrl(keycloakPropertyReader.getProperty("keycloak.auth-server-url"));
            }

            String realm = System.getProperty("keycloak.realm");
            if (!StringUtils.isBlank(realm)) {
                builder = builder.realm(realm);

            } else {
                builder = builder.realm(keycloakPropertyReader.getProperty("keycloak.realm"));
            }

            String clientId = System.getProperty("keycloak.clientId");
            if (!StringUtils.isBlank(clientId)) {
                builder = builder.clientId(clientId);

            } else {
                builder = builder.clientId(keycloakPropertyReader.getProperty("keycloak.resource"));
            }

            String clientSecret = System.getProperty("keycloak.secret");
            if (!StringUtils.isBlank(clientSecret)) {
                builder = builder.clientSecret(clientSecret);

            } else {
                builder = builder.clientSecret(keycloakPropertyReader.getProperty("keycloak.secret"));
            }

        } catch (Exception e) {
            log.error("Error: Loading keycloak admin configuration => {}", e.getMessage());
        }

        KeycloakAdminClientConfig config = builder.build();
        log.debug("Found keycloak configuration: {}", config);

        return config;
    }

    /**
     * It builds a {@link Keycloak} client from a given configuration. This client
     * is used to communicate with the Keycloak instance via REST API.
     * 
     * @param session the security context
     * @param config  keycloak configuration
     * @return Keycloak instance
     * @see Keycloak
     * @see KeycloakAdminClientConfig
     */
    public static Keycloak getKeycloakClient(KeycloakSecurityContext session, KeycloakAdminClientConfig config) {

        return KeycloakBuilder.builder() //
                .serverUrl(config.getServerUrl()) //
                .realm(config.getRealm()) //
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS) //
                .clientId(config.getClientId()) //
                .clientSecret(config.getClientSecret()) //
                .authorization(session.getTokenString()) //
                .build();
    }

    /**
     * Adds a role to a composite role. A composite role is just a role that
     * contains sub roles.
     * 
     * @param keycloak                  keycloak instance
     * @param keycloakAdminClientConfig keycloak configuration
     * @param client                    client id
     * @param role                      role to be added
     * @param compositeRole             where the role will be added
     */
    public static void addRoleToListOf(Keycloak keycloak, KeycloakAdminClientConfig keycloakAdminClientConfig, String client, String role, String compositeRole) {

        final String clientUuid = keycloak.realm(keycloakAdminClientConfig.getRealm()).clients().findByClientId(client).get(0).getId();

        RolesResource rolesResource = keycloak.realm(keycloakAdminClientConfig.getRealm()).clients().get(clientUuid).roles();

        final List<RoleRepresentation> existingRoles = rolesResource.list();

        final boolean roleExists = existingRoles.stream().anyMatch(r -> r.getName().equals(role));

        if (!roleExists) {
            RoleRepresentation roleRepresentation = new RoleRepresentation();
            roleRepresentation.setName(role);
            roleRepresentation.setClientRole(true);
            roleRepresentation.setComposite(false);

            rolesResource.create(roleRepresentation);
        }

        final boolean compositeExists = existingRoles.stream().anyMatch(r -> r.getName().equals(compositeRole));

        if (!compositeExists) {
            RoleRepresentation compositeRoleRepresentation = new RoleRepresentation();
            compositeRoleRepresentation.setName(compositeRole);
            compositeRoleRepresentation.setClientRole(true);
            compositeRoleRepresentation.setComposite(true);

            rolesResource.create(compositeRoleRepresentation);
        }

        final RoleResource compositeRoleResource = rolesResource.get(compositeRole);

        final boolean alreadyAdded = compositeRoleResource.getRoleComposites().stream().anyMatch(r -> r.getName().equals(role));

        if (!alreadyAdded) {
            final RoleRepresentation roleToAdd = rolesResource.get(role).toRepresentation();
            compositeRoleResource.addComposites(Collections.singletonList(roleToAdd));
        }
    }

    /**
     * Removes a given role from a composite role.
     * 
     * @param keycloak                  keycloak instance
     * @param keycloakAdminClientConfig keycloak configuration
     * @param role                      role to be deleted
     * @param compositeRole             where the role should be deleted
     */
    public static void removeRoleInListOf(Keycloak keycloak, KeycloakAdminClientConfig keycloakAdminClientConfig, String role, String compositeRole) {

        final String clientUuid = keycloak.realm(keycloakAdminClientConfig.getRealm()).clients().findByClientId(keycloakAdminClientConfig.getClientId()).get(0).getId();

        final RolesResource rolesResource = keycloak.realm(keycloakAdminClientConfig.getRealm()).clients().get(clientUuid).roles();

        final RoleResource compositeRoleResource = rolesResource.get(compositeRole);

        try {
            final RoleRepresentation roleToDelete = rolesResource.get(role).toRepresentation();
            compositeRoleResource.getRoleComposites().remove(roleToDelete);

        } catch (NotFoundException e) {
            log.warn("Role {} does not exists!", role);
        }
    }

    /**
     * Removes a role from a given list of roles.
     * 
     * @param listOfRoleRepresentation list of roles
     * @param roleToBeRemove           role to be remove from the list
     * @return
     */
    public static List<RoleRepresentation> removeRoleInList(List<RoleRepresentation> listOfRoleRepresentation, RoleRepresentation roleToBeRemove) {

        listOfRoleRepresentation.remove(roleToBeRemove);

        List<RoleRepresentation> updatedListRoleRepresentation = new ArrayList<>();
        for (RoleRepresentation roleRepresentationItem : listOfRoleRepresentation) {
            if (!roleToBeRemove.getName().equalsIgnoreCase(roleRepresentationItem.getName())) {
                updatedListRoleRepresentation.add(roleRepresentationItem);
            }
        }

        return updatedListRoleRepresentation;
    }
}
