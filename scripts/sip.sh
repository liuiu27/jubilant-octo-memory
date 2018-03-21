#!/bin/bash

## service name
## 服务所在目录
SERVICE_DIR=/app/sip/service-integration-platform/sh

cd $SERVICE_DIR

if [ "$1" == "" ]; then
    $1 = restart
fi

if [ "$2" == "" ] ; then
    $2 = test
fi

echo "parameter： $1  $2"

./eureka.sh $1 $2
echo "=== execute eureka"
./api-gateway.sh $1 $2
echo "=== execute gateway"
