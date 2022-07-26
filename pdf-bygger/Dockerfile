FROM navikt/java:17
USER root
ENV DEBIAN_FRONTEND="noninteractive" TZ="Europe/London"
COPY containerFiles/fonts /usr/share/fonts
COPY containerFiles/preferences.d /etc/apt/preferences.d
COPY containerFiles/sources.list.d /etc/apt/sources.list.d
RUN mv /etc/apt/sources.list /etc/apt/sources.list.old \
    && cp /etc/apt/sources.list.old /etc/apt/sources.list.d/stable.list
RUN apt -y --allow-releaseinfo-change -o Acquire::Check-Valid-Until=false update
RUN apt -y install tzdata
RUN apt install  -y -t testing\
    texlive-xetex=2021.20220204-1 \
    texlive-pictures=2021.20220204-1 \
    texlive-fonts-recommended=2021.20220204-1 \
      && rm -rf /var/lib/apt/lists/*
USER apprunner

COPY  build/libs/pdf-bygger.jar /app/app.jar
EXPOSE 8080
