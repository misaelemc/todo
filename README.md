# ToDo Project

Tecnologias Utilizadas

1.- Dagger 2 para proveer las dependencias de las clases al rededor del app
2.- ViewBinding para agilizar el uso de vistas
3.- Navigation Component para facilitar la navegacion entre fragmentos
4.- Lifecycle ViewModel, para el uso de live data y comunicacion entre el viewModel y la vista
5.- Firebase Storage para el almacenamiento de las imagenes 
6.- Firebase Database para almacenar las tareas en la base de datos
7.- Firebase Auth para el login y resgistro de usuarios
8.- Firebase ui database para la sincronizacion de la base de datos con los items a mostrar en el app

Pendientes:

Al ser tan poco tiempo para el desarrollo del app y todas las caracteristicas que se solicitaron quedaron estas cosas por tener: 

1.- Compartir las task a traves de apps
2.- Pruebas unitarias (Para esto tenia pensado utilizar mockito y junit pero no me dio el tiempo)
3.- Pruebas instrumentadas (Para esto iba a utilizar espresso)
4.- Geolocalizacion de la tarea (Esto hubiera sido simplemente agregar la lat y lng del usuario en ese momento cuando se crea la task)
5.- Agregar multiples imagenes (Por el momento se agrega una sola imagen, se puede mejorar y habilitar el uso de multiples images y poder tomar desde la camara, dejo un link de repo donde estoy trabajando con cameraX https://github.com/misaelemc/cameraX)

Decisiones: 

Al principio queria tomar la decision de crear la app modular, para mostrar mi experiencia trabajando con multi modules, pero tuve que deshacer el trabajo ya que hubiera tardado mas en tener listo el proyecto y separando responsabilidades, podria haber implementado una arquitectura para proyectos grandes donde se podria crear demo por cada feature que existe en el app ayudaria en tiempos de compilacion y velocidad de desarrollo a futuro implementando un patron de api-impl-wiring-demo 4 modulos, que es bastante interesante para proyectos grandes. La decision de hacerlo en un solo modulo vino mas por el tiempo que tenia para hacerla, elegi la arquitectura MVVM ya que me preguntaron si sabia usarla, y quise dejarla demostrado, 

El proyecto es basico, pero se deja abierto para hacerle mas mejoras. 
