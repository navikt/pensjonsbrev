# Brevoppskrift-backend

Modulen inneholder den versjonerte utkastmodellen, dokumentvalidering,
serverstempling av gjennomgang og JSON-serialisering.

Lagringslaget bruker PostgreSQL, Exposed, HikariCP og Flyway. Utkast lagres
som JSONB med revisjonskontroll, unik aktiv kilde og arkivering.
`openDatabase` kjører migreringene i `src/main/resources/db/migration`
før tilkoblingspoolen returneres. Modulen er et bibliotek uten HTTP-server
eller deploy.

Bygg og test:

```shell
./gradlew :brevoppskrift-backend:build
```

Lagringstestene krever Docker og starter en isolert PostgreSQL-container.

## Kildekatalog

`generateSourceCatalogue` leser registrerte maler og tilhørende Kotlin-kilder
fra én Git-commit (`HEAD`), ikke fra arbeidskopien. Oppgaven kjøres før
`processResources` og pakker katalogen i biblioteket. Importverktøyets
Kotlin-kompilator/PSI ligger i `authoringTools`, ikke på runtime-classpath.
Oppgaven kjøres også når bare committen har endret seg.

Katalogen oppgir hvilke maler som kan importeres, med diagnostikk for
profiler som ikke støttes. Importerte maler tillater bare tekstendringer;
struktur, metadata, felt og kildeidentitet er låst.

`BuildSourceCatalogue.persistent` beholder betrodde, redigerbare
kildeversjoner i PostgreSQL før katalogen tas i bruk. Tidligere utkast kan
dermed valideres selv om malen er endret eller fjernet fra neste bygg.
Historiske oppslag krever eksakt kilde-ID, commit og filhash-er.

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
