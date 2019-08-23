#!/bin/bash

#Itero sobre todos los directorios de private
for dir in /datos/nfs/users/private/*
do
	FULLNAME=$(basename $dir)
	EXT=$(echo $FULLNAME | sed 's/^.*\.//')
	NAME=$(basename $dir .${EXT})
	
#Si no es una carpeta .0jbpm
if [[ "$EXT" != "0jbpm" ]]; then

	#Comparo el numero de caracteres de la carpeta
	#con 32, que es el numero caracteres de un md5
	len=`echo $NAME | wc -c`
	len=`expr $len - 1`
	if [[ "$len" == "32" ]]; then 
		echo Operaciones en $NAME
		#Muevo la carpeta md5 a md5_0
		mv $dir $dir"_0"
		#Creo en enlace simbolico md5 apuntando a md5_0
		ln -s $dir"_0" $dir
		#Muevo out.png de public a private
		mv /datos/nfs/users/public/$NAME/out.png $dir/out.png
		#Elimino la carpeta publica
		rm -r /datos/nfs/users/public/$NAME/
	fi
fi
done
