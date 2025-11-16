#!/bin/bash
# Script interactivo para verificar la configuración del pipeline

echo ""
echo "════════════════════════════════════════════════════════════════"
echo "  🚀 VERIFICADOR DE CONFIGURACIÓN - Pipeline CI/CD"
echo "════════════════════════════════════════════════════════════════"
echo ""

CHECKS_PASSED=0
CHECKS_TOTAL=0

# Función para verificar
check() {
    local name="$1"
    local command="$2"
    
    CHECKS_TOTAL=$((CHECKS_TOTAL + 1))
    
    echo -n "  [$CHECKS_TOTAL/6] $name... "
    
    if eval "$command" > /dev/null 2>&1; then
        echo "✅"
        CHECKS_PASSED=$((CHECKS_PASSED + 1))
    else
        echo "❌"
    fi
}

# Verificaciones
echo "📋 Verificando archivos de configuración:"
echo ""

check "Workflow de GitHub Actions existe" \
    "test -f '.github/workflows/ci-cd.yml'"

check "sonar-project.properties existe" \
    "test -f 'sonar-project.properties'"

check "pom.xml existe" \
    "test -f 'pom.xml'"

check "mvnw existe" \
    "test -f 'mvnw'"

check "git repository" \
    "git rev-parse --git-dir"

check "README_PIPELINE.md existe" \
    "test -f 'README_PIPELINE.md'"

echo ""
echo "════════════════════════════════════════════════════════════════"
echo ""

if [ $CHECKS_PASSED -eq $CHECKS_TOTAL ]; then
    echo "✅ ¡TODOS LOS CHECKS PASARON! ($CHECKS_PASSED/$CHECKS_TOTAL)"
    echo ""
    echo "🎯 Próximos pasos:"
    echo "   1. Agregar los 5 secrets a GitHub"
    echo "   2. Hacer un test push a master"
    echo "   3. Ver en GitHub Actions como se ejecuta el pipeline"
    echo ""
else
    echo "⚠️ Algunos checks fallaron ($CHECKS_PASSED/$CHECKS_TOTAL)"
    echo ""
    echo "❌ Problemas encontrados:"
    if [ ! -f '.github/workflows/ci-cd.yml' ]; then
        echo "   - Falta: .github/workflows/ci-cd.yml"
    fi
    if [ ! -f 'sonar-project.properties' ]; then
        echo "   - Falta: sonar-project.properties"
    fi
    if [ ! -f 'pom.xml' ]; then
        echo "   - Falta: pom.xml"
    fi
    if [ ! -f 'mvnw' ]; then
        echo "   - Falta: mvnw"
    fi
    if ! git rev-parse --git-dir > /dev/null 2>&1; then
        echo "   - No es un repositorio git"
    fi
    echo ""
    echo "Solución: Asegúrate de estar en la raíz del proyecto Laboratorio"
fi

echo ""
echo "════════════════════════════════════════════════════════════════"
echo ""
