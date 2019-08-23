#!/bin/bash
#este script genera un psisban-logger.properties para cada usuario registrado
#Ojo, que aparte tiene el cacho de bash necesario para realizar acciones de mantenimiento en las carpetas privadas del usuario

for dir in /datos/nfs/users/private/*
do
	FULLNAME=$(basename $dir)
	EXT=$(echo $FULLNAME | sed 's/^.*\.//')
	NAME=$(basename $dir .${EXT})
	
if [[ "$EXT" != "0jbpm" ]]; then
	if file $dir | grep -e "_0:" > /dev/null; then

	#COMIENZO DE LA SUSTITUCIÓN/CREACIÓN DEL ARCHIVO
	#BORRAMOS EL SIMBÓLICO
	rm $dir/psisban-logger.properties

	#CHORRETÓN DE PROPERTIES
	echo "" >>$dir/psisban-logger.properties

	echo "# Configuración del sistema de Log de log4cplus" >>$dir/psisban-logger.properties
	echo " ">>$dir/psisban-logger.properties

	echo "# Un file rolling appender. Appenders disponibles ConsoleAppender, FileAppender, " >>$dir/psisban-logger.properties
	echo "# RollingFileAppender, DailyRollingFileAppender, SyslogAppender, NTEventLogAppender, " >>$dir/psisban-logger.properties
	echo "# SocketAppender, Win32DebugAppender." >>$dir/psisban-logger.properties
	echo " ">>$dir/psisban-logger.properties

	echo "log4cplus.appender.MyRollingFileAppender=log4cplus::RollingFileAppender" >>$dir/psisban-logger.properties
	echo "log4cplus.appender.MyRollingFileAppender.File=$dir/logs/avatar.log" >>$dir/psisban-logger.properties
	echo "log4cplus.appender.MyRollingFileAppender.MaxFileSize=10MB" >>$dir/psisban-logger.properties
	echo "log4cplus.appender.MyRollingFileAppender.MaxBackupIndex=1" >>$dir/psisban-logger.properties
	echo "log4cplus.appender.MyRollingFileAppender.layout=log4cplus::TTCCLayout" >>$dir/psisban-logger.properties
	echo "log4cplus.appender.MyRollingFileAppender.layout.ContextPrinting=enabled" >>$dir/psisban-logger.properties
	echo "log4cplus.appender.MyRollingFileAppender.layout.DateFormat=ISO8601" >>$dir/psisban-logger.properties
	echo " ">>$dir/psisban-logger.properties

	echo "log4cplus.appender.MyConsoleAppender=log4cplus::ConsoleAppender" >>$dir/psisban-logger.properties
	echo " ">>$dir/psisban-logger.properties

	echo "log4cplus.appender.ErrConsoleAppender=log4cplus::FileAppender ">>$dir/psisban-logger.properties
	echo "log4cplus.appender.ErrConsoleAppender.File=$dir/logs/errcon.log ">>$dir/psisban-logger.properties
	echo "log4cplus.appender.ErrConsoleAppender.layout=log4cplus::PatternLayout  ">>$dir/psisban-logger.properties
	echo "log4cplus.appender.ErrConsoleAppender.layout.ContextPrinting=enabled  ">>$dir/psisban-logger.properties
	echo "log4cplus.appender.ErrConsoleAppender.layout.DateFormat=log4cplus::PatternLayout ">>$dir/psisban-logger.properties
	echo "log4cplus.appender.ErrConsoleAppender.layout.ConversionPattern=%d{%m/%d/%y %H:%M:%S,%Q} [%t] %-5p %c{2} - %m%n ">>$dir/psisban-logger.properties
	echo " ">>$dir/psisban-logger.properties	


	echo "log4cplus.appender.MyFileAppender=log4cplus::FileAppender" >>$dir/psisban-logger.properties
	echo "log4cplus.appender.MyFileAppender.File=$dir/logs/avatar.log" >>$dir/psisban-logger.properties
	echo "log4cplus.appender.MyFileAppender.layout=log4cplus::TTCCLayout " >>$dir/psisban-logger.properties
	echo "log4cplus.appender.MyFileAppender.layout.ContextPrinting=enabled " >>$dir/psisban-logger.properties
	echo "log4cplus.appender.MyFileAppender.layout.DateFormat=ISO8601 " >>$dir/psisban-logger.properties
	echo " ">>$dir/psisban-logger.properties


	echo "# log level y appenders para el root logger " >>$dir/psisban-logger.properties
	 
	echo "log4cplus.rootLogger=INFO, MyFileAppender, MyConsoleAppender, ErrConsoleAppender " >>$dir/psisban-logger.properties

	echo "Creado psisban-logger.properties para: $NAME"
	
	fi
fi

done
