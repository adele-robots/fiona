Parametrización de la aplicación mediante archivos de configuración. 
Se usa GNU libconfig.

Las rutas de ficheros, dentro del archivo de configuración, deben ser con slashes tipo 
unix ('/'), y la raíz virtual será el directorio de datos de la aplicación.

getFilePath realiza la traducción a rutas tipo win32, a partir de algún mecanismo para
obtener la ruta del directorio de datos de la aplicación en tiempo de ejecución. El 
mecanismo actual es a través de la variable de entorno PSISBAN_APPLICATION_DATA.