# Incidencias de integración y estabilización

## Resumen

| Estado | Críticas | Altas | Medias | Bajas |
|---|---:|---:|---:|---:|
| Abiertas | 0 | 0 | 0 | 0 |
| Cerradas | 0 | 1 | 5 | 0 |

| Módulo | Incidencias |
|---|---:|
| Datos y flujo de ventas / E2E | 2 |
| Paginación | 1 |
| SPA y manejo de errores | 1 |
| Auditoría y permisos | 2 |

Se consideran cerradas las incidencias de código que tienen una corrección implementada. La actividad manual de demostración (ensayos, video y evidencia en el aula) está pendiente en los documentos de preparación; no se cuenta como incidencia de software.

## Registro

| ID y fecha | Módulo / severidad | Pasos para reproducir | Esperado / obtenido | Causa raíz | Corrección | Commit | Prueba que evita la regresión |
|---|---|---|---|---|---|---|---|
| INC-01 · 2026-11-12 | Ventas y datos E2E · media | Ejecutar el E2E en una base de desarrollo que no tenga los nombres fijos que el test asumía. | Esperado: la prueba prepara sus datos. Obtenido: falla al no encontrar el cliente/producto de ejemplo. | La prueba dependía de fixtures no garantizados. | Seleccionar producto activo con stock y crear cliente temporal único por ejecución; desactivarlo al terminar. | `c9acdae` | E2E de venta dinámico en `e2e/flujo-venta.spec.ts`. |
| INC-02 · 2026-11-12 | Formulario/confirmación de venta · media | Intentar confirmar una venta sin recorrer el botón de revisión. | Esperado: validar y mostrar confirmación. Obtenido: el test buscaba confirmar un diálogo que todavía no estaba abierto. | El script omitía el paso funcional intermedio. | El E2E pulsa “Revisar venta”, comprueba el diálogo y luego confirma. | `c9acdae` | Flujo E2E de venta con confirmación explícita. |
| INC-03 · 2026-11-13 | Paginación de ventas · alta | Consultar una página de ventas con volumen alto y observar el trabajo del servicio. | Esperado: Oracle limita filas a la página y cuenta resultados. Obtenido: el servidor cargaba toda la lista y la cortaba en memoria. | El controlador/servicio usaba una lista completa antes de mapear página. | `Pageable`, consulta paginada y `count(*)`; validar rango y lista de campos de orden. | `922ca4c` | `e2e/paginacion.spec.ts` verifica página 2, URL y filtros; medición API en el informe. |
| INC-04 · 2026-11-13 | SPA / expiración de sesión · media | Forzar un 401 durante el envío y dejar que el listado lea `rxResource.value()`. | Esperado: volver al login sin errores secundarios. Obtenido: Angular reportaba lectura de valor de recurso sin éxito. | El estado de recurso fallido no tenía valor disponible. | Leer `value()` solo cuando `hasValue()` y conservar la página anterior durante carga. | `116a171` | Flujo E2E simula 401 durante venta y comprueba `returnUrl`. |
| INC-05 · 2026-11-15 | SPA / Auditoría · media | Iniciar como admin e ir a `/auditoria`; probar además como vendedor. | Esperado: admin ve tabla y filtros; vendedor no ve el menú ni abre la ruta. Obtenido antes del cambio: la SPA no tenía vista de auditoría aunque el API existía. | La auditoría se había implementado solo en el backend. | Añadir página paginada con filtros URL, `USUARIOS_ADMINISTRAR` en ruta y menú, manteniendo protección backend. | `e0b3a63` (frontend). | E2E-12 valida filtros y evento; E2E-13 verifica menú y acceso denegado. |
| INC-06 · 2026-11-15 | Backend / eventos auditables · media | Registrar un cliente o eliminar una categoría elegible y buscar su evento en Auditoría. | Esperado: evento de alta/eliminación tras confirmar la transacción. Obtenido antes del cambio: no existían esos dos eventos de negocio. | Solo se publicaban eventos de venta, precio y producto. | Publicar `CLIENTE_REGISTRADO` y `CATEGORIA_ELIMINADA`; el listener persiste con `AFTER_COMMIT`. El detalle de alta no incluye PII. | `b0f6141` (backend). | E2E-12 crea un cliente temporal y comprueba el evento desde la SPA; la colección Postman incluye alta y consulta. |

## Inspección de intermitencias

La configuración de Playwright deja `retries: 0`, ejecuta con un worker y conserva `trace.zip` en fallos. No se registran reintentos como solución. Las tres corridas de 2026-11-15 pasaron completas (ver [resultados de pruebas](resultados-pruebas.md)).
