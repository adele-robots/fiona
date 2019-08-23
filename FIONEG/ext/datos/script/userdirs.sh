#!/bin/bash

#$1 es el md5 del usuario
#$2 es el numero de configuraciones(carpetas) que tendra

#crear directorio /datos/nfs/users/private/$1
mkdir /datos/nfs/users/private/$1_0

#crear directorio /datos/nfs/users/private/$1/UploadedSparks
mkdir /datos/nfs/users/private/$1_0/UploadedSparks

#crear directorio /datos/nfs/users/private/$1.0jbpm
mkdir /datos/nfs/users/private/$1.0jbpm

#crear directorio  /datos/nfs/users/private/$1/logs
mkdir /datos/nfs/users/private/$1_0/logs

#crear directorio  /datos/nfs/users/private/$1/UserSparkData
mkdir /datos/nfs/users/private/$1_0/UserSparkData

#copiar inis de componentes core al inicio de la cuenta
cp /datos/nfs/users/base/initialconf/* /datos/nfs/users/private/$1_0

#copiar archivo avatar.xml en /datos/nfs/users/private/$1
cp /datos/nfs/users/base/avatar.xml /datos/nfs/users/private/$1_0

#copiar archivo in.json en /datos/nfs/users/private/$1
cp /datos/nfs/users/base/in.json /datos/nfs/users/private/$1_0

#generar archivo eca.ini
./generaini.sh $1

#copiar archivo out.png en /datos/nfs/users/private/$1
cp /datos/nfs/users/base/out.png /datos/nfs/users/private/$1_0

#crear enlace simbólico en /datos/nfs/users/private/$1 apuntando a /adele/dev/workspace/ApplicationData
ln -s /adele/dev/workspace/ApplicationData /datos/nfs/users/private/$1_0

#crear enlace simbólico en /datos/nfs/users/private/$1 apuntando a /adele/dev/workspace/ApplicationData/psisban-logger.properties
#ln -s /adele/dev/workspace/ApplicationData/psisban-logger.properties /datos/nfs/users/private/$1
./makelogger_one.sh $1

#crear carpeta en el contexto del tomcat
#mkdir /opt/tomcat6/webapps/designer/stencilsets/$1.0jbpm/
mkdir /adele/dev/fawna2/proyectos/.metadata/.plugins/org.eclipse.wst.server.core/tmp0/wtpwebapps/designer/stencilsets/$1.0jbpm/
#mkdir /adele/dev/workspace_java/.metadata/.plugins/org.eclipse.wst.server.core/tmp0/wtpwebapps/designer/stencilsets/$1.0jbpm/

#enlace simbólico que apunta al json del entorno
#ln -s /datos/nfs/users/private/$1.0jbpm/$1.0jbpm.json /opt/tomcat6/webapps/designer/stencilsets/$1.0jbpm/$1.0jbpm.json
ln -s /datos/nfs/users/private/$1.0jbpm/$1.0jbpm.json /adele/dev/fawna2/proyectos/.metadata/.plugins/org.eclipse.wst.server.core/tmp0/wtpwebapps/designer/stencilsets/$1.0jbpm/$1.0jbpm.json

#enlaces simbólicos para las carpetas de datos
#ln -s /datos/nfs/basedesigner/src /opt/tomcat6/webapps/designer/stencilsets/$1.0jbpm/src
ln -s /datos/nfs/basedesigner/src /adele/dev/fawna2/proyectos/.metadata/.plugins/org.eclipse.wst.server.core/tmp0/wtpwebapps/designer/stencilsets/$1.0jbpm/src
#ln -s /datos/nfs/basedesigner/icons /opt/tomcat6/webapps/designer/stencilsets/$1.0jbpm/icons
ln -s /datos/nfs/basedesigner/icons /adele/dev/fawna2/proyectos/.metadata/.plugins/org.eclipse.wst.server.core/tmp0/wtpwebapps/designer/stencilsets/$1.0jbpm/icons
#ln -s /datos/nfs/basedesigner/view /opt/tomcat6/webapps/designer/stencilsets/$1.0jbpm/view
ln -s /datos/nfs/basedesigner/view /adele/dev/fawna2/proyectos/.metadata/.plugins/org.eclipse.wst.server.core/tmp0/wtpwebapps/designer/stencilsets/$1.0jbpm/view
#ln -s /datos/nfs/basedesigner/stencildata /opt/tomcat6/webapps/designer/stencilsets/$1.0jbpm/stencildata
ln -s /datos/nfs/basedesigner/stencildata /adele/dev/fawna2/proyectos/.metadata/.plugins/org.eclipse.wst.server.core/tmp0/wtpwebapps/designer/stencilsets/$1.0jbpm/stencildata

#Creamos las carpetas para las distintas configuraciones del usuario con indice de 1 al numero de carpetas -1
limit=`expr $2 - 1`
for i in `seq 1 $limit`
do
        mkdir /datos/nfs/users/private/$1_$i
	#UploadedSparks va a ser un enlace simbolico a la carpeta original
        ln -s /datos/nfs/users/private/$1_0/UploadedSparks /datos/nfs/users/private/$1_$i/UploadedSparks
        cp /datos/nfs/users/base/initialconf/* /datos/nfs/users/private/$1_$i
        cp /datos/nfs/users/base/avatar.xml /datos/nfs/users/private/$1_$i
        cp /datos/nfs/users/base/in.json /datos/nfs/users/private/$1_$i
        ln -s /datos/nfs/users/private/$1_0/generalConf.ini /datos/nfs/users/private/$1_$i/generalConf.ini
        cp /datos/nfs/users/base/out.png /datos/nfs/users/private/$1_$i
        ln -s /adele/dev/workspace/ApplicationData /datos/nfs/users/private/$1_$i/ApplicationData
	#El archivo psisban-logger.properties es igual en todas las configuraciones asi que tambien es enlace simbolico
        ln -s /datos/nfs/users/private/$1_0/psisban-logger.properties /datos/nfs/users/private/$1_$i/psisban-logger.properties
	ln -s /datos/nfs/users/private/$1_0/banners /datos/nfs/users/private/$1_$i/banners
        ln -s /datos/nfs/users/private/$1_0/icons /datos/nfs/users/private/$1_$i/icons
        ln -s /datos/nfs/users/private/$1_0/videos /datos/nfs/users/private/$1_$i/videos
        mkdir /datos/nfs/users/private/$1_$i/logs
	mkdir /datos/nfs/users/private/$1_$i/UserSparkData
done

#crear enlace simbolico al directorio base
ln -s /datos/nfs/users/private/$1_0 /datos/nfs/users/private/$1
