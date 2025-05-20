#!/bin/bash

read -p "Introduce la IP y puerto del broker (ej: 155.210.154.200:1099): " HOSTPORT

cd bin || { echo "No se encontró el directorio bin"; exit 1; }

FLAGS="-Djava.security.manager=default -Djava.security.policy=java.policy"

java $FLAGS Productor "$HOSTPORT"