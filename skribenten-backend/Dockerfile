FROM gcr.io/distroless/java21-debian12
COPY build/install/*/lib /app/lib
EXPOSE 8080
ENTRYPOINT ["java", "-cp", "/app/lib/*", "no.nav.pensjon.brev.skribenten.SkribentenAppKt"]