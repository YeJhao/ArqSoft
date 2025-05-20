#!/bin/bash

echo "Compilando código fuente"
mkdir -p bin
javac -d bin src/*.java > /dev/null 2>&1
cp src/java.policy bin/
echo "¡Compilación completada!"