#!/bin/bash

read -p "Introduce la IP completa de la máquina del broker (ej: 155.210.154.200): " IP

RUTA="/tmp/brokerMsg_$USER"

# Compilar
cd "$RUTA"
chmod +x *.sh
./compilar.sh

# Ejecutar cliente
FLAGS="-Djava.security.manager=default -Djava.security.policy=../java.policy"
cd bin
java $FLAGS Productor "${IP}:1099"
