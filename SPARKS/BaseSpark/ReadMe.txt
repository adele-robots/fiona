Notas sobre el sistema de componentes:
======================================


Todos los componentes deben satisfacer estos requisitos:

1.- Cada componente se implementa mediante una clase al menos, la clase principal.

2.- La clase principal hereda de "Component", definido en Component.h

3.- Adem�s mediante herencia m�ltiple debe heredar de las clases abstractas que 
	definen cada uno de los interfaces provistos. As� un puntero a la clase principal
	implementa las interfaces provistas.

4.- La clase principal, para cada interfaz requerido, incluir� un miembro cuyo tipo
	sea un puntero a otro componente que implemente la interfaz.
	
5.- El componente no compilar� a menos que la llamada a su constructor llame al 
	correspondiente constructor base Componente::Componente().

6.- El componente no compilar� si no se implementa initializeRequiredInterfaces. 

	a)	Su comportamiento debe ser rellenar los miembros para interfaces requeridos 
		desritos en (4). Para cada interfaz requerido se llamar� a 
		requestRequiredInterface<...>(). 
		
	b)	El sistema de componentes llamar� a initializeRequiredInterfaces() durante 
		la carga de la red de componentes, tras de lo cual los punteros definidos en (4) 
		apuntar�n a implementaciones v�lidas de los interfaces requeridos.
		
	c)	initializeRequiredInterfaces() se implementar� en el fichero de cabecera de la
		clase principal del componente.
	
7.- Un mismo componente puede proveer la implementaci�n de un interfaz requerido por 
	m�ltiples componentes. Por contra un interfaz requerido por un componente concreto 
	no puede ser provisto por varios componentes a la vez.
	
8.- Cada interfaz se define en un .H de �mbito global:
	<working-copy-root>\Programcion\Include.
	El .H de la implementaci�n del componente residir� en el directorio del proyecto
	de la soluci�n del componente.

9.- Los plugins son elementos desplegables que albergan las impolementaciones de los
	componentes y se cargan en tiempo de ejecuci�n. Incluyen la factor�a de los 
	componentes. Se organizan como DLLs con dependencias expl�citas a los proyectos
	de biblioteca est�tica en los que se implementa cada uno de los componentes que
	el plugin fabrica.

10.-Para cada nuevo plugin, habr� que a�adir una dependencia expl�cita para que 
	el proyecto Run dependa de los plugins (proyecto, bot�n dcho., deps. proy).

