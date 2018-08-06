FROM gliderlabs/alpine
RUN apk-install nginx && mkdir /tmp/nginx && mkdir -p /run/nginx
ADD ./nginx.conf /etc/nginx/nginx.conf
CMD nginx