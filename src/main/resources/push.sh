#!/bin/bash

#$1=路径-传摄像头id
#$2=rtsp地址
#$3=1280x720 清晰度

#创建nginx配置文件
content="application hls_$1 {
 live on;
 hls on;
 hls_path /opt/lampp/htdocs/stream/hls_$1;
 hls_fragment 5s;
}"
file="/usr/local/nginx/rtmp_conf/$1.conf"
echo $content > $file
echo "配置文件创建成功"
#创建nginx配置文件

/usr/local/nginx/sbin/nginx -s reload #重新载入nginx配置
echo "重新载入nginx配置"

stream_path=/opt/lampp/htdocs/stream/hls_$1
mkdir -p $stream_path
chmod -R 777 $stream_path
cd $stream_path
echo "进入$stream_path,开始转码推流"
nohup ffmpeg -i $2 -vcodec libx264 -vprofile baseline -acodec aac -ar 44100 -strict -2 -ac 1 -f flv -s $3 -q 10 rtmp://localhost:2016/hls_$1/film > log.log 2>&1 & echo $! > pid.txt
echo "$!-进程转码推流中推流地址："
url="http://47.99.207.5:88/stream/hls_$1/film.m3u8"
echo $url >push_url.txt
echo $url


