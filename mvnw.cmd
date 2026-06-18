@echo off
@REM ---------------------------------------------------------------------------
@REM Maven Wrapper simples para o EduvFinance Desktop.
@REM Baixa o Apache Maven na primeira execucao (dentro de .mvn) e o reutiliza
@REM depois. Assim nao e preciso instalar o Maven globalmente.
@REM Uso: mvnw clean javafx:run
@REM ---------------------------------------------------------------------------
setlocal enabledelayedexpansion

set "MAVEN_VERSION=3.9.9"
set "WRAPPER_DIR=%~dp0.mvn"
set "MAVEN_HOME=%WRAPPER_DIR%\apache-maven-%MAVEN_VERSION%"
set "MVN_CMD=%MAVEN_HOME%\bin\mvn.cmd"

if not exist "%MVN_CMD%" (
  echo [mvnw] Apache Maven %MAVEN_VERSION% nao encontrado localmente. Baixando...
  powershell -NoProfile -ExecutionPolicy Bypass -Command ^
    "$ErrorActionPreference='Stop';" ^
    "$ver='%MAVEN_VERSION%';" ^
    "$dir='%WRAPPER_DIR%';" ^
    "$url='https://repo.maven.apache.org/maven2/org/apache/maven/apache-maven/'+$ver+'/apache-maven-'+$ver+'-bin.zip';" ^
    "$zip=Join-Path $dir ('apache-maven-'+$ver+'-bin.zip');" ^
    "New-Item -ItemType Directory -Force -Path $dir | Out-Null;" ^
    "Write-Host ('[mvnw] Baixando '+$url);" ^
    "Invoke-WebRequest -Uri $url -OutFile $zip;" ^
    "Write-Host '[mvnw] Extraindo...';" ^
    "Expand-Archive -Path $zip -DestinationPath $dir -Force;" ^
    "Remove-Item $zip"
  if errorlevel 1 (
    echo [mvnw] Falha ao baixar o Maven. Verifique sua conexao com a internet. 1>&2
    exit /b 1
  )
)

call "%MVN_CMD%" %*
