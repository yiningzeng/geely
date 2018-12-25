#!/bin/bash
#$1=路径-传摄像头id
cd /opt/lampp/htdocs/stream/hls_$1
kill -9 `cat pid.txt`
echo "success"


