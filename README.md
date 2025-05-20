# 📚✨ BookStar
**Aplicación de calificación de libros**

## Módulos

1. Users - creación, actualización y busqueda de usuarios
2. Books - creación, modificación y busqueda de libros por id, titulo, autor...
3. Rating - Cración y modificación de las puntuaciónes de cada usuario a un libro.

## 🔐 Seguridad
Usa Auth Basic - Necesitas crear un usuario, activarlo y mandar alias y contraseña en al llamada.
Tras la creación del usuario se manda un email con el enlace para activar la cuenta con un token valido solo durante 24h.
Para ello se ha creado una tabla llamada ``activation_token``

## 🔍 Auditoria
Implantada a través de eventos de Spring y se persiste en la tabla ``audit_log``

## 🧪 Testing
1. Test de integración de todos los controladores - JaCoCo > 0.80
2. Test de carga con K6 🗻


## 🎨 Logo
![Logo de BookStar](src/main/resources/static/logo.png)

## 🚀 Cosas extra que puedes meter más adelante:
* Validación con Bean Validation (@NotNull, @Size, etc.).
* Paginación y ordenación.
* Circuit breakers y resiliencia (Resilience4j).
* Tracing (Zipkin) o monitoreo (Prometheus + Grafana).
* Comunicación entre servicios con Feign o Kafka si te animas.