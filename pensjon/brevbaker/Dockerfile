# syntax=docker/dockerfile:1
FROM gcr.io/distroless/java25-debian13:nonroot
COPY --exclude=*brevbaker-*.jar build/install/brevbaker/lib /app/lib
COPY build/install/brevbaker/lib/brevbaker-*.jar /app/lib
EXPOSE 8080
WORKDIR /app
ENTRYPOINT ["java", "-cp", "lib/*", "no.nav.pensjon.brev.BrevbakerAppKt"]