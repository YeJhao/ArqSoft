#!/bin/bash

RUTA="/tmp/rmi_$USER"
MAQUINA_CLIENT="202"
MAQUINA_BROKER="200"

# Compilar
cd "$RUTA"
chmod +x *.sh
./compilar.sh

# Ejecutar cliente
FLAGS="-Djava.security.manager=default -Djava.security.policy=../java.policy"
cd bin
java $FLAGS Cliente 155.210.154.$MAQUINA_BROKER:1099
