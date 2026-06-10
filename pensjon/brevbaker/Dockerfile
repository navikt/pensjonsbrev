# syntax=docker/dockerfile:1
FROM gcr.io/distroless/java25-debian13:nonroot
COPY --exclude=*brevbaker-*.jar build/install/brevbaker/lib /app/lib
COPY build/install/brevbaker/lib/brevbaker-*.jar /app/lib
# PDFBox gir warning viss Helvetica ikkje fins, sjølv om han ikkje blir brukt. Denne fila er eigentleg SourceSans3-Regular, men med endra metadata
COPY src/main/resources/fonts/Helvetica.ttf /usr/share/fonts/truetype/helvetica/Helvetica.ttf
EXPOSE 8080
WORKDIR /app
ENTRYPOINT ["java", "-cp", "lib/*", "no.nav.pensjon.brev.BrevbakerAppKt"]