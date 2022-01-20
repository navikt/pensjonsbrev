FROM navikt/java:17
USER root
ENV DEBIAN_FRONTEND="noninteractive" TZ="Europe/London"

RUN apt -y --allow-releaseinfo-change update && apt install -y \
    wget  \
    perl-tk \
#    texlive-xetex \
    texlive-base \
#    tzdata \
#    texlive-pictures \
#    texlive-fonts-recommended \
      && rm -rf /var/lib/apt/lists/*
RUN #apt install -y wget perl-tk
COPY containerFiles/install-tl-unx.tar.gz .
# install texlive package manager
RUN tar -xzf install-tl-unx.tar.gz
RUN (cd  ./install-tl-*/ \
    && touch test \
    && ./install-tl -profile test) #Install with empty profile for all default settings.

COPY containerFiles/fonts /usr/share/fonts
USER apprunner

COPY  build/libs/pdf-bygger.jar /app/app.jar
EXPOSE 8080
