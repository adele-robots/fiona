ps -eo uid,pid,lstart,cmd | 
grep [a]vatar |
tail -n+2 |
    while read PROC_UID PROC_PID PROC_LSTART_WDAY PROC_LSTART_MONTH PROC_LSTART_DAY PROC_LSTART_TIME PROC_LSTART_YEAR PROC_CMD; do
        SECONDS=$[$(date +%s) - $(date -d"$PROC_LSTART_WDAY $PROC_LSTART_MONTH $PROC_LSTART_DAY $PROC_LSTART_TIME $PROC_LSTART_YEAR" +%s)];
	echo $SECONDS >> /var/log/killed.log;
	echo $PROC_LSTART_WDAY $PROC_LSTART_MONTH $PROC_LSTART_DAY $PROC_LSTART_TIME $PROC_LSTART_YEAR >> /var/log/killed.log;
        if [ $PROC_UID -eq 0 -a $SECONDS -gt 600 ]; then
            echo $PROC_PID
	    # do save log for debugging
            cat /proc/$PROC_PID/cmdline >> /var/log/killed.log 2>&1
            echo ", details: " >> /var/log/killed.log
            date >> /var/log/killed.log
            ls -la /proc/$PROC_PID/ >> /var/log/killed.log 2>&1
        fi;
     done |
     xargs kill
