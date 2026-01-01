package com.example.todolist.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * CONFIGURACIÓN DE OPENAPI/SWAGGER
 * =================================
 *
 * Esta clase configura la documentación OpenAPI (Swagger) de nuestra API.
 *
 * ¿Qué es OpenAPI/Swagger?
 * ------------------------
 * - OpenAPI: Especificación estándar para describir APIs REST
 * - Swagger: Herramientas que implementan OpenAPI (UI, generadores, etc.)
 *
 * SpringDoc genera automáticamente la documentación basándose en:
 * 1. Los endpoints de nuestros Controllers
 * 2. Las anotaciones que agregamos (@Operation, @Parameter, etc.)
 * 3. Esta configuración
 *
 * URLs disponibles después de iniciar la aplicación:
 * --------------------------------------------------
 * - Swagger UI: http://localhost:8080/swagger-ui.html
 *   Interfaz gráfica para explorar y probar la API
 *
 * - OpenAPI JSON: http://localhost:8080/v3/api-docs
 *   Especificación en formato JSON (útil para herramientas)
 *
 * - OpenAPI YAML: http://localhost:8080/v3/api-docs.yaml
 *   Especificación en formato YAML
 *
 * @Configuration indica que esta clase contiene beans de configuración
 */
@Configuration
public class OpenApiConfig {

    /**
     * Inyectamos el nombre de la aplicación desde application.yml
     *
     * @Value permite inyectar valores de propiedades
     * El formato ${...} busca la propiedad en application.yml
     */
    @Value("${spring.application.name:Todo List API}")
    private String applicationName;

    /**
     * Bean que configura la documentación OpenAPI
     *
     * @Bean indica que este método produce un objeto gestionado por Spring
     * Este bean personaliza la información que aparece en Swagger UI
     *
     * @return Configuración OpenAPI personalizada
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                // Información general de la API
                .info(apiInfo())
                // Servidores donde está disponible la API
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Servidor de desarrollo local")
                ))
                // Tags para agrupar endpoints
                .tags(List.of(
                        new Tag()
                                .name("Tareas")
                                .description("Operaciones para gestionar tareas del To-Do List")
                ));
    }

    /**
     * Configura la información descriptiva de la API
     *
     * Esta información aparece en la parte superior de Swagger UI
     */
    private Info apiInfo() {
        return new Info()
                // Título de la API
                .title("Todo List API")
                // Descripción detallada (soporta Markdown)
                .description("""
                        ## API REST para gestión de tareas

                        Esta API permite crear, leer, actualizar y eliminar tareas de un To-Do List.

                        ### Características principales:
                        - **CRUD completo** de tareas
                        - **Filtros** por estado (completada/pendiente) y búsqueda de texto
                        - **Paginación** de resultados
                        - **Validación** de datos de entrada
                        - **Manejo consistente de errores**

                        ### Modelo de datos:
                        Cada tarea tiene:
                        - `id`: Identificador único (UUID)
                        - `title`: Título (3-120 caracteres)
                        - `description`: Descripción opcional (máx 2000 caracteres)
                        - `completed`: Estado de completado
                        - `createdAt`: Fecha de creación
                        - `updatedAt`: Fecha de última modificación

                        ### Códigos de respuesta:
                        | Código | Descripción |
                        |--------|-------------|
                        | 200 | Operación exitosa |
                        | 201 | Recurso creado |
                        | 204 | Eliminación exitosa |
                        | 400 | Error de validación |
                        | 404 | Recurso no encontrado |
                        | 500 | Error interno |
                        """)
                // Versión de la API
                .version("1.0.0")
                // Información de contacto
                .contact(new Contact()
                        .name("Equipo de Desarrollo")
                        .email("dev@example.com")
                        .url("https://github.com/example/todo-list-api"))
                // Licencia
                .license(new License()
                        .name("MIT License")
                        .url("https://opensource.org/licenses/MIT"));
    }
}
