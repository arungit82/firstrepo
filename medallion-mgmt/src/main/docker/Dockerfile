FROM alpine:3.3

RUN apk update && \
    apk upgrade && \
    apk add ca-certificates && \
    rm -rf /var/cache/apk/*

# openjdk-8-base contains no GUI support.
RUN echo '@v3.3_main http://nl.alpinelinux.org/alpine/v3.3/main' >> /etc/apk/repositories && \
    echo '@v3.3_community http://nl.alpinelinux.org/alpine/v3.3/community' >> /etc/apk/repositories && \
    apk update && \
    apk upgrade &&  \
    apk add "openjdk8-jre-base@v3.3_community=8.92.14-r0" &&  \
    rm -rf /var/cache/apk/*

# Springboot Sample Application
VOLUME /tmp

ADD medallion-mgmt-0.1.0.jar app.jar
RUN sh -c 'touch /app.jar'
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]