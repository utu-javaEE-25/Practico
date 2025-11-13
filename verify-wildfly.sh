#!/bin/bash
# Script para verificar conectividad con WildFly

WILDFLY_HOST="${1:-hcenuy.web.elasticloud.uy}"
WILDFLY_PORT="${2:-9990}"
WILDFLY_USER="${3:-admin}"
WILDFLY_PASSWORD="${4:-}"

echo "🔍 Verificando conectividad con WildFly..."
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo "Host: $WILDFLY_HOST"
echo "Puerto: $WILDFLY_PORT"
echo "Usuario: $WILDFLY_USER"
echo ""

# Verificar si se puede resolver el host
echo "1️⃣ Resolviendo DNS..."
if ! nslookup "$WILDFLY_HOST" > /dev/null 2>&1; then
    echo "❌ No se puede resolver el host: $WILDFLY_HOST"
    exit 1
fi
echo "✅ Host resuelto correctamente"
echo ""

# Verificar conectividad al puerto
echo "2️⃣ Verificando puerto $WILDFLY_PORT..."
if ! nc -zv "$WILDFLY_HOST" "$WILDFLY_PORT" > /dev/null 2>&1; then
    echo "❌ No se puede conectar a $WILDFLY_HOST:$WILDFLY_PORT"
    echo "   Posibles razones:"
    echo "   - El puerto no está abierto"
    echo "   - El firewall está bloqueando la conexión"
    echo "   - WildFly no está corriendo"
    exit 1
fi
echo "✅ Puerto $WILDFLY_PORT accesible"
echo ""

# Si se proporciona contraseña, intentar conectar con jboss-cli
if [ -n "$WILDFLY_PASSWORD" ]; then
    echo "3️⃣ Verificando credenciales..."
    
    if ! command -v jboss-cli.sh &> /dev/null; then
        echo "⚠️ jboss-cli.sh no encontrado. Instala WildFly CLI para verificar credenciales"
    else
        # Intentar conectar y listar deployments
        if jboss-cli.sh -c --controller="$WILDFLY_HOST:$WILDFLY_PORT" \
                        -u "$WILDFLY_USER" -p "$WILDFLY_PASSWORD" \
                        'ls deployment' > /dev/null 2>&1; then
            echo "✅ Credenciales válidas"
            echo ""
            echo "📦 Deployments actuales:"
            jboss-cli.sh -c --controller="$WILDFLY_HOST:$WILDFLY_PORT" \
                        -u "$WILDFLY_USER" -p "$WILDFLY_PASSWORD" \
                        'ls deployment'
        else
            echo "❌ Credenciales inválidas o error al conectar"
            exit 1
        fi
    fi
else
    echo "⚠️ Contraseña no proporcionada. No se puede verificar credenciales"
    echo "   Uso: $0 <host> <puerto> <usuario> <contraseña>"
fi

echo ""
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo "✅ ¡WildFly está accesible y listo para despliegue!"
