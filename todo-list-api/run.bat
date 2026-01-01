@echo off
REM =============================================================================
REM TODO LIST API - Windows Commands
REM =============================================================================
REM Uso: run.bat [comando]
REM Ejemplo: run.bat start
REM =============================================================================

if "%1"=="" goto help
if "%1"=="help" goto help
if "%1"=="install" goto install
if "%1"=="run" goto run
if "%1"=="dev" goto dev
if "%1"=="build" goto build
if "%1"=="test" goto test
if "%1"=="clean" goto clean
if "%1"=="db-start" goto db-start
if "%1"=="db-stop" goto db-stop
if "%1"=="db-logs" goto db-logs
if "%1"=="db-reset" goto db-reset
if "%1"=="all" goto all
if "%1"=="stop" goto stop

echo Comando no reconocido: %1
goto help

:help
echo.
echo ======================================================
echo              TODO LIST API - COMANDOS
echo ======================================================
echo.
echo   APLICACION
echo     run.bat install    - Instalar dependencias
echo     run.bat run        - Ejecutar la aplicacion
echo     run.bat dev        - Ejecutar en modo desarrollo
echo     run.bat build      - Compilar el proyecto (JAR)
echo     run.bat test       - Ejecutar tests
echo     run.bat clean      - Limpiar archivos compilados
echo.
echo   BASE DE DATOS (Docker)
echo     run.bat db-start   - Iniciar PostgreSQL
echo     run.bat db-stop    - Detener PostgreSQL
echo     run.bat db-logs    - Ver logs de PostgreSQL
echo     run.bat db-reset   - Reiniciar PostgreSQL
echo.
echo   ATAJOS
echo     run.bat all        - Iniciar BD + Ejecutar app
echo     run.bat stop       - Detener todo
echo.
goto end

:install
echo Instalando dependencias...
mvn dependency:resolve
goto end

:run
echo Iniciando aplicacion...
mvn spring-boot:run
goto end

:dev
echo Iniciando en modo desarrollo...
mvn spring-boot:run -Dspring-boot.run.profiles=dev
goto end

:build
echo Compilando proyecto...
mvn clean package -DskipTests
echo JAR generado en: target\todo-list-api-1.0.0-SNAPSHOT.jar
goto end

:test
echo Ejecutando tests...
mvn test
goto end

:clean
echo Limpiando archivos compilados...
mvn clean
goto end

:db-start
echo Iniciando PostgreSQL...
docker-compose up -d
echo PostgreSQL iniciado en puerto 5432
goto end

:db-stop
echo Deteniendo PostgreSQL...
docker-compose down
echo PostgreSQL detenido
goto end

:db-logs
docker-compose logs -f postgres
goto end

:db-reset
echo Reiniciando PostgreSQL (se eliminaran los datos)...
docker-compose down -v
docker-compose up -d
echo PostgreSQL reiniciado
goto end

:all
echo Iniciando PostgreSQL...
docker-compose up -d
echo Esperando a que PostgreSQL este listo...
timeout /t 3 /nobreak > nul
echo Iniciando aplicacion...
mvn spring-boot:run
goto end

:stop
echo Deteniendo todo...
docker-compose down
echo Todo detenido
goto end

:end
