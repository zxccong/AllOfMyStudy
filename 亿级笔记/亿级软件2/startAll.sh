#!/bin/sh  

#启动redis

cd /usr/local/redis/redis-2.8.19



nohup src/redis-server slave01.conf &
nohup src/redis-server slave02.conf &
nohup src/redis-server slave03.conf &
nohup src/redis-server slave04.conf &
nohup src/redis-server slave09.conf &
nohup src/redis-server slave10.conf &


#启动 twemproxy

cd /usr/local/twemproxy-master/twemproxy-0.4.0


src/nutcracker -d -c conf/nutcracker.yml




