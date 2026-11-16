# Lista de verificación previa a la demostración

## Servicios

- [ ] Docker Desktop está activo; Oracle responde en `localhost:1522`.
- [ ] Redis está activo y la aplicación usa el perfil `dev`.
- [ ] Backend activo en `http://localhost:8080`; `GET /actuator/health` responde `UP`.
- [ ] Nginx/SPA de producción está activo en `http://localhost:8081` (o preparar `ng serve` como plan alternativo).

## Datos y sesión

- [ ] Ejecutar solo en la base local preparada; validar el cliente de demostración y el producto con stock suficiente.
- [ ] Confirmar que admin, vendedor y almacenero existen. Obtener contraseñas desde la configuración local segura; no escribirlas en este archivo ni mostrarlas en capturas.
- [ ] Preparar una venta y verificar cómo se restablecerá el stock antes de repetirla.
- [ ] Cerrar sesiones y preparar pestañas para SPA, Swagger, auditoría y logs.

## Evidencia y plan alternativo

- [ ] Capturas organizadas en `docs/evidencias/s14-estabilizacion/`.
- [ ] Informe Playwright actualizado y con trazas de fallos revisadas.
- [ ] Video de respaldo grabado y enlace verificado.
- [ ] Tener capturas de respaldo de Inicio, Productos, ventas, auditoría y seguridad por si falla un servicio.
- [ ] Ocultar tokens, passwords, datos personales y secretos de configuración.
