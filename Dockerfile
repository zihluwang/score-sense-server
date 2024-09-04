FROM amazoncorretto:21
LABEL authors="zihluwang"

ENV TZ="Asia/Shanghai"
ENV ACTIVE_PROFILES=""

WORKDIR /app
ADD build/libs/score-sense-server-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["/bin/sh", "-c", "java -jar -Dspring.profiles.active=$ACTIVE_PROFILES app.jar"]