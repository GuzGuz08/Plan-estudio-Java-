# Hola Mundo - Spring Boot

Mi primer proyecto con Spring Boot.

## ¿Qué hace?

Es una API REST básica con dos endpoints:

| Método | URL | Qué hace |
|--------|-----|----------|
| GET | `/api/health` | Verifica que la API está corriendo |
| GET | `/api/welcome/{nombre}` | Retorna un saludo personalizado |

## ¿Cómo lo corro?

1. Asegurate de tener **Java 17+** instalado
2. Abrí una terminal en la carpeta del proyecto
3. Ejecutá:

```bash
mvn spring-boot:run
```

4. Abrí el navegador o Postman y probá:
   - http://localhost:8080/api/health
   - http://localhost:8080/api/welcome/Andres

## Estructura del proyecto

```
src/main/java/com/ceiba/holamundo/
├── HolaMundoApplication.java          ← Clase principal (arranca la app)
└── controller/
    ├── HealthController.java          ← Endpoint /api/health
    └── WelcomeController.java         ← Endpoint /api/welcome/{nombre}
```

## Respuestas de ejemplo

**GET /api/health**
```json
{
  "status": "OK",
  "message": "API funcionando"
}
```

**GET /api/welcome/Andres**
```json
{
  "message": "¡Hola Andres! Bienvenido a la API"
}
```
