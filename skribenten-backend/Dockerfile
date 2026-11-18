# syntax=docker/dockerfile:1
FROM europe-north1-docker.pkg.dev/cgr-nav/pull-through/nav.no/jre:openjdk-25
COPY --exclude=*skribenten-backend-*.jar build/install/skribenten-backend/lib /app/lib
COPY build/install/skribenten-backend/lib/skribenten-backend-*.jar /app/lib
# PDFBox gir warning viss Helvetica ikkje fins, sjølv om han ikkje blir brukt. Denne fila er eigentleg SourceSans3-Regular, men med endra metadata
COPY src/main/resources/fonts/Helvetica.ttf /usr/share/fonts/truetype/helvetica/Helvetica.ttf
COPY src/main/resources/fonts/LICENSE.txt /usr/share/fonts/truetype/helvetica/LICENSE.txt
EXPOSE 8080
WORKDIR /app
ENTRYPOINT ["java", "-cp", "lib/*", "no.nav.pensjon.brev.skribenten.SkribentenAppKt"]