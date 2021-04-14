
##音视频平台

##### 1. 去水印
#####
    docker run -itd --name convert-app -e active="dev" -e folder="/home/upload/" -p 8080:8080 -v /root/.m2:/root/.m2 -v /root/.ssh:/root/.ssh zhangyule1993/convert-app:v1.0.0.release