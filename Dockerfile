FROM maven:3.8.4-openjdk-17 AS build

WORKDIR /app

COPY . /app

RUN mvn package -DskipTests

FROM openjdk:17-jdk

COPY --from=build /app/target/*.jar /app/sistema_restaurante.jar

ENTRYPOINT ["java","-jar","/app/sistema_restaurante.jar"]