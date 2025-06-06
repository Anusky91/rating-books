# 📚✨ BookStar
**Aplicación de calificación de libros**

## 📦 Módulos

1. Users - creación, actualización y busqueda de usuarios
2. Books - creación, modificación y busqueda de libros por id, titulo, autor...
3. Rating - Cración y modificación de las puntuaciónes de cada usuario a un libro.
4. Favorites - Añadir y borrar libros favoritos del usuario.

## 🔐 Seguridad
Usa Auth Basic - Necesitas crear un usuario, activarlo y mandar alias y contraseña en al llamada.
Tras la creación del usuario se manda un email con el enlace para activar la cuenta con un token valido solo durante 24h.
Para ello se ha creado una tabla llamada ``activation_token``

## CQRS
Modulo de CQRS para separar responsabilidades.

## 🔍 Auditoria
Implantada a través de eventos de Spring y se persiste en la tabla ``audit_log``

## 🧪 Testing
**Para ejecutar los test hay que arrancar primero el contenedor de MailHog**
1. Test de integración de todos los controladores - JaCoCo > 0.80
2. Test de carga con K6 🗻 [Documentación](test-k6/k6-docs.md) 
3. Pi-Test configurado, >70% mutaciones matadas, comando para ejecutar los pitest  ``mvn org.pitest:pitest-maven:mutationCoverage``

## Observabilidad
Prometheus - URL [UI Web](http://localhost:9090/query)  
Grafana - URL [UI Web](http://localhost:3000/)
 - Login - ``admin`` ,  ``Liqui2022*-``     

Loki (logs) - A través de Grafana

## Swagger
Con la implementación de la librería **org.springdoc**
````
    <dependency>
        <groupId>org.springdoc</groupId>
        <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
        <version>2.8.6</version>
    </dependency>
````
Podemos acceder a la documentación de la api en [API Doc](http://localhost:8080/bookstar/swagger-ui/index.html)

## 🎨 Logo
![Logo de BookStar](src/main/resources/static/logo.png)

## 🚀 Cosas extra que puedes meter más adelante:
* Validación con Bean Validation (@NotNull, @Size, etc.).
* Paginación y ordenación.
* Circuit breakers y resiliencia (Resilience4j).
* Tracing (Zipkin) o monitoreo (Prometheus + Grafana).
* Comunicación entre servicios con Feign o Kafka si te animas.