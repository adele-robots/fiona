#!/bin/bash

#$1 es el md5 del usuario
#$2 es el idioma para el avatar
#$3 es el personaje elegido
#$4 es el color de la camiseta del personaje
#$5 es la frase inicial
#$6 es el email del usuario
#$7 es el nombre elegido para el avatar

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

#crear directorio /datos/nfs/users/private/$1
cp -R /datos/nfs/users/private/3170aa8d92dee2cd1efb58eb711dd34c /datos/nfs/users/private/$1

#Colocarnos en la carpeta recién creada
cd /datos/nfs/users/private/$1

#Copiar los archivos AIML que correspondan según el idioma elegido
cp AIML/$2/* UserSparkData/RebeccaAIMLSpark/UserFiles/ 

#Reemplazar las referencias al viejo md5 con el nuevo
sed -i 's/3170aa8d92dee2cd1efb58eb711dd34c/'$1'/g' *.*

#Tambien las referencias que contienen los archivos de configuración de los modelos
sed -i 's/3170aa8d92dee2cd1efb58eb711dd34c/'$1'/g' model/*

#Reemplazar el idioma por el que nos venga como parámetro
sed -i 's/Voice="en-GB";/Voice="'${lan[$2]}'"/g' PicoTTSThreadSpark.ini

#Reemplazar el personaje por el que nos venga como parámetro
sed -i 's/Character="3D_tshirt";/Character="'$3'_'$4'"/g' RemoteCharacterEmbodiment3DSpark.ini

#Reemplazar el visema por el que se precise segun el modelo
sed -i 's/Lips_Fake_Movement_Viseme="vis-eh_ShapeKey";/Lips_Fake_Movement_Viseme="'${viseme[$3]}'";/g' VoiceStartSpark.ini

#Reemplazar el script por el que se precise segun el modelo
sed -i 's/Script_Filename="script.js";/Script_Filename="'$3'.js"/g' ScriptV8ThreadSpark.ini

#Reemplazar la frase inicial que se lanza desde ScriptV8Spark (con retraso, para sincronizarla)
sed -i s/greeting/"$5"/g UserSparkData/ScriptV8ThreadSpark/UserFiles/3D_Rachel.js
sed -i s/greeting/"$5"/g UserSparkData/ScriptV8ThreadSpark/UserFiles/3D_Anna.js
sed -i s/greeting/"$5"/g UserSparkData/ScriptV8ThreadSpark/UserFiles/3D_Elvira.js

#Copiar el archivo de configuración de la cámara para el personaje elegido
cp CameraControlSpark.ini_$3 CameraControlSpark.ini

#########################
## Setup de modelos 3D ##
#########################

#Colocarnos en la carpeta del personaje 'Rachel'
cd /adele/dev/workspace/ApplicationData/Scene/raquel

#Copiar y renombrar archivos necesarios para el cambio de color (de momento 'white'/'blue'/'red'/'green'/'yellow')
#y fondo aleatorio del personaje por usuario
cp ../raquel_base/Default.material.3170aa8d92dee2cd1efb58eb711dd34c.xml Default.material.$1.xml
cp ../raquel_base/raquel.3170aa8d92dee2cd1efb58eb711dd34c_white.scene.xml raquel.$1_white.scene.xml
cp ../raquel_base/raquel.3170aa8d92dee2cd1efb58eb711dd34c_blue.scene.xml raquel.$1_blue.scene.xml
cp ../raquel_base/raquel.3170aa8d92dee2cd1efb58eb711dd34c_red.scene.xml raquel.$1_red.scene.xml
cp ../raquel_base/raquel.3170aa8d92dee2cd1efb58eb711dd34c_yellow.scene.xml raquel.$1_yellow.scene.xml
cp ../raquel_base/raquel.3170aa8d92dee2cd1efb58eb711dd34c_green.scene.xml raquel.$1_green.scene.xml

#Reemplazar las referencias al viejo md5 con el nuevo
sed -i 's/3170aa8d92dee2cd1efb58eb711dd34c/'$1'/g' *.*

#Colocarnos en la carpeta del personaje 'Anna'
cd /adele/dev/workspace/ApplicationData/Scene/anna

#Copiar y renombrar archivos necesarios para el cambio de color (de momento 'white'/'blue'/'red'/'green'/'yellow')
#y fondo aleatorio del personaje por usuario
cp ../anna_base/Material_background.material.3170aa8d92dee2cd1efb58eb711dd34c.xml Material_background.material.$1.xml
cp ../anna_base/muja09.3170aa8d92dee2cd1efb58eb711dd34c_white.scene.xml muja09.$1_white.scene.xml
cp ../anna_base/muja09.3170aa8d92dee2cd1efb58eb711dd34c_blue.scene.xml muja09.$1_blue.scene.xml
cp ../anna_base/muja09.3170aa8d92dee2cd1efb58eb711dd34c_red.scene.xml muja09.$1_red.scene.xml
cp ../anna_base/muja09.3170aa8d92dee2cd1efb58eb711dd34c_green.scene.xml muja09.$1_green.scene.xml
cp ../anna_base/muja09.3170aa8d92dee2cd1efb58eb711dd34c_yellow.scene.xml muja09.$1_yellow.scene.xml

#Reemplazar las referencias al viejo md5 con el nuevo
sed -i 's/3170aa8d92dee2cd1efb58eb711dd34c/'$1'/g' *.*

#Colocarnos en la carpeta del personaje 'Elvira'
cd /adele/dev/workspace/ApplicationData/Scene/theGirl

#Copiar y renombrar archivos necesarios para el cambio de color (de momento 'white'/'blue'/'red'/'green'/'yellow')
#y fondo aleatorio del personaje por usuario
cp ../elvira_base/Material_background.material.3170aa8d92dee2cd1efb58eb711dd34c.xml Material_background.material.$1.xml 
cp ../elvira_base/tipa11.3170aa8d92dee2cd1efb58eb711dd34c_blue.scene.xml tipa11.$1_blue.scene.xml
cp ../elvira_base/tipa11.3170aa8d92dee2cd1efb58eb711dd34c_white.scene.xml tipa11.$1_white.scene.xml
cp ../elvira_base/tipa11.3170aa8d92dee2cd1efb58eb711dd34c_red.scene.xml tipa11.$1_red.scene.xml
cp ../elvira_base/tipa11.3170aa8d92dee2cd1efb58eb711dd34c_green.scene.xml tipa11.$1_green.scene.xml
cp ../elvira_base/tipa11.3170aa8d92dee2cd1efb58eb711dd34c_yellow.scene.xml tipa11.$1_yellow.scene.xml

#Reemplazar las referencias al viejo md5 con el nuevo
sed -i 's/3170aa8d92dee2cd1efb58eb711dd34c/'$1'/g' *.*

###################################
## Configuración de estadísticas ##
###################################

if [ "$6" ]
then
# Copiamos el archivo de configuración "statistics" del directorio base
cp /datos/nfs/user/private/3170aa8d92dee2cd1efb58eb711dd34c/statistics /datos/nfs/users/private/$1/

# Nos colocamos en la carpeta del usuario recién creado
cd /datos/nfs/users/private/$1

# Realizamos las sustituciones oportunas de cada parte del fichero
# EMAIL
sed -i 's/valorEmail/'$6'/g' statistics
# NOMBRE AVATAR
sed -i 's/nombreAvatar/'$7'/g' statistics
# NOMBRE EMPRESA
sed -i 's/nombreEmpresa/'$6'/g' statistics
fi
