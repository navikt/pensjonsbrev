# Pensjonsbrev
This is a mono-repo for the microservices that together form the new letter ordering system.

## Lokal kjøring av brevbaker og pdf-bygger

For å kjøre løsningen lokalt må man ha docker og docker compose installert.
Bygging av brevbakeren krever at du har konfigurert gradle med packages.read token for å hente pakker.
Se [seksjonen under for oppsett av read token i gradle](#for-gradle).

Bruk følgende for å bygge og kjøre:
```bash
./gradlew :pensjon-brevbaker:build :pdf-bygger:build
```
Dersom du kun skal kjøre brevbaker og pdf-bygger og ikke skribenten må du fortsatt pga en bug i docker-compose generere tomme env files for skribenten:
```bash
(mkdir -p - skribenten-backend/secrets tjenestebuss-integrasjon/secrets skribenten-web/bff)
(touch skribenten-backend/secrets/azuread.env tjenestebuss-integrasjon/secrets/docker.env  skribenten-web/bff/.env)
```
```bash
docker-compose up -d --build
```

### Lokal kjøring av skribenten backend/front-end og brevbaker/pdf-bygger

1. For å hente alle secrets må du ha installert:
   * kubectl
   * python
   * vault
   * gcloud cli
   * kjørende docker/colima
   * naisdevice med standard dev-miljø tilganger og [tjenestebuss-q2](https://console.nav.cloud.nais.io/team/tjenestebuss-q2-naisdevice) gruppe-tilgang
   * Legg til `155.55.2.73	tjenestebuss-q2.adeo.no` i /etc/hosts
2. Hent alle secrets:
   ```bash
   (cd skribenten-backend && ./fetch-secrets.sh)
   ```
      ```bash
   (cd tjenestebuss-integrasjon && ./fetch-secrets.sh)
   ```
      ```bash
   (cd skribenten-web/bff && python3 setup_local_azure_secrets.py)
   ```
3. Sett opp tokens for npm og gradle [se oppsett av packages.read token](#oppsett-av-packagesread-token)
4. Kjør følgende for å bygge alle applikasjonene og publisere docker images til lokalt registry:
   ```bash
   (cd skribenten-web/bff && npm i && npm run build)
   (cd skribenten-web/frontend && npm i)
   (cd brevoppskrift-web/bff && npm i && npm run build)
   (cd brevoppskrift-web/frontend && npm i)
   ./gradlew build

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

### Debugge tjenester i docker
Ulike docker-tjenester har eksponerte porter som du kan koble en remote debugger på.

I [docker-compose.yml](docker-compose.yml) finner du de ulike portene som mappes til remote debug for de ulike tjenestene.
F.eks her hvor remote agent kjører i containeren på port 5008 og mappes ut til 5018 som du kan bruke til å koble til remote-debugger.
```yaml
ports:
- "5018:5008"
environment:
- JAVA_TOOL_OPTIONS=-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5008
```

### Oppsett av packages.read token
For å hente enkelte avhengigheter under byggene må du [lage ett github token](https://github.com/settings/tokens/new) med packages.read tilgang.
#### For gradle
legg tokenet og gir brukernavn i gradle.properties filen `$HOME/.gradle/gradle.properties`:
```
gpr.user=<github brukernavn>
gpr.token=<packages.read token>
```
#### For npm
For å hente npm pakker ved å legge inn brukernavn og samme token som passord med følgende kommando:
```bash
npm login --registry=https://npm.pkg.github.com --auth-type=legacy
```

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

## Kode generert av GitHub Copilot
Dette repoet bruker GitHub Copilot til å generere kode.