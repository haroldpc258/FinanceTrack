# FinanceTrack 💰

Una aplicación web robusta de gestión financiera empresarial desarrollada con Spring Boot, diseñada para ayudar a las empresas a monitorear sus ingresos, gastos y generar reportes financieros detallados.

## 📋 Descripción

FinanceTrack es una solución completa de gestión financiera que permite a las empresas llevar un control detallado de sus movimientos financieros, gestionar empleados y generar dashboards con métricas clave del negocio. La aplicación cuenta con diferentes niveles de acceso según el rol del usuario.

## 🚀 Tecnologías Utilizadas

- **Java 21** - Lenguaje de programación principal
- **Spring Boot 3.1.4** - Framework principal de la aplicación
- **Spring Data JPA** - Para la persistencia de datos
- **Spring Security con OAuth2** - Autenticación y autorización
- **Okta** - Proveedor de identidad OAuth2
- **Thymeleaf** - Motor de plantillas para las vistas web
- **MySQL** - Base de datos relacional
- **Lombok** - Para reducir código boilerplate
- **Maven** - Gestión de dependencias [1](#0-0) [2](#0-1) 

## 🏗️ Arquitectura del Sistema

La aplicación sigue el patrón MVC (Model-View-Controller) y está estructurada en las siguientes capas:

- **Entities** - Modelos de datos (User, Company, Movement)
- **Repositories** - Acceso a datos
- **Services** - Lógica de negocio
- **Controllers** - Controladores web
- **Views** - Plantillas Thymeleaf [3](#0-2) 

## 🔐 Sistema de Autenticación

La aplicación utiliza OAuth2 con Okta como proveedor de identidad, proporcionando:

- Autenticación segura sin manejo directo de contraseñas
- Integración con proveedores externos
- Gestión automática de sesiones
- Logout seguro con redirección [4](#0-3) 

## 👥 Gestión de Usuarios y Roles

El sistema maneja tres tipos de roles con diferentes permisos:

- **SUPER_USER** - Administrador del sistema con acceso completo
- **ADMINISTRATOR** - Administrador de empresa
- **OPERATIVE** - Usuario operativo con permisos limitados [5](#0-4) 

Cada usuario pertenece a una empresa y tiene información detallada como DNI, teléfono y estado. [6](#0-5) 

## 🏢 Gestión de Empresas

Las empresas pueden gestionar:

- Información básica (NIT, nombre, contacto)
- Lista de empleados asociados
- Movimientos financieros propios
- Estado de la empresa (ACTIVO/INACTIVO) [7](#0-6) 

## 💸 Gestión de Movimientos Financieros

El sistema permite registrar dos tipos de movimientos:

- **INCOME** - Ingresos
- **EXPENSE** - Gastos

Cada movimiento incluye:
- Monto
- Concepto/descripción
- Usuario que lo creó
- Fecha y hora de creación [8](#0-7) [9](#0-8) 

## 📊 Dashboard y Reportes

### Dashboard de Administrador
Para usuarios SUPER_USER, proporciona:
- Total de empresas registradas
- Nuevas empresas del mes
- Total de usuarios del sistema
- Actividad general del sistema
- Movimientos recientes globales [10](#0-9) 

### Dashboard de Empresa
Para usuarios regulares, incluye:
- Métricas financieras (ingresos, gastos, margen de ganancia)
- Comparativas mensuales con porcentajes de cambio
- Número de empleados activos
- Transacciones recientes de la empresa
- Indicadores visuales de tendencias [11](#0-10) 

## 📁 Estructura del Proyecto

```
src/main/java/edu/sena/finance/track/
├── controllers/          # Controladores web
│   ├── AdminController.java
│   ├── CompanyController.java
│   ├── DashboardController.java
│   ├── MovementController.java
│   └── UserController.java
├── entities/            # Modelos de datos
│   ├── Company.java
│   ├── Movement.java
│   ├── User.java
│   └── enums/
├── repositories/        # Acceso a datos
├── services/           # Lógica de negocio
│   ├── CompanyService.java
│   ├── DashboardService.java
│   ├── InvitationService.java
│   ├── MovementService.java
│   └── UserService.java
├── SecurityConfig.java         # Configuración de seguridad
├── UserAuthorizationInterceptor.java
└── FinanceTrackApplication.java
```

## ⚙️ Configuración y Instalación

### Prerrequisitos
- Java 21 o superior
- MySQL 8.0 o superior
- Maven 3.6 o superior
- Cuenta de Okta configurada

### Pasos de Instalación

1. **Clonar el repositorio:**
```bash
git clone https://github.com/haroldpc258/FinanceTrack.git
cd FinanceTrack
```

2. **Configurar base de datos:**
    - Crear una base de datos MySQL
    - Configurar las credenciales en `application.properties`

3. **Configurar Okta:**
    - Configurar `okta.oauth2.issuer` y `okta.oauth2.client-id` en las properties

4. **Ejecutar la aplicación:**
```bash
./mvnw spring-boot:run
```

5. **Acceder a la aplicación:**
    - URL: `http://localhost:8080`
    - La aplicación redirigirá a Okta para autenticación

## 🌟 Funcionalidades Principales

- ✅ **Autenticación OAuth2** con Okta
- ✅ **Multi-tenancy** por empresa
- ✅ **Control de acceso** basado en roles
- ✅ **Dashboard interactivo** con métricas en tiempo real
- ✅ **Gestión completa** de usuarios y empresas
- ✅ **Registro de movimientos** financieros
- ✅ **Reportes y estadísticas** detalladas
- ✅ **Sistema de invitaciones** para nuevos usuarios
- ✅ **Interfaz web responsive** con Thymeleaf
- ✅ **Auditoría** de transacciones con timestamps

## 🔧 Configuración de Desarrollo

Para desarrollo local, asegúrate de:
1. Configurar las variables de entorno para Okta
2. Tener MySQL ejecutándose localmente
3. Usar el perfil de desarrollo en Spring

## 📄 Licencia

Proyecto desarrollado como parte de la formación en Análisis y Desarrollo de Software en el SENA.

## 🤝 Contribuciones

Para contribuir al proyecto:
1. Fork del repositorio
2. Crear una branch para tu feature
3. Commit de los cambios
4. Push a la branch
5. Crear un Pull Request

