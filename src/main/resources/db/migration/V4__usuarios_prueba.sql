-- =====================================================================
-- V4: usuarios de prueba de la sesion 10
--
-- Deja los cinco usuarios de la guia con sus contrasenas conocidas y un
-- empleado (Maria) sin usuario para probar el alta.
--   admin      / Admin2026*
--   vendedor   / Vendedor2026*
--   almacen    / Almacen2026*
--   supervisor / Supervisor2026*   VENDEDOR + ALMACENERO
--   inactivo   / Inactivo2026*     VENDEDOR, deshabilitado
-- =====================================================================

-- Contrasenas de los usuarios que ya existen (hash BCrypt)
update usuarios set password = '$2a$10$NNNgOCCajIjVDGhg3u7sHOwLlLrVMdIALH/64AmPlFmmpiG1yinGa' where username = 'admin';
update usuarios set password = '$2a$10$EXmYQXoQUBed/ZQHfPVA1.eTtSKOdgQpsz3/ZODq.za2hyGfV40We' where username = 'vendedor';
update usuarios set password = '$2a$10$ciG8Sz1SQTaIK5Cau0G/5u1LqIPQvcujkc5hPRaGO/Fd7ChrFBkFK' where username = 'almacen';

-- Empleados nuevos (Maria queda sin usuario)
insert into empleados (dni, nombres, apellidos, cargo, email, telefono, estado, fecha_creacion)
select '43445566', 'Ana', 'Torres Vilca', 'Jefa de tienda', 'atorres@pharmasoft.pe', '951000444', true, systimestamp
from dual where not exists (select 1 from empleados where dni = '43445566');

insert into empleados (dni, nombres, apellidos, cargo, email, telefono, estado, fecha_creacion)
select '44556677', 'Jorge', 'Apaza Huanca', 'Vendedor', 'japaza@pharmasoft.pe', null, true, systimestamp
from dual where not exists (select 1 from empleados where dni = '44556677');

insert into empleados (dni, nombres, apellidos, cargo, email, telefono, estado, fecha_creacion)
select '45667788', 'Maria', 'Huaman Torres', 'Cajera', 'mhuaman@pharmasoft.pe', null, true, systimestamp
from dual where not exists (select 1 from empleados where dni = '45667788');

-- Usuarios nuevos
insert into usuarios (username, password, estado, empleado_id, fecha_creacion)
select 'supervisor', '$2a$10$j8xXh/XBHa/3jkViqNkMs.oZtvG6kL/QyF49kDAnnDrqktpT75sJK', true,
       (select id from empleados where dni = '43445566'), systimestamp
from dual where not exists (select 1 from usuarios where username = 'supervisor');

insert into usuarios (username, password, estado, empleado_id, fecha_creacion)
select 'inactivo', '$2a$10$oi118hH.Nfj8jY0/MOIWJ.i34FhMDm2jy0/OpPMjSMUPh1.W/C6De', false,
       (select id from empleados where dni = '44556677'), systimestamp
from dual where not exists (select 1 from usuarios where username = 'inactivo');

-- Roles de los usuarios nuevos (la supervisora tiene dos)
insert into usuario_roles (usuario_id, rol_id)
select u.id, r.id from usuarios u, roles r
where u.username = 'supervisor' and r.nombre in ('VENDEDOR', 'ALMACENERO')
  and not exists (select 1 from usuario_roles x where x.usuario_id = u.id and x.rol_id = r.id);

insert into usuario_roles (usuario_id, rol_id)
select u.id, r.id from usuarios u, roles r
where u.username = 'inactivo' and r.nombre = 'VENDEDOR'
  and not exists (select 1 from usuario_roles x where x.usuario_id = u.id and x.rol_id = r.id);