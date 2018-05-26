#!/bin/bash

CAS_HOME=/opt/cas
WAR="$CAS_HOME/cas.war"
LOG="/var/log/cas.log"
LOCK="/var/lock/subsys/cas"
export CAS_HOME

JAVA_OPTS="-Xms5024m -Xmx5536m -XX:PermSize=128M -XX:MaxPermSize=256M"

RETVAL=0

pid_of_cas() {
    pgrep -f "java.*cas"
}

start() {
    [ -e "$LOG" ] && cnt=`wc -l "$LOG" | awk '{ print $1 }'` || cnt=1

    echo -n $"Starting cas: "

    cd "$CAS_HOME"
    nohup java -jar ${JAVA_OPTS} "$WAR" >> "$LOG" 2>&1 &

    while { pid_of_cas > /dev/null ; } &&
        ! { tail +$cnt "$LOG" | grep -q 'Winstone Servlet Engine .* running' ; } ; do
        sleep 1
    done

    pid_of_cas > /dev/null
    RETVAL=$?
    [ $RETVAL = 0 ] && success $"$STRING" || failure $"$STRING"
    echo

    [ $RETVAL = 0 ] && touch "$LOCK"
}

stop() {
    echo -n "Stopping cas: "

    pid=`pid_of_cas`
    [ -n "$pid" ] && kill $pid
    RETVAL=$?
    cnt=10
    while [ $RETVAL = 0 -a $cnt -gt 0 ] &&
        { pid_of_cas > /dev/null ; } ; do
            sleep 1
            ((cnt--))
    done

    [ $RETVAL = 0 ] && rm -f "$LOCK"
    [ $RETVAL = 0 ] && success $"$STRING" || failure $"$STRING"
    echo
}

status() {
    pid=`pid_of_cas`
    if [ -n "$pid" ]; then
        echo "jenkins (pid $pid) is running..."
        return 0
    fi
    if [ -f "$LOCK" ]; then
        echo $"${base} dead but subsys locked"
        return 2
    fi
    echo "jenkins is stopped"
    return 3
}

# See how we were called.
case "$1" in
    start)
        start
        ;;
    stop)
        stop
        ;;
    status)
        status
        ;;
    restart)
        stop
        start
        ;;
    *)
        echo $"Usage: $0 {start|stop|restart|status}"
        exit 1
esac

exit $RETVAL