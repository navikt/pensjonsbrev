# Brevoppskrift-backend

Kotlin/Ktor-backend for brevoppskrifts malverksted. Eksisterende brevvisning,
søk og Word-eksport beholdes. Verkstedet har strukturerte utkast, NB/NN/EN,
scenarier, gjennomgang, vedlegg og Kotlin-eksport.

PostgreSQL brukes via JetBrains Exposed v1, med HikariCP og Flyway som i
skribenten-backend. Node-BFF-en validerer brukerens Entra-token gjennom Texas,
veksler det med OBO og håndhever CSRF før skriveruter. Backenden kontrollerer
JWT og at `azp` finnes blant Nais sine preautoriserte apper. Alle interne Nav-brukere kan lese;
medlemskap i konfigurert gruppe kreves for lagring, gjennomgang, KI og PR.
Det finnes ingen lokal brukerfallback.

Eksisterende brevvisning, søk og Word-eksport krever ikke innlogging.
Wonderwall håndterer innlogging ved inngang til malverkstedet, med
`autoLogin: false`. Verkstedets API krever gyldig bruker også for lesing.
BFF-en krever backendkonfigurasjon ved oppstart; verkstedet har ikke et aktiveringsflagg.
Dev-backenden har en manuell deploy-workflow;
ingen produksjonsutrulling eller eksterne credentials opprettes av bygg.
Nais bruker `.nais/nais.yaml` med miljøverdier fra `.nais/dev.yaml`.
Entra-skrivegruppen settes i `editor-group-id` i `.nais/dev.yaml`.
Skrivegruppen er `PENSJONSBREV_BREVOPPSKRIFT_EDITOR`.

## Bygg og test

Fra repositoryets rot:

```bash
./gradlew :brevbaker:brevdata:publishToMavenLocal :brevbaker:markup-model:publishToMavenLocal :brevbaker:markup-dsl:publishToMavenLocal :brevbaker:brevbaker-api:publishToMavenLocal
./gradlew :brevoppskrift-backend:build
./gradlew :brevoppskrift-backend:generateAuthoringApi
./gradlew :brevoppskrift-backend:verifyGeneratedTemplates --no-configuration-cache
```

`build` kjører tester og Kotlin-lint og pakker applikasjonen.
Databaseintegrasjonstestene krever Docker og starter sin egen PostgreSQL-container.
Testene bruker syntetiske utkast og lokalt genererte signeringsnøkler; de trenger
ikke Nav-innlogging eller eksterne integrasjoner. Modell-/API-generering skal
kjøres etter kontraktendringer; generert TypeScript skal ikke håndredigeres.

## Lokal Entra-konfigurasjon

`.nais/azuread-local.yaml` er en midlertidig, separat Entra-app for lokal testing.
Den er basert på Skribentens AzureAdApplication i dev, med egen backend-identitet.
Den endrer ikke appene som eksisterende dev-brukere benytter, og
inngår ikke i deploy-workflowen.

Opprett appen i dev-klyngen og hent konfigurasjonen når Azure-operatoren har
opprettet den tilhørende secreten:

```bash
kubectl --context dev-gcp --namespace pensjonsbrev apply -f brevoppskrift-backend/.nais/azuread-local.yaml
kubectl --context dev-gcp --namespace pensjonsbrev apply -f brevoppskrift-web/bff/.nais/azuread-local.yaml
./brevoppskrift-backend/fetch-secrets.sh
```

Henteskriptet krever `nais`, `jq` og innlogging med `nais login`. Det henter
hemmeligheter via Nais CLI, uten å lese Kubernetes-secrets med `kubectl`, og lagrer JSON og
en env-fil i `brevoppskrift-backend/secrets/`, som er git-ignorert. Bruk
`secrets/azuread.env` som miljøfil i lokal kjørekonfigurasjon; ikke legg verdiene
i repository eller logger. Sett også
`EDITOR_GROUP_ID=c331c338-5d39-45eb-a9c7-4e909c81768a` og konfigurer en lokal
PostgreSQL-database.

BFF-en bruker den midlertidige registreringen `brevoppskrift-web-local`.
Den er preautorisert mot den lokale backenden, med scope
`api://dev-gcp.pensjonsbrev.brevoppskrift-backend-local/.default`.
Dette endrer ikke innlogging eller utrulling av dev-BFF-en.

