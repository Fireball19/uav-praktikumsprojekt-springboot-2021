FROM gradle:jdk11 AS BUILD
WORKDIR /uav_build
COPY . /uav_build
RUN gradle bootJar

FROM adoptopenjdk:11-jre-hotspot
WORKDIR /uav
COPY --from=BUILD /uav_build/infrastructure/build/libs/infrastructure-*.jar \
                  /uav/infrastructure.jar
EXPOSE 8080

COPY --from=BUILD /uav_build/infrastructure/key.der /uav/infrastructure/key.der

CMD java -jar infrastructure.jar