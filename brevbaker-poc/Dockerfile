FROM navikt/java:15 as latexbase
USER root
ENV DEBIAN_FRONTEND="noninteractive" TZ="Europe/London"
#TODO no-cache
RUN apt-get -y install tzdata
RUN apt-get -y install texlive-latex-base  texlive-pictures
RUN apt-get -y install texlive-fonts-recommended

FROM latexbase as brevmaker
COPY build/libs/brevmaker-0.0.1-SNAPSHOT.jar app.jar
COPY src/main/resources/brev /app/

