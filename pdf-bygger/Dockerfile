FROM --platform=linux/amd64 eclipse-temurin:21-jre

EXPOSE 8080

RUN groupadd -r --gid 1001 apprunner && useradd -r --uid 1001 -g apprunner apprunner

ENV DEBIAN_FRONTEND="noninteractive" TZ="Europe/London"
ENV PATH="${PATH}:/app/tex/bin/x86_64-linux/"
ENV TEXINPUTS=".:/app/pensjonsbrev_latex//:"
RUN apt remove wget -y
RUN apt update && apt -y install tzdata perl-tk && rm -rf /var/lib/apt/lists/*
#To update TeXLive/XeLaTeX version, run the github workflow and replace the --from argument to newest release
COPY --from=ghcr.io/navikt/pensjonsbrev/pensjon-latex:2024-12-30-14_56 /app /app
COPY containerFiles/fonts /usr/share/fonts
RUN fc-cache /usr/share/fonts # Build font cache for faster latex compile time. About 400 ms faster per letter
COPY containerFiles/latex /app/pensjonsbrev_latex

USER apprunner
COPY  build/libs/pdf-bygger.jar /app/app.jar
CMD ["java", "-jar", "/app/app.jar"]