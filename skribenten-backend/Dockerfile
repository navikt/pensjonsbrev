# syntax=docker/dockerfile:1
FROM gcr.io/distroless/java25-debian13:nonroot
COPY --exclude=*skribenten-backend-*.jar build/install/skribenten-backend/lib /app/lib
COPY build/install/skribenten-backend/lib/skribenten-backend-*.jar /app/lib
EXPOSE 8080
WORKDIR /app
ENTRYPOINT ["java", "-cp", "lib/*", "no.nav.pensjon.brev.skribenten.SkribentenAppKt"]