Fjern den midlertidige registreringen etter testing:

```bash
kubectl --context dev-gcp --namespace pensjonsbrev delete -f brevoppskrift-backend/.nais/azuread-local.yaml
kubectl --context dev-gcp --namespace pensjonsbrev delete -f brevoppskrift-web/bff/.nais/azuread-local.yaml
```

## Lokal kjøring med Docker Compose

`docker-compose.brevoppskrift.yml` kombineres med hovedfilen. Det starter
Brevbaker/PDF-bygger og Brevoppskrifts BFF, backend, PostgreSQL 18, Wonderwall
og Texas, uten Skribenten. Hovedfilen og `skribenten`-profilen brukes som før,
men starter ikke lenger Brevoppskrift.

Opprett lokal backend-registrering som beskrevet over. Hent deretter lokale
backend-credentials, BFF-ens midlertidige lokale credentials og Brevbakers
Unleash-konfigurasjon:

```bash
./brevoppskrift-backend/fetch-secrets.sh
./brevoppskrift-web/bff/fetch-secrets.sh
(cd pensjon/brevbaker && ./fetch-secrets.sh)
```

Bygg applikasjonene før containerne; Dockerfilene bruker ferdigbygde artefakter.
Publiser først fellesbibliotekene lokalt som beskrevet under «Bygg og test».
Fra repositoryets rot:

```bash
./gradlew :pensjon:brevbaker:installDist :brevbaker:pdf-bygger:installDist :brevoppskrift-backend:installDist
npm run build --prefix brevoppskrift-web/bff
docker compose -f docker-compose.yml -f docker-compose.brevoppskrift.yml up -d --build
npm run dev --prefix brevoppskrift-web/frontend
```

Installer npm-avhengigheter med `npm ci` i BFF/frontend før første bygg dersom
de ikke er installert. Åpne `http://localhost:8088/vite-on`. Wonderwall bruker
den lokale BFF-registreringen og callback på `http://localhost:8088/oauth2/callback`,
som er deklarert i BFF-ens lokale AzureAD-manifest. Texas veksler token til
den lokale backenden. BFF-en eksponeres ikke direkte på verten.
Søk, brevvisning og Word-eksport kan brukes uten innlogging. Først når
malverkstedet åpnes, sendes uinnloggede brukere til Wonderwall.

Backend er tilgjengelig på `http://localhost:8087` (debugport `5018`) og krever
gyldig token på API-rutene. Compose setter editorgruppen og tilkobling til en
egen lokal database; ingen dev-database brukes. Data lagres i Compose-volumet
`brevoppskrift-db`. Utgående valgfrie integrasjoner er av som standard.

Stopp med samme filvalg. Ikke bruk `--remove-orphans` hvis andre lokale
applikasjoner kjører i samme Compose-prosjekt:

```bash
docker compose -f docker-compose.yml -f docker-compose.brevoppskrift.yml down
```

`down` beholder databasevolumet. `down -v` sletter lokale Brevoppskrift-data.
Ingen av kommandoene deployer til dev eller endrer Entra-registreringene.

### Lokal funksjonstest

1. Åpne `http://localhost:8088/vite-on` uten innlogging. Velg «Malverksted: arbeidsutkast» og logg inn.
2. Velg «Nytt arbeidsutkast». Opprett fra bunnen av med et testnavn, ønsket brevtype, modul og språk.
3. Rediger tittel og innhold på NB/NN/EN. Bruk «Datafelt og valg», «Scenarier» og «Forhåndsvisning» for å prøve variabler og ulike testdata.
4. Velg «Lagre», gå tilbake til listen og åpne utkastet igjen for å kontrollere at endringene er beholdt.
5. Prøv gjennomgang, vedlegg og kodegenerering i de tilhørende fanene.

«Nytt arbeidsutkast» krever medlemskap i konfigurert editorgruppe.
Ved «Lesetilgang» må gruppetilgangen kontrolleres; logg inn på nytt etter endringer.
Import fra eksisterende maler er bare tilgjengelig for støttede kildeprofiler.
KI, Slack og PR-opprettelse er av uten egen integrasjonskonfigurasjon.

