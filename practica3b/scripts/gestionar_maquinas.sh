#!/bin/bash

echo "¿Desea encender [ON] o apagar [OFF] las máquinas del laboratorio?"
read -p "> " OPCION

case "${OPCION^^}" in
    ON)
        echo "Encendiendo máquinas..."
        /usr/local/etc/wake -y lab102-200
        /usr/local/etc/wake -y lab102-205
        /usr/local/etc/wake -y lab102-202
        /usr/local/etc/wake -y lab102-207
        sleep 5; echo "¡Máquinas encendidas!"
        ;;
    OFF)
        echo "Apagando máquinas..."
        /usr/local/etc/shutdown.sh -y lab102-200
        /usr/local/etc/shutdown.sh -y lab102-205
        /usr/local/etc/shutdown.sh -y lab102-202
        /usr/local/etc/wake -y lab102-207
        echo "¡Máquinas apagadas!"
        ;;
    *)
        echo "Opción inválida. Usa ON o OFF."
        ;;
esac