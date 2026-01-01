package com.example.todolist;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * CLASE PRINCIPAL DE LA APLICACIÓN
 * =================================
 *
 * Esta es la clase de entrada de nuestra aplicación Spring Boot.
 * Aquí es donde todo comienza cuando ejecutas el proyecto.
 *
 * @SpringBootApplication es una anotación "mágica" que combina tres anotaciones:
 *
 * 1. @Configuration
 *    - Indica que esta clase puede contener definiciones de beans (@Bean)
 *    - Permite configurar la aplicación mediante código Java
 *
 * 2. @EnableAutoConfiguration
 *    - Le dice a Spring Boot que configure automáticamente la aplicación
 *    - Basándose en las dependencias del classpath, Spring configura:
 *      - Tomcat (servidor web)
 *      - JPA/Hibernate (acceso a base de datos)
 *      - Jackson (serialización JSON)
 *      - Y mucho más...
 *
 * 3. @ComponentScan
 *    - Escanea automáticamente el paquete actual y sus subpaquetes
 *    - Encuentra y registra todos los componentes:
 *      - @Controller / @RestController
 *      - @Service
 *      - @Repository
 *      - @Component
 *
 * CÓMO EJECUTAR LA APLICACIÓN:
 * ----------------------------
 *
 * Opción 1: Desde la terminal (con Maven)
 *   cd todo-list-api
 *   ./mvnw spring-boot:run
 *
 * Opción 2: Desde el IDE
 *   Ejecuta esta clase como una aplicación Java normal
 *   (clic derecho -> Run 'TodoListApplication')
 *
 * Opción 3: Como JAR empaquetado
 *   ./mvnw clean package
 *   java -jar target/todo-list-api-1.0.0-SNAPSHOT.jar
 *
 * Una vez iniciada, la API estará disponible en:
 *   http://localhost:8080/api/v1/tasks
 *
 * REQUISITOS PREVIOS:
 * -------------------
 * 1. Java 17 o superior instalado
 * 2. PostgreSQL ejecutándose en localhost:5432
 * 3. Base de datos 'todolist_db' creada
 *
 * CREAR LA BASE DE DATOS:
 * -----------------------
 *   psql -U postgres
 *   CREATE DATABASE todolist_db;
 *   \q
 */
@SpringBootApplication
public class TodoListApplication {

    /**
     * Método main - punto de entrada de la aplicación
     *
     * SpringApplication.run() hace todo el trabajo:
     * 1. Crea el contexto de Spring
     * 2. Escanea y registra todos los beans
     * 3. Configura la aplicación automáticamente
     * 4. Inicia el servidor Tomcat embebido
     * 5. Ejecuta las migraciones de Flyway
     *
     * @param args Argumentos de línea de comandos
     *             Ejemplo: --server.port=9090 para cambiar el puerto
     */
    public static void main(String[] args) {
        SpringApplication.run(TodoListApplication.class, args);
    }
}