En lastetilstand skal ikke bli stående uten ende. Nettverkskall fra verkstedet
har 20 sekunders tidsavbrudd; ved feil vises en melding med «Prøv igjen».
Hvis brukerinfo ikke lastes, undersøk status for `GET /api/v1/meg` i
nettleserens nettverksfane. JWT-valideringen på backend må kunne hente
Entras offentlige signeringsnøkler.

## Semantiske typer

ID-er, revisjoner, kildeopphav, brukeridentitet og integrasjonsparametre har
egne `@JvmInline value class`-typer gjennom DTO-er, tjenester og Exposed.
Pakk ut `.value` ved fil-, JWT- og HTTP-grenser. Ikke bruk `toString()` som
transportformat; Slack-webhookens strengrepresentasjon er maskert.

JSON, JSONB, katalog og genererte frontendtyper bruker fortsatt primitive
verdier. Jacksons `KotlinModule` håndterer innpakninger og map-nøkler uten
egne adaptere. Validering ligger fortsatt i validator, handlere og
adaptere; en innpakning er ikke et bevis på gyldig eller betrodd input.
Vedleggsreferanser forblir strenger, og global dokument-ID-unikhet
kontrolleres over strengverdien på tvers av de semantiske typene.

## Kildeimport og redigerbarhet

Katalogen inneholder 280 registrerte maler: 79 uføre, 104 pensjon, 95 alder og
2 planlegge-pensjon. 20 har betrodde tekstredigeringsprofiler, fordelt 10/4/6/0.
De øvrige 260 kan leses i eksisterende oppskriftsvisning, men import til
redigerbart utkast avvises med konkret diagnostikk.

Tekstprofilene tillater tekst- og oversettelsesendringer, ikke endring av
struktur, metadata, felt, valg eller scenarier. Original Kotlin, fraser og
skjult logikk bevares som låst kilde; de rekonstrueres eller evalueres ikke av
verkstedets forhåndsvisning. Preview viser denne begrensningen eksplisitt og
er ikke en produksjonsrendering. Dette er ikke full DSL-importdekning.
Nye strukturerte utkast bruker egen modell, generator og scenarioevaluering.

Ved eksport av importerte maler erstattes bare endrede strengliteraler per språk.
Uendrede literaler og øvrig kildekode beholdes byte-for-byte, inkludert
formatteringen, kommentarer og operatorer i sammensatte strenger.

Ved oppstart lagres betrodde, redigerbare katalogoppføringer uforanderlig i
PostgreSQL, identifisert av kilde-ID og commit. Eksisterende utkast valideres og
genereres mot sitt opprinnelige kildegrunnlag, også når en senere backend har
endret eller fjernet malen fra katalogen. Historikken lastes ved behov, ikke
som en voksende samling kataloger i minnet. Kildeopphav, filhash-er og låst
struktur valideres fortsatt; klienten kan ikke registrere kildegrunnlag.
Historiske oppføringer må beholdes så lenge utkast eller PR-jobber bruker dem.
Utkast fra før denne lagringen kan brukes dersom kilden fortsatt finnes i
byggets katalog; ellers må den opprinnelige, betrodde katalogen gjeninnføres.

## Kodegenerering

Nye autobrev og redigerbare brev har målprofiler for uføre, pensjon, alder og
planlegge-pensjon. Vedlegg kan genereres selvstendig eller som versjonerte kopier
i brevet. Vedleggsdata bindes til et objektfelt med samsvarende feltskjema;
nullable binding utelater vedlegget når objektet er null. Datafrie vedlegg
trenger ingen binding.

Double-valuta, vedlegg inni vedlegg og ulike vedleggstyper på samme objektbinding
avvises med diagnostikk, ikke en omtrentlig konvertering. Valgspørsmål bruker
DSL-ens felles tekst; ulike språkversjoner av spørsmålet kan ikke eksporteres.
Genereringsfeil hindrer READY og PR. Eksport gir ikke automatisk registrering,
API-release eller full kildeimport; gjeldende importprofilkrav gjelder også
etter at en generert mal er registrert.

## Oppstart

```bash
./gradlew :brevoppskrift-backend:run
```

Oppstart krever autentiseringskonfigurasjon, også lokalt. Det finnes ingen
automatisk fallback til en lokal bruker.

