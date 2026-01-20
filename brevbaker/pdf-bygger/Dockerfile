# syntax=docker/dockerfile:1
FROM --platform=linux/amd64 eclipse-temurin:25 AS jre-build
RUN $JAVA_HOME/bin/jlink \
         --add-modules java.base,jdk.jdwp.agent,java.xml,java.naming,java.management,java.instrument,jdk.unsupported,jdk.management,jdk.localedata \
         --strip-debug \
         --no-man-pages \
         --no-header-files \
         --compress=zip-6 \
         --output /javaruntime

FROM --platform=linux/amd64 debian:trixie-slim
ENV JAVA_HOME=/opt/java/openjdk
ENV PATH="${JAVA_HOME}/bin:${PATH}"
COPY --from=jre-build /javaruntime $JAVA_HOME

EXPOSE 8080

RUN groupadd -r --gid 1001 apprunner && useradd -r --uid 1001 -g apprunner apprunner

ENV DEBIAN_FRONTEND="noninteractive" TZ="Europe/London"
ENV PATH="${PATH}:/app/tex/bin/x86_64-linux/"
ENV TEXINPUTS=".:/app/pensjonsbrev_latex//:"
ENV FONTCONFIG_FILE=/usr/share/fonts/fonts.conf
ENV XDG_CACHE_HOME=/tmp/.cache
# Viktig å kopiere filer før vi installerer fontconfig, for den starter umiddelbart å bygge cachen
COPY containerFiles/fonts /usr/share/fonts
RUN apt update && apt -y install tzdata perl-tk fontconfig && rm -rf /var/lib/apt/lists/*
#To update TeXLive/XeLaTeX version, run the github workflow and replace the --from argument to newest release
COPY --from=ghcr.io/navikt/pensjonsbrev/pensjon-latex:2025-09-08-11_43 /app /app
COPY containerFiles/latex /app/pensjonsbrev_latex

RUN chmod -R o+w /tmp
USER apprunner
COPY --exclude=*pdf-bygger-*.jar build/install/pdf-bygger/lib /app/lib
COPY build/install/pdf-bygger/lib/pdf-bygger-*.jar /app/lib
ENTRYPOINT ["java", "-cp", "/app/lib/*", "no.nav.pensjon.brev.pdfbygger.PdfByggerAppKt"]