# 빌드 환경 
FROM gradle:8.5-jdk21 AS build
WORKDIR /app
COPY . .
RUN gradle clean build -x test

# 실행 환경
FROM amazoncorretto:21-alpine
WORKDIR /app

# 빌드 단계에서 생성된 jar 파일만 복사
COPY --from=build /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]