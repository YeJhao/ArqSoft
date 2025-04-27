#!/bin/bash

RUTA="/tmp/rmi_$USER"
MAQUINA="201"
MAQUINA_BROKER="200"

# Compilar
cd "$RUTA"
chmod +x *.sh
./compilar.sh

# Ejecutar servidores de servicios
FLAGS="-Djava.security.manager=default -Djava.security.policy=../java.policy"
cd bin
pkill -f rmiregistry
rmiregistry 1099 &
while ! nc -z 127.0.0.1 1099; do
    sleep 1
done
java $FLAGS InventarioImpl  155.210.154.$MAQUINA:1099 155.210.154.$MAQUINA_BROKER:1099 & sleep 3
java $FLAGS DiccionarioImpl 155.210.154.$MAQUINA:1099 155.210.154.$MAQUINA_BROKER:1099

