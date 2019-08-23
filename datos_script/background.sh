target=/adele/dev/workspace/ApplicationData/Scene/EPIG/fondo.jpg
files=(/adele/dev/workspace/ApplicationData/Scene/EPIG/bg/*)
f="${files[RANDOM % ${#files[@]}]}"
ln -fs $f $target
