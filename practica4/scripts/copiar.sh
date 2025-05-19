#!/bin/bash

read -p "Introduce las máquinas separadas por espacios (ej: lab102-200 lab103-201 una_ip_cualquiera): " -a MAQUINAS
RUTA="/tmp/brokerMsg_$USER"

cd ..

for HOST in "${MAQUINAS[@]}"; do
    echo "Subiendo archivos a $HOST:$RUTA..."
    ssh "$HOST" "rm -rf $RUTA && mkdir -p $RUTA"
    scp src/java.policy src/*.java scripts/*.sh "$HOST:$RUTA"
done

echo "¡Archivos copiados!"