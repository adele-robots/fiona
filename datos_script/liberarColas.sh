#!/bin/bash

if [ `ps -ef | grep "avatar.xml" | grep "Run" | wc -l` -eq 0 ]; then
        echo "No se ejecuta ningun avatar, liberamos las colas"

	ME=`whoami`

	IPCS_Q=`ipcs -q | egrep "0x[0-9a-f]+ [0-9]+" | grep $ME | cut -f2 -d" "`

	for id in $IPCS_Q; do
		ipcrm -q $id;
	done

	exit 0
fi
exit 1
