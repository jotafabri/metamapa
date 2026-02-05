# MetaMapa

Sistema de gestión de mapeos colaborativos de código abierto para recopilación, visibilización y almacenamiento descentralizado de información geoespacial.

**Trabajo Práctico de la materia Diseño de Sistemas de Información (UTN 2025)**

## Tecnologías

* Java 17
* Spring Boot
* Thymeleaf
* JUnit 5
* Maven 3.8.1+

## Arquitectura

Sistema descentralizado diseñado para ONGs, universidades y organismos estatales:

* **Fuentes estáticas**: Datasets CSV (≥10,000 registros)
* **Fuentes dinámicas**: Carga colaborativa anónima/registrada
* **Fuentes proxy**: Integración con servicios externos
* **Servicio de agregación**: Combinación multi-fuente
* **Visualización**: Interfaz web + API

## Modelo de Datos

**Hecho**: Título, descripción, categoría, coordenadas, fechas, origen, multimedia opcional.

**Colección**: Agrupación automática bajo criterios configurables.

**Contribuyente**: Anónimo o identificado (nombre, apellido, edad).

**Solicitud de Eliminación**: Fundamento ≥500 caracteres, estados: pendiente/aceptada/rechazada.

## Ejecutar tests
```bash
mvn test
```

## Validar proyecto
```bash
mvn clean verify
```

Ejecuta tests, checkstyle, detección de code smells y validación de cobertura.

## Formato CSV
```
Título,Descripción,Categoría,Latitud,Longitud,Fecha del hecho
```

Detección de duplicados por título. Sin carga completa en memoria.
