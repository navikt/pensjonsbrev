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
RUN mkdir -p /home/apprunner/.cache/fontconfig
ENV XDG_CACHE_HOME="/home/apprunner/.cache/fontconfig"
RUN apt update && apt -y install tzdata perl-tk fontconfig && rm -rf /var/lib/apt/lists/*
#To update TeXLive/XeLaTeX version, run the github workflow and replace the --from argument to newest release
COPY --from=ghcr.io/navikt/pensjonsbrev/pensjon-latex:2025-09-08-11_43 /app /app
COPY containerFiles/fonts /usr/share/fonts
RUN fc-cache --really-force /usr/share/fonts # Build font cache for faster latex compile time. About 400 ms faster per letter
COPY containerFiles/latex /app/pensjonsbrev_latex

RUN chown -R apprunner:apprunner /home/apprunner
RUN chown -R apprunner:apprunner /var/cache/fontconfig
USER apprunner
COPY --exclude=*pdf-bygger-*.jar build/install/pdf-bygger/lib /app/lib
COPY build/install/pdf-bygger/lib/pdf-bygger-*.jar /app/lib
ENTRYPOINT ["java", "-cp", "/app/lib/*", "no.nav.pensjon.brev.pdfbygger.PdfByggerAppKt"]