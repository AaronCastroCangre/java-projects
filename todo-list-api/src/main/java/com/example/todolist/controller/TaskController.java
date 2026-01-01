package com.example.todolist.controller;

import com.example.todolist.dto.ApiResponse;
import com.example.todolist.dto.PageResponse;
import com.example.todolist.dto.TaskRequest;
import com.example.todolist.dto.TaskResponse;
import com.example.todolist.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * CONTROLADOR REST DE TAREAS
 * ==========================
 *
 * Este controlador expone todos los endpoints para gestionar tareas.
 * Todas las respuestas usan el formato estándar ApiResponse:
 * {
 *   "success": true | false,
 *   "message": "mensaje",
 *   "data": { ... }
 * }
 */
@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Tareas", description = "API para gestión de tareas del To-Do List")
public class TaskController {

    private final TaskService taskService;

    /**
     * CREAR TAREA
     */
    @Operation(
            summary = "Crear una nueva tarea",
            description = """
                    Crea una nueva tarea en el sistema.

                    **Reglas de validación:**
                    - El título es obligatorio (3-120 caracteres)
                    - La descripción es opcional (máximo 2000 caracteres)
                    - Las tareas nuevas siempre inician con `completed: false`
                    """
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "201",
                    description = "Tarea creada exitosamente",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "success": true,
                                              "message": "Tarea creada exitosamente",
                                              "data": {
                                                "id": "550e8400-e29b-41d4-a716-446655440000",
                                                "title": "Comprar leche",
                                                "description": "Ir al supermercado",
                                                "completed": false,
                                                "createdAt": "2024-01-15T10:30:00Z",
                                                "updatedAt": "2024-01-15T10:30:00Z"
                                              },
                                              "timestamp": "2024-01-15T10:30:00Z"
                                            }
                                            """
                            )
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Error de validación",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "success": false,
                                              "message": "Error de validación",
                                              "errors": [
                                                "title: El título es obligatorio"
                                              ],
                                              "timestamp": "2024-01-15T10:30:00Z"
                                            }
                                            """
                            )
                    )
            )
    })
    @PostMapping
    public ResponseEntity<ApiResponse<TaskResponse>> createTask(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos de la nueva tarea",
                    required = true,
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = TaskRequest.class)
                    )
            )
            @Valid @RequestBody TaskRequest request
    ) {
        log.info("POST /api/v1/tasks - Creando nueva tarea");
        TaskResponse createdTask = taskService.createTask(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Tarea creada exitosamente", createdTask));
    }

    /**
     * LISTAR TAREAS
     */
    @Operation(
            summary = "Listar todas las tareas",
            description = """
                    Obtiene una lista paginada de tareas con filtros opcionales.

                    **Filtros disponibles:**
                    - `completed`: Filtrar por estado (true = completadas, false = pendientes)
                    - `q`: Buscar texto en título y descripción (case-insensitive)

                    **Paginación:**
                    - `page`: Número de página (comienza en 0)
                    - `size`: Cantidad de elementos por página (máximo 100)
                    """
    )
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<TaskResponse>>> getAllTasks(
            @Parameter(description = "Filtrar por estado de completado", example = "false")
            @RequestParam(required = false) Boolean completed,

            @Parameter(description = "Buscar texto en título y descripción", example = "comprar")
            @RequestParam(required = false) String q,

            @Parameter(description = "Número de página (comienza en 0)", example = "0")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Cantidad de elementos por página (máximo 100)", example = "10")
            @RequestParam(defaultValue = "10") int size
    ) {
        log.info("GET /api/v1/tasks - completed={}, q='{}', page={}, size={}", completed, q, page, size);

        if (size > 100) size = 100;
        if (size < 1) size = 10;
        if (page < 0) page = 0;

        PageResponse<TaskResponse> tasks = taskService.getAllTasks(completed, q, page, size);
        return ResponseEntity.ok(ApiResponse.success("Tareas obtenidas exitosamente", tasks));
    }

    /**
     * OBTENER TAREA POR ID
     */
    @Operation(
            summary = "Obtener una tarea por ID",
            description = "Obtiene los detalles completos de una tarea específica usando su UUID."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Tarea encontrada"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Tarea no encontrada",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "success": false,
                                              "message": "No se encontró la tarea con ID: 550e8400-e29b-41d4-a716-446655440000",
                                              "timestamp": "2024-01-15T10:30:00Z"
                                            }
                                            """
                            )
                    )
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TaskResponse>> getTaskById(
            @Parameter(description = "UUID de la tarea", required = true, example = "550e8400-e29b-41d4-a716-446655440000")
            @PathVariable UUID id
    ) {
        log.info("GET /api/v1/tasks/{} - Obteniendo tarea", id);
        TaskResponse task = taskService.getTaskById(id);
        return ResponseEntity.ok(ApiResponse.success("Tarea encontrada", task));
    }

    /**
     * ACTUALIZAR TAREA
     */
    @Operation(
            summary = "Actualizar una tarea",
            description = """
                    Actualiza todos los campos de una tarea existente.

                    **Campos actualizables:**
                    - `title`: Nuevo título (obligatorio, 3-120 caracteres)
                    - `description`: Nueva descripción (opcional)
                    - `completed`: Nuevo estado de completado (opcional)
                    """
    )
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TaskResponse>> updateTask(
            @Parameter(description = "UUID de la tarea a actualizar", required = true)
            @PathVariable UUID id,

            @Valid @RequestBody TaskRequest request
    ) {
        log.info("PUT /api/v1/tasks/{} - Actualizando tarea", id);
        TaskResponse updatedTask = taskService.updateTask(id, request);
        return ResponseEntity.ok(ApiResponse.success("Tarea actualizada exitosamente", updatedTask));
    }

    /**
     * ALTERNAR ESTADO DE COMPLETADO
     */
    @Operation(
            summary = "Alternar estado de completado",
            description = """
                    Alterna el estado `completed` de una tarea:
                    - Si está **completada** (true) → pasa a **pendiente** (false)
                    - Si está **pendiente** (false) → pasa a **completada** (true)
                    """
    )
    @PatchMapping("/{id}/toggle")
    public ResponseEntity<ApiResponse<TaskResponse>> toggleTaskCompleted(
            @Parameter(description = "UUID de la tarea", required = true)
            @PathVariable UUID id
    ) {
        log.info("PATCH /api/v1/tasks/{}/toggle - Alternando estado", id);
        TaskResponse updatedTask = taskService.toggleTaskCompleted(id);
        String message = updatedTask.getCompleted()
                ? "Tarea marcada como completada"
                : "Tarea marcada como pendiente";
        return ResponseEntity.ok(ApiResponse.success(message, updatedTask));
    }

    /**
     * ELIMINAR TAREA
     */
    @Operation(
            summary = "Eliminar una tarea",
            description = "Elimina una tarea de forma **permanente**."
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteTask(
            @Parameter(description = "UUID de la tarea a eliminar", required = true)
            @PathVariable UUID id
    ) {
        log.info("DELETE /api/v1/tasks/{} - Eliminando tarea", id);
        taskService.deleteTask(id);
        return ResponseEntity.ok(ApiResponse.success("Tarea eliminada exitosamente"));
    }
}
