#!/bin/bash

#Creo el archivo eca.ini entero para cambiarle las urls de red5
		
echo "	AudioVideoConfig_Remote_AvatarStream_AudioPacketSize = 576; 		">> /datos/nfs/users/private/$1/generalConf.ini
echo "	Body_Joints_HeadPanOffset = 0;		">> /datos/nfs/users/private/$1/generalConf.ini
echo "	Body_Joints_HeadTiltOffset = 0;		">> /datos/nfs/users/private/$1/generalConf.ini
echo "	Body_Joints_Eyes_HasEyeBones = TRUE;//FALSE;		">> /datos/nfs/users/private/$1/generalConf.ini
echo "	AudioVideoConfig_Remote_WebcamStream_URL= \"rtmp://aio0000.adelerobots.com:9080/FionaRed5/$1/usercam conn=S:masteruser conn=S:masterpass live=1\"; ">> /datos/nfs/users/private/$1/generalConf.ini
echo "	AudioVideoConfig_Remote_AvatarStream_URL= \"rtmp://aio0000.adelerobots.com:9080/FionaRed5/$1/avatar conn=S:masteruser conn=S:masterpass live=1\"; ">> /datos/nfs/users/private/$1/generalConf.ini
