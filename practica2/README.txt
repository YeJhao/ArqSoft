//-------------------------------------------------------------------------------------------
// File:   README.txt
// Author: Jorge Soria Romeo (872016) y Jiahao Ye (875490)
// Date:   17 de marzo de 2025
// Coms:   Fichero que contiene la documentación de la práctica 2 de Arquitectura Software.
//-------------------------------------------------------------------------------------------

El programa creado consiste en un editor de diagramas de clases, donde se pueden añadir,
borrar y crear asociaciones entre clases. Para añadir una clase se tiene que utilizar el botón
que se dispone en la ventana, se irán creando clases con identificadores de tipo: 0, 1, 2,...

Si se quiere borrar una clase, se tiene que hacer click derecho en el ratón sobre una clase.
No se podrá deshacer la eliminación y las siguientes clases que se añadan no tendrán nunca el
mismo identificador que el que se ha borrado.

Si se desea crear una asociación, se tiene que seleccionar una clase, dejando el cursor encima
de la clase y pulsando la tecla "s". Esto dejará la clase marcada en azul (indicando que está
seleccionado). Si ahora hacemos click izquierdo sobre el ratón y arrastramos el cursor a la
clase que se quiere asociar, se generará la asociación.



# Compilación y ejecución



Se proporcionan dos scripts que facilitan la compilación y ejecución del programa: "clean.sh"
y "run.sh", el primero elimina los ficheros ".class" que genera al compilar en Java y el segundo
realiza la compilación y ejecución.

- Para ejecutar el primer script:

./clean.sh

- Para ejecutar el segundo script:

./run.sh

Puede que sea necesario dar permisos de ejecución a los scripts, para poder ejecutar, para este
caso utilice el siguiente comando:

chmod u+x <fichero_script>

Por otro lado, si no quiere utilizar los scripts, puede realizarlo manualmente, de la siguiente
forma:

- Para compilar:

javac Application.java

Esto creará los ficheros ".class" necesario y posteriormente podrá ejecutar con:

java Application
