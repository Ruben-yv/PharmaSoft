# Auditoría de PharmaSoft

## Propósito y alcance

La bitácora permite reconstruir quién realizó una operación relevante, qué entidad afectó, cuándo ocurrió y desde qué solicitud se ejecutó. Solo los usuarios con `USUARIOS_ADMINISTRAR` pueden consultar `/api/v1/auditoria`. La SPA aplica el mismo permiso en la ruta y el menú; el backend sigue siendo la autoridad de seguridad.

## Eventos registrados

| Acción | Cuándo se registra | Detalle guardado |
|---|---|---|
| `VENTA_REGISTRADA` | Después de confirmar la transacción que guarda la venta y descuenta stock | Total e ítems; el usuario, ID de venta y `requestId` van en columnas separadas |
| `PRECIO_MODIFICADO` | Al guardar un cambio efectivo de precio | Precio anterior y nuevo |
| `PRODUCTO_ELIMINADO` | Al dar de baja un producto | Nombre del producto |
| `CLIENTE_REGISTRADO` | Después de confirmar el alta del cliente | Texto genérico, sin DNI, correo, teléfono ni dirección |
| `CATEGORIA_ELIMINADA` | Después de confirmar la eliminación permitida de una categoría | Nombre de categoría |
| `LOGIN_EXITOSO` | Cuando las credenciales permiten autenticar al usuario | Usuario autenticado; no guarda credenciales ni token |

Un intento de inicio de sesión fallido no se inserta como evento de la bitácora porque todavía no existe una identidad autenticada y confiable. El rechazo queda en el log de seguridad asociado al `requestId`; nunca se registran la contraseña ni el cuerpo de credenciales.

## Columnas de auditoría, bitácora y logs

- **Columnas de auditoría JPA:** `fechaCreacion`, `fechaModificacion`, `creadoPor` y `modificadoPor` pertenecen a la fila del producto, cliente o categoría y muestran quién creó o modificó el registro.
- **Bitácora (`auditoria_eventos`):** una fila por operación de negocio, con fecha, usuario, acción, entidad, ID de entidad, detalle acotado y `requestId`. Tiene paginación y filtros por usuario, acción y fechas.
- **Logs:** mensajes operativos y de diagnóstico. El filtro HTTP escribe el método, ruta, estado, duración y `requestId`; los logs no sustituyen la bitácora.

## Por qué se guarda después del commit

Los servicios publican `EventoAuditoria` dentro de la transacción de negocio. `AuditoriaListener` lo procesa con `@TransactionalEventListener(AFTER_COMMIT)`, abre una transacción nueva para persistir la bitácora y solo lo hace si la operación de negocio confirmó. Así, una venta fallida o revertida no deja un evento que afirme que se registró.

## Datos que nunca se guardan

No se guardan contraseñas, hashes de contraseña, JWT, encabezados `Authorization`, DNI, correos, teléfonos, direcciones ni cuerpos completos de solicitudes. El campo `detalle` se limita a información mínima y se trunca a 500 caracteres. Los logs usan IDs y `requestId`; no deben incluir secretos ni datos personales innecesarios.

## Trazabilidad de la venta de demostración

Completar esta tabla durante la grabación de la demostración con una venta realmente ejecutada y pegar las tres capturas en `docs/evidencias/s14-estabilizacion/`.

| Dato | Valor de la demostración |
|---|---|
| ID de venta y `creadoPor` en Ventas | Completar durante la demo |
| `VENTA_REGISTRADA` en la bitácora | Completar durante la demo |
| `requestId` común | Completar durante la demo |
| Línea del log `POST /api/v1/ventas -> 201` | Completar durante la demo |

La correlación se comprueba comparando el encabezado `X-Request-Id` devuelto por el POST, la columna `requestId` del evento y el MDC de los logs del backend.

## Código relacionado

- `src/main/java/pe/edu/epeu/sysventas/auditoria/EventoAuditoria.java`
- `src/main/java/pe/edu/epeu/sysventas/auditoria/AuditoriaListener.java`
- `src/main/java/pe/edu/epeu/sysventas/service/imp/AuditoriaServiceImpl.java`
- `src/main/java/pe/edu/epeu/sysventas/controller/AuditoriaController.java`
- `src/main/java/pe/edu/epeu/sysventas/config/SecurityConfig.java`
- Frontend: `pharma-frontend/src/app/features/auditoria/` y `pharma-frontend/src/app/app.routes.ts`
