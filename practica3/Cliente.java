package practica3;
// TODO : imports necesarios
public class Cliente
{
public static void main ( String [] args ) {
// TODO : Fijar el directorio donde se encuentra el java . policy
if ( System . getSecurityManager () == null ) {
// TODO : Crear administrador de seguridad
}
try
{
// Paso 1 - Obtener una referencia al objeto servidor creado anteriormente
// Nombre del host servidor o su IP. Es dó nde se buscar á al objeto remoto
String hostname = " IPremotehost "; // se puede usar "IP: puerto "
Collection server = ( Collection ) Naming . lookup ("//"+ hostname + "/ MyCollection ") ;
// Paso 2 - Invocar remotamente los metodos del objeto servidor :
// TODO : obtener el nombre de la colecci ón y el nú mero de libros
// TODO : cambiar el nombre de la coleccion y ver que ha funcionado
}
catch ( Exception ex )
{
System . out . println ( ex );
}
}
}