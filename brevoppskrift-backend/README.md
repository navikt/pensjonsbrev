# Brevoppskrift-backend

Modulen inneholder den versjonerte utkastmodellen, dokumentvalidering,
serverstempling av gjennomgang og JSON-serialisering.

Lagringslaget bruker PostgreSQL, Exposed, HikariCP og Flyway. Utkast lagres
som JSONB med revisjonskontroll, unik aktiv kilde og arkivering.
`openDatabase` kjører migreringene i `src/main/resources/db/migration`
før tilkoblingspoolen returneres. Ktor-applikasjonen bruker det samme
lagringslaget og pakker katalog, generator og forhåndsvisning.
Backend er ikke deployet eller koblet til BFF.

Bygg og test:

```shell
./gradlew :brevoppskrift-backend:build
```

Lagringstestene krever Docker og starter en isolert PostgreSQL-container.

## API og autentisering

`/api/v1` krever et gyldig Entra-token fra en forhåndsautorisert klient
med Nav-brukerkontekst. Lesere kan hente katalog, utkast, generert Kotlin
og forhåndsvisning. Endringer krever den konfigurerte editorgruppen.
Lagring bruker revisjonskontroll og serverstemplet gjennomgang.
Feil returneres som `application/problem+json`, og request-body har en
størrelsesgrense på 5 MiB, eller 200 KiB for KI-forslag.

Helseendepunktene `/isAlive` og `/isReady`, samt `/metrics`, er offentlige
i backend. Readiness krever fungerende database. KI og varsling er valgfrie;
capabilities oppgir om de er konfigurert. KI-rutene returnerer 503 uten tjeneste.
PR-ruter finnes, men innsending gir 503 uten konfigurert PR-tjeneste.
GitHub-adapter er ikke innført.

`application.conf` leser Entra- og databasekonfigurasjon fra miljøet.
Klientlisten kommer fra `AZURE_APP_PRE_AUTHORIZED_APPS`, og
`EDITOR_GROUP_ID` bestemmer skrivetilgang. `installDist` lager
applikasjonen under `build/install/brevoppskrift-backend`.
Git og Kotlin-kompilatoren trengs ikke i runtime.

## PR-jobber

`POST /api/v1/utkast/{id}/pr` krever editorrettighet, gjeldende revisjon
og et READY-utkast som består genereringskontrollen. Jobben lagrer et
uforanderlig revisjonssnapshot i PostgreSQL og returnerer 202. Gjentatt
innsending av samme revisjon returnerer samme jobb.

`GET /api/v1/utkast/{id}/pr` gir siste jobbstatus og eventuell PR-lenke
eller feilkode. Interne leases og dokumentinnhold returneres ikke.

Workeren starter med applikasjonen og krever en PR-tjeneste. Den bruker
tre-minutters leases, 150 sekunders operasjonsgrense og maksimalt tre
forsøk. Midlertidige feil prøves igjen etter 30 sekunder. Ved avslutning
avbrytes pågående transport; en ny worker kan overta etter lease-utløp.

Produksjon har foreløpig ingen PR-tjeneste, og `pullRequests` er false.
Testene bruker fake transport mot en isolert PostgreSQL-database.

## KI og Slack

Vertex aktiveres når `VERTEX_PROJECT_ID`, `VERTEX_LOCATION` og
`VERTEX_MODEL` er satt. Delvis eller ugyldig konfigurasjon avvises ved
oppstart. Adapteren henter token fra Google metadata-tjenesten med
`Metadata-Flavor: Google` og bruker `aiplatform.eu.rep.googleapis.com`.
Google Workload Identity og nettverkstilgang må være konfigurert i
kjøremiljøet; ingen credentials hentes ved applikasjonsoppstart.

KI-forslag krever editorrettighet og lagres ikke automatisk. Maksimalt
to forslag behandles samtidig, med 30 sekunders samlet tidsgrense.
Teksten kan ha inntil 100 000 tegn og instruksjonen 1 000 tegn.
Svar må bevare plassholdere og balansert `<b>`/`<i>`-markering; annen
HTML avvises. Tokenresponser begrenses til 16 KiB og modellresponser
til 1 MiB før JSON-parsing. Forslag er ikke faglig godkjenning.

Slack aktiveres uavhengig med `SLACK_WEBHOOK_URL`, som må være en
HTTPS-webhook under `hooks.slack.com/services/`. Webhooken skal komme
fra secret-konfigurasjon, ikke kildekode. Varsler inneholder hendelsestype,
antall og eventuell verkstedlenke, ikke utkastnavn eller dokumenttekst.
Varslingsfeil hindrer ikke lagring av utkast.

HTTP-klientene følger ikke redirects og prøver ikke kall automatisk på nytt.
Vertex har 20 sekunders request-/socketgrense, Slack 3 sekunder og
tilkoblinger 5 sekunder. Metrikker bruker faste tjeneste-/operasjonsnavn
uten URL-er, webhooker, token eller brevtekst. Adapterne deler en
applikasjonseid engine og lukkes ved avslutning; fake engines i tester
beholder sitt eksterne eierskap.

## Generert API-kontrakt

