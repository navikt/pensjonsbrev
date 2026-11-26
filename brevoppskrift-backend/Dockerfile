# syntax=docker/dockerfile:1
FROM europe-north1-docker.pkg.dev/cgr-nav/pull-through/nav.no/jre:openjdk-25
COPY --exclude=*brevoppskrift-backend-*.jar build/install/brevoppskrift-backend/lib /app/lib
COPY build/install/brevoppskrift-backend/lib/brevoppskrift-backend-*.jar /app/lib
EXPOSE 8080
WORKDIR /app
ENTRYPOINT ["java", "-cp", "lib/*", "no.nav.pensjon.brev.brevoppskrift.BrevoppskriftAppKt"]
