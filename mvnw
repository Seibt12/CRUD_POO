#!/bin/sh
# ---------------------------------------------------------------------------
# Maven Wrapper simples para o EduvFinance Desktop (Linux/macOS/Git Bash).
# Baixa o Apache Maven na primeira execucao (dentro de .mvn) e o reutiliza.
# Uso: ./mvnw clean javafx:run
# ---------------------------------------------------------------------------
set -e

MAVEN_VERSION=3.9.9
WRAPPER_DIR="$(cd "$(dirname "$0")" && pwd)/.mvn"
MAVEN_HOME="$WRAPPER_DIR/apache-maven-$MAVEN_VERSION"
MVN_CMD="$MAVEN_HOME/bin/mvn"

if [ ! -x "$MVN_CMD" ]; then
  url="https://repo.maven.apache.org/maven2/org/apache/maven/apache-maven/$MAVEN_VERSION/apache-maven-$MAVEN_VERSION-bin.zip"
  zip="$WRAPPER_DIR/apache-maven-$MAVEN_VERSION-bin.zip"
  mkdir -p "$WRAPPER_DIR"
  echo "[mvnw] Apache Maven $MAVEN_VERSION nao encontrado localmente. Baixando..."
  if command -v curl >/dev/null 2>&1; then
    curl -fsSL -o "$zip" "$url"
  elif command -v wget >/dev/null 2>&1; then
    wget -q -O "$zip" "$url"
  else
    echo "[mvnw] curl ou wget necessario para baixar o Maven." 1>&2
    exit 1
  fi
  unzip -q "$zip" -d "$WRAPPER_DIR"
  rm -f "$zip"
fi

exec "$MVN_CMD" "$@"
