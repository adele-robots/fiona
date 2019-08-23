#$1 el email del usuario
#$2 nombre del avatar del usuario

sql_host='localhost'
slq_usuario='root'
sql_password='Aio0000'
sql_database='fionadb'

### Se monta los parámetros de conexión
sql_args="-h $sql_host -u $slq_usuario -p$sql_password -D $sql_database -s --skip-column-names -e"

# Buscamos el cn del avatar cuyo nombre coincide con el pasado como parámetro
cnAvatar=$(mysql $sql_args "SELECT CN_AVATAR FROM avatar WHERE DC_Avatar like '$2' AND CN_Usuario=(SELECT CN_USuario FROM usuario WHERE DC_EMAIL like '$1');")

#Calculamos el md5 del nuevo usuario
md5=$(echo -n "$1$cnAvatar" | md5sum | awk '{print $1}')

#echo $md5
#echo $1
echo $cnAvatar

