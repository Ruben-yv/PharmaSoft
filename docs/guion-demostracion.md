# Guion de demostración PharmaSoft (10 minutos)

Ensayar dos veces con cronómetro. Mantener abierta la versión de producción servida por Nginx (`http://localhost:8081`) y tener el backend en perfil `dev`, Oracle y Redis activos. No mostrar secretos ni el token en Swagger.

| Tiempo | Bloque | Qué mostrar y decir |
|---|---|---|
| 0:00–1:00 | Contexto | Presentar el problema de la botica, los perfiles admin/vendedor/almacenero y el diagrama `arquitectura.png`. Explicar SPA Angular, API Spring Boot, Oracle y Redis. |
| 1:00–4:30 | Flujo principal | Iniciar como vendedor; seleccionar cliente, producto y cantidad; revisar y confirmar; mostrar el comprobante, la venta y el stock reducido. |
| 4:30–6:00 | Seguridad | Mostrar menú del vendedor, intentar `/auditoria` directamente y obtener acceso denegado; mostrar el 401 de token inválido y el 403 de auditoría desde la API. |
| 6:00–7:30 | Volumen y rendimiento | Mostrar productos paginados (5.000+), cambio de página reflejado en la URL, consulta paginada de Oracle, métricas Actuator y caché Redis. No inferir mejoras de rendimiento sin comparar mediciones. |
| 7:30–8:30 | Auditoría | Buscar la venta recién creada en Auditoría; comparar ID, usuario y `requestId` con la venta y el log del backend. |
| 8:30–10:00 | Calidad | Ejecutar `npx playwright test`; enseñar el resumen de pruebas e incidencias cerradas. Cerrar indicando las decisiones técnicas y límites conocidos. |

## Ensayos

Registrar los dos ensayos reales. No completar tiempos o incidencias sin ejecutarlos.

| Ensayo | Fecha | Duración | Desvíos y ajuste aplicado |
|---|---|---|---|
| 1 | Pendiente de realizar | — | — |
| 2 | Pendiente de realizar | — | — |

## Respaldo

Grabar la demo completa, máximo 10 minutos, y guardar el archivo en `docs/evidencias/s14-estabilizacion/`. Enlazar el video aquí después de subirlo a un almacenamiento accesible al docente: **pendiente de grabar/subir**.

Si falla un servicio: detener la demo en ese bloque, usar las capturas del repositorio como respaldo, explicar qué componente falló y no presentar una respuesta simulada como ejecución en vivo.
