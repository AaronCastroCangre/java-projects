package com.example.todolist.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.Instant;
import java.util.List;

/**
 * DTO GENÉRICO PARA RESPUESTAS DE LA API
 * =======================================
 *
 * Este DTO estandariza TODAS las respuestas de la API con el formato:
 * {
 *   "success": true | false,
 *   "message": "mensaje descriptivo",
 *   "data": { ... } o null,
 *   "errors": [...] (solo en caso de errores de validación)
 * }
 *
 * Beneficios de este formato:
 * ---------------------------
 * 1. Consistencia: Todas las respuestas tienen la misma estructura
 * 2. Predecible: El cliente siempre sabe qué esperar
 * 3. Informativo: Siempre hay un mensaje descriptivo
 * 4. Flexible: El campo "data" puede contener cualquier tipo de dato
 *
 * @param <T> El tipo de datos que contiene la respuesta
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(
        name = "ApiResponse",
        description = "Formato estándar de respuesta de la API"
)
public class ApiResponse<T> {

    @Schema(
            description = "Indica si la operación fue exitosa",
            example = "true"
    )
    private boolean success;

    @Schema(
            description = "Mensaje descriptivo del resultado",
            example = "Tarea creada exitosamente"
    )
    private String message;

    @Schema(
            description = "Datos de la respuesta (puede ser un objeto, lista, o null)"
    )
    private T data;

    @Schema(
            description = "Lista de errores (solo presente cuando success=false)",
            example = "[\"El título es obligatorio\", \"La descripción es muy larga\"]"
    )
    private List<String> errors;

    @Schema(
            description = "Marca de tiempo de la respuesta",
            example = "2024-01-15T10:30:00Z"
    )
    private Instant timestamp;

    // =========================================================================
    // MÉTODOS DE FÁBRICA PARA RESPUESTAS EXITOSAS
    // =========================================================================

    /**
     * Respuesta exitosa con datos y mensaje personalizado
     */
    public static <T> ApiResponse<T> success(String message, T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .timestamp(Instant.now())
                .build();
    }

    /**
     * Respuesta exitosa con datos (mensaje genérico)
     */
    public static <T> ApiResponse<T> success(T data) {
        return success("Operación exitosa", data);
    }

    /**
     * Respuesta exitosa sin datos (solo mensaje)
     */
    public static <T> ApiResponse<T> success(String message) {
        return ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .timestamp(Instant.now())
                .build();
    }

    // =========================================================================
    // MÉTODOS DE FÁBRICA PARA RESPUESTAS DE ERROR
    // =========================================================================

    /**
     * Respuesta de error con mensaje
     */
    public static <T> ApiResponse<T> error(String message) {
        return ApiResponse.<T>builder()
                .success(false)
                .message(message)
                .timestamp(Instant.now())
                .build();
    }

    /**
     * Respuesta de error con mensaje y lista de errores detallados
     */
    public static <T> ApiResponse<T> error(String message, List<String> errors) {
        return ApiResponse.<T>builder()
                .success(false)
                .message(message)
                .errors(errors)
                .timestamp(Instant.now())
                .build();
    }

    // =========================================================================
    // CÓDIGOS DE ERROR (para referencia)
    // =========================================================================
    public static final String MSG_CREATED = "Recurso creado exitosamente";
    public static final String MSG_UPDATED = "Recurso actualizado exitosamente";
    public static final String MSG_DELETED = "Recurso eliminado exitosamente";
    public static final String MSG_FOUND = "Recurso encontrado";
    public static final String MSG_NOT_FOUND = "Recurso no encontrado";
    public static final String MSG_VALIDATION_ERROR = "Error de validación";
    public static final String MSG_INTERNAL_ERROR = "Error interno del servidor";
}
