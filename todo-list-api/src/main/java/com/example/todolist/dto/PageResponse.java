package com.example.todolist.dto;

import lombok.*;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.function.Function;

/**
 * DTO PARA RESPUESTAS PAGINADAS
 * =============================
 *
 * Cuando hay muchos registros, no es eficiente devolverlos todos de una vez.
 * La paginación divide los resultados en "páginas" más pequeñas.
 *
 * Este DTO envuelve los resultados paginados con información útil como:
 * - Los elementos de la página actual
 * - Número de página actual
 * - Tamaño de página
 * - Total de elementos
 * - Si hay más páginas
 *
 * Ejemplo de respuesta paginada:
 * {
 *   "content": [
 *     { "id": "...", "title": "Tarea 1", ... },
 *     { "id": "...", "title": "Tarea 2", ... }
 *   ],
 *   "page": 0,
 *   "size": 10,
 *   "totalElements": 45,
 *   "totalPages": 5,
 *   "first": true,
 *   "last": false
 * }
 *
 * @param <T> El tipo de elementos en la página (ej: TaskResponse)
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PageResponse<T> {

    /**
     * Lista de elementos en la página actual
     */
    private List<T> content;

    /**
     * Número de página actual (empieza en 0)
     */
    private int page;

    /**
     * Cantidad de elementos por página
     */
    private int size;

    /**
     * Número total de elementos en todas las páginas
     */
    private long totalElements;

    /**
     * Número total de páginas
     */
    private int totalPages;

    /**
     * ¿Es la primera página?
     */
    private boolean first;

    /**
     * ¿Es la última página?
     */
    private boolean last;

    /**
     * Convierte un Page de Spring Data a nuestro PageResponse
     *
     * Este método usa un "mapper" para convertir cada elemento.
     * Por ejemplo, convierte Page<Task> a PageResponse<TaskResponse>.
     *
     * Uso:
     * Page<Task> tasksPage = taskRepository.findAll(pageable);
     * PageResponse<TaskResponse> response = PageResponse.fromPage(
     *     tasksPage,
     *     TaskResponse::fromEntity
     * );
     *
     * @param page   El Page de Spring Data
     * @param mapper Función para convertir cada elemento (ej: Task -> TaskResponse)
     * @param <E>    Tipo de la entidad original
     * @param <T>    Tipo del DTO de respuesta
     * @return PageResponse con los datos convertidos
     */
    public static <E, T> PageResponse<T> fromPage(Page<E> page, Function<E, T> mapper) {
        // Convertimos cada elemento usando el mapper
        List<T> content = page.getContent()
                .stream()
                .map(mapper)
                .toList();

        return PageResponse.<T>builder()
                .content(content)
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .first(page.isFirst())
                .last(page.isLast())
                .build();
    }
}
