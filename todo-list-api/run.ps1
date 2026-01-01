# =============================================================================
# TODO LIST API - PowerShell Commands
# =============================================================================
# Uso: .\run.ps1 [comando]
# Ejemplo: .\run.ps1 start
# =============================================================================

param(
    [Parameter(Position=0)]
    [string]$Command = "help"
)

function Show-Help {
    Write-Host ""
    Write-Host "======================================================" -ForegroundColor Cyan
    Write-Host "             TODO LIST API - COMANDOS" -ForegroundColor Cyan
    Write-Host "======================================================" -ForegroundColor Cyan
    Write-Host ""
    Write-Host "  APLICACION" -ForegroundColor Yellow
    Write-Host "    .\run.ps1 install    - Instalar dependencias"
    Write-Host "    .\run.ps1 run        - Ejecutar la aplicacion"
    Write-Host "    .\run.ps1 dev        - Ejecutar en modo desarrollo"
    Write-Host "    .\run.ps1 build      - Compilar el proyecto (JAR)"
    Write-Host "    .\run.ps1 test       - Ejecutar tests"
    Write-Host "    .\run.ps1 clean      - Limpiar archivos compilados"
    Write-Host ""
    Write-Host "  BASE DE DATOS (Docker)" -ForegroundColor Yellow
    Write-Host "    .\run.ps1 db-start   - Iniciar PostgreSQL"
    Write-Host "    .\run.ps1 db-stop    - Detener PostgreSQL"
    Write-Host "    .\run.ps1 db-logs    - Ver logs de PostgreSQL"
    Write-Host "    .\run.ps1 db-reset   - Reiniciar PostgreSQL"
    Write-Host ""
    Write-Host "  ATAJOS" -ForegroundColor Yellow
    Write-Host "    .\run.ps1 all        - Iniciar BD + Ejecutar app"
    Write-Host "    .\run.ps1 stop       - Detener todo"
    Write-Host ""
}

switch ($Command) {
    "help" {
        Show-Help
    }
    "install" {
        Write-Host "Instalando dependencias..." -ForegroundColor Green
        mvn dependency:resolve
    }
    "run" {
        Write-Host "Iniciando aplicacion..." -ForegroundColor Green
        mvn spring-boot:run
    }
    "dev" {
        Write-Host "Iniciando en modo desarrollo..." -ForegroundColor Green
        mvn spring-boot:run -D"spring-boot.run.profiles=dev"
    }
    "build" {
        Write-Host "Compilando proyecto..." -ForegroundColor Green
        mvn clean package -DskipTests
        Write-Host "JAR generado en: target\todo-list-api-1.0.0-SNAPSHOT.jar" -ForegroundColor Cyan
    }
    "test" {
        Write-Host "Ejecutando tests..." -ForegroundColor Green
        mvn test
    }
    "clean" {
        Write-Host "Limpiando archivos compilados..." -ForegroundColor Green
        mvn clean
    }
    "db-start" {
        Write-Host "Iniciando PostgreSQL..." -ForegroundColor Green
        docker-compose up -d
        Write-Host "PostgreSQL iniciado en puerto 5432" -ForegroundColor Cyan
    }
    "db-stop" {
        Write-Host "Deteniendo PostgreSQL..." -ForegroundColor Green
        docker-compose down
        Write-Host "PostgreSQL detenido" -ForegroundColor Cyan
    }
    "db-logs" {
        docker-compose logs -f postgres
    }
    "db-reset" {
        Write-Host "Reiniciando PostgreSQL (se eliminaran los datos)..." -ForegroundColor Yellow
        docker-compose down -v
        docker-compose up -d
        Write-Host "PostgreSQL reiniciado" -ForegroundColor Cyan
    }
    "all" {
        Write-Host "Iniciando PostgreSQL..." -ForegroundColor Green
        docker-compose up -d
        Write-Host "Esperando a que PostgreSQL este listo..." -ForegroundColor Yellow
        Start-Sleep -Seconds 3
        Write-Host "Iniciando aplicacion..." -ForegroundColor Green
        mvn spring-boot:run
    }
    "stop" {
        Write-Host "Deteniendo todo..." -ForegroundColor Green
        docker-compose down
        Write-Host "Todo detenido" -ForegroundColor Cyan
    }
    default {
        Write-Host "Comando no reconocido: $Command" -ForegroundColor Red
        Show-Help
    }
}
