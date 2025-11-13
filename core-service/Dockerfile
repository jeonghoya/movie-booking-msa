# 1. 빌드(Build) 단계
FROM gradle:8.5.0-jdk17 AS BUILDER
WORKDIR /app
COPY build.gradle settings.gradle ./
RUN gradle dependencies --no-daemon
COPY src ./src
# 소스 코드가 복사된 후, 'build' 대신 'bootJar'를 실행
RUN gradle bootJar --no-daemon

# 2. 실행(Runtime) 단계: 신뢰할 수 있는 다른 OpenJDK 17 이미지로 변경
FROM eclipse-temurin:17-jre-focal
WORKDIR /app

# .jar 파일 경로는 build/libs/demo-0.0.1-SNAPSHOT.jar가 기본입니다.
COPY --from=BUILDER /app/build/libs/*.jar app.jar

# 컨테이너가 8082 포트를 노출하도록 설정
EXPOSE 8082

ENTRYPOINT ["java", "-jar", "app.jar"]