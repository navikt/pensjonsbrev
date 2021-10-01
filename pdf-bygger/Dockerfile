FROM navikt/java:15
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

COPY  build/libs/pdf-bygger.jar /app/app.jar
EXPOSE 8080
