# Incidencias de integración y estabilización S14

## Estado al cierre

Se registraron cuatro incidencias durante la integración y las pruebas. Todas quedaron corregidas y la suite completa pasó tres veces seguidas: 21 de 21 ejecuciones, sin reintentos.

| ID | Módulo | Severidad | Descripción y causa | Corrección | Estado y commit |
|---|---|---|---|---|---|
| INC-01 | Ventas y datos de prueba | Media | La prueba inicial asumía que existían un cliente “Mamani” y el producto “Paracetamol”; esos datos no estaban presentes en el esquema local. | La prueba consulta un producto disponible de la primera página y crea un cliente temporal único, que desactiva al terminar. | Cerrada. `c9acdae` |
| INC-02 | Prueba end-to-end de venta | Media | El flujo intentaba confirmar sin abrir antes el resumen de venta. | Playwright pulsa “Revisar venta”, verifica el cuadro de confirmación y luego envía la operación. | Cerrada. `c9acdae` |
| INC-03 | Paginación de ventas | Alta | El controlador cortaba en memoria una lista de ventas que el servicio había cargado completa; el listado no limitaba el trabajo en Oracle. | La búsqueda usa `Pageable`, consulta paginada y `count(*)`, con validación de rango y lista permitida para ordenar. | Cerrada. `922ca4c` |
| INC-04 | Manejo de errores de carga | Media | Al responder 401 durante la prueba de vencimiento, `linkedSignal` leía el valor de un `rxResource` fallido y Angular imprimía un error en consola. | El listado solo lee `value()` cuando el recurso tiene un valor; conserva la página anterior durante carga y evita la lectura cuando falla. | Cerrada. `116a171` |

## Verificación posterior

- `npm run build`: compilación Angular finalizada correctamente.
- `npx playwright test --repeat-each=3`: 21 pruebas pasadas de 21, en 39,6 segundos.
- Flujo de venta: alta y selección de cliente temporal, reautenticación tras un 401 durante la confirmación, registro, bitácora con `requestId`, actualización del reporte y stock actualizado al consultar Productos.
- Seguridad: vendedor recibe 403 al consultar `/api/v1/auditoria`; el endpoint responde para administrador.
- Tamaño máximo: Productos responde 409 al solicitar `tamanio=500`.
- Backend: `mvnw -DskipTests package` finalizó con `BUILD SUCCESS`; la aplicación se ejecutó con perfil `dev` y Flyway registró el esquema en versión 5.

## Consideraciones de prueba

El script de volumen solo debe ejecutarse en el esquema local de desarrollo. Cada corrida exitosa del flujo E2E deja una venta de prueba y desactiva el cliente temporal para preservar la referencia de la venta. El script de carga rechaza una segunda ejecución si ya existen nombres `Producto de prueba`; no debe ejecutarse de nuevo sobre esta base cargada.

