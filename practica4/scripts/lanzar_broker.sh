#!/bin/bash

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
java $FLAGS ConsumidorImpl "$NOMCONSUMER" "$NOMCOLA" "127.0.0.1:1099"