# ===================================================================
# Dockerfile Final Definitivo - Estrategia index.html
# ===================================================================

# --- ETAPA 1: Construcción con Maven ---
# Usamos una imagen oficial de Maven con Java 17 para compilar el proyecto.
FROM maven:3.9.9-eclipse-temurin-17 AS builder

# Establecemos el directorio de trabajo dentro de la imagen.
WORKDIR /app

# Copiamos todo el código fuente del proyecto.
COPY . .

# Ejecutamos el comando de Maven para compilar todo y generar el .ear.
RUN mvn clean install -DskipTests


# --- ETAPA 2: Ejecución con WildFly ---
# Usamos la imagen oficial de WildFly 30 que corre sobre Java 17.
FROM quay.io/wildfly/wildfly:30.0.1.Final-jdk17

# Copiamos el archivo .ear que generamos en la etapa anterior (builder)
# al directorio de despliegues de WildFly.
COPY --from=builder /app/ear/target/*.ear /opt/jboss/wildfly/standalone/deployments/

# Exponemos el puerto 8080 para que la aplicación sea accesible.
EXPOSE 8080

# Establecemos las opciones de memoria de Java para un consumo reducido.
ENV JAVA_OPTS="-Xms128m -Xmx256m -XX:MetaspaceSize=64M -XX:MaxMetaspaceSize=128m"

# El comando estándar para iniciar WildFly.
CMD ["/opt/jboss/wildfly/bin/standalone.sh", "-b", "0.0.0.0"]