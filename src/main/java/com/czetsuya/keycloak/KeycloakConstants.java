package com.czetsuya.keycloak;

import com.google.common.collect.ImmutableList;

/**
 * @author Edward P. Legaspi | czetsuya@gmail.com
 * 
 * @version 0.0.1
 * @since 0.0.1
 */
public class KeycloakConstants {
    
    private KeycloakConstants() {
        
    }

    public static final String ROLE_API_ACCESS = "apiAccess";
    public static final String ROLE_GUI_ACCESS = "guiAccess";
    public static final String ROLE_ADMINISTRATOR = "administrator";
    public static final String ROLE_USER_MANAGEMENT = "userManagement";
    public static final ImmutableList<String> ROLE_KEYCLOAK_DEFAULT_EXCLUDED = ImmutableList.of("uma_authorization", "offline_access");
}
