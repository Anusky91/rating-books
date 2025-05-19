# 📚✨ BookStar
**Aplicación de calificación de libros**

## Módulos

1. Users - creación, actualización y busqueda de usuarios
2. Books - creación, modificación y busqueda de libros por id, titulo, autor...
3. Rating - Cración y modificación de las puntuaciónes de cada usuario a un libro.

## 🔐 Seguridad
Usa Auth Basic - Necesitas crear un usuario, activarlo y mandar alias y contraseña en al llamada.

## 🔍 Auditoria
Implantada a través de eventos de Spring y se persiste en la tabla ``audit_log``

## 🎨 Logo
![Logo de BookStar](src/main/resources/static/logo.png)

## 🚀 Cosas extra que puedes meter más adelante:
* Validación con Bean Validation (@NotNull, @Size, etc.).
* Paginación y ordenación.
* Circuit breakers y resiliencia (Resilience4j).
* Tracing (Zipkin) o monitoreo (Prometheus + Grafana).
* Comunicación entre servicios con Feign o Kafka si te animas.