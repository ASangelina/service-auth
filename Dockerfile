#fase de build
FROM amazoncorretto:21-alpine   as build
WORKDIR /app
COPY . .
# Adiciona permissão de execução ao mvnw
RUN chmod +x mvnw
RUN ./mvnw clean package

# fase para gerar a imagem docker de prod
FROM build as prod
WORKDIR /app
COPY --from=build /app/target/*.jar /app/application.jar
EXPOSE 8080:8080
CMD ["java", "-jar", "application.jar"]
