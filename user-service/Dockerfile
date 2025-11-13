# 1. 빌드(Build) 단계: Java 17과 Gradle을 사용하여 .jar 파일을 만듭니다.
FROM gradle:8.5.0-jdk17 AS BUILDER

# 작업 디렉토리 설정
WORKDIR /app

# build.gradle 파일만 먼저 복사하여 의존성을 캐시합니다.
COPY build.gradle settings.gradle ./
RUN gradle dependencies --no-daemon

# 전체 소스 코드를 복사합니다.
COPY src ./src

# 소스 코드가 복사된 후, 'build' 대신 'bootJar'를 실행
RUN gradle bootJar --no-daemon

# 2. 실행(Runtime) 단계: 신뢰할 수 있는 다른 OpenJDK 17 이미지로 변경
FROM eclipse-temurin:17-jre-focal

# 작업 디렉토리 설정
WORKDIR /app

# 빌드 단계(BUILDER)에서 생성된 .jar 파일을 복사합니다.
# .jar 파일 경로는 build/libs/demo-0.0.1-SNAPSHOT.jar가 기본입니다.
# (만약 build.gradle에서 이름을 바꿨다면 이 부분을 수정해야 합니다)
COPY --from=BUILDER /app/build/libs/*.jar app.jar

# 컨테이너가 8081 포트를 노출하도록 설정 (application.properties와 일치)
EXPOSE 8081

# 컨테이너 시작 시 실행될 명령어
ENTRYPOINT ["java", "-jar", "app.jar"]