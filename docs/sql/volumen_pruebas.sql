-- Solo para el esquema local de desarrollo o pruebas. Nunca ejecutar en producción.
-- Ejecutar después de aplicar V5__auditoria.sql.
-- Este archivo no forma parte de Flyway y se puede omitir/recrear según el entorno.

insert into productos (
    nombre, precio, stock, estado, categoria_id, fecha_creacion, creado_por
)
select 'Producto de prueba ' || lpad(level, 5, '0'),
       round(dbms_random.value(1, 200), 2),
       trunc(dbms_random.value(0, 500)),
       true,
       (select min(id) from categorias),
       systimestamp,
       'carga'
from dual
connect by level <= 5000;

insert into ventas (fecha, total, estado, cliente_id, creado_por)
select systimestamp - numtodsinterval(dbms_random.value(0, 365), 'DAY'),
       round(dbms_random.value(5, 500), 2),
       'REGISTRADA',
       (select min(id) from clientes),
       'carga'
from dual
connect by level <= 20000;

-- Estas ventas no tienen detalle: sirven para medir la consulta de lista,
-- pero no para validar reportes ni el flujo de venta.
commit;
