#!/bin/bash

MAQUINAS=("lab102-200" "lab102-202" "lab102-203")
RUTA="/tmp/rmi_$USER"

# Copiamos los ficheros fuente y scripts en las máquinas que usaremos

cd ..

for HOST in "${MAQUINAS[@]}"; do
    echo "Subiendo archivos a $HOST:$RUTA..."
    ssh "$HOST" "rm -rf $RUTA && mkdir -p $RUTA"
    scp src/java.policy src/*.java scripts/*.sh "$HOST:$RUTA"
done

echo "¡Archivos copiados!"