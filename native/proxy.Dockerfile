FROM gliderlabs/alpine
RUN apk-install nginx && mkdir /tmp/nginx && mkdir -p /run/nginx
ADD ./nginx.conf /etc/nginx/nginx.conf
ARG bucketName
RUN sed -i.bak s/%BUCKET_HOST%/$bucketName.s3.amazonaws.com/g /etc/nginx/nginx.conf
CMD nginx