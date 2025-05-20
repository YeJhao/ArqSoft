#!/bin/bash

read -p "Introduce el host y puerto para el broker (ej: 155.210.154.200:1099): " HOSTPORT

cd bin || { echo "No se encontró el directorio bin"; exit 1; }

FLAGS="-Djava.security.manager=default -Djava.security.policy=java.policy"

PORT="${HOSTPORT##*:}"

pkill -f rmiregistry
rmiregistry "$PORT" &

while ! nc -z 127.0.0.1 "$PORT"; do
    sleep 1
done

java $FLAGS BrokerImpl "$HOSTPORT"
