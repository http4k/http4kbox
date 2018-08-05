FROM findepi/graalvm:native as builder
WORKDIR /builder
ADD ./build/libs/native.jar /builder/native.jar
ADD ./reflectconfig.json /builder/reflectconfig.json

RUN native-image \
     -H:+ReportUnsupportedElementsAtRuntime \
     -H:ReflectionConfigurationFiles=reflectconfig.json \
     -H:EnableURLProtocols=http \
     -H:IncludeResources=.*http4k.* \
     -J-Xms3G -J-Xmx4G --no-server \
     -jar native.jar

FROM debian:stable-slim
EXPOSE 10000
COPY --from=builder /builder/native /native

CMD ["./native"]