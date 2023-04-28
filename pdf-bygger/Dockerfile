FROM ghcr.io/navikt/baseimages/temurin:17
USER root
ENV DEBIAN_FRONTEND="noninteractive" TZ="Europe/London"
ENV PATH="${PATH}:/app/tex/bin/x86_64-linux/"
COPY containerFiles/fonts /usr/share/fonts
RUN apt -y --allow-releaseinfo-change -o Acquire::Check-Valid-Until=false update
RUN apt -y install tzdata perl-tk

#To update TeXLive/XeLaTeX version, run the github workflow and replace the --from argument to newest release
COPY --from=ghcr.io/navikt/pensjonsbrev/pensjon-latex:2022-09-15-12_53 /app /app
USER apprunner

COPY  build/libs/pdf-bygger.jar /app/app.jar
EXPOSE 8080