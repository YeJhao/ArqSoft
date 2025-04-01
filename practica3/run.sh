#!/bin/bash

#-----------Comprobar-número-parámetros----------
if [ $# -eq 0 ]; then
    echo "Uso: $0 --server | --client"
    exit 1
fi

#--------------------Compilar--------------------
for fich in *.java; do
    class_fich="${fich%.java}.class"
    if [ ! -f "$class_fich" ] || [ "$fich" -nt "$class_fich" ]; then
        javac "$fich"
    fi
done

#--------------------Ejecutar--------------------
if [ "$1" == "--server" ]; then
    pkill -f rmiregistry
    rmiregistry 32000 &
    while ! nc -z 127.0.0.1 32000; do
        sleep 1
    done
    java CollectionImpl
elif [ "$1" == "--client" ]; then
    java Cliente
else
    echo "Opción no válida: $1"
    echo "Uso: $0 --server | --client"
    exit 1
fi
