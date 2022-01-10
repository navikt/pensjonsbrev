FROM navikt/java:17
USER root
ENV DEBIAN_FRONTEND="noninteractive" TZ="Europe/London"

RUN apt -y --allow-releaseinfo-change update && apt install -y \
    texlive-xetex \
    tzdata \
    texlive-pictures \
    texlive-fonts-recommended \
      && rm -rf /var/lib/apt/lists/*
COPY containerFiles/fonts /usr/share/fonts
USER apprunner

COPY  build/libs/pdf-bygger.jar /app/app.jar
EXPOSE 8080
