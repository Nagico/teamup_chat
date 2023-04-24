FROM amazoncorretto:17-alpine-jdk as spring

RUN apk -U upgrade

RUN apk add curl

RUN apk upgrade musl

WORKDIR /etc/spring

EXPOSE 8060

HEALTHCHECK --interval=30s --timeout=3s --retries=3 CMD curl http://localhost:8060 || exit 1

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/etc/spring/app.jar"]

COPY build/libs/lib lib

COPY build/libs/app.jar app.jar