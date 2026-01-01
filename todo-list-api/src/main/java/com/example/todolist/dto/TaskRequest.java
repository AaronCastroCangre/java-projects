package com.example.todolist.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * DTO DE REQUEST PARA TAREAS
 * ==========================
 *
 * ¿Qué es un DTO?
 * ---------------
 * DTO significa "Data Transfer Object" (Objeto de Transferencia de Datos).
 * Es una clase que usamos para recibir datos del cliente (frontend, Postman, etc.)
 * y enviar datos de vuelta.
 *
 * ¿Por qué separar DTOs de Entidades?
 * ------------------------------------
 * 1. SEGURIDAD: No exponemos campos internos como createdAt o id
 * 2. VALIDACIÓN: Podemos tener validaciones diferentes para crear y actualizar
 * 3. FLEXIBILIDAD: Si cambia la BD, los DTOs pueden quedarse igual
 * 4. CLARIDAD: El cliente solo ve los campos que le interesan
 *
 * Anotaciones Swagger (@Schema):
 * ------------------------------
 * - description: Describe el propósito del campo
 * - example: Muestra un valor de ejemplo en la documentación
 * - requiredMode: Indica si el campo es obligatorio
 * - maxLength/minLength: Límites de longitud para strings
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(
        name = "TaskRequest",
        description = "Datos para crear o actualizar una tarea"
)
public class TaskRequest {

    /**
     * Título de la tarea (obligatorio)
     */
    @Schema(
            description = "Título de la tarea",
            example = "Comprar leche",
            requiredMode = Schema.RequiredMode.REQUIRED,
            minLength = 3,
            maxLength = 120
    )
    @NotBlank(message = "El título es obligatorio")
    @Size(min = 3, max = 120, message = "El título debe tener entre 3 y 120 caracteres")
    private String title;

    /**
     * Descripción de la tarea (opcional)
     */
    @Schema(
            description = "Descripción detallada de la tarea (opcional)",
            example = "Ir al supermercado y comprar 2 litros de leche desnatada",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            maxLength = 2000
    )
    @Size(max = 2000, message = "La descripción no puede exceder 2000 caracteres")
    private String description;

    /**
     * Estado de la tarea (opcional al crear, usado al actualizar)
     */
    @Schema(
            description = "Estado de completado de la tarea. Ignorado al crear (siempre inicia en false). Usado al actualizar.",
            example = "false",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    private Boolean completed;
}
