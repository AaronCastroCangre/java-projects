package com.example.todolist.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.UUID;

/**
 * ENTIDAD TASK
 * ============
 * Esta clase representa una tarea en nuestra aplicación To-Do List.
 *
 * ¿Qué es una Entidad?
 * --------------------
 * Una entidad es una clase Java que se mapea a una tabla en la base de datos.
 * Cada instancia de esta clase representa una fila en la tabla "tasks".
 * JPA/Hibernate se encarga de convertir entre objetos Java y registros SQL.
 *
 * Anotaciones usadas:
 * -------------------
 * @Entity       - Marca esta clase como una entidad JPA
 * @Table        - Especifica el nombre de la tabla en la base de datos
 * @Id           - Indica cuál es la clave primaria
 * @Column       - Configura cómo se mapea cada campo a una columna
 *
 * Lombok:
 * -------
 * Usamos Lombok para reducir código repetitivo:
 * @Getter/@Setter  - Genera getters y setters automáticamente
 * @NoArgsConstructor - Genera constructor sin argumentos (requerido por JPA)
 * @AllArgsConstructor - Genera constructor con todos los argumentos
 * @Builder         - Permite construir objetos de forma fluida: Task.builder().title("...").build()
 */
@Entity
@Table(name = "tasks")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Task {

    /**
     * ID de la tarea (clave primaria)
     *
     * Usamos UUID en lugar de Long/Integer porque:
     * - Son únicos globalmente
     * - No revelan información sobre la cantidad de registros
     * - Son más seguros en APIs públicas
     *
     * @GeneratedValue con GenerationType.UUID genera el UUID automáticamente
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    /**
     * Título de la tarea
     *
     * - nullable = false: Este campo es obligatorio en la base de datos
     * - length = 120: Máximo 120 caracteres
     *
     * Las validaciones de negocio (mínimo 3 caracteres) se hacen en el DTO,
     * no en la entidad. La entidad solo refleja la estructura de la BD.
     */
    @Column(name = "title", nullable = false, length = 120)
    private String title;

    /**
     * Descripción de la tarea (opcional)
     *
     * - columnDefinition: Especifica el tipo de columna exacto en PostgreSQL
     * - length: Límite de caracteres
     *
     * Al ser opcional, puede ser null.
     */
    @Column(name = "description", length = 2000)
    private String description;

    /**
     * Estado de completado de la tarea
     *
     * - nullable = false: Siempre debe tener un valor
     * - Por defecto es false (definido en la BD y en el builder)
     */
    @Column(name = "completed", nullable = false)
    @Builder.Default
    private Boolean completed = false;

    /**
     * Fecha y hora de creación
     *
     * @CreationTimestamp: Hibernate establece este valor automáticamente
     * al crear el registro. Solo se asigna una vez.
     *
     * - updatable = false: No se puede modificar después de la creación
     */
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    /**
     * Fecha y hora de última actualización
     *
     * @UpdateTimestamp: Hibernate actualiza este valor automáticamente
     * cada vez que se modifica el registro.
     */
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    /**
     * Método de conveniencia para alternar el estado completed
     *
     * Esto encapsula la lógica de cambio de estado.
     * Es mejor que acceder directamente al campo desde fuera.
     */
    public void toggleCompleted() {
        this.completed = !this.completed;
    }

    /**
     * toString personalizado para debugging
     *
     * No usamos @ToString de Lombok para tener control sobre qué campos mostrar
     * y evitar problemas con relaciones lazy en el futuro.
     */
    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", completed=" + completed +
                ", createdAt=" + createdAt +
                '}';
    }

    /**
     * equals y hashCode basados solo en el ID
     *
     * Esto es importante para JPA: dos entidades son iguales si tienen el mismo ID.
     * No usamos @EqualsAndHashCode de Lombok porque necesitamos manejar el caso
     * donde el ID puede ser null (antes de persistir).
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id != null && id.equals(task.id);
    }

    @Override
    public int hashCode() {
        // Usamos un valor constante para entidades transitorias (sin ID)
        // Esto es una práctica recomendada para entidades JPA
        return getClass().hashCode();
    }
}
