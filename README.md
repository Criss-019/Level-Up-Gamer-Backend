# Level-Up Gamer - Backend API REST (Spring Boot + MySQL + JWT)

Backend de servicios REST para la plataforma de comercio electrónico de productos gamer **Level-Up Gamer**. Desarrollado con **Java 21**, **Spring Boot 4.1.3**, persistencia relacional en **MySQL 8.0** con **Spring Data JPA**, seguridad sin estado con **Spring Security 6 + JWT (JSON Web Tokens)** y documentación interactiva mediante **Swagger UI / OpenAPI 3.0**.

---

## 👥 Integrantes del Equipo (Duoc UC)
* **Cristián Carvajal**
* **Sebastián Castillo**

* **Repositorio Backend:** https://github.com/Criss-019/Level-Up-Gamer-Backend.git  
* **Repositorio Frontend:** https://github.com/Criss-019/Level-Up-Gamer.git  

---

## 🚀 Arquitectura y Tecnologías del Backend

* **Lenguaje:** Java 21 LTS
* **Framework:** Spring Boot 4.1.3
* **Capa de Persistencia:** Spring Data JPA (Hibernate ORM)
* **Base de Datos:** MySQL Server 8.0 / MariaDB en puerto alternativo **`3307`** (Base de datos: `level_up_gamer_db`)
* **Seguridad y Autenticación:** Spring Security 6 con JSON Web Tokens (**JJWT 0.12.6**) sin estado y algoritmo HMAC-SHA256
* **Documentación Interactiva:** Springdoc OpenAPI 3.0 (`springdoc-openapi-starter-webmvc-ui` v2.6.0)
* **Gestor de Construcción:** Apache Maven con Maven Wrapper (`./mvnw`)

### Estructura en Capas (Separación de Responsabilidades)

```
com.duoc.level_up_gamer_backend/
├── config/             # Configuración de OpenAPI / Swagger UI (SwaggerConfig.java)
├── controller/         # Controladores REST versionados (/api/v1/...)
├── model/              # Entidades relacionales JPA (Usuario, Rol, Categoria, Producto, Boleta, DetalleBoleta)
├── repository/         # Interfaces Spring Data JPA para acceso a MySQL
├── security/           # Filtros de autenticación JWT (JwtAuthFilter, JwtService, SecurityConfig)
├── service/            # Lógica de negocio transaccional (@Transactional) y validaciones
└── DataInitializer.java# Poblado automático de roles, usuarios base y productos gamer
```

---

## 📊 Modelo de Datos Relacional y Dependencias

El modelo relacional implementado en MySQL garantiza integridad referencial estricta:

```
[CATEGORIA] <─── [PRODUCTO] <─── [DETALLE_BOLETA] ───> [BOLETA] ───> [USUARIO] ───> [ROL]
```

1. **`PRODUCTO` depende de `CATEGORIA`:** Clave foránea `categoria_id`.
2. **`USUARIO` depende de `ROL`:** Clave foránea `rol_id` (`ROLE_ADMIN`, `ROLE_VENDEDOR`, `ROLE_CLIENTE`).
3. **`BOLETA` depende de `USUARIO`:** Clave foránea `usuario_id` que identifica al cliente que efectuó la compra.
4. **`DETALLE_BOLETA` depende de `BOLETA` y `PRODUCTO`:** Entidad asociativa muchos a muchos que congela cantidad y precio unitario pactado al momento de la venta.

---

## 🔐 Cuentas de Acceso Preconfiguradas

Al iniciar la aplicación, `DataInitializer.java` verifica y genera automáticamente las cuentas de prueba con contraseñas encriptadas con **BCrypt**:

| Rol | Correo Electrónico (Email) | Contraseña | Vistas y Permisos Asociados |
| :--- | :--- | :--- | :--- |
| **`ROLE_ADMIN`** | `admin@levelup.com` | `admin123` | **Acceso Total:** CRUD de Productos, Categorías, Órdenes y Gestión de Usuarios y Roles. |
| **`ROLE_VENDEDOR`** | `vendedor@levelup.com` | `vendedor123` | **Panel de Ventas:** Visualización de catálogo y detalle de stock, auditoría y detalle de órdenes/boletas. **Sin acceso a usuarios ni respaldos.** |
| **`ROLE_CLIENTE`** | `cliente@levelup.com` | `cliente123` | **Tienda Pública:** Catálogo público, carrito y emisión de boletas personales. |