Konfigurasjon følger Skribenten: `application.conf` kobler miljøvariablene til
`@Serializable`-typene i `BrevoppskriftConfig.kt`, som leses med Ktors `getAs`.
`EngineMain` starter modulen fra HOCON; `Dependencies.kt` registrerer tjenester
med Ktor DI. Ruter og applikasjonsoppsett henter avhengigheter fra registeret,
og DI lukker databasepool, integrasjonstjenester, HTTP-engine, metrikker og
varslingssender og PR-arbeider ved stopp.
Valgfrie integrasjoner er av uten konfigurasjon; delvis konfigurasjon avvises
ved oppstart. Tjenestene leser ikke miljøvariabler direkte.

`Routing.kt`, `ErrorHandling.kt`, `Logging.kt` og `Metrics.kt` avgrenser
HTTP-oppsettet. Skriveflyten ligger i `application/DraftHandler.kt`, uten
Ktor-kallkontekst. PR-bestilling og oppslag ligger i `application/PrJobHandler.kt`.
Begge repositoryene får samme Exposed `Database` gjennom
`Transactional`, som utfører JDBC-transaksjoner på IO-dispatcher uten
automatisk retry. PR-snapshot og låst utkastoppslag er én transaksjon.
Poolen har maksimalt tre tilkoblinger og minimum én per pod.

Utgående klienter deler en DI-eid engine, men har separate policyer for
GitHub, Vertex og Slack. Hver integrasjon oppretter sin klient via en stateless
`HttpClientFactory` og lukker klienten i `close()`, som i Skribenten.
Redirecter og generell HTTP-retry er avslått;
PR-jobben eier retry. Slack-feil påvirker ikke databasecommit.
Ingen bruker-/korrelasjonsheadere videresendes automatisk til tredjeparter.

Integrasjonene registreres separat i DI: `AiService`, `NotificationService`
og `PrService` er valgfrie adaptere. `AiSuggestions` håndhever KI-grensen,
`DraftNotifications` sender best-effort-varsler etter commit, og
`PrJobWorker` behandler vedvarende PR-jobber etter applikasjonsoppstart.
Varsling og PR-arbeid har separate coroutine-scopes og lukkes uavhengig.

API-generering produserer modellskjemaer og TypeScript, ikke en full OpenAPI-
beskrivelse av rutene. Endepunktgenerering og bytte av typegenerator inngår ikke.

| Miljøvariabel | Innhold |
|---|---|
| `AZURE_OPENID_CONFIG_ISSUER` | Forventet Entra-issuer |
| `AZURE_APP_CLIENT_ID` | Backendens audience |
| `AZURE_OPENID_CONFIG_JWKS_URI` | URL til issuerens offentlige signeringsnøkler |
| `AZURE_APP_PRE_AUTHORIZED_APPS` | Nais-generert JSON-liste med tillatte kallere (`name`, `clientId`) |
| `EDITOR_GROUP_ID` | Entra-gruppen som gir skriverolle |
| `PORT` | Valgfri port, standard 8080 |
| `DB_HOST`, `DB_PORT`, `DB_DATABASE` | PostgreSQL fra Nais, prefix `DB` |
| `DB_JDBC_URL` | Valgfri eksplisitt JDBC-URL i stedet for host/port/database |
| `DB_USERNAME`, `DB_PASSWORD` | Databasecredentials; aldri i repository eller logger |
| `PUBLIC_WEB_URL` | Lenker i varsler; standard dev-ingress |
| `VERTEX_PROJECT_ID`, `VERTEX_LOCATION`, `VERTEX_MODEL` | Valgfri KI-integrasjon, med workload identity og Vertex-rettighet |
| `SLACK_WEBHOOK_URL` | Valgfri webhook fra secret |
| `GITHUB_APP_ID`, `GITHUB_INSTALLATION_ID`, `GITHUB_APP_PRIVATE_KEY` | Valgfri GitHub App; RSA PEM, PKCS#1 eller PKCS#8 |
| `GITHUB_PAT` | Midlertidig alternativ til GitHub App; kan ikke kombineres med App-variablene |

API-et krever bearer-token med gyldig signatur, issuer, audience, levetid,
`azp` fra en preautorisert app og brukerclaims. Gruppen avgjør `canEdit`; den begrenser
ikke lesing eller validering.

## API

