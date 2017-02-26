# BQ_TEST
Prueba aplicacion BQ

Durante el desarrollo de la app segui estos pasos:

Primero la creacion de la actividad de Login tras modificar la que nos da android por defecto
  Esta ha sido la actividad que mas quebraderos de cabeza me ha dado debido a que obtuve una api key de evernote que no me permitia 
  recuperar las notas de mi usuario (era una api key basic y para recuperarlas se necesita una full)
  Devido a estos inconvenientes y hasta que entendi que era problema de la que decidi ir haciendo la actividad principal para no
  ir perdiendo tiempo
  
Desarrollo de la actividad principal
  Tras definir lo que iba a contener, empece por añadirle notas de prueba al no tener acceso aun a mi cuenta pero implementar los metodos   que una vez obtenido el token me permitirian sacar los datos que necesitaba para mi clase NoteBQ.
  Una vez obtenidas las notas de mi cuenta implemente en esta clase la ordenacion por titulo o por fecha (por defecto viene por fecha       desde el servidor)
  
Desarrollo de clases auxiliares
  En un principio pense en utilizar fragment para la creacion y visualizacion de notas pero considere que al disponer de poco tiempo seria   mejor utilizar activities.
  
 Faltaria por hacer la escritura por pantalla que nunca lo he investigado. 
  
