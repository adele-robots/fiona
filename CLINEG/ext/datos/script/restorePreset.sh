#!/bin/bash

#Script para hacer restore de configuraciones del avatar
#$1 es el md5 del mail del user
#$2 es el mode, el nombre del preset que se quiere restaurar


#Sustituir out.png en public
#Cambiado a private por cambio en sistema de ficheros
cp /datos/nfs/configpresets/$2/out.png /datos/nfs/users/private/$1


#Sustituir in.json y avatar.xml en private
cp /datos/nfs/configpresets/$2/in.json /datos/nfs/users/private/$1
cp /datos/nfs/configpresets/$2/avatar.xml /datos/nfs/users/private/$1


#Sustituir configuración necesaria
cp /datos/nfs/configpresets/$2/conf/*.ini /datos/nfs/users/private/$1
