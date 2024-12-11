FROM --platform=linux/amd64 eclipse-temurin:21-jre

EXPOSE 8080

RUN groupadd -r --gid 1001 apprunner && useradd -r --uid 1001 -g apprunner apprunner

ENV DEBIAN_FRONTEND="noninteractive" TZ="Europe/London"
ENV PATH="${PATH}:/app/tex/bin/x86_64-linux/"
ENV TEXINPUTS=".:/app/pensjonsbrev_latex//:"
COPY containerFiles/fonts /usr/share/fonts
COPY containerFiles/latex /app/pensjonsbrev_latex
RUN apt update && apt -y install tzdata perl-tk && rm -rf /var/lib/apt/lists/*
#To update TeXLive/XeLaTeX version, run the github workflow and replace the --from argument to newest release
COPY --from=ghcr.io/navikt/pensjonsbrev/pensjon-latex:2022-09-15-12_53 /app /app

USER apprunner
COPY  build/libs/pdf-bygger.jar /app/app.jar
CMD ["java", "-jar", "/app/app.jar"]