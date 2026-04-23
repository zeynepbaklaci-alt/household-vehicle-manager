# Gestor de Vehículos del Hogar

## Descripción General

Household Vehicle Manager es una aplicación full‑stack diseñada para gestionar los vehículos de un hogar y sus eventos asociados.

El sistema permite:
- Registrar y gestionar vehículos
- Controlar el consumo y coste de combustible
- Registrar pólizas de seguro y revisiones ITV
- Generar recordatorios automáticos basados en fechas
- Visualizar notificaciones pendientes en un panel central

El proyecto está compuesto por una **aplicación de escritorio JavaFX (frontend)** y una **API REST con Spring Boot (backend)**, desarrolladas íntegramente por la autora como práctica académica full‑stack.

---

## Arquitectura

La aplicación sigue una arquitectura cliente‑servidor:

- **Frontend**: Aplicación de escritorio JavaFX
- **Backend**: API REST con Spring Boot
- **Base de datos**: PostgreSQL
- **Autenticación**: JWT (JSON Web Token)

El cliente JavaFX se comunica con el backend mediante peticiones HTTP REST.

---

## Tecnologías Utilizadas

### Frontend
- Java
- JavaFX
- Java HTTP Client

### Backend
- Java
- Spring Boot
- Spring Security (JWT)
- Spring Data JPA
- APIs REST

### Base de Datos
- PostgreSQL

---

## Funcionalidades Principales

### Gestión de Vehículos
- Crear, listar, editar y eliminar vehículos
- Autorización basada en hogares
- Validación de formularios y reglas de negocio

### Control de Combustible
- Registro de repostajes
- Seguimiento del odómetro
- Cálculo automático del coste total
- Edición y eliminación de registros

### Seguros e ITV
- Registro de pólizas de seguro
- Registro de inspecciones ITV
- Validaciones de fechas y datos
- Historial de seguros e ITV por vehículo

### Sistema de Recordatorios
- Creación automática de recordatorios para:
    - Vencimiento de seguros
    - Vencimiento de ITV
- Programación basada en fechas (ej. 30 días antes)
- Visualización de recordatorios pendientes
- Ciclo de vida completo (pendiente → descartado)
- Código de colores según urgencia

### Panel Principal
- Visualización centralizada de notificaciones
- Indicador de recordatorios pendientes
- Historial de recordatorios

---

## Validaciones

- Campos obligatorios en todos los formularios
- El valor del odómetro debe ser numérico y no negativo
- Las fechas deben ser coherentes
- Prevención de recordatorios duplicados
- Control de acceso mediante autorización de hogar

---

## Alcance del Proyecto

Proyecto educativo enfocado en:
- Buenas prácticas de arquitectura
- Separación de responsabilidades
- Seguridad y autorización
- Lógica basada en tiempo
- Experiencia de usuario realista

Frontend y backend desarrollados completamente por la autora.

---

## Application Screenshots

### Vehicle List
screenshots/vehicle-list.png

### Vehicle Detail View
screenshots/vehicle-detail.png

### Fuel Management
screenshots/fuel-tab.png

### Insurance Management
screenshots/insurance-tab.png

### ITV Management
screenshots/itv-tab.png

### Reminder Dashboard
screenshots/reminder-dashboard.png

---

## Autora

**Zeynep BAKLACI**  
Product Software Engineer – Barcelona