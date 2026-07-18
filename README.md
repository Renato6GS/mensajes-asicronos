# Proyecto: Microservicios con Eureka, Gateway, Resilience4j, GitHub Actions y SonarQube

Proyecto del curso de Programación Avanzada en Java. Sistema simple de
**productos y pedidos** que demuestra 5 conceptos:

| # | Concepto | Dónde se aplica |
|---|----------|-----------------|
| 1 | **Eureka** (descubrimiento) | Módulo `eureka-server` (:8761). Los servicios se registran y se localizan por nombre lógico. |
| 2 | **Spring Cloud Gateway** | Módulo `api-gateway` (:8080). Punto de entrada único, enruta con `lb://` usando Eureka. |
| 3 | **Resilience4j** | Módulo `servicio-pedidos`. Circuit Breaker + fallback al llamar a `servicio-productos`. |
| 4 | **GitHub Actions** | `.github/workflows/ci.yml`. Compila, prueba y analiza en cada push a `main`. |
| 5 | **SonarQube** | Paso final del pipeline. Config de ejemplo en `sonar-project.properties.example`. |

Los diagramas Mermaid están en [diagrams/](diagrams/) (se pueden pegar en
https://mermaid.live para exportarlos como imagen al documento Word).

## Módulos

```
mensajes-asicronos/
├── eureka-server/        Registro de servicios          :8761
├── api-gateway/          Puerta de entrada única        :8080
├── servicio-productos/   Catálogo en memoria            :8081
├── servicio-pedidos/     Crea pedidos, usa Resilience4j :8082
├── diagrams/             Diagramas Mermaid (.mmd)
└── .github/workflows/    Pipeline de CI
```

## Requisitos

- Java 17+ y Maven (probado con JDK 25 de SDKMAN)

## Cómo ejecutar (4 terminales, en este orden)

```bash
mvn clean install                          # 1. compilar todo una vez

mvn -pl eureka-server spring-boot:run      # terminal 1
mvn -pl servicio-productos spring-boot:run # terminal 2
mvn -pl servicio-pedidos spring-boot:run   # terminal 3
mvn -pl api-gateway spring-boot:run        # terminal 4
```

Espera ~30 segundos a que todos aparezcan en el panel de Eureka:
http://localhost:8761

> **Nota:** si el puerto 8080 ya está ocupado en tu máquina (en WSL suele
> pasar), levanta el gateway en otro puerto:
> `mvn -pl api-gateway spring-boot:run -Dspring-boot.run.arguments=--server.port=8090`
> y usa `localhost:8090` en las pruebas.

## Cómo probar cada concepto

**Eureka + Gateway** (todo entra por el puerto 8080 del gateway):

```bash
curl http://localhost:8080/api/productos        # enrutado a :8081
curl http://localhost:8080/api/pedidos/nuevo    # enrutado a :8082 → estado CREADO
```

**Resilience4j** (circuit breaker):

1. Detén `servicio-productos` (Ctrl+C en la terminal 2).
2. Llama varias veces: `curl http://localhost:8080/api/pedidos/nuevo`
   → responde `PENDIENTE: catálogo no disponible (fallback activado)`
   sin lanzar error 500.
3. Tras 5 llamadas con 50% de fallos el circuito se **abre**; verifica el estado en:
   http://localhost:8082/actuator/circuitbreakers
4. Levanta de nuevo productos; después de 10s el circuito pasa a
   semiabierto, prueba 2 llamadas y se **cierra**.

**Pruebas unitarias:**

```bash
mvn test
```

## Configurar SonarQube (SonarCloud)

1. Crea tu cuenta en https://sonarcloud.io con tu cuenta de GitHub.
2. Importa el repositorio; anota el **Project Key** y la **Organization**.
3. Genera un token: *My Account → Security → Generate Token*.
4. En GitHub: *Settings → Secrets and variables → Actions* → crea el
   secret `SONAR_TOKEN` con el token.
5. En SonarCloud, desactiva *Automatic Analysis* (Administration →
   Analysis Method) porque el análisis lo hace el pipeline.
6. Reemplaza `TU_PROJECT_KEY` y `TU_ORGANIZACION` en
   [.github/workflows/ci.yml](.github/workflows/ci.yml).
   Ver detalles en [sonar-project.properties.example](sonar-project.properties.example).

## GitHub Actions

El pipeline `.github/workflows/ci.yml` se dispara en cada push/PR a `main`:

1. Checkout del código
2. JDK 17 (Temurin) con caché de Maven
3. `mvn verify` — compila los 4 módulos y ejecuta las pruebas con cobertura JaCoCo
4. `mvn sonar:sonar` — envía el análisis a SonarCloud
