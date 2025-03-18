#!/bin/bash

#-----------Comprobar-número-parámetros----------
if [ $# -eq 0 ]; then
    echo "Uso: $0 --server | --client"
    exit 1
fi

#--------------------Compilar--------------------
javac --enable-preview -d bin *.java

#--------------------Ejecutar--------------------
if [ "$1" == "--server" ]; then
    java --enable-preview -cp bin practica3.CollectionImpl
elif [ "$1" == "--client" ]; then
    java --enable-preview -cp bin practica3.Cliente
else
    echo "Opción no válida: $1"
    echo "Uso: $0 --server | --client"
    exit 1
fi
