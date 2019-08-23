#!/bin/bash

#$1 es el md5 del usuario
#$2 es el numero de configuraciones(carpetas) que tendra

unlink /datos/nfs/users/private/$1

#Si tiene que tener menos configuraciones eliminamos carpetas
for i in `seq $2 9`
do
	if [ -d /datos/nfs/users/private/$1_$i ];
	then
		rm -r /datos/nfs/users/private/$1_$i
	fi
done

ln -s /datos/nfs/users/private/$1_0 /datos/nfs/users/private/$1

#Si tiene que tener mas, las creamos
limit=`expr $2 - 1`
for i in `seq 1 $limit`
do
	if [ ! -d /datos/nfs/users/private/$1_$i ];
	then
        	mkdir /datos/nfs/users/private/$1_$i
		#UploadedSparks va a ser un enlace simbolico a la carpeta original
        	ln -s /datos/nfs/users/private/$1_0/UploadedSparks /datos/nfs/users/private/$1_$i/UploadedSparks
        	cp /datos/nfs/users/base/initialconf/* /datos/nfs/users/private/$1_$i
        	cp /datos/nfs/users/base/avatar.xml /datos/nfs/users/private/$1_$i
        	cp /datos/nfs/users/base/in.json /datos/nfs/users/private/$1_$i
        	ln -s /datos/nfs/users/private/$1_0/generalConf.ini /datos/nfs/users/private/$1_$i/generalConf.ini
        	cp /datos/nfs/users/base/out.png /datos/nfs/users/private/$1_$i
        	ln -s /adele/dev/workspace/ApplicationData /datos/nfs/users/private/$1_$i/ApplicationData
		#El archivo psisban-logger.properties es igual en todas las configuraciones asi que tambien es enlace simbolico
        	ln -s /datos/nfs/users/private/$1_0/psisban-logger.properties /datos/nfs/users/private/$1_$i/psisban-logger.properties
		ln -s /datos/nfs/users/private/$1_0/banners /datos/nfs/users/private/$1_$i/banners
		ln -s /datos/nfs/users/private/$1_0/icons /datos/nfs/users/private/$1_$i/icons
		ln -s /datos/nfs/users/private/$1_0/videos /datos/nfs/users/private/$1_$i/videos
		mkdir /datos/nfs/users/private/$1_$i/logs
		mkdir /datos/nfs/users/private/$1_$i/UserSparkData
	fi
done
