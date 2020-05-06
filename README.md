[![patreon](https://c5.patreon.com/external/logo/become_a_patron_button.png)](https://www.patreon.com/bePatron?u=12280211)
[![License: GPL v3](https://img.shields.io/badge/License-GPLv3-blue.svg)](https://www.gnu.org/licenses/gpl-3.0)

# Keycloak Utilities

This project demonstrates how a third-party application can communicate and manage Keycloak resources via API.

## Instructions

1.) Using Spring STS IDE, create a new project using the Spring Starter Project template.

2.) Make sure to add the following dependencies: keycloak-spring-boot-starter and spring-boot-starter-security.

3.) We need to extend the class KeycloakWebSecurityConfigurerAdapter as specified in the keycloak securing apps documentation below. See the code below.

4.) Due to some Keycloak issues, we need to extend the class KeycloakSpringBootConfigResolver.

5.) I created a utility class that will help us initialize a Keycloak class that we can use to communicate and manage a Keycloak instance - KeycloakAdminClientUtils.

6.) We then need a service to get or manage information from Keycloak depending on the user's role. For instance, the user I'm using has a manage-realm role, this means that I can call almost all the API provided by Keycloak. In my example, I'm returning the user's role as well as its profile. See class KeycloakAdminClientService.

7.) I create a REST controller class to use the service in #6 for demo. See KeycloakController.

8.) And finally, don't forget to specify the Keycloak configuration in application.properties.

## To Run

1.) Create a new realm by importing the file config/balambgarden-realm.json.

2.) Create users by selecting Import menu / Select a file and select config/balambgarden-users-0.json. Select on Fail skip.

3.) Install Postman and Import the collection config/keycloak-admin-api.postman_collection.json.

*Make sure that the Login request in postman is pointing to the correct Keycloak login url.

## References
 
 - https://czetsuya-tech.blogspot.com/2020/03/keycloak-admin-rest-api-in-spring-boot.html

## Authors

 * **Edward P. Legaspi** - *Java Architect* - [czetsuya](https://github.com/czetsuya)
