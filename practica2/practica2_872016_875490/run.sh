#!/bin/bash
./clean.sh
javac -d bin Application.java
java -cp bin Application
