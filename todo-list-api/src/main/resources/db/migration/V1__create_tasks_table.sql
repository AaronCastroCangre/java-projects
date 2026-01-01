-- =============================================================================
-- MIGRACIÓN V1: Crear tabla de tareas (tasks)
-- =============================================================================
-- Esta es una migración de Flyway. El nombre del archivo es importante:
-- V1__create_tasks_table.sql
--   ^- V seguida de un número indica la versión
--      ^^- Doble guión bajo separa la versión de la descripción
--         ^^^^^^^^^^^^^^^^^^^^- Descripción de lo que hace la migración
--
-- Flyway ejecutará las migraciones en orden numérico (V1, V2, V3, etc.)
-- y registrará cuáles ya fueron ejecutadas en la tabla flyway_schema_history.
-- =============================================================================

-- -----------------------------------------------------------------------------
-- EXTENSIÓN UUID
-- -----------------------------------------------------------------------------
-- PostgreSQL necesita esta extensión para generar UUIDs automáticamente.
-- 'IF NOT EXISTS' evita errores si la extensión ya está instalada.
-- -----------------------------------------------------------------------------
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- -----------------------------------------------------------------------------
-- TABLA: tasks
-- -----------------------------------------------------------------------------
-- Esta tabla almacenará todas las tareas de nuestra aplicación To-Do.
-- Cada columna está documentada para facilitar la comprensión.
-- -----------------------------------------------------------------------------
CREATE TABLE tasks (

    -- -------------------------------------------------------------------------
    -- ID: Identificador único de la tarea
    -- -------------------------------------------------------------------------
    -- Usamos UUID en lugar de números secuenciales por varias razones:
    -- 1. Son únicos globalmente (no se repiten aunque tengas múltiples servidores)
    -- 2. No revelan información sobre cuántos registros existen
    -- 3. Son más difíciles de adivinar (mejora la seguridad)
    --
    -- uuid_generate_v4() genera un UUID aleatorio automáticamente
    -- -------------------------------------------------------------------------
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),

    -- -------------------------------------------------------------------------
    -- TITLE: Título de la tarea
    -- -------------------------------------------------------------------------
    -- NOT NULL: Este campo es obligatorio
    -- VARCHAR(120): Máximo 120 caracteres
    -- -------------------------------------------------------------------------
    title VARCHAR(120) NOT NULL,

    -- -------------------------------------------------------------------------
    -- DESCRIPTION: Descripción detallada de la tarea
    -- -------------------------------------------------------------------------
    -- Este campo es opcional (puede ser NULL)
    -- VARCHAR(2000): Máximo 2000 caracteres para descripciones largas
    -- -------------------------------------------------------------------------
    description VARCHAR(2000),

    -- -------------------------------------------------------------------------
    -- COMPLETED: Indica si la tarea está completada
    -- -------------------------------------------------------------------------
    -- BOOLEAN: Solo puede ser true o false
    -- DEFAULT FALSE: Las tareas nuevas empiezan como no completadas
    -- NOT NULL: Siempre debe tener un valor
    -- -------------------------------------------------------------------------
    completed BOOLEAN NOT NULL DEFAULT FALSE,

    -- -------------------------------------------------------------------------
    -- CREATED_AT: Fecha y hora de creación
    -- -------------------------------------------------------------------------
    -- TIMESTAMP WITH TIME ZONE: Almacena la fecha/hora con zona horaria
    -- DEFAULT CURRENT_TIMESTAMP: Se establece automáticamente al crear
    -- NOT NULL: Siempre debe tener un valor
    -- -------------------------------------------------------------------------
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

    -- -------------------------------------------------------------------------
    -- UPDATED_AT: Fecha y hora de última actualización
    -- -------------------------------------------------------------------------
    -- Se actualiza cada vez que se modifica la tarea
    -- Inicialmente es igual a created_at
    -- -------------------------------------------------------------------------
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- -----------------------------------------------------------------------------
-- ÍNDICES
-- -----------------------------------------------------------------------------
-- Los índices mejoran el rendimiento de las búsquedas.
-- Creamos índices para las columnas que usaremos frecuentemente en filtros.
-- -----------------------------------------------------------------------------

-- Índice para filtrar por estado (completed)
-- Útil para: GET /tasks?completed=true
CREATE INDEX idx_tasks_completed ON tasks(completed);

-- Índice para ordenar por fecha de creación
-- Útil para: mostrar las tareas más recientes primero
CREATE INDEX idx_tasks_created_at ON tasks(created_at DESC);

-- Índice para búsqueda de texto en título
-- Útil para: GET /tasks?q=busqueda
-- Usamos gin_trgm_ops para búsquedas parciales (LIKE '%texto%')
-- NOTA: Requiere la extensión pg_trgm, que agregamos a continuación

-- Extensión para búsquedas de texto similares
CREATE EXTENSION IF NOT EXISTS pg_trgm;

-- Índice GIN para búsqueda de texto en título y descripción
CREATE INDEX idx_tasks_title_trgm ON tasks USING gin(title gin_trgm_ops);
CREATE INDEX idx_tasks_description_trgm ON tasks USING gin(description gin_trgm_ops);

-- -----------------------------------------------------------------------------
-- COMENTARIOS EN LA TABLA
-- -----------------------------------------------------------------------------
-- Los comentarios ayudan a documentar la base de datos
-- Se pueden ver con: \d+ tasks en psql
-- -----------------------------------------------------------------------------
COMMENT ON TABLE tasks IS 'Tabla principal para almacenar tareas del To-Do List';
COMMENT ON COLUMN tasks.id IS 'Identificador único de la tarea (UUID v4)';
COMMENT ON COLUMN tasks.title IS 'Título de la tarea (3-120 caracteres, obligatorio)';
COMMENT ON COLUMN tasks.description IS 'Descripción detallada de la tarea (opcional, máx 2000 caracteres)';
COMMENT ON COLUMN tasks.completed IS 'Indica si la tarea está completada';
COMMENT ON COLUMN tasks.created_at IS 'Fecha y hora de creación de la tarea';
COMMENT ON COLUMN tasks.updated_at IS 'Fecha y hora de la última actualización';
