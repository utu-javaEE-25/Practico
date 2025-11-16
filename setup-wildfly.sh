#!/bin/bash
# Script para configurar usuario de administración en WildFly

WILDFLY_HOME="${1:-.}"
USERNAME="${2:-admin}"
PASSWORD="${3:-admin123}"

if [ ! -f "$WILDFLY_HOME/bin/add-user.sh" ]; then
    echo "❌ Error: No se encontró WildFly en $WILDFLY_HOME"
    echo "Uso: ./setup-wildfly.sh /ruta/a/wildfly [usuario] [contraseña]"
    exit 1
fi

echo "🔧 Configurando WildFly en: $WILDFLY_HOME"
echo "👤 Usuario: $USERNAME"
echo ""

# Crear usuario de administración
"$WILDFLY_HOME/bin/add-user.sh" "$USERNAME" "$PASSWORD" --silent

if [ $? -eq 0 ]; then
    echo ""
    echo "✅ Usuario '$USERNAME' creado exitosamente"
    echo "🔐 Contraseña: $PASSWORD"
    echo ""
    echo "📝 Guarda estas credenciales para el pipeline:"
    echo "   WILDFLY_USER=$USERNAME"
    echo "   WILDFLY_PASSWORD=$PASSWORD"
else
    echo "❌ Error al crear el usuario"
    exit 1
fi
