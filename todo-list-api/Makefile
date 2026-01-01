# =============================================================================
# MAKEFILE - TODO LIST API
# =============================================================================
# Comandos útiles para ejecutar y gestionar la aplicación.
#
# Uso: make <comando>
# Ejemplo: make run
# =============================================================================

.PHONY: help install run dev clean build test db-start db-stop db-logs db-reset all

# Comando por defecto: mostrar ayuda
help:
	@echo ""
	@echo "╔════════════════════════════════════════════════════════════════╗"
	@echo "║                    TODO LIST API - COMANDOS                     ║"
	@echo "╠════════════════════════════════════════════════════════════════╣"
	@echo "║  APLICACIÓN                                                     ║"
	@echo "║    make install    - Instalar dependencias                      ║"
	@echo "║    make run        - Ejecutar la aplicación                     ║"
	@echo "║    make dev        - Ejecutar en modo desarrollo                ║"
	@echo "║    make build      - Compilar el proyecto (JAR)                 ║"
	@echo "║    make test       - Ejecutar tests                             ║"
	@echo "║    make clean      - Limpiar archivos compilados                ║"
	@echo "║                                                                 ║"
	@echo "║  BASE DE DATOS (Docker)                                         ║"
	@echo "║    make db-start   - Iniciar PostgreSQL                         ║"
	@echo "║    make db-stop    - Detener PostgreSQL                         ║"
	@echo "║    make db-logs    - Ver logs de PostgreSQL                     ║"
	@echo "║    make db-reset   - Reiniciar PostgreSQL (elimina datos)       ║"
	@echo "║                                                                 ║"
	@echo "║  ATAJOS                                                         ║"
	@echo "║    make all        - Iniciar BD + Ejecutar aplicación           ║"
	@echo "║    make stop       - Detener todo                               ║"
	@echo "╚════════════════════════════════════════════════════════════════╝"
	@echo ""

# -----------------------------------------------------------------------------
# APLICACIÓN
# -----------------------------------------------------------------------------

# Instalar dependencias
install:
	@echo "📦 Instalando dependencias..."
	mvn dependency:resolve

# Ejecutar la aplicación
run:
	@echo "🚀 Iniciando aplicación..."
	mvn spring-boot:run

# Ejecutar en modo desarrollo (con profile dev)
dev:
	@echo "🔧 Iniciando en modo desarrollo..."
	mvn spring-boot:run -Dspring-boot.run.profiles=dev

# Compilar el proyecto
build:
	@echo "🔨 Compilando proyecto..."
	mvn clean package -DskipTests
	@echo "✅ JAR generado en: target/todo-list-api-1.0.0-SNAPSHOT.jar"

# Ejecutar tests
test:
	@echo "🧪 Ejecutando tests..."
	mvn test

# Limpiar archivos compilados
clean:
	@echo "🧹 Limpiando archivos compilados..."
	mvn clean

# -----------------------------------------------------------------------------
# BASE DE DATOS (Docker)
# -----------------------------------------------------------------------------

# Iniciar PostgreSQL
db-start:
	@echo "🐘 Iniciando PostgreSQL..."
	docker-compose up -d
	@echo "✅ PostgreSQL iniciado en puerto 5432"

# Detener PostgreSQL
db-stop:
	@echo "🛑 Deteniendo PostgreSQL..."
	docker-compose down
	@echo "✅ PostgreSQL detenido"

# Ver logs de PostgreSQL
db-logs:
	docker-compose logs -f postgres

# Reiniciar PostgreSQL (elimina datos)
db-reset:
	@echo "⚠️  Reiniciando PostgreSQL (se eliminarán todos los datos)..."
	docker-compose down -v
	docker-compose up -d
	@echo "✅ PostgreSQL reiniciado"

# Estado de la base de datos
db-status:
	@docker-compose ps

# -----------------------------------------------------------------------------
# ATAJOS
# -----------------------------------------------------------------------------

# Iniciar todo (BD + App)
all: db-start
	@echo "⏳ Esperando a que PostgreSQL esté listo..."
	@sleep 3
	@make run

# Detener todo
stop: db-stop
	@echo "✅ Todo detenido"
