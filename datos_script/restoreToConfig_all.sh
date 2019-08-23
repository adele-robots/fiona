#!/bin/bash

#Este script realiza una labor de mantenimiento para resetear a una configuración por defecto toma un parámetro
#$1 es la configuración que se desea restaurar de entre las que se encuentran en /datos/nfs/configpresets (actualmente base, rebbecca y facetracker)
#un ejemplo de llamada sería: /datos/script/restoreToConfig_all.sh rebbecca

for dir in /datos/nfs/users/private/*
do
	FULLNAME=$(basename $dir)
	EXT=$(echo $FULLNAME | sed 's/^.*\.//')
	NAME=$(basename $dir .${EXT})
	
if [[ "$EXT" != "0jbpm" ]]; then

	#COMIENZO DE LA SUSTITUCIÓN/CREACIÓN DEL ARCHIVO
	#BORRAMOS EL SIMBÓLICO
	/datos/script/restorePreset.sh $NAME $1


fi

done
