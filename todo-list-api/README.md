# Todo List API

API REST para gestión de tareas desarrollada con Spring Boot 3 y PostgreSQL.
Proyecto orientado a principiantes con código bien comentado y fácil de entender.

## Tecnologías Utilizadas

- **Java 17**
- **Spring Boot 3.2.1**
- **Spring Data JPA**
- **PostgreSQL**
- **Flyway** (migraciones de base de datos)
- **Lombok** (reducción de código repetitivo)
- **Jakarta Validation** (validación de datos)
- **SpringDoc OpenAPI** (documentación Swagger)
- **Docker Compose** (contenedorización de PostgreSQL)

## Estructura del Proyecto

```
src/main/java/com/example/todolist/
├── TodoListApplication.java    # Clase principal
├── config/
│   └── OpenApiConfig.java      # Configuración de Swagger
├── controller/
│   └── TaskController.java     # Endpoints REST
├── service/
│   └── TaskService.java        # Lógica de negocio
├── repository/
│   └── TaskRepository.java     # Acceso a datos
├── entity/
│   └── Task.java               # Entidad JPA
├── dto/
│   ├── TaskRequest.java        # DTO de entrada
│   ├── TaskResponse.java       # DTO de salida
│   ├── ErrorResponse.java      # DTO de errores
│   └── PageResponse.java       # DTO de paginación
└── exception/
    ├── TaskNotFoundException.java      # Excepción personalizada
    └── GlobalExceptionHandler.java     # Manejador de errores
```

## Requisitos Previos

1. **Java 17** o superior
2. **Maven 3.6+** (o usar el wrapper incluido `./mvnw`)
3. **Docker y Docker Compose** (recomendado) o **PostgreSQL 12+** instalado localmente

## Inicio Rápido

La forma más rápida de ejecutar el proyecto:

```bash
# 1. Iniciar PostgreSQL con Docker
docker-compose up -d

# 2. Ejecutar la aplicación
./mvnw spring-boot:run

# 3. Abrir Swagger UI en el navegador
open http://localhost:8080/swagger-ui.html
```

## Configuración de la Base de Datos

### Opción 1: Docker Compose (Recomendado)

El proyecto incluye un archivo `docker-compose.yml` que configura PostgreSQL automáticamente.

```bash
# Iniciar PostgreSQL
docker-compose up -d

# Verificar que está corriendo
docker-compose ps

# Ver logs
docker-compose logs -f postgres

# Detener
docker-compose down

# Detener y eliminar datos
docker-compose down -v
```

**Configuración del contenedor:**

| Propiedad | Valor |
|-----------|-------|
| Contenedor | `todolist-postgres` |
| Puerto | `5432` |
| Base de datos | `todolist_db` |
| Usuario | `postgres` |
| Contraseña | `postgres` |

### Opción 2: PostgreSQL Local

Si prefieres usar PostgreSQL instalado localmente:

```bash
# Conectar a PostgreSQL
psql -U postgres

# Crear la base de datos
CREATE DATABASE todolist_db;

# Verificar
\l

# Salir
\q
```

### Configurar credenciales

Si tus credenciales son diferentes, edita `src/main/resources/application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/todolist_db
    username: postgres
    password: postgres
```

## Ejecución

### Linux / macOS

Usa el Makefile incluido:

```bash
# Ver todos los comandos disponibles
make help

# Iniciar todo (PostgreSQL + Aplicación)
make all

# O paso a paso:
make db-start   # Iniciar PostgreSQL
make run        # Ejecutar la aplicación
```

**Comandos disponibles:**

| Comando | Descripción |
|---------|-------------|
| `make all` | Iniciar BD + Aplicación |
| `make run` | Ejecutar la aplicación |
| `make db-start` | Iniciar PostgreSQL |
| `make db-stop` | Detener PostgreSQL |
| `make db-logs` | Ver logs de PostgreSQL |
| `make db-reset` | Reiniciar BD (elimina datos) |
| `make build` | Compilar JAR |
| `make test` | Ejecutar tests |
| `make clean` | Limpiar compilados |
| `make stop` | Detener todo |

### Windows

Usa los scripts incluidos:

**CMD (run.bat):**
```cmd
REM Ver ayuda
run.bat help

REM Iniciar todo
run.bat all

REM O paso a paso:
run.bat db-start
run.bat run
```

**PowerShell (run.ps1):**
```powershell
# Ver ayuda
.\run.ps1 help

# Iniciar todo
.\run.ps1 all

# O paso a paso:
.\run.ps1 db-start
.\run.ps1 run
```

**Comandos directos (sin scripts):**
```powershell
docker-compose up -d
mvn spring-boot:run
```

### Requisitos para Windows

