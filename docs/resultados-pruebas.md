# Resultados de pruebas end-to-end y API

## Playwright

Comando requerido:

```powershell
npx playwright test --repeat-each=3 --reporter=list,html
```

| Fecha | Versión probada (commit) | Casos por corrida | Corridas | Aprobadas | Fallidas | Duración total | Informe HTML |
|---|---|---:|---:|---:|---:|---|---|
| 2026-11-15 | Front `2dab0b2`; backend `b0f6141` | 13 | 3 | 39 | 0 | 53,9 s | [Informe HTML archivado](evidencias/s14-estabilizacion/playwright-report/index.html) |

Las pruebas se configuran con `retries: 0` y sin `waitForTimeout`. Conservar una traza `trace.zip` de cualquier fallo y registrarla en [incidencias](incidencias.md) antes de corregirla.

## Cobertura declarada

La suite de 13 casos comprueba autenticación y `returnUrl`, creación de cliente de prueba, flujo de venta y confirmación, stock, caché, auditoría con `requestId`, precio, reportes, paginación URL, filtros, permisos, token alterado, venta vacía y venta con stock insuficiente. El listado completo se conserva en `pharma-frontend/e2e/`. Cada una de las tres corridas ejecutó los 13 casos una vez; Playwright se configuró con cero retries.

## Postman

La colección [PharmaBackend](postman/PharmaBackend.postman_collection.json) contiene solicitudes con scripts de comprobación para `200`, `201`, `400`, `401`, `403`, `404` y `409`, usando variables de colección para tokens y datos temporales. Ejecutarla solo en desarrollo: crea un cliente y una venta de prueba, y reduce stock.

| Fecha | Entorno | Casos ejecutados | Resultado | Captura/enlace |
|---|---|---|---|---|
| Pendiente de ejecutar desde Postman | Desarrollo local | 200/201/400/401/403/404/409 disponibles en la colección | No se afirma una corrida Newman/Postman | `evidencias/s14-estabilizacion/` |
