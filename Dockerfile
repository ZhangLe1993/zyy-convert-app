FROM docker.io/zhangyule1993/java-base:v1.0.0
# 安装yasm (nasm/yasm必须有其一)
RUN yum -y update && yum install -y net-tools \
	libgomp.x86_64 \
	libtool-ltdl-devel.x86_64 \
	vim \
	psmisc.x86_64 \
	libuuid-devel.x86_64 \
	zlib-devel.x86_64 \
	unixODBC-devel.x86_64 \
	mariadb-devel.x86_64 \
	dos2unix \
	make \
	gcc \
	gcc-c++ \
	kernel-devel \
	autoconf \
	automake

WORKDIR /home
# 安装yasm
RUN wget https://www.tortall.net/projects/yasm/releases/yasm-1.3.0.tar.gz
RUN tar -zxvf yasm-1.3.0.tar.gz
RUN cd /home/yasm-1.3.0 && ./configure && make && make install

#安装ffmpeg 必须要加--enable-shared 否则不能生成so动态库文件  尽量使用 && 连接符号连接命令，可以使镜像的体积变小。减少构建的层数。
RUN wget http://www.ffmpeg.org/releases/ffmpeg-4.2.2.tar.gz
RUN tar -zxvf ffmpeg-4.2.2.tar.gz
RUN cd /home/ffmpeg-4.2.2 && ./configure  --disable-static --enable-shared --enable-gpl --enable-nonfree --enable-pthreads --enable-encoders --enable-decoders --prefix=/usr/local/ffmpeg \
&& make && make install
#设置环境变量
ENV FFMPEG_HOME /usr/local/ffmpeg
ENV LD_LIBRARY_PATH /usr/local/ffmpeg/lib
ENV PATH $PATH:$FFMPEG_HOME/bin:$LD_LIBRARY_PATH
# 项目相关
ENV active $ACTIVE

ENV folder $folder

COPY settings.xml /home/settings.xml

COPY deploy.sh /home/deploy.sh

COPY /out/app.jar /home/app.jar

WORKDIR /home

RUN mkdir upload

CMD ["sh", "deploy.sh"]