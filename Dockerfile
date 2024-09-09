FROM amazoncorretto:21
LABEL authors="zihluwang"

ENV TZ="Asia/Shanghai"
ENV ACTIVE_PROFILES=""

WORKDIR /app
ADD build/libs/score-sense-server-0.1.0.jar app.jar

ENTRYPOINT ["/bin/sh", "-c", "java -jar -Dspring.profiles.active=$ACTIVE_PROFILES app.jar"]