| Endepunkt | Adferd |
|---|---|
| `GET /api/v1/meg` | Validert bruker med `canEdit` |
| `GET /api/v1/integrasjoner` | Faktisk konfigurerte funksjoner |
| `GET /api/v1/katalog` | Byggbasert kildekatalog med redigerbarhet og diagnostikk |
| `GET /api/v1/katalog/{sourceId}` | Importert struktur; ikke tapsfull dokumentasjons-JSON |
| `GET /api/v1/utkast`, `GET /api/v1/utkast/{id}` | Aktive utkast |
| `GET /api/v1/utkast/{id}/bruk` | Aktive brev som holder vedleggskopi, med kopi-revisjon |
| `POST /api/v1/utkast` | Nytt utkast eller første import fra katalogen |
| `PUT /api/v1/utkast/{id}` | Eksplisitt lagring med forventet revisjon |
| `POST /api/v1/utkast/{id}/status` | WORK eller READY; READY krever gyldig kodegenerering |
| `POST /api/v1/utkast/{id}/grunnlinje` | Godkjenning av gjeldende struktur som ny grunnlinje |
| `POST /api/v1/utkast/{id}/arkiver` | DONE, RESET for import eller DISCARDED for nytt utkast |
| `POST /api/v1/generer` | Autoritativ Kotlin-eksport med diagnostikk |
| `POST /api/v1/forhandsvisning` | Evaluert scenario-HTML |
| `POST /api/v1/ki/nynorsk`, `POST /api/v1/ki/klarsprak` | Forslag, aldri automatisk lagring/godkjenning |
| `POST /api/v1/utkast/{id}/pr`, `GET /api/v1/utkast/{id}/pr` | Revisjonsbundet PR-jobb og status |
| `POST /api/v1/utkast/valider` | Validerer utkast uten å lagre; tilgjengelig for innloggede lesere |
| `GET /isAlive` | Prosessen svarer |
| `GET /isReady` | Database kan leses |
| `GET /metrics` | Prometheus-metrikker |

Validering krever `application/json`, håndhever en grense på 5 MiB mens
kroppen leses og avviser ukjent format, ukjente egenskaper og skalartypekonvertering.
Gyldige utkast gir `200` med `{"valid":true}`. Semantiske feil gir `422`
med feltnære feilkoder. API-feil bruker `application/problem+json` uten å gjengi
innsendt dokumentinnhold.

Utkastformatet støtter `NB`, `NN` og `EN`, overskrifter, avsnitt, lister, tabeller,
vilkår, gjentakelser, felt og boolske saksbehandlervalg. Listedata er foreløpig
objektlister. Felt- og blokk-ID-er må være unike. Oversettelser må dekke nøyaktig
de valgte språkene; nullable objektfelt og lister må være bundet i riktig scope.
Ukjente eller ikke-støttede kildekonstruksjoner skal gi importdiagnostikk og
ikke-redigerbar kilde, ikke en omtrentlig konvertering. Katalogen bygges fra
Kotlin-kildene og sporer SHA-256 for opphav. Dokumentasjonens traverserings-ID-er
brukes ikke som kildeidentitet.

Nye skrivende ruter må autentiseres og bruke `ApplicationCall.requireEditor()`
før sideeffekter. Historiske kommentarer/svar/statushendelser er append-only
og stemples på serveren. PUT kan ikke omskrive kildeopphav eller grunnlinje.
Optimistiske konflikter gir `409` med `currentRevision`; klienten beholder
egne endringer. Databasen låser raden ved oppdatering, og en delvis unik indeks
sikrer ett aktivt arbeidsutkast per kilde også ved samtidige førstegangsimporter.

## Valgfrie integrasjoner

KI tar bare eksplisitt valgt maltekst, ikke hele utkastet eller testdata. Vertex
bruker regionalt EU-endepunkt, workload identity, timeout og to samtidige kall
per pod. Forslaget må bevare plassholdere og balansert støttet formatering.
Brukeren må akseptere det eksplisitt; et forslag mot gammel tekst avvises i UI.
Bruk kun syntetiske scenarier, aldri persondata, ved malarbeid og KI.

Slack sendes best-effort etter vellykket commit. Varslene inneholder hendelsestype,
antall og utkastlenke, ikke dokumenttekst, kommentarer eller brukerens navn.
Slack-feil gjør ikke en lagring mislykket.

