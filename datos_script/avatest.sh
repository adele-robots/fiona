#!/bin/bash
export DISPLAY=:0.0
export LD_LIBRARY_PATH=/adele/dev/workspace/Run/bin:/adele/dev/workspace/lib:/adele/dev/workspace/Dependencies/xerces-c/lib:/adele/dev/workspace/Dependencies/boost-1-46/lib
export PSISBAN_APPLICATION_DATA=/datos/nfs/users/private/2ff773bb61cae5cd81daba4324fa7f9c


DISPLAY=:0 /adele/dev/workspace/Run/Debug/./Run /datos/nfs/users/private/2ff773bb61cae5cd81daba4324fa7f9c/avatar.xml 
#PID="$!"
echo "PIDrun:$!"


