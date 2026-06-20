@echo off

echo Subindo containers...
docker compose up -d

echo Terminando inicializacao, aguarde...
:loop
curl -s http://localhost:8080 >nul 2>nul
if %errorlevel% neq 0 (
    timeout /t 2 >nul
    goto loop
)
for /f "delims=" %%i in ('
  curl -s -o nul -w "%%{http_code}" http://localhost:8080/usuario/primeirousuario
') do set HTTP_CODE=%%i
if "%HTTP_CODE%" NEQ "401" (
    echo Primeiro usuario criado, cpf: 11111111111, senha: 123
)

echo Sistema iniciado! Acesse http://localhost:5173

pause