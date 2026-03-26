# syntax=docker/dockerfile:1
# --- Stage 1: JRE Build ---
FROM --platform=linux/amd64 eclipse-temurin:25 AS jre-build
RUN $JAVA_HOME/bin/jlink \
         --add-modules java.base,jdk.jdwp.agent,java.xml,java.naming,java.management,java.instrument,jdk.unsupported,jdk.management,jdk.localedata \
         --strip-debug \
         --no-man-pages \
         --no-header-files \
         --compress=zip-6 \
         --output /javaruntime

# --- Stage 2: Typst Downloader & Verifier ---
FROM --platform=linux/amd64 debian:trixie-slim AS typst-downloader
# Replace with the specific version and verified SHA256 hash
ARG TYPST_VERSION=0.14.2
ARG TYPST_HASH="a6044cbad2a954deb921167e257e120ac0a16b20339ec01121194ff9d394996d"
RUN apt-get update && apt-get install -y --no-install-recommends curl xz-utils ca-certificates && rm -rf /var/lib/apt/lists/*
WORKDIR /typst
RUN curl -sSLO "https://github.com/typst/typst/releases/download/v${TYPST_VERSION}/typst-x86_64-unknown-linux-musl.tar.xz" \
    && echo "${TYPST_HASH}  typst-x86_64-unknown-linux-musl.tar.xz" | sha256sum -c - \
    && tar -xJf typst-x86_64-unknown-linux-musl.tar.xz --strip-components=1

# --- Stage 3: Final Runtime ---
FROM --platform=linux/amd64 debian:trixie-slim AS final
ENV JAVA_HOME=/opt/java/openjdk
ENV PATH="${JAVA_HOME}/bin:${PATH}"
COPY --from=jre-build /javaruntime $JAVA_HOME

EXPOSE 8080

RUN groupadd -r --gid 1001 apprunner && useradd -r --uid 1001 -g apprunner apprunner

ENV DEBIAN_FRONTEND="noninteractive" TZ="Europe/London"
ENV PATH="${PATH}:/app/tex/bin/x86_64-linux/:/app/typst/"
ENV TEXINPUTS=".:/app/pensjonsbrev_latex//:"
ENV FONTCONFIG_FILE=/usr/share/fonts/fonts.conf
ENV XDG_CACHE_HOME=/tmp/.cache
ENV XDG_DATA_HOME=/tmp/.local/share

# Viktig å kopiere filer før vi installerer fontconfig, for den starter umiddelbart å bygge cachen
COPY containerFiles/fonts /usr/share/fonts
RUN apt update && apt install -y --no-install-recommends tzdata perl-tk fontconfig && rm -rf /var/lib/apt/lists/*
#To update TeXLive/XeLaTeX version, run the github workflow and replace the --from argument to newest release
COPY --from=ghcr.io/navikt/pensjonsbrev/pensjon-latex:2025-09-08-11_43 /app /app
COPY containerFiles/latex /app/pensjonsbrev_latex

COPY --from=typst-downloader /typst/typst /app/typst/typst
COPY containerFiles/typst /app/typst

COPY --exclude=*pdf-bygger-*.jar build/install/pdf-bygger/lib /app/lib
COPY build/install/pdf-bygger/lib/pdf-bygger-*.jar /app/lib

# Make /app read-only for apprunner (owned by root, readable/executable by others)
RUN chown -R root:root /app && chmod -R 755 /app && chmod -R o+w /tmp
USER apprunner
ENTRYPOINT ["java", "-cp", "/app/lib/*", "no.nav.pensjon.brev.pdfbygger.PdfByggerAppKt"]