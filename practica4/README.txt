//-------------------------------------------------------------------------------------------
// File:   README.txt
// Author: Jorge Soria Romeo (872016) y Jiahao Ye (875490)
// Date:   29 de abril de 2025
// Coms:   Fichero que contiene la documentación de la práctica 4 de Arquitectura Software.
//-------------------------------------------------------------------------------------------

# INTRODUCCIÓN
El código fuente presentado implementa los componentes de un broker de mensajes
(Message-Oriented Middleware, MOM). La práctica presenta una versión básica y una serie de
adiciones que se pueden añadir para llegar a una versión avanzada.

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

1. Pasar el código fuente a las máquinas de tu elección

2. Dar permisos de ejecución
>> chmod +x scripts/*.sh

3. Compilar en las distintas máquinas
>> ./scripts/compilar.sh

4. Lanzar el broker
>> ./scripts/lanzar_broker.sh

5. Lanzar el productor
>> ./scripts/lanzar_productor.sh

6. Lanzar el consumidor
>> ./scripts/lanzar_consumidor.sh

Al lanzar el script lanzar_broker.sh, se te pedirá vía terminal
   -> ip:puerto donde se ejecutará el broker.

Al lanzar el script lanzar_productor.sh, se te pedirá vía terminal
   -> ip:puerto de la maquina donde se está ejecutando el broker

Al lanzar el script lanzar_consumidor.sh, se te pedirá vía terminal
   -> ip:puerto de la maquina donde se está ejecutando el broker
   -> nombre del consumidor (a tu elección)
   -> nombre de la cola de mensajes a la que deseas suscribirte
       -> Deberás antes crear una nueva cola usando el menú del productor
          Por eso es mejor ejecutar antes lanzar_productor.sh

Se pueden lanzar múltiples consumidores y productores.
