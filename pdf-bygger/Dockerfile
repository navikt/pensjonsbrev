FROM navikt/java:15 as latexbase
USER root
ENV DEBIAN_FRONTEND="noninteractive" TZ="Europe/London"
#TODO no-cache
RUN apt-get -y install tzdata
RUN apt-get -y install texlive-latex-base  texlive-pictures
RUN apt-get -y install texlive-fonts-recommended

FROM latexbase as build
# prepare gradle layer
RUN mkdir -p /code/gradle
COPY  /gradlew /settings.gradle /code/
COPY  gradle /code/gradle
RUN (cd /code && ls)
RUN (cd /code && ./gradlew wrapper)
# prepare layer with code
COPY . /code
RUN (cd /code && ./gradlew build)

FROM build as brevmaker
COPY --from=build /code/build/libs/pdf-bygger.jar /app/app.jar
EXPOSE 8080