GitHub App-tokenet begrenses til `navikt/pensjonsbrev` og `contents` /
`pull_requests`. PR-jobber har unik utkast/revisjon, lagret snapshot og databaselease.
Importerte maler får PR-gren fra utkastets betrodde kildecommit, ikke fersk main.
PR-en peker mot main; utvikler håndterer eventuelle merge-konflikter, CI-feil og
tilpasninger til nyere kode. Nye maler uten kildegrunnlag får gren fra fersk main.
Kildecommit oppgis i PR-beskrivelsen. SHA-256 og kildeopphav valideres mot
katalogen eller lagret kildehistorikk først. Kopierte kilder fra forskjellige
commits må være kompatible med grenens grunnlag; dette sjekkes med komplette
Git-trær, ikke ett kall per kildefil. Avkortede eller ugyldige trær avvises.
Jobben kontrollerer også tillatte Kotlin-stier, genererte
sjekklister i `src/test/resources/brevoppskrift/` og at nye filer ikke overskriver
uimportert kode. Den lager en unik branch og en **draft PR**,
aldri merge. Retry gjenbruker eksisterende PR/branch og overskriver ikke en endret
branch. Crash recovery og transiente HTTP-feil har maksimalt tre forsøk.
Kildehash kontrolleres også for kopierte vedlegg; motstridende hash avvises.
Utvikler må kontrollere registrering, fixtures, feature toggles og kompatibilitet
før merge; dette er ikke en automatisk malrelease.

### Midlertidig lokal PR-testing med PAT

Bruk en fine-grained PAT med kort utløp, begrenset til `navikt/pensjonsbrev`,
med **Contents: Read and write** og **Pull requests: Read and write**.
Tokenet må være tillatt av organisasjonen og har ikke flere rettigheter enn
brukeren som opprettet det.

Opprett `brevoppskrift-backend/secrets/github.env` lokalt og sett `GITHUB_PAT`
til tokenverdien. Filen er git-ignorert og lastes valgfritt av Compose.
Ikke legg tokenet i Compose-filen, repositoryet eller logger.
`GITHUB_APP_ID`, `GITHUB_INSTALLATION_ID` og `GITHUB_APP_PRIVATE_KEY` må være
utelatt når PAT brukes. Tomt token, ufullstendig App-konfigurasjon eller begge
autentiseringsmåter samtidig gjør at oppstart feiler.

Fra repositoryets rot:

```bash
./gradlew :brevoppskrift-backend:installDist
docker compose -f docker-compose.yml -f docker-compose.brevoppskrift.yml up -d --no-deps --build brevoppskrift-backend
```

Oppdater verkstedet og kontroller at `GET /api/v1/integrasjoner` viser
`pullRequests: true`. Lagre et gyldig utkast, marker det som klart og velg
PR-opprettelse under «Levering». Dette oppretter en **ekte draft-PR i
`navikt/pensjonsbrev`**, ikke i en testfork. Tilgangskontroll, revisjonskontroll,
kildekontroll og tillatte filstier er de samme som med GitHub App.

For å slå av PAT-støtten, fjern tokenet eller miljøfilen og opprett
backend-containeren på nytt. Tilbakekall tokenet etter testing.
GitHub App kan senere aktiveres med de tre App-variablene uten PAT.
Dev-manifestet aktiverer ikke PAT eller GitHub-tilgang automatisk.

Valgfrie integrasjoner trenger separate secrets, IAM/App-rettigheter og eksplisitt
outbound-policy til `api.github.com`, `hooks.slack.com` og
`aiplatform.eu.rep.googleapis.com`. De er ikke aktivert i pilotmanifestet.

## Dev-pilot og rollback

1. Sett samme godkjente Entra-skrivegruppe i backendens `.nais/dev.yaml`
   og BFF-ens `.nais/bff-dev.yaml`. BFF-manifestet oppretter Entra-app og
   Wonderwall med `autoLogin: false`; eksisterende leseflyter krever ikke innlogging.
2. Kjør den manuelle backend-deployen fra main. Nais preautoriserer
   BFF-en via `accessPolicy.inbound`. Workflowen krever bygg, kontrakt-/generatorverifisering og
   attestert/signert image før deploy. Nais oppretter egen database;
   Flyway kjører ved oppstart.
