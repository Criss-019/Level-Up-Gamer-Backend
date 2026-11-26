@echo off
title Level-Up Gamer - Detener Sistema Fullstack
color 0C

echo ===================================================================
echo               DETENIENDO LEVEL-UP GAMER FULLSTACK
echo ===================================================================
echo.

echo [1/4] Deteniendo Frontend React (puerto 5173)...
for /f "tokens=5" %%a in ('netstat -aon ^| findstr :5173 ^| findstr LISTENING') do (
    taskkill /F /PID %%a >nul 2>&1
)
taskkill /F /IM node.exe >nul 2>&1

echo [2/4] Deteniendo Backend Spring Boot (puerto 8089)...
for /f "tokens=5" %%a in ('netstat -aon ^| findstr :8089 ^| findstr LISTENING') do (
    taskkill /F /PID %%a >nul 2>&1
)
taskkill /F /IM java.exe >nul 2>&1

echo [3/4] Deteniendo Servidor Apache...
taskkill /F /IM httpd.exe >nul 2>&1

echo [4/4] Deteniendo Base de Datos MySQL...
taskkill /F /IM mysqld.exe >nul 2>&1

echo.
echo ===================================================================
echo     TODOS LOS SERVICIOS Y PROCESOS HAN SIDO DETENIDOS!
echo ===================================================================
echo.
pause
