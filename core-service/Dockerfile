# 1. 빌드(Build) 단계
FROM gradle:8.5.0-jdk17 AS BUILDER
WORKDIR /app
COPY build.gradle settings.gradle ./
RUN gradle dependencies --no-daemon
COPY src ./src
RUN gradle build --no-daemon -x test

# 2. 실행(Runtime) 단계
FROM openjdk:17-jre-slim
WORKDIR /app

# .jar 파일 경로는 build/libs/demo-0.0.1-SNAPSHOT.jar가 기본입니다.
COPY --from=BUILDER /app/build/libs/*.jar app.jar

# 컨테이너가 8082 포트를 노출하도록 설정
EXPOSE 8082

ENTRYPOINT ["java", "-jar", "app.jar"]