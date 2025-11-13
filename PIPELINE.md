# 🚀 Pipeline CI/CD - Funcionamiento

## Descripción General

Este pipeline automatiza la compilación, análisis de calidad y despliegue de tu aplicación cada vez que haces push a la rama `master`.

---

## 📋 ¿Qué hace?

### En cada PUSH a `master`:

```
1. 🔨 Compilación
   └─ Maven compila el proyecto con JDK 21
   
2. 📊 Análisis de Calidad
   └─ SonarCloud analiza bugs, vulnerabilidades y code smells
   
3. ✅ Tests
   └─ Se ejecutan todas las pruebas unitarias
   
4. 🚀 Despliegue
   └─ La aplicación se despliega automáticamente en WildFly
```

### En PRs:

Solo se ejecutan pasos 1-3 (no hay despliegue).

---

## 🔐 Configuración Requerida

Debes agregar estos **5 secrets** en GitHub (Settings → Secrets and variables → Actions):

| Secret | Valor |
|--------|-------|
| `SONAR_TOKEN` | Token de tu cuenta SonarCloud |
| `WILDFLY_HOST` | `hcenuy.web.elasticloud.uy` |
| `WILDFLY_PORT` | `9990` |
| `WILDFLY_USER` | Tu usuario de WildFly |
| `WILDFLY_PASSWORD` | Tu contraseña de WildFly |

---

## 📁 Archivos del Pipeline

- **`.github/workflows/ci-cd.yml`** - Definición del pipeline
- **`sonar-project.properties`** - Configuración de SonarCloud
- **`pom.xml`** - Plugin de SonarCloud agregado

---

## 📊 Monitoreo

- **GitHub Actions**: https://github.com/utu-javaEE-25/Laboratorio/actions
- **SonarCloud**: https://sonarcloud.io/projects
- **WildFly Console**: https://hcenuy.web.elasticloud.uy:4848/console/index.html

---

## 🎯 Próximos Pasos

1. Agregar los 5 secrets a GitHub
2. Hacer push a `master`
3. Ver el pipeline en acción en GitHub Actions

¡Listo! Cada futura compilación será automática. 🎉
