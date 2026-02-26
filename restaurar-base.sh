#!/bin/bash

DUMP="bancotestedouglas.backup"
DUMP_AUDITORIA="cerurbAuditoria.backup"
CONTAINER_POSTGRES="postgres-cerurb"
DB="bancotestedouglas"
DB_AUDITORIA="CerurbProAuditoria"

docker container cp $DUMP_AUDITORIA $CONTAINER_POSTGRES:/tmp/
docker container cp $DUMP $CONTAINER_POSTGRES:/tmp/
##docker container exec -it $CONTAINER_POSTGRES createdb -U postgres $DB
docker container exec -it $CONTAINER_POSTGRES pg_restore --clean -U postgres -d $DB /tmp/$DUMP
docker container exec -it $CONTAINER_POSTGRES pg_restore --clean -U postgres -d $DB_AUDITORIA /tmp/$DUMP_AUDITORIA
echo "Restauração concluída!"
