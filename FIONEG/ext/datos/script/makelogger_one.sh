#!/bin/bash

#COMIENZO DE LA SUSTITUCIÓN/CREACIÓN DEL ARCHIVO
#BORRAMOS EL SIMBÓLICO (aquí no hace falta)
#$1 = md5
#$md5_0 = md5_0
md5_0=$1"_0"
rm /datos/nfs/users/private/$md5_0/psisban-logger.properties

#CHORRETÓN DE PROPERTIES
echo "" >>/datos/nfs/users/private/$md5_0/psisban-logger.properties

echo "# Configuración del sistema de Log de log4cplus" >>/datos/nfs/users/private/$md5_0/psisban-logger.properties
echo " ">>/datos/nfs/users/private/$md5_0/psisban-logger.properties
echo "# Un file rolling appender. Appenders disponibles ConsoleAppender, FileAppender, " >>/datos/nfs/users/private/$md5_0/psisban-logger.properties
echo "# RollingFileAppender, DailyRollingFileAppender, SyslogAppender, NTEventLogAppender, " >>/datos/nfs/users/private/$md5_0/psisban-logger.properties
echo "# SocketAppender, Win32DebugAppender." >>/datos/nfs/users/private/$md5_0/psisban-logger.properties
echo " ">>/datos/nfs/users/private/$md5_0/psisban-logger.properties
echo "log4cplus.appender.MyRollingFileAppender=log4cplus::RollingFileAppender" >>/datos/nfs/users/private/$md5_0/psisban-logger.properties
echo "log4cplus.appender.MyRollingFileAppender.File=avatar.log" >>/datos/nfs/users/private/$md5_0/psisban-logger.properties
echo "log4cplus.appender.MyRollingFileAppender.MaxFileSize=10MB" >>/datos/nfs/users/private/$md5_0/psisban-logger.properties
echo "log4cplus.appender.MyRollingFileAppender.MaxBackupIndex=1" >>/datos/nfs/users/private/$md5_0/psisban-logger.properties
echo "log4cplus.appender.MyRollingFileAppender.layout=log4cplus::TTCCLayout" >>/datos/nfs/users/private/$md5_0/psisban-logger.properties
echo "log4cplus.appender.MyRollingFileAppender.layout.ContextPrinting=enabled" >>/datos/nfs/users/private/$md5_0/psisban-logger.properties
echo "log4cplus.appender.MyRollingFileAppender.layout.DateFormat=ISO8601" >>/datos/nfs/users/private/$md5_0/psisban-logger.properties
echo " ">>/datos/nfs/users/private/$md5_0/psisban-logger.properties

echo "log4cplus.appender.MyConsoleAppender=log4cplus::ConsoleAppender" >>/datos/nfs/users/private/$md5_0/psisban-logger.properties
echo " ">>/datos/nfs/users/private/$md5_0/psisban-logger.properties

echo "log4cplus.appender.MyFileAppender=log4cplus::FileAppender" >>/datos/nfs/users/private/$md5_0/psisban-logger.properties
echo "log4cplus.appender.MyFileAppender.File=/datos/nfs/users/private/$1/logs/avatar.log" >>/datos/nfs/users/private/$md5_0/psisban-logger.properties
echo "log4cplus.appender.MyFileAppender.layout=log4cplus::TTCCLayout " >>/datos/nfs/users/private/$md5_0/psisban-logger.properties
echo "log4cplus.appender.MyFileAppender.layout.ContextPrinting=enabled " >>/datos/nfs/users/private/$md5_0/psisban-logger.properties
echo "log4cplus.appender.MyFileAppender.layout.DateFormat=ISO8601 " >>/datos/nfs/users/private/$md5_0/psisban-logger.properties
echo " ">>/datos/nfs/users/private/$md5_0/psisban-logger.properties

echo "log4cplus.appender.ErrConsoleAppender=log4cplus::FileAppender ">>/datos/nfs/users/private/$md5_0/psisban-logger.properties
echo "log4cplus.appender.ErrConsoleAppender.File=/datos/nfs/users/private/$1/logs/errcon.log ">>/datos/nfs/users/private/$md5_0/psisban-logger.properties
echo "log4cplus.appender.ErrConsoleAppender.layout=log4cplus::PatternLayout  ">>/datos/nfs/users/private/$md5_0/psisban-logger.properties
echo "log4cplus.appender.ErrConsoleAppender.layout.ContextPrinting=enabled  ">>/datos/nfs/users/private/$md5_0/psisban-logger.properties
echo "log4cplus.appender.ErrConsoleAppender.layout.DateFormat=log4cplus::PatternLayout ">>/datos/nfs/users/private/$md5_0/psisban-logger.properties
echo "log4cplus.appender.ErrConsoleAppender.layout.ConversionPattern=%d{%m/%d/%y %H:%M:%S,%Q} [%t] %-5p %c{2} - %m%n ">>/datos/nfs/users/private/$md5_0/psisban-logger.properties

echo "# log level y appenders para el root logger " >>/datos/nfs/users/private/$md5_0/psisban-logger.properties
echo " ">>/datos/nfs/users/private/$md5_0/psisban-logger.properties
echo "log4cplus.rootLogger=INFO, MyFileAppender, MyConsoleAppender, ErrConsoleAppender " >>/datos/nfs/users/private/$md5_0/psisban-logger.properties
