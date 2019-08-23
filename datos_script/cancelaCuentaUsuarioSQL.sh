#!/bin/bash
#### Defino los parametros de conexión a la BD mysql
# $1 email usuario

declare -a idsAvatares


ERRORS=0
sql_host='localhost'
slq_usuario='root'
sql_password='Aio0000'
sql_database='fionadb'

### Se monta los parámetros de conexión
sql_args="-h $sql_host -u $slq_usuario -p$sql_password -D $sql_database -s --skip-column-names -e"

### Comprobamos que el usuario cuya cuenta va a cancelarse existe en la BBDD
existeEmail=$(mysql $sql_args "SELECT COUNT(CN_USUARIO) FROM  usuario WHERE DC_EMAIL = '$1'")

#echo "El resultado de la consulta es:"${existeEmail}

#Si el usuario no existe lanzamos un error y salimos del script
if [ $existeEmail -eq 0 ]
then
	ERRORS=1
	exit $ERRORS
else
	#Si el usuario existe primero marcamos su cuenta como cancelada
	$(mysql $sql_args "UPDATE usuario SET FL_CANCELADA='1' WHERE DC_EMAIL='$1';")
	
	#Recuperamos el CN_USUARIO que se corresponde con el email pasado como parámetro
	idUsuario=$(mysql $sql_args "SELECT CN_USUARIO FROM usuario WHERE DC_EMAIL='$1';")
	#Ahora buscamos los ids de los avatares del usuario
	idsAvatares=$(mysql $sql_args "SELECT CN_AVATAR FROM avatar WHERE CN_USUARIO=$idUsuario;")

	#Para cada avatar del usuario, se marca FL_Activo a 0
	for idAvatar in ${idsAvatares[*]}
	do
		$(mysql $sql_args "UPDATE avatar SET FL_ACTIVO='0' WHERE CN_AVATAR=$idAvatar;")
		#Calculamos el md5 del usuario-avatar
		md5=$(echo -n "$1$idAvatar" | md5sum | awk '{print $1}')
		#Borramos la carpeta cuyo nombre coincide con ese md5
		#echo "rm -r /datos/nfs/users/private/$md5"
		rm -r /datos/nfs/users/private/$md5
		#Borramos archivos de personajes
		#Raquel
		#echo "rm /adele/dev/workspace/ApplicationData/Scene/raquel/*$md5*.*"
		rm /adele/dev/workspace/ApplicationData/Scene/raquel/*$md5*.*
		#Anna
		#echo "rm /adele/dev/workspace/ApplicationData/Scene/anna/*$md5*.*"
		rm /adele/dev/workspace/ApplicationData/Scene/anna/*$md5*.*
		#Elvira
		#echo "rm /adele/dev/workspace/ApplicationData/Scene/theGirl/*$md5*.*"
		rm /adele/dev/workspace/ApplicationData/Scene/theGirl/*$md5*.*
	done
fi

exit $ERRORS
