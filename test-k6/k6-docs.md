## Como ejecutar los test de carga - K6
### 1. Arrancar la aplicación con profine k6
````shell
  mvn spring-boot:run "-Dspring-boot.run.profiles=k6"
````
### 2. Ejecutar el scripts de k6
En la carpeta ``test-k6`` hay una serie de script con diferentes test de carga.
Para ejecutarlo se hace con el siguiente comando

````shell
    k6 run test-k6/testCreateUser.js
````

En el caso de testear algun endpoint que necesite de un usuario autenticado habia que crear y activar primero el usuario via Postman.

#### Ejemplo create user
``localhost:8086/bookstar/public/users``
````json
{
  "firstName" : "Prueba",
  "lastName" : "Activación",
  "alias" : "activeUser",
  "email" : "activeUser@hotmail.com",
  "password" : "Prueba1233*!",
  "phoneNumber" : "612345678",
  "country" : "ES",
  "birthDate" : "1901-01-01",
  "role" : "ADMIN",
  "avatarUrl" : ""
}
````

#### Ejemplo active user
**El token se puede recuperar del log de la aplicación y sustituirlo en la llamada**
``localhost:8086/bookstar/auth/activate?token=558b05c3-9176-4955-8ed1-d8f464a7718e``