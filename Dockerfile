FROM amazoncorretto:21
LABEL authors="zihluwang"
LABEL org.opencontainers.image.source="https://github.com/OnixByte-Business/score-sense"

ARG APP_VERSION=""

ENV TZ="Asia/Shanghai"
ENV ACTIVE_PROFILES=""

WORKDIR /app
ADD build/libs/score-sense-server-$APP_VERSION.jar app.jar

ENTRYPOINT ["/bin/sh", "-c", "java -jar -Dspring.profiles.active=$ACTIVE_PROFILES app.jar"]