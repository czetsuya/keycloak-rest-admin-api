[![License: GPL v3](https://img.shields.io/badge/License-GPLv3-blue.svg)](https://www.gnu.org/licenses/gpl-3.0)

**Help me build more open-source projects by becoming my sponsor.*

# Keycloak REST Admin API Demonstration

This project demonstrates how a third-party application can communicate and manage Keycloak resources via API.

## Instructions

1. Using Spring STS IDE, create a new project using the Spring Starter Project template.
2. Make sure to add the following dependencies: keycloak-spring-boot-starter and spring-boot-starter-security.
3. We need to extend the class KeycloakWebSecurityConfigurerAdapter as specified in the keycloak securing apps documentation below. See the code below.
4. Due to some Keycloak issues, we need to extend the class KeycloakSpringBootConfigResolver.
5. I created a utility class that will help us initialize a Keycloak class that we can use to communicate and manage a Keycloak instance - KeycloakAdminClientUtils.
6. We then need a service to get or manage information from Keycloak depending on the user's role. For instance, the user I'm using has a manage-realm role, this means that I can call almost all the API provided by Keycloak. In my example, I'm returning the user's role as well as its profile. See class KeycloakAdminClientService.
7. I create a REST controller class to use the service in #6 for demo. See KeycloakController.
8. Don't forget to specify the Keycloak configuration in application.properties.

## Run as Standalone

1. Download and run keycloak.
2. Create a new realm and users by importing the file config/balambgarden-realm.json.
3. If you are using an IDE make sure to set the environment variable keycloak.secret=332e78cb-0487-46a8-949d-7c2a09cd380c. This is use when calling the getProfile API.
4. Now we're ready to run the tests inside the postman collection.

## Run as a Dockerized Container

You must have docker installed on your local machine.

1. Make sure to change the value of keycloak.auth-server-url in application.properties to where you will be running docker-compose.
2. Build the application. Whether by using mvn in command line or in your IDE.
3. Open your terminal.
4. Navigate to the root folder of the project.
5. Enter: "docker-compose up --build" without the " and press enter.
6. Now we're ready to run the tests inside the postman collection.

If will take a while during the first time as it will download Keycloak.

## Run Keycloak as a Atandalone Container

docker run -d --name=keycloak10 -p 8081:8080 -e KEYCLOAK_USER=admin -e KEYCLOAK_PASSWORD=kerri jboss/keycloak:10.0.1

## Testing

1. Install Postman 
2. Import the postman environment settings deployment/czetsuya-course.postman_environment.json 
and make sure to update the value of keycloak_url and api_url to where you deploy keycloak and running this project.
3. Import the collection deployment/keycloak-admin-api.postman_collection.json.
4. Run the Login test first so that the access_token will be initialized.

## References
 
 - https://czetsuya-tech.blogspot.com/2020/03/keycloak-admin-rest-api-in-spring-boot.html
 - https://czetsuya-tech.blogspot.com/2019/10/docker-and-kubernetes.html
 - https://hub.docker.com/r/jboss/keycloak/
 - https://spring.io/guides/gs/spring-boot-docker/
 - https://www.keycloak.org/documentation
 - https://www.keycloak.org/docs-api/10.0/rest-api/index.html

## Authors

 * **Edward P. Legaspi** - *Java Architect* - [czetsuya](https://github.com/czetsuya)
