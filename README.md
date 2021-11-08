# pensjonsbrev
This is a mono-repo for the microservices that together form the new letter ordering system.


## Lokal kjøring av brevbaker og pdf-bygger

For å kjøre løsningen lokalt må man ha docker og docker compose installert.
Kjør `docker compose up -d` i rot-katalogen til prosjektet.

### Debugge pdf-bygger lokalt
Pdf-byggeren er avhengig av ulike pakker for å kompilere LaTeX til pdf.
Derfor må man kjøre pdf-bygger java applikasjonen inne i containeren for at den skal fungere.
For å debugge pdf-byggeren må man derfor sette opp remote debug mot applikasjonen som kjører inne i containeren.

1. Lag en ny run configuration i Intellij av typen Dockerfile.
   * Sett bind ports til `8081:8080 5005:5005`
   * Sett en environment variabel `JAVA_OPTS=-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005`
     Om denne verdien er forskjellig i neste steg, erstatt den.
   
2. Lag en ny run configuration av typen remote jvm debug. Under before launch legg til "Run another configuration" og velg
    docker compose konfigurasjonen vi lagde i forrige steg. 
3. Kjør remote jvm debug konfigurasjonen.