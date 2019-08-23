#!/bin/bash
PIDS=`ps -ef | grep avatar | awk '{print $2;}'`
if [ -z "$PIDS" ]; then
  exit 1
else
  for PID in $PIDS; do
    if [ $PID -eq $1 ]
      then
        exit 0
    fi
  done
  exit 1
fi

