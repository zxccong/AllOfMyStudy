#!/bin/sh  

#启动redis

cd /etc/init.d/

./redis_7001 start
./redis_7002 start

#启动Zk

cd /usr/local/zk/bin/

zkServer.sh start

#启动nginx
cd /usr/servers/nginx/sbin/

./nginx 

#启动卡夫卡kafka

cd /usr/local/kafka


nohup bin/kafka-server-start.sh config/server.properties &

#启动stome

storm nimbus >/dev/null 2>&1 &
storm supervisor >/dev/null 2>&1 &
storm ui >/dev/null 2>&1 &
storm logviewer >/dev/null 2>&1 &



