# syntax=docker/dockerfile:1
FROM gcr.io/distroless/java25-debian13
COPY --exclude=*skribenten-backend-*.jar build/install/skribenten-backend/lib /app/lib
COPY build/install/skribenten-backend/lib/skribenten-backend-*.jar /app/lib
EXPOSE 8080
ENTRYPOINT ["java", "-cp", "/app/lib/*", "no.nav.pensjon.brev.skribenten.SkribentenAppKt"]