@echo off
title Level-Up Gamer - Lanzador Fullstack
color 0B

set "PATH=C:\Program Files\nodejs;%PATH%"

echo ===================================================================
echo               LEVEL-UP GAMER - E-COMMERCE FULLSTACK
echo ===================================================================
echo.

rem Detección inteligente de la ruta de instalación de XAMPP (C: o D:)
set "XAMPP_DIR="
if exist "D:\Xampp\mysql_start.bat" set "XAMPP_DIR=D:\Xampp"
if exist "C:\xampp\mysql_start.bat" set "XAMPP_DIR=C:\xampp"
if exist "D:\xampp\mysql_start.bat" set "XAMPP_DIR=D:\xampp"
if exist "C:\Xampp\mysql_start.bat" set "XAMPP_DIR=C:\Xampp"

echo [1/3] Iniciando Servidor MySQL y Apache...
if defined XAMPP_DIR (
    echo       Ruta detectada de XAMPP: %XAMPP_DIR%
    if exist "%XAMPP_DIR%\mysql_start.bat" start "MySQL Server (3307)" "%XAMPP_DIR%\mysql_start.bat"
    if exist "%XAMPP_DIR%\apache_start.bat" start "Apache Server" "%XAMPP_DIR%\apache_start.bat"
) else (
    echo       AVISO: No se detecto XAMPP en C:\ ni D:\. Asegurese de iniciar MySQL manualmente.
)
timeout /t 3 /nobreak >nul

echo [2/3] Iniciando Backend en Spring Boot (Puerto 8089)...
start "Backend Spring Boot" cmd /k "cd /d "%~dp0level-up-gamer-backend" && java -jar target\level-up-gamer-backend-0.0.1-SNAPSHOT.jar"
timeout /t 6 /nobreak >nul

echo [3/3] Iniciando Frontend React (Puerto 5173)...
start "Frontend React Vite" cmd /k "cd /d "%~dp0Level-Up Gamer" && set "PATH=C:\Program Files\nodejs;%%PATH%%" && npm run dev"

echo.
echo ===================================================================
echo      PROYECTO INICIADO EXITOSAMENTE SIN DOCKER!
echo ===================================================================
echo  - Frontend Tienda:      http://localhost:5173
echo  - Backend API Catálogo: http://localhost:8089/api/v1/productos
echo  - Swagger UI Endpoints: http://localhost:8089/swagger-ui/index.html
echo  - phpMyAdmin (MySQL):   http://localhost/phpmyadmin
echo ===================================================================
echo.
pause
