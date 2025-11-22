# syntax=docker/dockerfile:1
FROM gcr.io/distroless/java21-debian12
COPY --exclude=*tjenestebuss-integrasjon-*.jar build/install/tjenestebuss-integrasjon/lib /app/lib
COPY build/install/tjenestebuss-integrasjon/lib/tjenestebuss-integrasjon-*.jar /app/lib
EXPOSE 8080
ENTRYPOINT ["java", "-cp", "/app/lib/*", "no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.TjenestebussIntegrasjonApplicationKt"]