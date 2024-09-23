FROM amazoncorretto:21
LABEL authors="zihluwang"

ENV TZ="Asia/Shanghai"
ENV ACTIVE_PROFILES=""

WORKDIR /app
ADD build/libs/score-sense-server-canary-2409231424.jar app.jar

ENTRYPOINT ["/bin/sh", "-c", "java -jar -Dspring.profiles.active=$ACTIVE_PROFILES app.jar"]