FROM openjdk:17-slim AS builder

ARG ENV=local
ARG ENABLE_BUILD=true

WORKDIR /code

COPY ./ .
#COPY ./.run ./.run
#COPY ./config/app.${ENV}.env ./config/app.env

ENV JVM_OPTS="-Xmx2048m"
ENV GRADLE_OPTS="-Xmx1536m -XX:+HeapDumpOnOutOfMemoryError -Dkotlin.compiler.execution.strategy=in-process -Dkotlin.incremental=false"

RUN if [ "$ENABLE_BUILD" = "true" ] ; then \
    ./gradlew :application:signup:build -x test ; \
    fi

RUN if [ "$(ls -l /code/application/signup/build/libs/*.jar | grep .jar)" = "" ] ; then \
    ./gradlew :application:signup:build -x test ; \
    fi

# Runner
FROM openjdk:17-slim

ARG ENV=local

WORKDIR /app

# RUN addgroup -S spring && adduser -S spring -G spring
RUN adduser spring

COPY --from=builder /code/application/signup/build/libs/*.jar /app/libs/app-signup-backend.jar
COPY --from=builder /code/config/app.dev.env /app/config/app.env
COPY --from=builder /code/.run /app/.run

RUN chown -R spring:spring /app
RUN chmod a+x /app/.run

RUN mkdir -p /data/files && chown -R spring:spring /data/files && chmod -R a+w /data/files
RUN mkdir -p /data/logs && chown -R spring:spring /data/logs && chmod -R a+w /data/logs

VOLUME /data/files
VOLUME /data/logs

EXPOSE 8080
EXPOSE 5080

USER spring:spring

ENTRYPOINT [ "sh", "/app/.run", "--run-mode=docker"]
