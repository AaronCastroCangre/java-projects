package com.example.todolist.repository;

import com.example.todolist.entity.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * REPOSITORIO DE TAREAS
 * =====================
 *
 * ¿Qué es un Repository?
 * ----------------------
 * Un Repository es una interfaz que nos permite acceder a la base de datos
 * sin escribir código SQL manualmente. Spring Data JPA genera la implementación
 * automáticamente.
 *
 * JpaRepository<Task, UUID>:
 * - Task: La entidad que maneja este repositorio
 * - UUID: El tipo del ID de la entidad
 *
 * Métodos que obtenemos GRATIS (sin escribir código):
 * ---------------------------------------------------
 * - save(Task task)         -> Guardar o actualizar una tarea
 * - findById(UUID id)       -> Buscar por ID (devuelve Optional<Task>)
 * - findAll()               -> Obtener todas las tareas
 * - findAll(Pageable)       -> Obtener tareas con paginación
 * - delete(Task task)       -> Eliminar una tarea
 * - deleteById(UUID id)     -> Eliminar por ID
 * - existsById(UUID id)     -> Verificar si existe
 * - count()                 -> Contar total de tareas
 *
 * Métodos personalizados:
 * -----------------------
 * Podemos agregar métodos propios usando:
 * 1. Nombres de método que Spring Data entiende (findByTitle, findByCompleted, etc.)
 * 2. Anotación @Query para SQL/JPQL personalizado
 *
 * @Repository es opcional aquí porque Spring Data JPA la detecta automáticamente,
 * pero la incluimos para ser explícitos.
 */
@Repository
public interface TaskRepository extends JpaRepository<Task, UUID> {

    /**
     * Buscar tareas por estado de completado (con paginación)
     *
     * Spring Data JPA genera automáticamente la consulta SQL basándose
     * en el nombre del método:
     * "findByCompleted" -> "SELECT * FROM tasks WHERE completed = ?"
     *
     * @param completed Estado a buscar (true o false)
     * @param pageable  Información de paginación (página, tamaño, ordenamiento)
     * @return Página de tareas que coinciden con el estado
     */
    Page<Task> findByCompleted(Boolean completed, Pageable pageable);

    /**
     * Buscar tareas por texto en título o descripción (con paginación)
     *
     * Esta consulta usa JPQL (Java Persistence Query Language), que es
     * similar a SQL pero trabaja con entidades Java en lugar de tablas.
     *
     * LOWER() convierte a minúsculas para búsqueda case-insensitive.
     * LIKE con % permite búsqueda parcial (contiene el texto).
     *
     * @param searchTerm Texto a buscar (se buscará en minúsculas)
     * @param pageable   Información de paginación
     * @return Página de tareas que contienen el texto
     */
    @Query("SELECT t FROM Task t WHERE " +
           "LOWER(t.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(t.description) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<Task> searchByTitleOrDescription(
            @Param("searchTerm") String searchTerm,
            Pageable pageable
    );

    /**
     * Buscar por texto Y estado de completado (combinado)
     *
     * Permite filtrar por ambos criterios a la vez:
     * - Buscar "comprar" en tareas completadas
     * - Buscar "urgente" en tareas pendientes
     *
     * @param searchTerm Texto a buscar
     * @param completed  Estado de completado
     * @param pageable   Información de paginación
     * @return Página de tareas que coinciden con ambos criterios
     */
    @Query("SELECT t FROM Task t WHERE " +
           "(LOWER(t.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(t.description) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) " +
           "AND t.completed = :completed")
    Page<Task> searchByTitleOrDescriptionAndCompleted(
            @Param("searchTerm") String searchTerm,
            @Param("completed") Boolean completed,
            Pageable pageable
    );

    /**
     * Contar tareas por estado
     *
     * Útil para mostrar estadísticas como:
     * - "Tienes 5 tareas pendientes"
     * - "Has completado 10 tareas"
     *
     * @param completed Estado a contar
     * @return Número de tareas con ese estado
     */
    long countByCompleted(Boolean completed);
}