---

## 📡 Endpoints de la API REST (`/api/v1`)

### 1. Autenticación (`/api/v1/auth`)
* `POST /api/v1/auth/login`: Autentica credenciales y emite token JWT firmado con claims de rol.
* `POST /api/v1/auth/registro`: Registra nuevos usuarios asignando por defecto `ROLE_CLIENTE`.
* `GET /api/v1/auth/me`: Valida token y retorna información del usuario autenticado (*Requiere Token*).

### 2. Catálogo de Productos (`/api/v1/productos`)
* `GET /api/v1/productos`: Consulta pública del catálogo de productos con stock y categoría.
* `GET /api/v1/productos/{id}`: Detalle de un producto específico.
* `POST /api/v1/productos`: Creación de nuevo producto (*Exclusivo ROLE_ADMIN*).
* `PUT /api/v1/productos/{id}`: Modificación de producto existente (*Exclusivo ROLE_ADMIN*).
* `DELETE /api/v1/productos/{id}`: Eliminación física de producto (*Exclusivo ROLE_ADMIN*).

### 3. Categorías (`/api/v1/categorias`)
* `GET /api/v1/categorias`: Listar todas las categorías (Consolas, Videojuegos, Accesorios, Sillas).
* `POST /api/v1/categorias`: Registrar nueva categoría (*Exclusivo ROLE_ADMIN*).

### 4. Boletas y Órdenes de Compra (`/api/v1/boletas`)
* `POST /api/v1/boletas`: Procesa la compra enviada desde el carrito, valida stock, descuenta unidades de la tabla `producto` en MySQL y emite boleta y detalle en una transacción atómica (`@Transactional`).
* `GET /api/v1/boletas`: Listar historial de órdenes (*Autorizado para ROLE_ADMIN y ROLE_VENDEDOR*).
* `GET /api/v1/boletas/{id}`: Detalle de boleta con desglose de ítems, cantidades y cliente.

### 5. Administración de Usuarios (`/api/v1/usuarios`)
* `GET /api/v1/usuarios`: Lista completa de usuarios registrados (*Exclusivo ROLE_ADMIN - 403 Forbidden para Vendedores y Clientes*).
* `GET /api/v1/usuarios/{id}`: Detalle de usuario.
* `PUT /api/v1/usuarios/{id}`: Modificar datos y cambiar rol (promover a vendedor o admin).
* `DELETE /api/v1/usuarios/{id}`: Dar de baja cuenta de usuario.

---

## 🛠️ Puesta en Marcha del Backend

### 1. Configurar la Base de Datos MySQL (Puerto 3307)
Asegurarse de tener el servicio MySQL iniciado en el puerto **`3307`** (en XAMPP `my.ini` con `port=3307`). La base de datos `level_up_gamer_db` y sus tablas se generan automáticamente al iniciar gracias a `createDatabaseIfNotExist=true` y `ddl-auto: update`.

### 2. Compilar y Ejecutar

#### Opción A: Con Maven Wrapper (Recomendada en desarrollo)
```bash
# Windows
.\mvnw.cmd spring-boot:run

# Linux / macOS
./mvnw spring-boot:run
```

#### Opción B: Ejecutar el JAR precompilado
```bash
./mvnw clean package

java -jar target/level-up-gamer-backend-0.0.1-SNAPSHOT.jar
```

#### Opción C: Con el lanzador automatizado
Hacer doble clic en `INICIAR_PROYECTO.bat` en la raíz de la entrega.

---

## 📖 Documentación Interactiva Swagger UI

Una vez levantado el servidor, la documentación OpenAPI 3.0 con soporte para **Bearer JWT Token** está disponible en:

🔗 **Swagger UI:** `http://localhost:8089/swagger-ui/index.html`  
🔗 **OpenAPI JSON:** `http://localhost:8089/v3/api-docs`

> **Instrucciones para autorizar en Swagger:**
> 1. Iniciar sesión en `POST /api/v1/auth/login` con `admin@levelup.com` / `admin123`.
> 2. Copiar el token recibido en la respuesta.
> 3. Hacer clic en el botón verde **Authorize** (candado arriba a la derecha) y pegar el token.
> 4. Los endpoints privados (`POST /api/v1/productos`, etc.) se ejecutarán con autorización exitosa.
