#!/bin/bash

#Creo el archivo eca.ini entero para cambiarle las urls de red5
		
echo "AudioVideoConfig_Remote_AvatarStream_AudioPacketSize = 576; 		">> /datos/nfs/users/private/$1_0/generalConf.ini
echo "Body_Joints_HeadPanOffset = 0;		">> /datos/nfs/users/private/$1_0/generalConf.ini
echo "Body_Joints_HeadTiltOffset = 0;		">> /datos/nfs/users/private/$1_0/generalConf.ini
echo "Body_Joints_Eyes_HasEyeBones = TRUE;//FALSE;		">> /datos/nfs/users/private/$1_0/generalConf.ini
echo "AudioVideoConfig_Remote_WebcamStream_URL= \"rtmp://195.55.126.110:80/FionaRed5/$1/usercam conn=S:masteruser conn=S:masterpass live=1\"; ">> /datos/nfs/users/private/$1_0/generalConf.ini
echo "AudioVideoConfig_Remote_AvatarStream_URL= \"rtmp://195.55.126.110:80/FionaRed5/$1/avatar conn=S:masteruser conn=S:masterpass live=1\"; ">> /datos/nfs/users/private/$1_0/generalConf.ini
