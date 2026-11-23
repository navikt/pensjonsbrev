# syntax=docker/dockerfile:1
FROM europe-north1-docker.pkg.dev/cgr-nav/pull-through/nav.no/jre:openjdk-25
COPY --exclude=*brevbaker-*.jar build/install/brevbaker/lib /app/lib
COPY build/install/brevbaker/lib/brevbaker-*.jar /app/lib
EXPOSE 8080
WORKDIR /app
ENTRYPOINT ["java", "-cp", "lib/*", "no.nav.pensjon.brev.BrevbakerAppKt"]