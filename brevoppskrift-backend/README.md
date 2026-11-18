# Brevoppskrift: modell og kontrakt

Modulen inneholder den versjonerte utkastmodellen, dokumentvalidering,
serverstempling av gjennomgang og JSON-serialisering. Den er et bibliotek
uten HTTP-server, database eller deploy.

Bygg og generer kontrakten:

```shell
./gradlew :brevoppskrift-backend:generateAuthoringApi :brevoppskrift-backend:build
```

Kotlin-modellen er autoritativ. `generateAuthoringApi` lager
`brevoppskrift-web/frontend/src/api/brevoppskrift-api.generated.ts` og
JSON-skjemaer i `build/generated/authoring/openapi.json`. Genererte filer
skal ikke redigeres for hånd. OpenAPI-filen beskriver modellkomponenter,
ikke HTTP-endepunkter.
