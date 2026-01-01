package com.example.todolist.dto;

import com.example.todolist.entity.Task;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

/**
 * DTO DE RESPONSE PARA TAREAS
 * ===========================
 *
 * Este DTO representa los datos que devolvemos al cliente cuando
 * solicita información sobre una tarea.
 *
 * ¿Por qué no devolver la entidad directamente?
 * ----------------------------------------------
 * 1. Control sobre qué campos exponemos
 * 2. Podemos formatear datos de manera diferente
 * 3. Evitamos problemas de serialización con relaciones JPA
 * 4. Desacoplamos la API de la estructura de la BD
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(
        name = "TaskResponse",
        description = "Representación completa de una tarea"
)
public class TaskResponse {

    @Schema(
            description = "Identificador único de la tarea (UUID v4)",
            example = "550e8400-e29b-41d4-a716-446655440000",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    private UUID id;

    @Schema(
            description = "Título de la tarea",
            example = "Comprar leche"
    )
    private String title;

    @Schema(
            description = "Descripción detallada de la tarea",
            example = "Ir al supermercado y comprar 2 litros de leche desnatada",
            nullable = true
    )
    private String description;

    @Schema(
            description = "Indica si la tarea está completada",
            example = "false"
    )
    private Boolean completed;

    @Schema(
            description = "Fecha y hora de creación de la tarea (formato ISO 8601)",
            example = "2024-01-15T10:30:00Z",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    private Instant createdAt;

    @Schema(
            description = "Fecha y hora de la última actualización (formato ISO 8601)",
            example = "2024-01-15T14:45:30Z",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    private Instant updatedAt;

    /**
     * Método estático para convertir una entidad Task a TaskResponse
     *
     * Este patrón (llamado "factory method" o "mapper") nos permite
     * convertir fácilmente entre entidades y DTOs.
     *
     * @param task La entidad Task a convertir
     * @return Un nuevo TaskResponse con los datos de la entidad
     */
    public static TaskResponse fromEntity(Task task) {
        return TaskResponse.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .completed(task.getCompleted())
                .createdAt(task.getCreatedAt())
                .updatedAt(task.getUpdatedAt())
                .build();
    }
}
