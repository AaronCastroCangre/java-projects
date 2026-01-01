package com.example.todolist.exception;

import java.util.UUID;

/**
 * EXCEPCIÓN: TAREA NO ENCONTRADA
 * ==============================
 *
 * Esta excepción se lanza cuando se intenta acceder a una tarea que no existe.
 * Por ejemplo, si alguien intenta obtener /api/v1/tasks/{id} con un ID inexistente.
 *
 * ¿Por qué crear excepciones personalizadas?
 * ------------------------------------------
 * 1. Son más descriptivas que excepciones genéricas
 * 2. Permiten manejar errores específicos de manera diferente
 * 3. Hacen el código más legible y mantenible
 * 4. Facilitan la documentación de casos de error
 *
 * Extendemos RuntimeException (excepción no chequeada):
 * - No es obligatorio usar try-catch
 * - Se propagará hasta el GlobalExceptionHandler
 * - Es el estándar en aplicaciones Spring modernas
 */
public class TaskNotFoundException extends RuntimeException {

    /**
     * ID de la tarea que no fue encontrada
     * Guardamos el ID para poder incluirlo en el mensaje de error
     */
    private final UUID taskId;

    /**
     * Constructor que recibe el ID de la tarea no encontrada
     *
     * @param taskId El UUID de la tarea que se intentó buscar
     */
    public TaskNotFoundException(UUID taskId) {
        super(String.format("No se encontró la tarea con ID: %s", taskId));
        this.taskId = taskId;
    }

    /**
     * Constructor que recibe un mensaje personalizado
     *
     * @param message Mensaje de error personalizado
     */
    public TaskNotFoundException(String message) {
        super(message);
        this.taskId = null;
    }

    /**
     * Obtiene el ID de la tarea que no fue encontrada
     *
     * @return El UUID de la tarea, o null si se usó el constructor con mensaje
     */
    public UUID getTaskId() {
        return taskId;
    }
}