3. BFF-ens `.nais/bff-dev.yaml` setter `BREVOPPSKRIFT_BACKEND_URL:
   http://brevoppskrift-backend`, `BREVOPPSKRIFT_BACKEND_SCOPE:
   api://dev-gcp.pensjonsbrev.brevoppskrift-backend/.default` og `PUBLIC_ORIGIN:
   https://pensjon-brevoppskrift.intern.dev.nav.no`. Sett `PUBLIC_ORIGINS:
   https://pensjon-brevoppskrift.ansatt.dev.nav.no` for den andre eksisterende
   ingressen. Protokollene må være like; API-svar caches ikke.
4. Kontroller anonymt søk, brevvisning og Word-eksport, innlogging ved åpning
   av verkstedet og 401 uten HTML/redirect fra verkstedets API. Kontroller
   leser/skriver, 403 uten gruppe, CSRF, første import, to samtidige
   lagringer, alle språk, scenario-preview, grunnlinje, vedlegg, eksport og
   RESET/reimport. Kontroller uføre før de andre modulene. Aktiver integrasjonene
   enkeltvis og kontroller KI-avvisning og utkast-PR uten merge.

Rollback: rull tilbake berørt BFF-/backend-image uten å slette databasen.
Behold Wonderwall med `autoLogin: false`, slik at gammel funksjonalitet fortsatt
kan brukes uten innlogging. En utilgjengelig backend stopper ikke søk,
brevvisning eller Word-eksport. Produksjon krever egen beslutning og konfigurasjon.
Mønsterrefaktoreringen endrer ikke database-/utkastformat. Ved tilbakeføring
avsluttes arbeideren; en uferdig lease kan gjenopptas etter utløp, med samme
snapshot og kontroll av eventuelt allerede opprettet GitHub-resultat.

## Drift

Readiness-feil: kontroller Cloud SQL, pool, migrasjonsstatus og tilgang; ikke
logg credentials eller JSONB-innhold. Exposed SQL-logging er avslått.
Ved konflikter skal brukeren hente serverversjonen eller eksportere lokale
endringer, ikke forsøke automatisk overskriving.

Følg Ktor-metrikkene `ktor_http_server_requests_seconds_*` med `route`, `method`
og `status`, og readiness/HTTP 5xx. HTTP-histogrammene utelater prober/metrikker
og podadresse; readiness følges derfor også gjennom podstatus.
Følg `brevoppskrift_http_client_seconds_*` med faste `service`, `operation`,
`method`, `status` og `outcome`, samt Hikari-målene. Følg også
`brevoppskrift_draft_*_total` for
lagring/status/arkivering/konflikter og `brevoppskrift_pr_jobs_total` for jobbresultat.
Ingen metrikktagger inneholder bruker, utkast-ID eller tekst. Følg trygge logghendelser for
utilgjengelig PR-worker og Slack. Alarmer på vedvarende readiness-feil og
forhøyet feilrate må settes opp før en live pilot; ingen alert er aktivert her.
Ved `SOURCE_CHANGED` importer på nytt etter avklart arkivering.
Ved PR-feil/timeout skal eksisterende jobb, unik branch og PR kontrolleres før
ny revisjon sendes; aldri slett en branch automatisk for å prøve igjen.

Logger er JSON med `x_correlationId` i MDC. CallId bruker samme oppsett som
Skribenten: ikke-tomme `X-Request-ID`-verdier beholdes, og Ktor genererer ID
når den mangler. ID-en returneres også ved API-feil. Kallloggen bruker Ktors
standardformat med metode, faktisk sti, status og tidsbruk, og utelater prober.
URL-parametre skal ikke inneholde personopplysninger. Ikke aktiver SQL-/HTTP-bodylogging.
Nais Java-autoinstrumentering er ikke aktivert; URL-/SQL-redigering og
eksterne klientspans må avklares før aktivering.

Etter godkjent dev-deploy: kontroller helse og shutdown, 401/403, lagring/409,
generering/preview, korrelasjon og histogrammer. Kontroller med syntetiske
markører at logger/metrikker ikke inneholder dokumenttekst, identitet,
tokens eller webhook. Entra/DB-feil håndteres separat fra forventede
konflikter, avviste utkast og best-effort Slack-feil.