1. **Java 17+** - [Descargar Temurin](https://adoptium.net/)
2. **Maven 3.6+** - [Descargar Maven](https://maven.apache.org/download.cgi)
3. **Docker Desktop** - [Descargar Docker](https://www.docker.com/products/docker-desktop/)

Verificar instalación:
```powershell
java -version
mvn -version
docker --version
```

### Compilar y ejecutar JAR

```bash
# Linux/macOS
make build
java -jar target/todo-list-api-1.0.0-SNAPSHOT.jar

# Windows
run.bat build
java -jar target\todo-list-api-1.0.0-SNAPSHOT.jar
```

### Desde el IDE

Ejecuta la clase `TodoListApplication.java` como aplicación Java.

---

La aplicación estará disponible en: **http://localhost:8080**

## Endpoints de la API

Base URL: `http://localhost:8080/api/v1`

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `/tasks` | Crear tarea |
| GET | `/tasks` | Listar tareas (con filtros) |
| GET | `/tasks/{id}` | Obtener tarea por ID |
| PUT | `/tasks/{id}` | Actualizar tarea |
| PATCH | `/tasks/{id}/toggle` | Alternar estado completado |
| DELETE | `/tasks/{id}` | Eliminar tarea |

## Documentación Swagger (OpenAPI)

La API incluye documentación interactiva generada automáticamente con SpringDoc OpenAPI.

### URLs de Documentación

Una vez ejecutada la aplicación, accede a:

| Recurso | URL |
|---------|-----|
| **Swagger UI** | http://localhost:8080/swagger-ui.html |
| **OpenAPI JSON** | http://localhost:8080/v3/api-docs |
| **OpenAPI YAML** | http://localhost:8080/v3/api-docs.yaml |

### Swagger UI

Swagger UI te permite:

- **Explorar** todos los endpoints de la API
- **Probar** las operaciones directamente desde el navegador
- **Ver** ejemplos de request y response
- **Entender** los esquemas de datos (DTOs)

### Captura de pantalla

```
┌─────────────────────────────────────────────────────────────┐
│  Todo List API                                    v1.0.0    │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  Tareas                                                     │
│  ├── POST   /api/v1/tasks         Crear una nueva tarea     │
│  ├── GET    /api/v1/tasks         Listar todas las tareas   │
│  ├── GET    /api/v1/tasks/{id}    Obtener tarea por ID      │
│  ├── PUT    /api/v1/tasks/{id}    Actualizar una tarea      │
│  ├── PATCH  /api/v1/tasks/{id}/toggle  Alternar estado      │
│  └── DELETE /api/v1/tasks/{id}    Eliminar una tarea        │
│                                                             │
│  Schemas                                                    │
│  ├── TaskRequest                                            │
│  ├── TaskResponse                                           │
│  ├── PageResponse                                           │
│  └── ErrorResponse                                          │
│                                                             │
└─────────────────────────────────────────────────────────────┘
```

## Formato de Respuestas

Todas las respuestas de la API usan un formato estándar:

```json
{
  "success": true | false,
  "message": "Mensaje descriptivo",
  "data": { ... },
  "errors": [...],
  "timestamp": "2024-01-15T10:30:00Z"
}
```

| Campo | Descripción |
|-------|-------------|
| `success` | `true` si la operación fue exitosa, `false` si hubo error |
| `message` | Mensaje descriptivo del resultado |
| `data` | Datos de la respuesta (objeto, lista, o null) |
| `errors` | Lista de errores (solo cuando `success=false`) |
| `timestamp` | Marca de tiempo de la respuesta |

## Ejemplos de Uso (cURL)

### Crear una tarea

```bash
curl -X POST http://localhost:8080/api/v1/tasks \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Comprar leche",
    "description": "Ir al supermercado y comprar 2 litros de leche"
  }'
```

**Respuesta (201 Created):**
```json
{
  "success": true,
  "message": "Tarea creada exitosamente",
  "data": {
    "id": "550e8400-e29b-41d4-a716-446655440000",
    "title": "Comprar leche",
    "description": "Ir al supermercado y comprar 2 litros de leche",
    "completed": false,
    "createdAt": "2024-01-15T10:30:00Z",
    "updatedAt": "2024-01-15T10:30:00Z"
  },
  "timestamp": "2024-01-15T10:30:00Z"
}
```

### Listar todas las tareas

```bash
curl http://localhost:8080/api/v1/tasks
```

**Respuesta (200 OK):**
```json
{
  "success": true,
  "message": "Tareas obtenidas exitosamente",
  "data": {
    "content": [
      {
        "id": "550e8400-e29b-41d4-a716-446655440000",
        "title": "Comprar leche",
        "description": "Ir al supermercado",
        "completed": false,
        "createdAt": "2024-01-15T10:30:00Z",
        "updatedAt": "2024-01-15T10:30:00Z"
      }
    ],
    "page": 0,
    "size": 10,
    "totalElements": 1,
    "totalPages": 1,
    "first": true,
    "last": true
  },
  "timestamp": "2024-01-15T10:30:00Z"
}
```

### Listar con filtros

```bash
# Solo tareas completadas
curl "http://localhost:8080/api/v1/tasks?completed=true"

# Solo tareas pendientes
curl "http://localhost:8080/api/v1/tasks?completed=false"

# Buscar por texto
curl "http://localhost:8080/api/v1/tasks?q=leche"

# Combinar filtros
curl "http://localhost:8080/api/v1/tasks?completed=false&q=comprar"

# Con paginación
curl "http://localhost:8080/api/v1/tasks?page=0&size=5"
```

### Obtener tarea por ID

```bash
curl http://localhost:8080/api/v1/tasks/550e8400-e29b-41d4-a716-446655440000
```

### Actualizar tarea

```bash
curl -X PUT http://localhost:8080/api/v1/tasks/550e8400-e29b-41d4-a716-446655440000 \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Comprar leche y pan",
    "description": "Ir al supermercado",
    "completed": true
  }'
```

### Alternar estado (completada/pendiente)

```bash
curl -X PATCH http://localhost:8080/api/v1/tasks/550e8400-e29b-41d4-a716-446655440000/toggle
```

### Eliminar tarea

```bash
curl -X DELETE http://localhost:8080/api/v1/tasks/550e8400-e29b-41d4-a716-446655440000
```

**Respuesta (200 OK):**
```json
{
  "success": true,
  "message": "Tarea eliminada exitosamente",
  "timestamp": "2024-01-15T10:30:00Z"
}
```

## Manejo de Errores

Los errores también usan el formato estándar con `success: false`:

### Tarea no encontrada (404)

```json
{
  "success": false,
  "message": "No se encontró la tarea con ID: 550e8400-e29b-41d4-a716-446655440000",
  "timestamp": "2024-01-15T10:30:00Z"
}
```

### Error de validación (400)

```json
{
  "success": false,
  "message": "Error de validación",
  "errors": [
    "title: El título es obligatorio",
    "title: El título debe tener entre 3 y 120 caracteres"
  ],
  "timestamp": "2024-01-15T10:30:00Z"
}
```

### Error interno (500)

```json
{
  "success": false,
  "message": "Error interno del servidor. Por favor, intenta más tarde.",
  "timestamp": "2024-01-15T10:30:00Z"
}
```

## Modelo de Datos

### Task (Tarea)

| Campo | Tipo | Restricciones |
|-------|------|---------------|
| id | UUID | PK, auto-generado |
| title | String | Obligatorio, 3-120 caracteres |
| description | String | Opcional, máximo 2000 caracteres |
| completed | Boolean | Por defecto: false |
| createdAt | Instant | Auto-generado |
| updatedAt | Instant | Auto-actualizado |

## Pruebas con Postman

Puedes importar esta colección en Postman para probar la API:

1. Crea una nueva colección llamada "Todo List API"
2. Configura la variable `baseUrl` como `http://localhost:8080/api/v1`
3. Crea requests para cada endpoint

## Notas para Principiantes

### Arquitectura en Capas

```
Cliente (Postman/Frontend)
         ↓
    Controller   ← Recibe peticiones HTTP, devuelve respuestas
         ↓
     Service     ← Lógica de negocio, validaciones
         ↓
    Repository   ← Acceso a base de datos
         ↓
    Database     ← PostgreSQL
```

### Flujo de una Petición

1. El cliente envía una petición HTTP
2. El **Controller** recibe la petición y extrae los parámetros
3. El **Service** ejecuta la lógica de negocio
4. El **Repository** realiza operaciones en la base de datos
5. El resultado sube por las capas hasta llegar al cliente

### ¿Por qué usamos DTOs?

- **Seguridad**: No exponemos campos internos
- **Flexibilidad**: Podemos cambiar la BD sin afectar la API
- **Validación**: Validamos la entrada antes de procesarla
- **Claridad**: El cliente solo ve lo que necesita

## Próximos Pasos (Ideas para expandir)

- Agregar tests unitarios e integración
- Implementar ordenamiento personalizado
- Agregar categorías/etiquetas a las tareas
- Implementar fechas de vencimiento
- Agregar prioridades (alta, media, baja)
- ~~Documentar con Swagger/OpenAPI~~ ✓ Completado
- ~~Configurar Docker Compose~~ ✓ Completado
