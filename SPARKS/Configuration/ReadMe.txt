Parametrizaci�n de la aplicaci�n mediante archivos de configuraci�n. 
Se usa GNU libconfig.

Las rutas de ficheros, dentro del archivo de configuraci�n, deben ser con slashes tipo 
unix ('/'), y la ra�z virtual ser� el directorio de datos de la aplicaci�n.

getFilePath realiza la traducci�n a rutas tipo win32, a partir de alg�n mecanismo para
obtener la ruta del directorio de datos de la aplicaci�n en tiempo de ejecuci�n. El 
mecanismo actual es a trav�s de la variable de entorno PSISBAN_APPLICATION_DATA.