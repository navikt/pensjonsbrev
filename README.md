# Pensjonsbrev

This is a mono-repo for the microservices that together form the new letter ordering system.

## Dokumentasjon

Dokumentasjonen fra `docs`-mappa i dette repoet blir automatisk publisert til https://navikt.github.io/pensjonsbrev/

## Lokal kjøring av brevbaker og pdf-bygger

For å kjøre løsningen lokalt må man ha docker og docker compose installert.
Bygging av brevbakeren krever at du har konfigurert gradle med packages.read token for å hente pakker.
Se [seksjonen under for oppsett av read token i gradle](#for-gradle).

Bruk følgende for å bygge og kjøre:

```bash
./gradlew :pensjon:brevbaker:build :brevbaker:pdf-bygger:build
```

Dersom du kun skal kjøre brevbaker og pdf-bygger og ikke skribenten må du fortsatt pga en bug i docker-compose generere tomme env files for skribenten:

```bash
(mkdir -p - skribenten-backend/secrets skribenten-web/bff)
(touch skribenten-backend/secrets/azuread.env skribenten-backend/secrets/unleash.env skribenten-web/bff/.env)
```

```bash
docker-compose up -d --build
```

### Lokal kjøring av skribenten backend/front-end og brevbaker/pdf-bygger

1. For å hente alle secrets må du ha installert:
   - kubectl
   - python
   - vault
   - gcloud cli
   - kjørende docker/colima
   - naisdevice med standard dev-miljø tilganger
2. Hent alle secrets:
   ```bash
   ./fetch-secrets.sh
   ```
3. Sett opp tokens for npm og gradle [se oppsett av packages.read token](#oppsett-av-packagesread-token)
4. Kjør følgende for å bygge alle applikasjonene og publisere docker images til lokalt registry:

   ```bash
   (cd skribenten-web/bff && npm ci && npm run build)
   (cd skribenten-web/frontend && npm ci)
   (cd brevoppskrift-web/bff && npm ci && npm run build)
   (cd brevoppskrift-web/frontend && npm ci)
   ./gradlew build -x test
   ```

5. Kjør alle backend-tjenester
   ```bash
   docker compose --profile skribenten up -d --build
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

#### Kjøre PEN lokalt utenfor docker compose

Vi har ikke noe bra oppsett for dette, men her er en oppskrift på hvordan man kan løse det.

1. Endre PEN_URL environment variable i docker-compose.yaml for skribenten-backend til `http://host.docker.internal:8089/pen/api/`

Om du får ConnectTimeoutException på kall til PEN fra skribenten, så betyr det mest sannsynlig at du har en brannmur som blokkerer. Følgende oppskrift er for linux.

1. Kjør `docker network ls` og merk deg NETWORK ID for "pensjon-local"
2. Sjekk at du har et network interface med navnet `br-<NETWORK ID>` ved å kjøre `ip link show`
3. Legg til en (midlertidig) regel for å tillate tilkobling til host fra docker compose med `sudo iptables -I INPUT 1 -i br-<NETWORK ID> -j ACCEPT`

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

### Endringer i biblioteks-koden

Vi bruker Kotlin Gradle-pluginens innebygde ABI-validering (`abiValidation`) for å se etter endringer i koden i modulene som inngår i biblioteket (per nå `brevbaker:brevbaker-api`, `brevbaker:markup-model`, `brevbaker:markup-dsl`, `brevbaker:pdf-bygger-api`, `brevbaker:pdf-bygger-dsl`, `brevbaker:dsl` og `brevbaker:core`). Denne holder oversikt representert i .api-filer i disse modulene.

Ved endringer av public-kode i disse modulene - inkludert sletting av metoder eller nye metoder - må du huske å kjøre `gradle updateKotlinAbi` og sjekke inn de oppdaterte .api-filene. Glemmer du dette vil bygget feile - det kjører automatisk `gradle checkKotlinAbi`-kommandoen.

Mer om dette på https://kotlinlang.org/docs/api-guidelines-backward-compatibility.html

### Publiserte artefakter og modulstruktur

Bibliotekssiden av repoet er delt langs arkitektur, ikke livssyklus, og alle artefaktene slippes i
**lockstep** fra `brevbakerVersion` i rot-`gradle.properties`:

| Modul | Artefakt | Publisering |
|---|---|---|
| `brevbaker:brevdata` (`brevbaker/brevdata`) | `brevdata` | publiseres, men deklareres normalt ikke direkte — kommer transitivt via api-modellene |
| `brevbaker:markup-model` (`brevbaker/markup/model`) | `markup-model` | publiseres, men deklareres normalt ikke direkte — kommer transitivt |
| `brevbaker:markup-dsl` (`brevbaker/markup/dsl`) | `markup-dsl` | som over |
| `brevbaker:pdf-bygger-api` (`brevbaker/pdf-bygger/api`) | `pdf-bygger-api` | som over |
| `brevbaker:pdf-bygger-dsl` (`brevbaker/pdf-bygger/dsl`) | `pdf-bygger-dsl` | deklareres direkte av konsumenter |
| `brevbaker:brevbaker-api` (`brevbaker/brevbaker-api`) | `brevbaker-api` | deklareres direkte av konsumenter |
| `brevbaker:bom` | `brevbaker-bom` | BOM som holder alt på samme versjon |
| `brevbaker:jackson` | – | **publiseres aldri**, kun `project(...)` |

`markup-model` er den rene datamodellen uten avhengigheter. Typene der inngår i signaturen til både
`brevbaker-api` (`BestillRedigertBrevRequestV2`) og `pdf-bygger-api` (`LetterPDFRequest`), og må derfor
være ett versjonert artefakt begge peker på — ikke en klasse som bundles to steder. Konsumenter som
bruker begge bør importere `brevbaker-bom` slik at de ikke kan havne i versjonssprik.

`brevdata` er gulvet i stabelen og skal aldri få avhengigheter: det er det eneste PENs api-modeller
kompilerer mot, så alt som legges der arver hver eneste bestiller. Derfor ligger `IBrevkategori` og
`ISakstype` her i stedet for nøstet i `TemplateDescription` — bestillerne implementerer dem, mens
`TemplateDescription` er selve HTTP-svaret og hører hjemme i `brevbaker-api`.

In-repo konsumenter av bibliotekmodulene bruker `project(...)`, ikke publiserte koordinater, så en
glemt versjonsbump kan ikke gi en stille feil jar innad i biblioteket. Api-modellene
(`pensjon`/`alder`/`ufoere:api-model`) konsumeres derimot av malene ved *publiserte* koordinater, og
POM-en deres drar inn `brevdata` transitivt. Etter en bump av `brevbakerVersion` må du derfor kjøre

```bash
./gradlew :brevbaker:brevdata:publishToMavenLocal :brevbaker:markup-model:publishToMavenLocal \
  :brevbaker:markup-dsl:publishToMavenLocal :brevbaker:pdf-bygger-api:publishToMavenLocal \
  :brevbaker:pdf-bygger-dsl:publishToMavenLocal :brevbaker:brevbaker-api:publishToMavenLocal
```

før du bygger malene — ellers får du `Could not find no.nav.brev.brevbaker:brevdata:<versjon>`. Dette
er samme mønster som for api-modellene selv, og CI gjør det samme før den bygger noe som konsumerer
en api-model.

#### Rekkefølge ved release

`pensjon`/`alder`/`ufoere:api-model` publiseres med en POM som peker på `brevdata` ved koordinat. Bumper
du `brevbakerVersion` **og** en api-model-versjon i samme PR, må biblioteket
(`brevbaker-bibliotek.yaml`) rekke å publisere før api-modellene, ellers peker POM-en deres på en
`brevdata` som ikke finnes ennå. Lokalt løser du det med `publishToMavenLocal` som beskrevet over.

#### Opt-in-markører

Modellen har `internal constructor` + `@ConsistentCopyVisibility`, så `copy()` er utilgjengelig utenfor
`markup-model` og ferdig bygget markup kan ikke muteres forbi valideringen i builderne. Det finnes
nøyaktig to opt-in-markører:

- `@MarkupModelApi` — konstruksjon av modellen utenom DSL-en, via `object MarkupModel`. Brukes av
  DSL-en selv og av `letterPDFRequestModel(...)`.
- `@ExtendedMarkupDsl` — den id-eksplisitte DSL-en i `no.nav.brev.brevbaker.markup.dsl.extended`, som
  også gir tilgang til `variable(...)` og `editBehaviour`. Den ligger i `markup-dsl` (ikke i en egen
  modul), nettopp for at builder-sømmene skal kunne forbli `internal`. `brevbaker:core` — som eier
  id-tildelingen i `Letter2MarkupV2` — opter inn på modulnivå.

Malforfattere skal aldri trenge noen av dem.

### Intern modul: `brevbaker:jackson`

`brevbaker:jackson` eier **kun serialiseringen** av trafikk mellom våre egne applikasjoner, og
publiseres aldri. Den holder ingen modelltyper og re-eksporterer ingen artefakter; hver modul
deklarerer selv de modellene den bruker.

- Depend på `project(":brevbaker:jackson")` bare når du faktisk trenger `internalObjectMapper()`.
- All intern serialisering går gjennom Jackson via `internalObjectMapper()` i
  `no.nav.brev.brevbaker.jackson`. `markup-model` har ingen serialiseringsavhengighet; hvert element
  har i stedet en ekte `val type: Type`, og polymorf deserialisering konfigureres i
  `MarkupJacksonModule` (v2) og `LetterMarkupV1JacksonModule` (v1). Legger du til en sealed subtype
  uten å registrere den der, feiler drift-testen i `MarkupJacksonModuleTest`.
- Golden-JSON-en under `brevbaker/jackson/src/test/resources/golden/` låser wire-formatet mot
  pdf-bygger og skribenten. Regenerer bevisst med `REGENERER_GOLDEN=true ./gradlew :brevbaker:jackson:test`.

### Ytelsestesting med locust

Ytelsestesten er i utgangspunktet satt opp til å teste vedtaksbrevet UNG_UFOER_AUTO.

1. Evt. rediger `brevbaker/locust/autobrev_request.json` om du ønsker å teste et annet brev.
2. Kjør `.brevbaker/locust/fetch-secrets.sh`
3. Start docker compose med locust profil `docker compose --profile locust up`
4. Gå inn på locust grensesnittet via http://localhost:8089/ og skriv inn url til endepunktet du ønsker å ytelses-teste.
   [Se dokumentasjon fra locust for mer info om bruk.](http://docs.locust.io/en/stable/quickstart.html#locust-s-web-interface)

## Miljøvariabler for integrasjonstester

Følgende miljøvariabler kan settes for å styre oppførselen til `PDFByggerTestContainer` under kjøring av integrasjonstester:

| Miljøvariabel                 | Beskrivelse |
|-------------------------------|-------------|
| `BRUK_LOKAL_PDF_BYGGER`       | Sett til `true` for å kjøre integrasjonstestene mot din lokalt bygde pdf-bygger (`pensjonsbrev-pdf-bygger:latest`) i stedet for å hente imaget fra GitHub Container Registry. |
| `TESTCONTAINERS_REUSE_ENABLE` | Sett til `true` for å gjenbruke pdf-bygger-containeren mellom kjøringer, noe som kan redusere oppstartstid ved lokal utvikling. Husk å stoppe den kjørende testcontaineren manuelt dersom du ønsker å oppdatere docker-imaget. |

### Bygge nytt lokalt pdf-bygger image

Bygg først jar-filen og deretter docker-imaget:

```bash
./gradlew :brevbaker:pdf-bygger:installDist
docker build brevbaker/pdf-bygger
```

### Eksempel på lokal kjøring av integrasjonstester med lokal pdf-bygger

```bash
BRUK_LOKAL_PDF_BYGGER=true ./gradlew integrationTest
```

For å gjenbruke test-containeren mellom ulike tester og moduler:

```bash
BRUK_LOKAL_PDF_BYGGER=true TESTCONTAINERS_REUSE_ENABLE=true ./gradlew integrationTest
```

> **Merk:** Når du bruker `TESTCONTAINERS_REUSE_ENABLE=true` vil containeren fortsette å kjøre mellom test-kjøringer. Husk å stoppe den manuelt (f.eks. med `docker stop <container-id>`) dersom du har bygget et nytt pdf-bygger image og ønsker at testene skal bruke det oppdaterte imaget.

## Endring av obligatoriske felter i API-model

Brevbakeren bruker pensjon-api-model, alder-api-model og ufoere-api-model for bestilling av brev.
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

# Oppdatere LaTeX mal/avhengigheter

## Iterere på endringer

For å fort kunne oppdatere latex filene i pdf-byggeren under kjøring, anbefales det å kjøre følgende kommando som before launch for LatexVisualITest.

```bash
docker exec -u 0 -it pensjonsbrev-pdf-bygger-1 rm -rf /app/pensjonsbrev_latex && docker cp .brevbaker/pdf-bygger/containerFiles/latex pensjonsbrev-pdf-bygger-1:/app/pensjonsbrev_latex/
```

Da vil du kunne se på pensjon/brevbaker/build/test_visual/pdf resultatet av endringen fort.

## Se forskjell mellom endringer og gammel versjon

For å se at du kun har endret det du skal, så kan du kjøre følgende script etterpå:

```bash
folder=.brevbaker/pdf-bygger/build/test_visual
original_files=$folder/image_old
compare_to_folder=$folder/pdf
mogrify_folder=$folder/image_new
output_folder=$folder/out
mkdir -p $output_folder
mkdir -p $mogrify_folder
magick mogrify -path $mogrify_folder -format png -background white -alpha remove -alpha off -density 200 -quality 85 $compare_to_folder/*.pdf
for absolutefilename in $original_files/*.png; do
      filename=$(basename "$absolutefilename")
      echo -e "\n-------------------------------"
      echo comparing $filename
      magick compare -metric MAE -density 150 -compose multiply $original_files/$filename $mogrify_folder/$filename $output_folder/$filename
      echo -e "\n-------------------------------"
done
```

Først en gang for å lage bilder i image_new, så kan du kopiere bildene til image_old for å få ett sammenligningsgrunnlag.
Deretter kan du kjøre scriptet på nytt og få vite hvor ulike de er, samt en diff mellom bildene i out mappen.

Du vil også kunne se disse endringene med reg-suit ved å lage en pull-request.

## Oppdatere latex biblioteker

Ved først bygge brevbaker/pdf-bygger/latex.Dockerfile, så sette "from" i brevbaker/pdf-bygger/Dockerfile, kan du iterere over det å oppdatere latex imaget/pakker.
Når du er ferdig med det, så kan du kjøre github action workflowen "update-latex-image" på branchen, så vil den publisere ett nytt dato-stemplet image som kan tas i bruk i pdf-bygger/Dockerfile.

Vær obs på at pdf-bygger kjører med en egendefinert Java Runtime, bygd opp i pdf-bygger sin Dockerfile, som kun har med modulene fra Java vi bruker. Dermed får vi en så liten runtime som mulig. Ulempa med dette er at vi må passe på litt ekstra ved endringer. For eksempel er `localedata`-modulen viktig for å få norsk dato formatert riktig. Sjekk reg-suit eller ny opp mot gammel pdf fra lokal generering ved endringer i latex-delen, eller tekniske endringer som for eksempel Java-oppgradering, av pdf-bygger for å se at ting ser likt ut.

# Kode generert av GitHub Copilot

Dette repoet inneholder forekomster av kode generert av GitHub Copilot.

## Troubleshooting

#### Error når jeg prøver å koble meg til kubernetes cluster

```
ERROR: Cannot connect to kubernetes cluster dev-gcp:  getting credentials
Have you remembered to connect naisdevice? (see https://doc.nais.io/basics/access/)
```

- Hvis du har tidligere fulgt setupen, kan du prøve å verifiser at du er autensitert i gcloud, og potensielt oppdatere credentials ved å kjøre `gcloud auth login --update-adc`, så kan du prøve igjen.

#### Får ikke kjørt jq etter å ha lastet den ned

- Prøv `brew install jq`.
