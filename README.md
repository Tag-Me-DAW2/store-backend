# Tag-Me Store · backend

API REST de una tienda online: catálogo, usuarios, carrito, pedidos y pago con tarjeta. Es la mitad tienda del proyecto Tag-Me; cobra a través de la API del banco ([bank-backend](https://github.com/Tag-Me-DAW2/bank-backend)). Frontends: [store-client-frontend](https://github.com/Tag-Me-DAW2/store-client-frontend) y [store-admin-frontend](https://github.com/Tag-Me-DAW2/store-admin-frontend).

**Stack:** Java 21 · Spring Boot · Spring Data JPA · MariaDB · Flyway · password4j · Apache HttpClient 5 · JUnit, Mockito y Testcontainers.

## Arquitectura

```
controller   endpoints REST (públicos y de administración), modelos web y mappers
domain       modelos, servicios, excepciones y repositorios (interfaces)
persistence  entidades JPA, DAOs y repositorios que implementan los del dominio
integration  cliente HTTP genérico y servicio de pago contra la API del banco
web          filtros: sesión por token, login requerido, administrador y CORS
spring       configuración y manejo global de errores
```

Los repositorios son interfaces del dominio y la persistencia las implementa. El módulo `integration` aísla todo lo que habla con el banco, de modo que el pago se prueba con mocks sin depender de él.

## Autenticación

Sesiones con token: `POST /auth/login` crea una sesión guardada en base de datos (`tb_sessions`) y devuelve un token que se envía como `Authorization: Bearer <token>`. Un filtro lo resuelve a usuario, otro exige estar logueado en las rutas protegidas y otro limita `/admin/**` a administradores. Las contraseñas se guardan con hash (password4j).

## Endpoints

| Recurso | Rutas |
|---|---|
| `/auth` | `POST /login`, `POST /logout`, `GET`, `POST /verify-password` |
| `/users` | `GET`, `GET /{id}`, `POST`, `POST /customer`, `PUT /{id}`, `PUT /password/{id}`, `DELETE /{id}` |
| `/products` | `GET`, `GET /filter`, `GET /{id}`, `GET /total` |
| `/categories` | `GET`, `GET /{id}` |
| `/cart` | `GET /active/{userId}`, `PUT`, `POST /pay/credit-card` |
| `/orders` | `GET /user/{userId}` |
| `/admin/products` | `POST`, `PUT /{id}`, `DELETE /{id}` |
| `/admin/categories` | `POST`, `PUT /{id}`, `DELETE /{id}` |
| `/admin/orders` | `GET`, `GET /count`, `POST /{orderId}/retry` |

## Pago con tarjeta

El carrito es un pedido en estado `PENDING`. `POST /cart/pay/credit-card` lo pasa a `PROCESSING` (evita pagarlo dos veces) y llama al banco desde el módulo `integration`:

- Si el banco confirma, el pedido pasa a `PAYED` y se borran los datos de pago guardados.
- Si el banco no responde, el pedido se queda en `PROCESSING` y un administrador puede reintentarlo con `POST /admin/orders/{orderId}/retry`.
- Si falla por otro motivo, el pedido pasa a `CANCELLED`.

## Arrancar en local

Necesita Java 21, una MariaDB con la base `db_tagme` y el backend del banco en `localhost:8081`. El perfil por defecto es `dev`; Flyway crea las tablas al iniciar.

```bash
./mvnw spring-boot:run
```

## Tests

```bash
./mvnw test
```

Los tests de persistencia usan Testcontainers, así que necesitan Docker.
