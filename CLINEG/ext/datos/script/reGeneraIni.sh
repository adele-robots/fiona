#!/bin/bash

#Creo el enlace símbolico para que la imagen publicitaria se 
#coja del directorio adecuado en función de la resolución
target=/datos/nfs/users/private/$1/backgroundCommercial
files=(/datos/nfs/advertising/$8/*)
f="${files[RANDOM % ${#files[@]}]}"
ln -fs $f $target

#Creo el archivo eca.ini entero para cambiarle las urls de red5
rm /datos/nfs/users/private/$1_0/generalConf.ini;
		
echo "AudioVideoConfig_Remote_AvatarStream_AudioPacketSize = 576; 		">> /datos/nfs/users/private/$1_0/generalConf.ini
echo "Body_Joints_HeadPanOffset = 0;		">> /datos/nfs/users/private/$1_0/generalConf.ini
echo "Body_Joints_HeadTiltOffset = 0;		">> /datos/nfs/users/private/$1_0/generalConf.ini
echo "Body_Joints_Eyes_HasEyeBones = TRUE;//FALSE;		">> /datos/nfs/users/private/$1_0/generalConf.ini
echo "Has_Advertising = $7;              ">> /datos/nfs/users/private/$1_0/generalConf.ini
echo "Advertising_Image_Filepath = \"/datos/nfs/users/private/$1/backgroundCommercial\";              ">> /datos/nfs/users/private/$1_0/generalConf.ini
echo "AudioVideoConfig_Width = $3;	">> /datos/nfs/users/private/$1_0/generalConf.ini
echo "AudioVideoConfig_Height = $4;	">> /datos/nfs/users/private/$1_0/generalConf.ini
echo "AudioVideoConfig_Remote_AvatarStream_CodecVideoBitRate = $5;	">> /datos/nfs/users/private/$1_0/generalConf.ini
echo "AudioVideoConfig_Remote_AvatarStream_CodecAudioBitRate = $6;	">> /datos/nfs/users/private/$1_0/generalConf.ini
echo "AudioVideoConfig_Remote_WebcamStream_URL= \"rtmp://192.168.1.138/FionaRed5/$2/usercam conn=S:masteruser conn=S:masterpass live=1\"; ">> /datos/nfs/users/private/$1_0/generalConf.ini
echo "AudioVideoConfig_Remote_AvatarStream_URL= \"rtmp://192.168.1.138/FionaRed5/$2/avatar conn=S:masteruser conn=S:masterpass live=1\"; ">> /datos/nfs/users/private/$1_0/generalConf.ini
