# Brevoppskrift: utkastmodell og lagring

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
