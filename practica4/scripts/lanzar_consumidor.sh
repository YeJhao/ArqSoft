#!/bin/bash

read -p "Introduce la IP completa de la máquina del broker (ej: 155.210.154.200): " IP
read -p "Introduce el nombre del consumidor: " NOMCONSUMER
read -p "Introduce el nombre de la cola: " NOMCOLA 

RUTA="/tmp/brokerMsg_$USER"

# Compilar
cd "$RUTA"
chmod +x *.sh
./compilar.sh

# Ejecutar broker
FLAGS="-Djava.security.manager=default -Djava.security.policy=../java.policy"
cd bin
pkill -f rmiregistry
rmiregistry 1099 &
while ! nc -z 127.0.0.1 1099; do
    sleep 1
done
java $FLAGS ConsumidorImpl "$NOMCONSUMER" "$NOMCOLA" "$IP" 1099