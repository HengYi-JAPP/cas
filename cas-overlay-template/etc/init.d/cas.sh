#!/bin/bash
### BEGIN INIT INFO
# Provides:             cas
# Required-Start:       $local_fs $network
# Required-Stop:        $local_fs $network
# Default-Start:        2 3 4 5
# Default-Stop:         0 1 6
# Description:          cas-undertow
### END INIT INFO

# Source function library.
. /etc/rc.d/init.d/functions

DIR="/opt/cas"

case "$1" in
start)
su -c "${DIR}/run.sh start"
;;
stop)
su -c "${DIR}/run.sh stop"
;;
restart)
su -c "${DIR}/run.sh restart"
;;
status)
su -c "${DIR}/run.sh status"
;;
*)
echo "Usage: {start|stop|restart|status}" >&2
exit 1
;;
esac
exit 0