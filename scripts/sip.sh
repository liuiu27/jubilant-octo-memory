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

echo "execute parameter： $1  $2"

./api-gateway.sh $1 $2
echo "=== execute gateway"

./cdd-service.sh $1 $2
echo "=== execute cdd-service"

./config-service.sh $1 $2
echo "=== execute config-service"

./iqiyi-service.sh $1 $2
echo "=== execute iqiyi-service"

./lakala-service.sh $1 $2
echo "=== execute lakala-service"

./notify-service.sh $1 $2
echo "=== execute notify-service"

./order-service.sh $1 $2
echo "=== execute order-service"

./orgsupplier-service.sh $1 $2
echo "=== execute orgsupplier-service"

./product-service.sh $1 $2
echo "=== execute product-service"

./recharge-service.sh $1 $2
echo "=== execute recharge-service"

./tencent-service.sh $1 $2
echo "=== execute tencent-service"

./trvok-service.sh $1 $2
echo "=== execute trvok-service"

./voucher-service.sh $1 $2
echo "=== execute voucher-service"

sleep 10

./cache-service.sh $1 $2
echo "=== execute cache-service"

exit 0