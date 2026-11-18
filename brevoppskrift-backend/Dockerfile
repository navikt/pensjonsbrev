FROM gcr.io/distroless/java25-debian13:nonroot
COPY build/install/brevoppskrift-backend/lib /app/lib
WORKDIR /app
EXPOSE 8080
ENTRYPOINT ["java", "-cp", "lib/*", "no.nav.pensjon.brev.brevoppskrift.BrevoppskriftAppKt"]
