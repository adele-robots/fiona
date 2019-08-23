#!/bin/bash
export DISPLAY=:0.0
export LD_LIBRARY_PATH=/adele/dev/workspace/Run/bin:/adele/dev/workspace/lib:/adele/dev/workspace/Dependencies/xerces-c/lib:/adele/dev/workspace/Dependencies/boost-1-46/lib:/adele/dev/workspace/Dependencies/boost_1_52_0/stage/lib/
export LD_LIBRARY_PATH=${LD_LIBRARY_PATH}:/adele/dev/workspace/ApplicationData/ivona/x86_64-pc-linux-gnu/lib/
export PSISBAN_APPLICATION_DATA=/datos/nfs/users/private/$1

# ***** Modified for ramdomize the background image
target=/adele/dev/workspace/ApplicationData/Scene/raquel_treelogic/fondo.jpg
files=(/adele/dev/workspace/ApplicationData/Scene/raquel_treelogic/bg/*)
f="${files[RANDOM % ${#files[@]}]}"
ln -fs $f $target
# ***** END OF MODIFICATION

# ***** Modified for ramdomize the background image
target=/datos/nfs/users/private/$1/fondo.jpg
files=(/datos/nfs/users/private/$1/bg/*)
f="${files[RANDOM % ${#files[@]}]}"
ln -fs $f $target
# ***** END OF MODIFICATION

DISPLAY=:0 /adele/dev/workspace/Run/Debug/./Run /datos/nfs/users/private/$1/avatar.xml 1>> /tmp/avatar.log && 2>> /tmp/avatar.log &
#DISPLAY=:0 strace -f -v /adele/dev/workspace/Run/Debug/./Run /datos/nfs/users/private/$1/avatar.xml >> /tmp/avatar.log 2>&1 &&

echo "PIDrun:$!" 
#trap bashtrap SIGTERM


#bashtrap()
#{
#kill $PID
#kill -9 $$
#}



