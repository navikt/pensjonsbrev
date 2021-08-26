FROM navikt/java:15 as latexbase
USER root
ENV DEBIAN_FRONTEND="noninteractive" TZ="Europe/London"

# allow non-free sources for microsoft fonts
COPY container/nonfree.sh nonfree.sh
RUN ./nonfree.sh

RUN apt -y --allow-releaseinfo-change update && apt install -y \
    texlive-xetex \
    tzdata \
    texlive-pictures \
    texlive-fonts-recommended \
    ttf-mscorefonts-installer \
      && rm -rf /var/lib/apt/lists/*

USER apprunner

####
# Build layer
#
FROM navikt/java:15 as build

RUN mkdir -p /tmp/gradle_cache && \
    mkdir -p /tmp/code/

WORKDIR /tmp/code

# Prepare gradle and project dependencies
ENV GRADLE_USER_HOME /tmp/gradle_cache
COPY --chown=apprunner:apprunner gradle /tmp/code/gradle
COPY --chown=apprunner:apprunner build.gradle.kts gradlew gradle.properties settings.gradle /tmp/code/
RUN ./gradlew clean build test --info --stacktrace

# Build project
COPY --chown=apprunner:apprunner . /tmp/code
RUN ./gradlew build

###
# Application container
#
FROM latexbase
COPY --from=build /tmp/code/build/libs/pdf-bygger.jar /app/app.jar
EXPOSE 8080