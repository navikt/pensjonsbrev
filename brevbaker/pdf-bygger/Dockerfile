# syntax=docker/dockerfile:1
# --- Stage 1: JRE Build ---
FROM --platform=linux/amd64 eclipse-temurin:25 AS jre-build

# Module list derived from `jdeps --print-module-deps` against the installed
# jdeps --multi-release 25 --ignore-missing-deps --print-module-deps --class-path 'brevbaker/pdf-bygger/build/install/pdf-bygger/lib/*' brevbaker/pdf-bygger/build/install/pdf-bygger/lib/pdf-bygger-*.jar

#using jlink instead to not include modules we dont need. Saves a bunch of space.
RUN $JAVA_HOME/bin/jlink \
         --add-modules java.base,jdk.jdwp.agent,java.xml,java.naming,java.management,java.instrument,jdk.unsupported,jdk.management,jdk.localedata \
         --strip-debug \
         --no-man-pages \
         --no-header-files \
         --compress=zip-6 \
         --output /javaruntime

# --- Stage 2: Typst binary + Noto fonts ---
FROM --platform=linux/amd64 debian:trixie-slim AS assets
ARG TYPST_VERSION=0.14.2
ARG TYPST_HASH="a6044cbad2a954deb921167e257e120ac0a16b20339ec01121194ff9d394996d"
RUN apt-get update && apt-get install -y --no-install-recommends \
        curl xz-utils ca-certificates fonts-noto-core \
    && rm -rf /var/lib/apt/lists/*
WORKDIR /typst
RUN curl -sSLO "https://github.com/typst/typst/releases/download/v${TYPST_VERSION}/typst-x86_64-unknown-linux-musl.tar.xz" \
    && echo "${TYPST_HASH}  typst-x86_64-unknown-linux-musl.tar.xz" | sha256sum -c - \
    && tar -xJf typst-x86_64-unknown-linux-musl.tar.xz --strip-components=1

# --- Stage 3: Final Runtime
FROM --platform=linux/amd64 gcr.io/distroless/base-debian13:nonroot
ENV JAVA_HOME=/opt/java/openjdk
ENV PATH="${JAVA_HOME}/bin:/app/typst/:${PATH}"
ENV TZ="Europe/London"

EXPOSE 8080

COPY --from=jre-build /javaruntime $JAVA_HOME

COPY --from=assets /typst/typst /app/typst/typst
COPY --from=assets /usr/share/fonts/truetype/noto /usr/share/fonts/truetype/noto
# `nice` (fra coreutils) brukes til å starte typst med lavere OS-prioritet enn
# JVM-prosessen. Det sikrer at Ktor/Netty alltid får CPU under høy compile-last,
# slik at /isAlive og /isReady svarer kjapt selv om mange typst-prosesser kjører.
COPY --from=assets /usr/bin/nice /usr/bin/nice
COPY containerFiles/typst /app/typst
COPY containerFiles/fonts /app/typst/fonts

COPY --exclude=*pdf-bygger-*.jar build/install/pdf-bygger/lib /app/lib
COPY build/install/pdf-bygger/lib/pdf-bygger-*.jar /app/lib

WORKDIR /app

ENTRYPOINT ["java", "-cp", "/app/lib/*", "no.nav.pensjon.brev.pdfbygger.PdfByggerAppKt"]