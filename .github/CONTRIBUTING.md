# Guía de contribución

Gracias por tu interés en contribuir a este proyecto.

## Requisitos generales

- Sigue las buenas prácticas de Java y Spring Boot.
- Escribe código limpio y documentado.
- Añade pruebas unitarias para nuevas funcionalidades.
- Usa `feature branches` para tus cambios.
- Realiza `pull requests` claros y descriptivos.

## Principios de arquitectura

- Este proyecto utiliza Domain-Driven Design (DDD) y arquitectura hexagonal.
- Organiza el código siguiendo los principios de DDD: separa dominios, usa value objects, entidades, agregados, repositorios, etc.
- Mantén la separación entre capas de dominio, aplicación e infraestructura.
- Evita dependencias directas entre el dominio y la infraestructura.

## Estilo de código

- Usa el formateo estándar de Java.
- Nombra las variables y métodos de forma descriptiva.
- No dejes código comentado innecesario.

## Proceso de Pull Request

1. Haz un fork y crea tu rama (`feature/nombre-cambio`).
2. Realiza tus cambios y añade pruebas.
3. Asegúrate de que los tests pasan y la cobertura es al menos del 80%.
4. Envía el pull request y describe tus cambios.

## Normas para asistentes de IA

Estas normas también deben ser seguidas por cualquier asistente de inteligencia artificial que colabore en este repositorio.