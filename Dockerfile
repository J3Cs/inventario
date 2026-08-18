# 1. Etapa de compilación (Build Stage)
FROM eclipse-temurin:25-jdk-alpine AS builder

WORKDIR /app

# Copiar archivos de configuración de Maven
COPY .mvn/ .mvn
COPY mvnw pom.xml ./

# Otorgar permisos de ejecución al wrapper
RUN chmod +x mvnw

# Descargar dependencias para aprovechar la caché de capas de Docker
RUN ./mvnw dependency:go-offline

# Copiar el código fuente y empaquetar
COPY src ./src
RUN ./mvnw clean package -DskipTests

# 2. Etapa de ejecución (Runtime Stage)
FROM eclipse-temurin:25-jre-alpine

WORKDIR /app

# Crear usuario no root por seguridad
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Copiar el archivo JAR generado en la etapa anterior
COPY --from=builder /app/target/*.jar app.jar

# Exponer el puerto predeterminado de Spring Boot
EXPOSE 8080

# Comando de inicio
ENTRYPOINT ["java", "-jar", "app.jar"]