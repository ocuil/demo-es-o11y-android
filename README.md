# Proyecto Todo List - Gestión de Tareas con Observabilidad

Este proyecto es una aplicación de gestión de tareas ("todo list") diseñada para ser observable mediante OpenTelemetry (Otel) y Elasticsearch. Está orientada a entornos de laboratorio, pruebas de concepto y formaciones.

## Características

- **Gestión de Tareas:** Los usuarios pueden crear, actualizar y eliminar tareas.
- **Observabilidad con OpenTelemetry:** Instrumentación para rastrear y monitorear el comportamiento de la aplicación.
- **Integración con Elasticsearch:** Los datos observables generados por Otel se envían a un clúster de Elasticsearch para su análisis y visualización.
- **Entorno de laboratorio:** Ideal para realizar pruebas y formaciones en entornos controlados.

## Tecnologías Utilizadas

- **Java / Kotlin** (o tu lenguaje de programación preferido para backend)
- **OpenTelemetry:** Para la trazabilidad y monitoreo de la aplicación.
- **Elasticsearch:** Como almacén de datos para las trazas y métricas generadas por Otel.
- **Gradle/Maven:** Sistema de gestión de dependencias y construcción de la aplicación.

## Requisitos

Antes de ejecutar la aplicación, asegúrate de tener los siguientes componentes:

- **JDK 11+** o superior.
- **Gradle/Maven** instalado.
- **Elasticsearch:** Debes tener acceso a un clúster de Elasticsearch en tu entorno de laboratorio.

## Instalación y Ejecución

1. **Clona este repositorio**:

   ```bash
   git clone git@github.com:ocuil/demo-es-o11y-android.git
   cd demo-es-o11y-android