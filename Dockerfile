FROM eclipse-temurin:24-jre

WORKDIR /app

COPY foxy/build/libs/MarisaStuff-*.jar MarisaStuff.jar

ENV JAVA_OPTS="-Xms256M -Xmx512M"
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar MarisaStuff.jar"]