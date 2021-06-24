FROM navikt/java:15 as latexbase
USER root
ENV DEBIAN_FRONTEND="noninteractive" TZ="Europe/London"
#TODO no-cache
RUN apt-get -y install tzdata
RUN apt-get -y install texlive-latex-base  texlive-pictures
RUN apt-get -y install texlive-fonts-recommended

# Plugin and dependency caching layer
FROM latexbase as gradle-cache
RUN mkdir -p /tmp/gradle_cache
RUN mkdir -p /tmp/code/
COPY  gradle /tmp/code/gradle
ENV GRADLE_USER_HOME /tmp/gradle_cache
COPY build.gradle.kts gradlew gradle.properties settings.gradle /tmp/code/
RUN (cd /tmp/code && ./gradlew clean build test --info --stacktrace)

FROM gradle-cache as build
# prepare gradle layer
# prepare layer with code
COPY . /tmp/code
RUN (cd /tmp/code && ./gradlew build)

FROM build as brevmaker
COPY --from=build /tmp/code/build/libs/pdf-bygger.jar /app/app.jar
EXPOSE 8080