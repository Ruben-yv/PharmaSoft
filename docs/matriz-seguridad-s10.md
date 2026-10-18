# Matriz de pruebas de seguridad - Actividad autónoma S10

**Sistema:** PharmaBackend / sysventas  
**Rama:** `feature/seguridad-pruebas-varillas`  
**Alcance:** 16 casos base, 4 casos propios y 8 casos de gestión de contraseñas, roles y estado de usuarios.

> Los resultados de API quedan pendientes de ejecución manual. Al preparar esta rama, Oracle no tenía un listener disponible en `localhost:1522` (ORA-12541); por eso no se inventan códigos, cuerpos ni evidencias. La compilación sí se completó con Maven.

| ID | Grupo | Usuario / token | Petición y datos | Resultado esperado (HTTP y mensaje) | Resultado obtenido | Estado | Evidencia |
|---|---|---|---|---|---|---|---|
| A-01 | Autenticación | Admin, vendedor, almacén y supervisor | POST `/auth/login` para cada cuenta de prueba | 200; devuelve token, roles y permisos; supervisor reúne VENDEDOR y ALMACENERO | Pendiente de ejecutar con API | Pendiente | `A-01_admin.png`, `A-01_vendedor.png`, `A-01_almacen.png`, `A-01_supervisor.png` |
| A-02 | Autenticación | Usuario inexistente y vendedor | POST `/auth/login` con usuario inexistente y luego contraseña incorrecta | 401 y el mismo mensaje genérico en ambos intentos | Pendiente de ejecutar con API | Pendiente | `A-02_postman.png` |
| A-03 | Autenticación | Usuario inactivo | POST `/auth/login` con contraseña correcta de la cuenta deshabilitada | 401; mismo mensaje genérico, sin revelar el estado o existencia de la cuenta | Pendiente de ejecutar con API | Pendiente | `A-03_postman.png` |
| A-04 | Autenticación | Vendedor | POST `/auth/login` con username `VENDEDOR` | Regla elegida: el username no distingue mayúsculas; login 200 y username normalizado en la respuesta | Pendiente de ejecutar con API | Pendiente | `A-04_postman.png` |
| A-05 | Autenticación (propio) | Vendedor autenticado | GET `/auth/me` y revisar respuesta | 200; perfil contiene roles/permisos y nunca expone `password` ni su hash | Pendiente de ejecutar con API | Pendiente | `A-05_postman.png` |
| T-01 | Token | Sin prefijo Bearer | GET `/auth/me`, encabezado Authorization con el token sin `Bearer ` | 401, `Debe iniciar sesión para acceder a este recurso` | Pendiente de ejecutar con API | Pendiente | `T-01_postman.png` |
| T-02 | Token | Token del vendedor modificado | Alterar un carácter del payload y GET `/auth/me` | 401, `El token no es válido` | Pendiente de ejecutar con API | Pendiente | `T-02_postman.png` |
| T-03 | Token | Token vigente anterior | Cambiar el secreto de desarrollo, reiniciar, enviar token anterior | 401, `El token no es válido`; restaurar el secreto y reiniciar después | Pendiente de ejecutar con API | Pendiente | `T-03_postman.png` |
| T-04 | Token | Token emitido con expiración de 1 minuto | Emitir token, esperar expiración y GET `/auth/me` | 401, `El token ha expirado. Inicie sesión nuevamente` | Pendiente de ejecutar con API | Pendiente | `T-04_postman.png` |
| T-05 | Token | Token válido de prueba | Decodificar localmente su payload | `sub`, `authorities`, `iat` y `exp` correctos; sin contraseñas ni otros datos sensibles | Pendiente de ejecutar con API | Pendiente | `T-05_payload.png` (ocultar token completo) |
| T-06 | Token (propio) | Sin encabezado Authorization | GET `/auth/me` sin encabezado | 401 con el mensaje genérico de inicio de sesión | Pendiente de ejecutar con API | Pendiente | `T-06_postman.png` |
| P-01 | Permisos | Vendedor | POST, PUT y DELETE `/categorias` | 403 en las tres operaciones | Pendiente de ejecutar con API | Pendiente | `P-01_postman.png` |
| P-02 | Permisos | Almacenero | GET `/clientes` y POST `/ventas` | 403 en ambas operaciones | Pendiente de ejecutar con API | Pendiente | `P-02_postman.png` |
| P-03 | Permisos | Vendedor y almacenero | GET `/reportes/productos-mas-vendidos` con cada token | 403 para ambos roles | Pendiente de ejecutar con API | Pendiente | `P-03_postman.png` |
| P-04 | Permisos | Admin | GET de categorías, productos, clientes, ventas y reportes | 2xx en cada módulo, usando datos existentes | Pendiente de ejecutar con API | Pendiente | `P-04_postman.png` |
| P-05 | Permisos (propio) | Sin token | GET `/productos` | 401; distinguir usuario no autenticado (401) de usuario autenticado sin permiso (403) | Pendiente de ejecutar con API | Pendiente | `P-05_postman.png` |
| U-01 | Usuarios | Admin | POST `/usuarios`: contraseña `abcdefgh`; repetir con `rolIds: []` | 400 en ambos casos por reglas de contraseña y rol requerido | Pendiente de ejecutar con API | Pendiente | `U-01_postman.png` |
| U-02 | Usuarios | Admin | Crear cuenta para empleado ya vinculado (409); luego para María sin cuenta (201); revisar respuesta y tablas | 409 por relación empleado-usuario única; luego 201; respuesta sin password, hash BCrypt y fila `usuario_roles` | Pendiente de ejecutar con API y SQL | Pendiente | `U-02_postman.png`, `U-02_sql.png` |
| C-01 | CORS | Origen SPA `http://localhost:4200` | OPTIONS previo a GET protegido, después enviar GET con token | OPTIONS 200 y GET 200 o 401 por token; nunca error CORS | Pendiente de ejecutar desde SPA | Pendiente | `C-01_network.png` |
| C-02 | CORS (propio) | Origen no permitido | OPTIONS con `Origin: http://localhost:9999` | No se devuelve `Access-Control-Allow-Origin` para ese origen | Pendiente de ejecutar con API | Pendiente | `C-02_postman.png` |
| B-01 | Contraseña | Vendedor | PUT `/auth/password` con actual correcta y nueva `NuevaClave2026` | 204; login con nueva contraseña funciona; el hash almacenado cambia; el token existente sigue válido hasta expirar | Pendiente de ejecutar con API | Pendiente | `B-01_postman.png`, `B-01_hash.png` |
| B-02 | Contraseña | Vendedor | PUT `/auth/password` con contraseña actual incorrecta | 409, `La contraseña actual no es correcta`; no cambia el hash | Pendiente de ejecutar con API | Pendiente | `B-02_postman.png` |
| B-03 | Contraseña | Vendedor | PUT `/auth/password` con nueva igual a la actual | 409, `La nueva contraseña debe ser distinta de la actual` | Pendiente de ejecutar con API | Pendiente | `B-03_postman.png` |
| B-04 | Contraseña | Vendedor | PUT `/auth/password` con nueva menor de 8 caracteres o sin número/letra | 400 con errores de validación; no cambia el hash | Pendiente de ejecutar con API | Pendiente | `B-04_postman.png` |
| B-05 | Roles | Admin; usuario objetivo de pruebas | PUT `/usuarios/{id}/roles` con `rolIds` nuevos; luego GET protegido con el token previamente emitido al objetivo | 200; reemplaza relaciones; respuesta trae roles/permisos nuevos; siguiente petición con el mismo token aplica permiso actualizado | Pendiente de ejecutar con API y SQL | Pendiente | `B-05_postman.png`, `B-05_sql.png` |
| B-06 | Roles | Admin | PUT con conjunto vacío; luego usuario inexistente y rol inexistente | 400 para conjunto vacío; 404 para usuario o rol que no existe | Pendiente de ejecutar con API | Pendiente | `B-06_postman.png` |
| B-07 | Roles | Admin sobre su propio ID | PUT `/usuarios/{idAdmin}/roles` retirando ADMIN | 409, `Un ADMIN no puede quitarse a sí mismo el rol ADMIN`; relaciones intactas | Pendiente de ejecutar con API | Pendiente | `B-07_postman.png` |
| B-08 | Estado de usuario | Admin y cuenta de prueba | PATCH estado de otro usuario a false; probar token existente y login; probar desactivación propia y usuario inexistente | 200 al desactivar otro; token existente y login del desactivado responden 401 `El usuario está deshabilitado`/mensaje genérico; 409 al desactivarse a sí mismo; 404 si no existe | Pendiente de ejecutar con API | Pendiente | `B-08_postman.png` |

## Criterios de cierre

- Ejecutar cada petición con las cuentas de prueba de la migración V4 y capturar método, URL, encabezados no secretos, estado HTTP y respuesta.
- No incluir tokens reales en el repositorio ni mostrar el token completo en capturas.
- Restaurar `app.jwt.secret` y `app.jwt.expiracion-minutos` después de T-03/T-04. Reactivar cuentas y restaurar roles/contraseñas de prueba al finalizar B-01/B-05/B-08.
- No anotar `Pasa` o `Falla` hasta comparar la respuesta real con la expectativa.
