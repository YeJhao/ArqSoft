//-------------------------------------------------------------------------------------------
// File:   README.txt
// Author: Jorge Soria Romeo (872016) y Jiahao Ye (875490)
// Date:   20 de abril de 2025
// Coms:   Fichero que contiene la documentación de la práctica 3 de Arquitectura Software.
//-------------------------------------------------------------------------------------------

El sistema creado consiste en la implementación del patrón arquitectural "Broker", se dispone
de un cliente interactivo por terminal, un par de servidores con funciones que proporcionan y
el broker que sirve como intermediario entre el cliente y el servidor.



# Compilación y ejecución

Preparar todo:

1)   scp -r practica3b a<NIP>@central.cps.unizar.es:/home/a<NIP>/arquitectura_software             Copiar código fuente y scripts en central
2)   ssh a<NIP>@central.cps.unizar.es                                                              Conectarse a central
3)   cd arquitectura_software/practica3b/scripts                                                   Acceder a arquitectura_software/practica3b/scripts
4)   chmod u+x *.sh                                                                                Dar permisos de ejecución a los scripts
5)   ./gestionar_maquinas.sh (Introducir por entrada estándar ON)                                  Encender las máquinas de L102 que vamos a emplear
6)   ./copiar.sh                                                                                   Copiar el código fuente en cada una de las máquinas que emplearemos

Broker (lab102-200):

1)   ssh a<NIP>@central.cps.unizar.es
2)   ssh lab102-200
3)   cd /tmp/rmi_a<NIP>
4)   ./lanzar_broker.sh

Servidores (lab102-201):

1)   ssh a<NIP>@central.cps.unizar.es
2)   ssh lab102-201
3)   cd /tmp/rmi_a<NIP>
4)   ./lanzar_servidores.sh

Cliente (lab102-202):

1)   ssh a<NIP>@central.cps.unizar.es
2)   ssh lab102-202
3)   cd /tmp/rmi_a<NIP>
4)   ./lanzar_servidores.sh
