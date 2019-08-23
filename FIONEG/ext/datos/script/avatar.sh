#!/bin/bash
export DISPLAY=:0.0
export LD_LIBRARY_PATH=/adele/dev/workspace/Dependencies/Horde3D/CMake/Horde3D/Source/Horde3DEngine:/adele/dev/workspace/Dependencies/Horde3D/CMake/Horde3D/Source/Horde3DUtils/:/adele/dev/workspace/Run/bin/:/adele/dev/workspace/Dependencies/log4cplus-1.0.4/src/.libs:/adele/dev/workspace/Dependencies/glfw-2.7.3/lib/x11:/adele/dev/workspace/Dependencies/ffmpeg-0.7.12/libavformat:/adele/dev/workspace/Dependencies/ffmpeg-0.7.12/libswscale:/adele/dev/workspace/Dependencies/ffmpeg-0.7.12/libavcodec:/adele/dev/workspace/Dependencies/ffmpeg-0.7.12/libavutil
export LD_LIBRARY_PATH=${LD_LIBRARY_PATH}:/adele/dev/workspace/libs:/adele/dev/workspace/Dependencies/boost_1_52_0/stage/lib

export PSISBAN_APPLICATION_DATA=/datos/nfs/users/private/$1

DISPLAY=:0 /adele/dev/workspace/Run/Debug/./Run /datos/nfs/users/private/$1/avatar.xml >> /tmp/avatar.log 2>&1 &&

echo "PIDrun:$!"



