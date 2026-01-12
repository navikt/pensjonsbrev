# syntax=docker/dockerfile:1
FROM gcr.io/distroless/java25-debian13
COPY --exclude=*pensjon-brevbaker-*.jar build/install/pensjon-brevbaker/lib /app/lib
COPY build/install/pensjon-brevbaker/lib/pensjon-brevbaker-*.jar /app/lib
EXPOSE 8080
ENTRYPOINT ["java", "-cp", "/app/lib/*", "no.nav.pensjon.brev.BrevbakerAppKt"]