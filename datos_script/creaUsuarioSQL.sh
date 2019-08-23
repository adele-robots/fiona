#!/bin/sh
#### Defino los parametros de conexión a la BD mysql
# $1 email usuario
# $2 password usuario
# $3 número de usuarios concurrentes
# $4 id unidad de tiempo
# $5 id de la resolución
# $6 alta disponibilidad (1 o 0)
# $7 nombre de usuario del usuario
# $8 nombre del avatar
ERRORS=0
sql_host='localhost'
slq_usuario='root'
sql_password='Aio0000'
sql_database='fionadb'

### Se monta los parámetros de conexión
sql_args="-h $sql_host -u $slq_usuario -p$sql_password -D $sql_database -s --skip-column-names -e"

### Sentencia para insertar el usuario en AIOXXXX
existeEmail=$(mysql $sql_args "SELECT COUNT(CN_USUARIO) FROM  usuario WHERE DC_EMAIL = '$1'")

#echo "El resultado de la consulta es:"${existeEmail}

#Buscar el hosting que se corresponde con los parámetros
cnHosting=$(mysql $sql_args "SELECT CN_HOSTING FROM hosting WHERE CN_UNIT=$4 AND NU_USERS=$3 AND DC_RESOLUTION='$5' AND FL_HighAvailability='$6'")

if [ -z "$cnHosting" ]
then

ERRORS=1
exit $ERRORS

fi

#sleep 1s
#echo "El resultado de la consulta de hosting es:"${cnHosting}
if [ $existeEmail -eq 0 ]
then
	#Insertar nuevo usuario en la BBDD
	idUsuario=$(mysql $sql_args "INSERT INTO usuario(DC_EMAIL,DC_PASSWORD,DC_USERNAME,CN_ROLE,NU_NUMPROCESOS) VALUES ('$1','$2','$7',2,$3);SELECT LAST_INSERT_ID();")
	#idUsuario=$(mysql $sql_args "SELECT LAST_INSERT_ID()")
	#echo "El identificador del usuario insertado es: "${idUsuario}

	if [ -z "$idUsuario" ]
	then
		ERRORS=2
		exit $ERRORS
	fi

	idAvatar=$(mysql $sql_args "INSERT INTO avatar(CN_USUARIO,CN_HOSTING,FL_ACTIVO,FE_UPLOAD,DC_AVATAR) VALUES($idUsuario,$cnHosting,'1',now(),'$8');SELECT LAST_INSERT_ID()")
#echo "Finalizada inserción de usuario y avatar"

else

	#Buscar el usuario en la BBDD
	idUsuario=$(mysql $sql_args "SELECT CN_USUARIO FROM usuario WHERE DC_EMAIL = '$1'")

	if [ -z "$idUsuario" ]
        then
                ERRORS=3
                exit $ERRORS
        fi


	#Comprobar campo 'FL_Cancelada' del usuario
	flCancelada=$(mysql $sql_args "SELECT FL_CANCELADA FROM usuario WHERE DC_EMAIL = '$1'")

	#Si 'FL_Cancelada' está a 1, quiere decir que una cuenta cancelada está siendo reactivada,
	#por lo que debemos marcar 'FL_Cancelada' a 0
	if [ $flCancelada -eq 1 ]
	then
		$(mysql $sql_args "UPDATE usuario SET FL_CANCELADA='0' WHERE CN_USUARIO=$idUsuario;")	
	fi

	
	#Insertar el nuevo avatar para el usuario indicado
	idAvatar=$(mysql $sql_args "INSERT INTO avatar(CN_USUARIO,CN_HOSTING,FL_ACTIVO,FE_UPLOAD,DC_AVATAR) VALUES($idUsuario,$cnHosting,'1',now(),'$8');SELECT LAST_INSERT_ID();")
#	echo "Finalizada inserción del avatar para el usuario $1"

fi
echo $idAvatar
exit $ERRORS
