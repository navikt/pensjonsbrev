# Brevoppskrift: utkastmodell, lagring og kildekatalog

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
