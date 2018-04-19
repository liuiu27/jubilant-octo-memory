#!/bin/bash

## java env
export JAVA_HOME=/app/sip/jdk1.8.0_151
export JRE_HOME=$JAVA_HOME/jre

## service name
## 服务所在目录
SERVICE_DIR=/app/sip/service-integration-platform/jar
## 服务名称
SERVICE_NAME=destdo-service
JAR_NAME=$SERVICE_NAME\.jar
RUN_PARAM=--spring.profiles.active=$2
PID=$SERVICE_NAME\.pid

cd $SERVICE_DIR

case "$1" in

    start)
        ##nohup &  以守护进程启动
        nohup $JRE_HOME/bin/java -Dloader.path=$SERVICE_NAME/lib/ -Xms256m -Xmx512m -jar $JAR_NAME $RUN_PARAM >/dev/null 2>&1 &
        echo $! > $SERVICE_DIR/$PID
        echo "=== start $SERVICE_NAME"
        sleep 5
        ;;

    stop)
        kill `cat $SERVICE_DIR/$PID`
        rm -rf $SERVICE_DIR/$PID
        echo "=== stop $SERVICE_NAME"

        ## 停止5秒
        sleep 5
        ##
        P_ID=`ps -ef | grep -w "$SERVICE_NAME" | grep -v "grep" | awk '{print $2}'`
        if [ "$P_ID" == "" ]; then
            echo "=== $SERVICE_NAME process not exists or stop success"
        else
            echo "=== $SERVICE_NAME process pid is:$P_ID"
            echo "=== begin kill $SERVICE_NAME process, pid is:$P_ID"
            kill -9 $P_ID
        fi
        ;;

    restart)
        $0 stop
        sleep 2
        $0 start
        echo "=== restart $SERVICE_NAME"
        ;;

    *)
        ## restart
        $0 stop
        sleep 2
        $0 start
        ;;
esac
exit 0