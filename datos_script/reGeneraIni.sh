#!/bin/bash
echo $2 >/tmp/room.txt
#Creo el archivo eca.ini entero para cambiarle las urls de red5
rm /datos/nfs/users/private/$1/generalConf.ini;


echo "AudioVideoConfig_Remote_AvatarStream_CodecVideoBitRate = 1000000;      ">> /datos/nfs/users/private/$1/generalConf.ini
echo "AudioVideoConfig_Remote_AvatarStream_CodecAudioBitRate = 64000;      ">> /datos/nfs/users/private/$1/generalConf.ini

grep "libRemotePhotoCharacterSpark.so"  /datos/nfs/users/private/$1/avatar.xml > /dev/null;
if [ $? -ne 0 ]; then
# For Horde Engine
  echo "AudioVideoConfig_Remote_AvatarStream_AudioPacketSize = 576; ">> /datos/nfs/users/private/$1/generalConf.ini
  echo "AudioVideoConfig_Width = 768;      ">> /datos/nfs/users/private/$1/generalConf.ini
  echo "AudioVideoConfig_Height = 550;     ">> /datos/nfs/users/private/$1/generalConf.ini
else
# For Photo Realistic engine
  echo "AudioVideoConfig_Remote_AvatarStream_AudioPacketSize = 1152; ">> /datos/nfs/users/private/$1/generalConf.ini
  echo "AudioVideoConfig_Width = 640;      ">> /datos/nfs/users/private/$1/generalConf.ini
 echo "AudioVideoConfig_Height = 480;     ">> /datos/nfs/users/private/$1/generalConf.ini
fi

echo "Body_Joints_HeadPanOffset = 0;		">> /datos/nfs/users/private/$1/generalConf.ini
echo "Body_Joints_HeadTiltOffset = 0;		">> /datos/nfs/users/private/$1/generalConf.ini
echo "Body_Joints_Eyes_HasEyeBones = TRUE;//FALSE;		">> /datos/nfs/users/private/$1/generalConf.ini
echo "AudioVideoConfig_Remote_WebcamStream_URL= \"rtmp://195.55.126.59/FionaRed5/$2/usercam conn=S:masteruser conn=S:masterpass live=1\"; ">> /datos/nfs/users/private/$1/generalConf.ini
echo "AudioVideoConfig_Remote_AvatarStream_URL= \"rtmp://195.55.126.59/FionaRed5/$2/avatar conn=S:masteruser conn=S:masterpass live=1\"; ">> /datos/nfs/users/private/$1/generalConf.ini
