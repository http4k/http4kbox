FROM findepi/graalvm:native as builder
WORKDIR /builder
ADD ./build/libs/native.jar /builder/native.jar

RUN native-image \
     -H:+ReportUnsupportedElementsAtRuntime \
     -H:IncludeResources=.*http4k.* \
     -J-Xms3G -J-Xmx4G --no-server \
     -jar native.jar

FROM alpine:latest
EXPOSE 10000
COPY --from=builder /builder/native .

CMD ["./native", "10000"]