# Defensa técnica: preguntas y respuestas

Usar estas respuestas como guía de estudio y explicarlas con palabras propias durante la sustentación.

1. **¿Por qué paginar en el servidor? ¿Qué consultas ejecuta Oracle?** La API pide solo la ventana solicitada y hace una consulta de conteo para total de páginas; la SPA no descarga miles de filas para descartarlas. Se ve en `VentaRepository.buscarPaginado`, `ProductoRepository` y sus servicios paginados.
2. **¿Cómo se evita una venta a medias y una auditoría falsa?** `VentaServiceImpl.registrar` valida cliente/productos y stock dentro de `@Transactional`; el descuento condicional y el guardado se confirman juntos. El evento se publica en esa transacción y `AuditoriaListener` lo persiste en `AFTER_COMMIT`.
3. **¿Qué sucede si vence el token a mitad de una venta?** El interceptor adjunta el JWT. Un 401 cierra la sesión, limpia `sessionStorage` y manda a `/login` con `returnUrl`; se vuelve a autenticar el usuario y el flujo solicita de nuevo los datos antes de confirmar.
4. **¿Por qué invalidar caché de productos al vender?** La venta descuenta stock; `@CacheEvict` evita servir el inventario anterior desde Redis.
5. **¿Qué diferencia hay entre log, columna de auditoría y bitácora?** El log explica solicitudes y diagnóstico con `requestId`; las columnas guardan quién creó/modificó una fila; la bitácora guarda cada operación relevante y su entidad.
6. **¿Cómo evalúas la estabilidad de un E2E? ¿Qué hiciste con la prueba inestable?** Se repite la suite sin reintentos y se inspecciona la traza si falla. Los errores se convierten en incidencias reproducibles y la causa se corrige; no se oculta con `waitForTimeout` o retries.
7. **¿Cómo proteges Auditoría?** `SecurityConfig` requiere `USUARIOS_ADMINISTRAR`; `permisoGuard` protege la ruta y el menú se filtra con el mismo permiso. El backend vuelve a validar el JWT, por lo que esconder el enlace no reemplaza autorización.
8. **¿Qué información se excluye de la auditoría?** Contraseñas, tokens, encabezados de autorización y datos personales que no hacen falta. Se guardan campos mínimos y el detalle está limitado.
9. **¿Cómo se correlaciona una acción con logs?** El `RequestIdFilter` genera/propaga el ID, lo pone en MDC y devuelve `X-Request-Id`; el evento copia ese mismo valor. Se comparan la respuesta, la fila y la línea de log.
10. **¿Qué hace Playwright y qué no prueba?** Prueba rutas, interacción de la SPA, respuestas reales de API y reglas integradas con datos de prueba. No reemplaza revisión de seguridad, pruebas de carga ni una prueba manual de la operación real.

## Archivos para explicar en el código

- Backend: `service/imp/VentaServiceImpl.java`, `service/imp/ProductoServiceImpl.java`, `auditoria/AuditoriaListener.java`, `service/imp/AuditoriaServiceImpl.java`, `controller/AuditoriaController.java`, `config/SecurityConfig.java`.
- Frontend: `features/auditoria/pages/auditoria-list/`, `features/auditoria/services/auditoria-service.ts`, `core/guards/permiso-guard.ts`, `layout/sidebar/sidebar.ts`, `core/interceptors/error-interceptor-interceptor.ts`.
- Pruebas: `pharma-frontend/e2e/` y `pharma-frontend/playwright.config.ts`.
