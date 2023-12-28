# Pensjonsbrev
This is a mono-repo for the microservices that together form the new letter ordering system.


### Lokal kjøring av skribenten backend/front-end og brevbaker/pdf-bygger

1. For å hente alle secrets må du ha installert:
   * kubectl
   * python
   * vault
   * gcloud cli
   * docker/colima
   * naisdevice med standard dev-miljø tilganger og tjenestebuss-q2
2. Hent alle secrets:
   ```bash
   (cd skribenten-backend && ./fetch-secrets.sh)
   (cd tjenestebuss-integrasjon && ./fetch-secrets.sh)
   (cd skribenten-web/bff && python3 setup_local_azure_secrets.py)
   ```
3. For å hente enkelte avhengigheter under byggene må du [lage ett github token](https://github.com/settings/tokens/new)
   med `packages.read` tilgang i `$HOME/.gradle/gradle.properties`:
   ```
   gpr.user=<github brukernavn>
   gpr.token=<packages.read token>
   ```
   Tilsvarende for trengs for npm i `$HOME/.npmrc`:
   ```
   //npm.pkg.github.com/:_authToken=<packages.read token>
   ```
4. Kjør følgende for å bygge alle applikasjonene og publisere docker images til lokalt registry:
   ```bash
   ./gradlew :tjenestebuss-integrasjon:publishImageToLocalRegistry :skribenten-backend:build :pensjon-brevbaker:build :pdf-bygger:build
   (cd skribenten-web/bff && npm i && npm run build)
   (cd skribenten-web/frontend && npm i)
   ```
5. Kjør alle backend-tjenester
   ```bash
   docker-compose --profile skribenten up -d --build
   ```
6. Kjør front-end. Applikasjonen krever at du logger på med en @trygdeetaten.no test bruker med saksbehandler tilganger.
   ```bash
   npm run dev --prefix skribenten-web/frontend
   ```
7. Åpne http://localhost:8083/vite-on for å koble front-enden opp mot bff(backend for front-end).



## Lokal kjøring av brevbaker og pdf-bygger

For å kjøre løsningen lokalt må man ha docker og docker compose installert.
Kjør `docker compose up -d` i rot-katalogen til prosjektet.

### Debugge pdf-bygger lokalt
Pdf-byggeren er avhengig av ulike pakker for å kompilere LaTeX til pdf.
Derfor må man kjøre pdf-bygger java applikasjonen inne i containeren for at den skal fungere.
Når man kjører lokalt med docker compose er den allerede satt opp til remote debug på port 5016 ([se docker-compose.yml](docker-compose.yml)).

### Ytelsestesting med locust
Ytelsestesten er i utgangspunktet satt opp til å teste vedtaksbrevet UNG_UFOER_AUTO.
1. Evt. rediger `locust/autobrev_request.json` om du ønsker å teste et annet brev.
2. Kjør `./locust/fetch-secrets.sh`
3. Start docker compose med locust profil `docker compose --profile locust up`
4. Gå inn på locust grensesnittet via http://localhost:8089/ og skriv inn url til endepunktet du ønsker å ytelses-teste.
[Se dokumentasjon fra locust for mer info om bruk.](http://docs.locust.io/en/stable/quickstart.html#locust-s-web-interface)

## Endring av obligatoriske felter i API-model

Brevbakeren bruker pensjon-brevbaker-api-model for bestilling av brev.
Api modellen eksporteres som artifakt og brukes av eksterne systemer for å fylle ut informasjon som kreves ved bestilling av brev.

Vi må kunne endre på obligatoriske felter i api modellen uten å ødelegge pågående brevbestillinger i produksjon.
For å oppnå dette må man ha en overgangsperiode hvor brevbakeren er kompatibel med gammel og ny versjon av api modellen.
Vi kan ikke bytte direkte over til ny versjon uten å gjøre avsender inkompatibel med mottaker(brevbaker), noe som vil kreve nedetid.

### Lage duplikate felter i en overgangsperiode

La oss si at vi skal erstatte ett felt annetBeloep med barnetillegg:

```
// før
data class PensjonsBrevDto(
    val annetBeloep: Int,
)

// etter
data class PensjonsBrevDto(
    val barnetillegg: Kroner,
)
```

En strategi for overgangen kan se slik ut:
1. Påkrev begge versjoner av feltene samtidig og bruk denne modellen på avsender-siden. 
   Brevbakeren er satt opp til å ignorere ukjente felter(fail on unknown properties=false), så den vil fortsette å lese annetBeloep i dette tilfellet.
    ```
    // overgangsperiode
    data class PensjonsBrevDto(
        val barnetillegg: Kroner,
        val annetBeloep: Int,
    )
    ```
2. Ta i bruk de nye feltene i Brevbakeren. Nå vil brevbakeren ignorere det gamle feltet og bruke det nye:
   ```
   // 
   data class PensjonsBrevDto(
       val barnetillegg: Kroner,
   )
    ```
3. Ta i bruk den nye versjonen i avsender systemet.