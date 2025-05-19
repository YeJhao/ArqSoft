#!/bin/bash

while true; do
    read -p "Selecciona una máquina del lab102 (ej: 200): " NUMERO
    if [[ "$NUMERO" =~ ^[0-9]{3}$ ]]; then
        break
    else
        echo "Número de máquina inválido. Intentalo de nuevo."
    fi
done

MAQUINA="lab102-$NUMERO"

while true; do
    read -p "Selecciona opción ON o OFF para $MAQUINA: " OPCION
    OPCION_UPPER="${OPCION^^}"
    if [[ "$OPCION_UPPER" == "ON" ]]; then
        echo "Encendiendo máquina ${MAQUINA}..."
        /usr/local/etc/wake -y "$MAQUINA" &> /dev/null
        break
    elif [[ "$OPCION_UPPER" == "OFF" ]]; then
        echo "Apagando máquina ${MAQUINA}..."
        /usr/local/etc/shutdown.sh -y "$MAQUINA" &> /dev/null
        break
    else
        echo "Respuestas válidas: ON o OFF."
    fi
done