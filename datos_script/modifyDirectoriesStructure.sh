#!/bin/bash

#$1 es el md5 del usuario
#$2 es el idioma para el avatar
#$3 es el personaje elegido
#$4 es el color de la camiseta del personaje
#$5 es la frase inicial

declare -A lan

lan[es]='es-ES'
lan[en-US]='en-US'
lan[en-UK]='en-GB'
lan[fr]='fr-FR'
lan[de]='de-DE'
lan[it]='it-IT'

declare -A viseme

viseme[3D_Rachel]='VSMIH'
viseme[3D_Elvira]='vis-eh_ShapeKey'
viseme[3D_Anna]='vis-eh_ShapeKey'

#Copiamos el archivo PicoTTSThreadSpark.ini de la carpeta base
cp /datos/nfs/users/private/3170aa8d92dee2cd1efb58eb711dd34c/PicoTTSThreadSpark.ini /datos/nfs/users/private/$1/

#Copiamos el archivo RemoteCharacterEmbodiment3DSpark.ini de la carpeta base
cp /datos/nfs/users/private/3170aa8d92dee2cd1efb58eb711dd34c/RemoteCharacterEmbodiment3DSpark.ini /datos/nfs/users/private/$1/

#Copiamos el archivo VoiceStartSpark.ini de la carpeta base
cp /datos/nfs/users/private/3170aa8d92dee2cd1efb58eb711dd34c/VoiceStartSpark.ini /datos/nfs/users/private/$1/

#Copiamos el archivo ScriptV8ThreadSpark.ini de la carpeta base
cp /datos/nfs/users/private/3170aa8d92dee2cd1efb58eb711dd34c/ScriptV8ThreadSpark.ini /datos/nfs/users/private/$1/

#Copiamos los archivos de script para el ScriptV8Spark de la carpeta base
cp /datos/nfs/users/private/3170aa8d92dee2cd1efb58eb711dd34c/UserSparkData/ScriptV8ThreadSpark/UserFiles/* /datos/nfs/users/private/$1/UserSparkData/ScriptV8ThreadSpark/UserFiles/

#Borrar contenido de la carpeta 'bg' que contiene los fondos usados aleatoriamente
rm /datos/nfs/users/private/$1/bg/*.jpg

#Borrar contenido de la carpeta del AIML del usuario
rm /datos/nfs/users/private/$1/UserSparkData/RebeccaAIMLSpark/UserFiles/*.aiml

#Colocarnos en la carpeta del usuario
cd /datos/nfs/users/private/$1

#Copiar el archivo de configuración de la cámara para el personaje elegido
cp CameraControlSpark.ini_$3 CameraControlSpark.ini

#Reemplazar las referencias al viejo md5 con el nuevo
sed -i 's/3170aa8d92dee2cd1efb58eb711dd34c/'$1'/g' *.*

#Reemplazar el idioma por el que nos venga como parámetro
sed -i 's/Voice="en-GB";/Voice="'${lan[$2]}'"/g' PicoTTSThreadSpark.ini

#Reemplazar el personaje por el que nos venga como parámetro
sed -i 's/Character="3D_tshirt";/Character="'$3'_'$4'"/g' RemoteCharacterEmbodiment3DSpark.ini

#Reemplazar el visema por el que se precise segun el modelo
sed -i 's/Lips_Fake_Movement_Viseme="vis-eh_ShapeKey";/Lips_Fake_Movement_Viseme="'${viseme[$3]}'";/g' VoiceStartSpark.ini

#Reemplazar el script por el que se precise segun el modelo
sed -i 's/Script_Filename="script.js";/Script_Filename="'$3'.js"/g' ScriptV8ThreadSpark.ini

#Reemplazar la frase inicial que se lanza desde ScriptV8Spark (con retraso, para sincronizarla)
#sed -i s/greeting/"$TEST2"/g UserSparkData/ScriptV8ThreadSpark/UserFiles/3D_Rachel.js
sed -i s/greeting/"$5"/g UserSparkData/ScriptV8ThreadSpark/UserFiles/3D_Rachel.js
sed -i s/greeting/"$5"/g UserSparkData/ScriptV8ThreadSpark/UserFiles/3D_Anna.js
sed -i s/greeting/"$5"/g UserSparkData/ScriptV8ThreadSpark/UserFiles/3D_Elvira.js

