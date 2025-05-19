//-------------------------------------------------------------------------------------------
// File:   README.txt
// Author: Jorge Soria Romeo (872016) y Jiahao Ye (875490)
// Date:   29 de abril de 2025
// Coms:   Fichero que contiene la documentación de la práctica 4 de Arquitectura Software.
//-------------------------------------------------------------------------------------------

# INTRODUCCIÓN
El código fuente presentado implementa los componentes de un broker de mensajes
(Message-Oriented Middleware, MOM).La práctica presenta una versión básica y una
serie de adiciones que se pueden añadir para llegar a una versión avanzada.

Hemos añadido:
 1. Listado de colas existentes
 2. Reconocimiento (ACK) de mensajes
      2.1 Cuando un consumidor procesa el mensaje envía un ACK. El mensaje
          no se elimina de la cola hasta que se recibe el ACK correspondiente.
      2.2 Hay un tiempo de expiración para recibir el ACK, si no se recibe se
          reenvía (en nuestro caso 2 min)
 3. Fair dispatch
 4. Durabilidad (se guarda en disco cola de mensajes y mensajes pendientes de ACK)
 5. Máquinas diferentes



# Compilación y ejecución

1. Pasar el código fuente con scp a aNIP@lab000.cps.unizar.es
2. Encender las máquinas de lab102 de tu elección que vas a emplear
       
        >> cd scripts; chmod +x *.sh

        >> ./gestionar_maquinas.sh
               Selecciona una máquina del lab102 (ej: 200): 
               >> 200
               Selecciona opción ON o OFF para lab102-200:
               >> ON

        >> ./gestionar_maquinas.sh
               Selecciona una máquina del lab102 (ej: 200): 
               >> 201
               Selecciona opción ON o OFF para lab102-201:
               >> ON

        >> ./gestionar_maquinas.sh
               Selecciona una máquina del lab102 (ej: 200): 
               >> 202
               Selecciona opción ON o OFF para lab102-202:
               >> ON

3. Copiar el código fuente a /tmp/brokerMsg_$USER de esas máquinas
        
        >> ./copiar.sh
               Introduce las máquinas separadas por espacios (ej: lab102-200 lab103-201 una_ip_cualquiera): 
               >> lab102-200 lab102-201 lab102-202

4. (desde lab000) ssh lab102-200;
   cd /tmp/brokerMsg_a<NIP>
   ./lanzar_broker.sh


5. (desde lab000) ssh lab102-201
   cd /tmp/brokerMsg_a<NIP>
   ./lanzar_productor.sh

6. (desde lab000) ssh lab102-202
   cd /tmp/brokerMsg_a<NIP>
   ./lanzar_consumidor.sh
