package com.example.todolist.service;

import com.example.todolist.dto.PageResponse;
import com.example.todolist.dto.TaskRequest;
import com.example.todolist.dto.TaskResponse;
import com.example.todolist.entity.Task;
import com.example.todolist.exception.TaskNotFoundException;
import com.example.todolist.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * SERVICIO DE TAREAS
 * ==================
 *
 * ¿Qué es un Service?
 * -------------------
 * El Service es la capa donde va la LÓGICA DE NEGOCIO de nuestra aplicación.
 * Actúa como intermediario entre el Controller (que recibe peticiones HTTP)
 * y el Repository (que accede a la base de datos).
 *
 * Responsabilidades del Service:
 * ------------------------------
 * 1. Validaciones de negocio (más allá de las básicas de los DTOs)
 * 2. Orquestación de operaciones (combinar varias acciones)
 * 3. Transformación de datos (Entity <-> DTO)
 * 4. Manejo de transacciones
 * 5. Lógica que no pertenece al Controller ni al Repository
 *
 * Anotaciones usadas:
 * -------------------
 * @Service: Marca esta clase como un componente de servicio de Spring
 * @RequiredArgsConstructor: Lombok genera el constructor con los campos final
 * @Slf4j: Lombok genera un logger llamado 'log'
 * @Transactional: Maneja transacciones de base de datos
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class TaskService {

    /**
     * Inyección de dependencias
     *
     * Spring inyecta automáticamente el TaskRepository aquí.
     * Usamos 'final' para que sea inmutable después de la construcción.
     *
     * @RequiredArgsConstructor de Lombok genera:
     * public TaskService(TaskRepository taskRepository) {
     *     this.taskRepository = taskRepository;
     * }
     */
    private final TaskRepository taskRepository;

    /**
     * Crear una nueva tarea
     *
     * @Transactional asegura que si algo falla, se revierte todo.
     * Por ejemplo, si hubiera múltiples operaciones de BD, todas
     * se ejecutarían como una unidad atómica.
     *
     * @param request DTO con los datos de la nueva tarea
     * @return TaskResponse con los datos de la tarea creada (incluye ID y fechas)
     */
    @Transactional
    public TaskResponse createTask(TaskRequest request) {
        log.info("Creando nueva tarea con título: {}", request.getTitle());

        // Construimos la entidad Task desde el DTO
        // El ID, createdAt y updatedAt se generan automáticamente
        Task task = Task.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .completed(false)  // Las tareas nuevas siempre empiezan pendientes
                .build();

        // Guardamos en la base de datos
        Task savedTask = taskRepository.save(task);

        log.info("Tarea creada exitosamente con ID: {}", savedTask.getId());

        // Convertimos la entidad a DTO de respuesta
        return TaskResponse.fromEntity(savedTask);
    }

    /**
     * Obtener una tarea por su ID
     *
     * @Transactional(readOnly = true) optimiza las operaciones de solo lectura
     * (no bloquea la BD para escrituras)
     *
     * @param id UUID de la tarea a buscar
     * @return TaskResponse con los datos de la tarea
     * @throws TaskNotFoundException si la tarea no existe
     */
    @Transactional(readOnly = true)
    public TaskResponse getTaskById(UUID id) {
        log.debug("Buscando tarea con ID: {}", id);

        // findById devuelve Optional<Task>, usamos orElseThrow para manejar el caso vacío
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Tarea no encontrada con ID: {}", id);
                    return new TaskNotFoundException(id);
                });

        return TaskResponse.fromEntity(task);
    }

    /**
     * Listar todas las tareas con filtros opcionales y paginación
     *
     * Este método maneja varios casos:
     * 1. Sin filtros: devuelve todas las tareas
     * 2. Con filtro 'completed': solo tareas completadas/pendientes
     * 3. Con filtro 'q' (búsqueda): busca en título y descripción
     * 4. Con ambos filtros: combina los criterios
     *
     * @param completed Filtro por estado (opcional)
     * @param search    Texto a buscar (opcional)
     * @param page      Número de página (empieza en 0)
     * @param size      Cantidad de elementos por página
     * @return PageResponse con las tareas y metadatos de paginación
     */
    @Transactional(readOnly = true)
    public PageResponse<TaskResponse> getAllTasks(
            Boolean completed,
            String search,
            int page,
            int size
    ) {
        log.debug("Listando tareas - completed: {}, search: '{}', page: {}, size: {}",
                completed, search, page, size);

        // Creamos el objeto Pageable que define la paginación y el orden
        // Ordenamos por fecha de creación descendente (más recientes primero)
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        Page<Task> tasksPage;

        // Determinamos qué consulta usar según los filtros
        boolean hasSearch = search != null && !search.trim().isEmpty();
        boolean hasCompleted = completed != null;

        if (hasSearch && hasCompleted) {
            // Caso 1: Buscar por texto Y estado
            log.debug("Aplicando filtro de búsqueda Y estado");
            tasksPage = taskRepository.searchByTitleOrDescriptionAndCompleted(
                    search.trim(), completed, pageable);
        } else if (hasSearch) {
            // Caso 2: Solo buscar por texto
            log.debug("Aplicando filtro de búsqueda");
            tasksPage = taskRepository.searchByTitleOrDescription(search.trim(), pageable);
        } else if (hasCompleted) {
            // Caso 3: Solo filtrar por estado
            log.debug("Aplicando filtro de estado");
            tasksPage = taskRepository.findByCompleted(completed, pageable);
        } else {
            // Caso 4: Sin filtros, devolver todo
            log.debug("Sin filtros, devolviendo todas las tareas");
            tasksPage = taskRepository.findAll(pageable);
        }

        log.info("Encontradas {} tareas (página {} de {})",
                tasksPage.getTotalElements(), page + 1, tasksPage.getTotalPages());

        // Convertimos Page<Task> a PageResponse<TaskResponse>
        return PageResponse.fromPage(tasksPage, TaskResponse::fromEntity);
    }

    /**
     * Actualizar una tarea existente
     *
     * Actualiza todos los campos editables (title, description, completed)
     * con los valores del request.
     *
     * @param id      UUID de la tarea a actualizar
     * @param request DTO con los nuevos datos
     * @return TaskResponse con los datos actualizados
     * @throws TaskNotFoundException si la tarea no existe
     */
    @Transactional
    public TaskResponse updateTask(UUID id, TaskRequest request) {
        log.info("Actualizando tarea con ID: {}", id);

        // Primero verificamos que la tarea existe
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("No se puede actualizar: tarea no encontrada con ID: {}", id);
                    return new TaskNotFoundException(id);
                });

        // Actualizamos los campos
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());

        // Solo actualizamos 'completed' si viene en el request
        if (request.getCompleted() != null) {
            task.setCompleted(request.getCompleted());
        }

        // El updatedAt se actualiza automáticamente gracias a @UpdateTimestamp
        Task updatedTask = taskRepository.save(task);

        log.info("Tarea actualizada exitosamente: {}", id);

        return TaskResponse.fromEntity(updatedTask);
    }

    /**
     * Alternar el estado de completado de una tarea
     *
     * Si está completada -> pasa a pendiente
     * Si está pendiente -> pasa a completada
     *
     * @param id UUID de la tarea
     * @return TaskResponse con el nuevo estado
     * @throws TaskNotFoundException si la tarea no existe
     */
    @Transactional
    public TaskResponse toggleTaskCompleted(UUID id) {
        log.info("Alternando estado de tarea con ID: {}", id);

        Task task = taskRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("No se puede alternar: tarea no encontrada con ID: {}", id);
                    return new TaskNotFoundException(id);
                });

        // Usamos el método de conveniencia de la entidad
        task.toggleCompleted();

        Task updatedTask = taskRepository.save(task);

        log.info("Tarea {} ahora está: {}",
                id, updatedTask.getCompleted() ? "COMPLETADA" : "PENDIENTE");

        return TaskResponse.fromEntity(updatedTask);
    }

    /**
     * Eliminar una tarea
     *
     * @param id UUID de la tarea a eliminar
     * @throws TaskNotFoundException si la tarea no existe
     */
    @Transactional
    public void deleteTask(UUID id) {
        log.info("Eliminando tarea con ID: {}", id);

        // Verificamos que existe antes de eliminar
        if (!taskRepository.existsById(id)) {
            log.warn("No se puede eliminar: tarea no encontrada con ID: {}", id);
            throw new TaskNotFoundException(id);
        }

        taskRepository.deleteById(id);

        log.info("Tarea eliminada exitosamente: {}", id);
    }
}
