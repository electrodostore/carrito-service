# 🛒 Carrito Service

## 📌 Descripción
Microservicio encargado de la gestión del carrito de compras dentro de ElectrodoStore. Permite crear carritos, agregar productos, modificar cantidades y realizar la compra final.

Este servicio actúa como **orquestador del flujo de compra**, integrando múltiples microservicios como clientes, productos y ventas.

---

## ⚙️ Tecnologías utilizadas

- Java + Spring Boot
- Spring Data JPA
- MySQL
- Spring Cloud (Eureka Client)
- OpenFeign
- Resilience4j (Circuit Breaker + Retry)

---

## 🧩 Responsabilidades

- Crear carritos asociados a un cliente
- Agregar productos al carrito
- Eliminar productos del carrito
- Modificar cantidades de productos
- Calcular total del carrito
- Ejecutar la compra del carrito
- Orquestar comunicación con otros microservicios

---

## 🗄️ Base de datos

Este servicio maneja su propia base de datos MySQL, siguiendo el patrón **Database per Service**, lo que garantiza independencia y desacoplamiento respecto a otros microservicios.

---
## 🔢️ Modelo de datos

El carrito está compuesto por:

- **🛍️ Productos** (snapshot embebido)
- **👤 Cliente** (snapshot embebido)
- **💰 Total calculado**
- **🔄 Estado del carrito** (CREATED, PURCHASED, etc.)

Se utilizan **snapshots** para evitar dependencias directas con otros servicios en tiempo de lectura.

### 📨 DTOs

| DTO | Uso                                       |
|---|-------------------------------------------|
| `CarritoResponseDto` | Salida — detalle completo del carrito     |
| `CarritoCreoadoResponseDto` | Salida — confirmación de creación y id    |
| `ProductoAgregarDto` | Entrada — agregar producto al carrito     |
| `ProductoCambiarCantidadDto` | Entrada — modificar cantidad              |
| `ProductoResponseDto` | Salida — datos del producto en el carrito |
| `ClienteResponseDto` | Salida — datos del dueño del carrito      |
---

## 🔗 Endpoints principales

```http
GET    /carritos
GET    /carritos/{carritoId}
POST   /carritos
POST   /carritos/{carritoId}/agregar-productos
DELETE /carritos/{carritoId}/eliminar-producto/{productoId}
PATCH  /carritos/{carritoId}/actualizar-cantidad-producto
POST   /carritos/comprar-carrito/{carritoId}
```

---

## 🔄 Integración con otros servicios

Este servicio se integra con:

- **🛍️ producto-service** → consulta de productos y validación de stock
- **👤 cliente-service** → validación de cliente
- **💳 venta-service** → registro de la compra

La comunicación se realiza mediante **Spring Cloud OpenFeign**.

---

## 🛡️ Resiliencia (Circuit Breaker + Retry)

La comunicación con **producto-service**, **cliente-service** y **venta-service** están protegidas mediante patrones de resiliencia utilizando **Resilience4j**:

- **Circuit Breaker** → Evita llamadas repetidas a un servicio caído
- **Retry** → Reintenta automáticamente en fallos transitorios
- **Fallback** → Proporciona una respuesta controlada en caso de error

### 🔁 Flujo de resiliencia

1. Se intenta consumir el servicio de productos, clientes o ventas
2. Si falla la integración, se realizan reintentos automáticos
3. Si el fallo persiste, se activa el **Circuit Breaker**
4. Se ejecuta el método **fallback**
5. Se lanza una excepción controlada (`ServiceUnavailable`)

### ⚠️ Fallback

Cuando alguno de los servicios no está disponible:

- Se registra un log de advertencia
- Se lanza una excepción de tipo infraestructura
- Se evita propagar errores internos al cliente

---

## 🔁 Estrategia clave

- Se filtran excepciones de dominio (`BusinessException`)
- Se propagan errores reales del negocio
- Solo errores técnicos generan `ServiceUnavailable`

Esto evita ocultar problemas reales bajo errores genéricos.

---

## ⚠️ Manejo de errores

Se implementa un manejador global con @RestControllerAdvice.

### Excepciones manejadas:
- `CarritoNotFoundException`
- `ProductoNotFoundException`
- `ClienteNotFoundException`
- `ProductoStockInsuficienteException`
- `CarritoPurchasedException`
- `ServiceUnavailable`

### Estructura de error:

```bash
{
  "timestamp": "2026-03-28T12:00:00",
  "status": 404,
  "error": "NOT_FOUND",
  "errorCode": "PRODUCT_NOT_FOUND",
  "mensaje": "Producto no encontrado"
}
```

---

## 🔍 Interpretación de errores (ErrorDecoder)

Se implementan ErrorDecoders personalizados para:

- Traducir errores HTTP en excepciones de dominio
- Interpretar `errorCode` provenientes de otros servicios

####  **Ejemplo:**

- `CLIENT_NOT_FOUND` → `ClienteNotFoundException`
- `PRODUCT_NOT_FOUND` → `ProductoNotFoundException`
- `PRODUCT_STOCK_INSUFICIENTE` → excepción específica

Esto permite mantener consistencia en el dominio sin depender de respuestas externas.

---

## 🎯 Flujo de compra (caso principal)
1. Se obtiene el carrito
2. Se valida el cliente
3. Se consultan productos
4. Se valida stock
5. Se calcula el total
6. Se envía la venta a venta-service
7. Se actualiza el estado del carrito a PURCHASED

---

## 🌐 Registro en Eureka

El servicio se registra automáticamente en Eureka Server, permitiendo su descubrimiento dinámico.

---

## ▶️ Ejecución local

> ⚠️ Requiere que **Config Server** y **Eureka Server** estén corriendo antes de iniciar este servicio.

**Con Maven**
```bash
# Corre en el puerto 8282
mvn spring-boot:run
```

**Con Docker**
```bash
docker build -t carrito-service .
```

---

## 🔌 Configuración de red

| Propiedad | Valor                  |
|---|------------------------|
| Puerto interno | `8282`                 |
| Acceso externo | ❌ Solo vía API Gateway |

---

## 💡 Decisiones de diseño

- Uso de snapshots para desacoplar servicios
- Uso de DTOs para controlar la entrada y salida de datos
- Separación clara entre lógica de negocio e integración
- Manejo centralizado de errores
- Resiliencia en llamadas externas
- Interpretación de errores externos a dominio propio

---

## 🚀 Mejoras futuras
- Implementación de autenticación (JWT / OAuth2)
- Uso de eventos (Kafka/RabbitMQ) para compras
- Cache para productos
- Observabilidad (tracing + logs distribuidos)

---
