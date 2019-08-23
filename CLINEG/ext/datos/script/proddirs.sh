#!/bin/bash
#se crea un sólo usuario (nikola.tesla@adelerobots.com::temporal)

sudo chown -R adele:adele /datos
sudo chmod -R 774 /datos

sudo rm -r /datos `find -type d -name "*.svn"`

ln -s  /datos/nfs/users/private/1cf79a1b295a9393d63a76f528a3ed90/ApplicationData
ln -s /adele/dev/workspace/ApplicationDataDevelopment/psisban-logger.properties /nfs/users/private/1cf79a1b295a9393d63a76f528a3ed90/ApplicationData/psisban-logger.properties

