#!/bin/bash

#crear directorio /datos/nfs/users/private/$1
mkdir /datos/nfs/users/private/$1

#crear directorio /datos/nfs/users/private/$1/UploadedSparks
mkdir /datos/nfs/users/private/$1/UploadedSparks

#crear directorio /datos/nfs/users/private/$1.0jbpm
mkdir /datos/nfs/users/private/$1.0jbpm

#crear directorio  /datos/nfs/users/public/$1
mkdir /datos/nfs/users/public/$1

#copiar inis de componentes core al inicio de la cuenta
cp /datos/nfs/users/base/initialconf/* /datos/nfs/users/private/$1

#copiar archivo avatar.xml en /datos/nfs/users/private/$1
cp /datos/nfs/users/base/avatar.xml /datos/nfs/users/private/$1

#copiar archivo in.json en /datos/nfs/users/private/$1
cp /datos/nfs/users/base/in.json /datos/nfs/users/private/$1

#generar archivo eca.ini
./generaini.sh $1

#copiar archivo out.png en /datos/nfs/users/public/$1
cp /datos/nfs/users/base/out.png /datos/nfs/users/public/$1

#crear enlace simbólico en /datos/nfs/users/private/$1 apuntando a /adele/dev/workspace/ApplicationData
ln -s /adele/dev/workspace/ApplicationData /datos/nfs/users/private/$1

#crear enlace simbólico en /datos/nfs/users/private/$1 apuntando a /adele/dev/workspace/ApplicationData/psisban-logger.properties
#ln -s /adele/dev/workspace/ApplicationData/psisban-logger.properties /datos/nfs/users/private/$1
./makelogger_one.sh $1

#crear carpeta en el contexto del tomcat
mkdir /opt/tomcat6/webapps/designer/stencilsets/$1.0jbpm/
#mkdir /adele/dev/workspace_java/.metadata/.plugins/org.eclipse.wst.server.core/tmp0/wtpwebapps/designer/stencilsets/$1.0jbpm/

#enlace simbólico que apunta al json del entorno
ln -s /datos/nfs/users/private/$1.0jbpm/$1.0jbpm.json /opt/tomcat6/webapps/designer/stencilsets/$1.0jbpm/$1.0jbpm.json

#enlaces simbólicos para las carpetas de datos
ln -s /datos/nfs/basedesigner/src /opt/tomcat6/webapps/designer/stencilsets/$1.0jbpm/src
ln -s /datos/nfs/basedesigner/icons /opt/tomcat6/webapps/designer/stencilsets/$1.0jbpm/icons
ln -s /datos/nfs/basedesigner/view /opt/tomcat6/webapps/designer/stencilsets/$1.0jbpm/view
ln -s /datos/nfs/basedesigner/stencildata /opt/tomcat6/webapps/designer/stencilsets/$1.0jbpm/stencildata