OpenAPI-spesifikasjonen på `/swagger-internal/documentation.json` bygges
fra de reelle Ktor-rutene, kodeinferens og Jackson-modellen.
`RequestBodyLimit` avgrenser innlesingen før `receive<T>()` deserialiserer.
Body- og svartypene infereres fra rutene. Felles feilresponser og krav om
request-body legges til via Ktors stabile OpenAPI-modell.
Tester kontrollerer konvolutter, feilformat, skalartyper, defaultverdier,
diskriminatorer, nullbarhet, maps og rekursive referanser.

```shell
./gradlew :brevoppskrift-backend:generateApiTypes
```

Oppgaven starter testapplikasjonen med en syntetisk PostgreSQL-database,
eksporterer `build/openapi-spec.json` og bruker `openapi-typescript` til
å generere `brevoppskrift-web/frontend/src/verksted/api-types.ts`.
CI regenererer og kontrollerer kontraktdrift. Filen skal ikke håndredigeres.

Npm-verktøyene ligger separat i `openapi/`: generatoren trenger
TypeScripts JavaScript-kompilator-API, ikke frontendens native kompilator.

## Kildekatalog

`generateSourceCatalogue` leser registrerte maler og tilhørende Kotlin-kilder
fra én Git-commit (`HEAD`), ikke fra arbeidskopien. Oppgaven kjøres før
`processResources` og pakker katalogen i biblioteket. Importverktøyets
Kotlin-kompilator/PSI ligger i `authoringTools`, ikke på runtime-classpath.
Oppgaven kjøres også når bare committen har endret seg.

Bygget bruker `/usr/bin/git` uten PATH-oppslag. En annen installasjon kan
angis med `-PauthoringGitExecutable=/absolutt/sti/til/git`; relative stier
og filer som ikke er kjørbare, avvises.

Katalogen oppgir hvilke maler som kan importeres, med diagnostikk for
profiler som ikke støttes. Importerte maler tillater bare tekstendringer;
struktur, metadata, felt og kildeidentitet er låst.

`BuildSourceCatalogue.persistent` beholder betrodde, redigerbare
kildeversjoner i PostgreSQL før katalogen tas i bruk. Tidligere utkast kan
dermed valideres selv om malen er endret eller fjernet fra neste bygg.
Historiske oppslag krever eksakt kilde-ID, commit og filhash-er.
Ved validering av endringer brukes `checkSource(draft, expected)` med
lagret utkast som forventet opphav. Repositoryet avviser også endret
opphav for roten og eksisterende vedleggskopier innenfor skrivetransaksjonen.

## Kotlin-generering

`KotlinDraftGenerator` lager mal, DTO, syntetiske rendringstester og en
utviklersjekkliste for nye autobrev, redigerbare brev og vedlegg.
Målmodulene er uføre, pensjon, alder og planlegge-pensjon.
Profiler som ikke støttes, avvises med diagnostikk uten genererte filer.

For importerte maler patches bare endrede tekstliteraler i den betrodde
kildeversjonen. Uendret kilde og tilhørende DTO beholdes byte for byte,
inkludert kommentarer, formatering og språkorden. Generering krever ikke
Git eller et kildecheckout ved kjøring.

Genererte filer returneres som data; modulen skriver dem ikke inn i
målmodulene, registrerer ingen nye maler og utfører ingen publisering.
Utvikleren må kontrollere blant annet brevkode, registrering,
feature-toggle og eventuell publisering av API-modeller før innføring.

Kontroller kompilering, HTML-rendring og DTO-JSON mot de faktiske
målmodulene og Brevbakers Jackson-oppsett:

```shell
./gradlew :brevbaker:brevdata:publishToMavenLocal :brevbaker:markup-model:publishToMavenLocal :brevbaker:markup-dsl:publishToMavenLocal :brevbaker:brevbaker-api:publishToMavenLocal
./gradlew :brevoppskrift-backend:verifyGeneratedTemplates --no-configuration-cache
```

Kontrollen bruker syntetiske autobrev, redigerbare brev og vedlegg i alle
fire målmoduler, på bokmål, nynorsk og engelsk. Prøvekildene og resultatene
ligger under modulens `build/`; målmodulenes kildefiler endres ikke.
Denne kontrollen krever deaktivert configuration cache. Vanlig modulbygg
kan fortsatt bruke configuration cache.

## HTML-forhåndsvisning

`HtmlDraftPreview` validerer utkastet og viser strukturerte tekstnoder,
felt, vilkår, gjentakelser og vedlegg på valgt språk og med valgt scenario.
Tekst og scenarioverdier HTML-escapes. Manglende verdier, låst kildeinnhold
og avkorting gir eksplisitte advarsler.

Forhåndsvisningen tolker bare utkastmodellen. Den kjører ikke Kotlin,
frasekall eller skjult logikk fra originalmalen. For kildeimporterte
utkast vises denne begrensningen i resultatets advarsler.

HTML er ikke en garanti for ferdig PDF-layout eller full semantikk i
originalmalen. Faktisk rendring må fortsatt kontrolleres med målmodulens
tester. Forhåndsvisningen er en ren transformasjon uten HTTP-server,
nettverkskall eller lagringsendringer.
