# Brevoppskriften - Autodokumentasjon for Brevbaker brev

For å bruke npm trenger du ha `node` installert.
Vi anbefaler at du bruker [asdf](https://asdf-vm.com/) slik at du automatisk kjører nødvendige pakker på støttet versjon.
`asdf` vil sette riktige versjoner for diverse pakker som trengs i dette repoet. Se `.tool-versions` i rot-folderen.

Etter at Node er installert kjører du følgende kommandoer for å starte:

```bash
npm i
npm run dev
```

Da vil Vite sin dev-server kjøre på `localhost:5173`.
For at API-kall skal fungere lokalt, anbefaler vi at du kjører via BFF.

For lokal kjøring med innlogging og malverksted, følg
[oppsettet for Docker Compose](../../brevoppskrift-backend/README.md#lokal-kjøring-med-docker-compose)
for bygg og henting av credentials. Fra repositoryets rot:

```bash
docker compose -f docker-compose.yml -f docker-compose.brevoppskrift.yml up -d --build
```

Åpne http://localhost:8088/vite-on

## Kjøre lokal frontend mot dev-miljøet

Start dev-serveren lokalt (`npm run dev`) og gå deretter til
https://pensjon-brevoppskrift.intern.dev.nav.no/vite-on

## Malverksted (arbeidsutkast)

Ruter: `/arbeidsutkast` (liste), `/arbeidsutkast/{id}` (editor), `/opprett` (nytt utkast fra bunnen eller fra malkatalogen).
All kode ligger i `src/verksted/`. API-et ligger under `/api/v1`.
Søk, dokumentasjon og Word-eksport krever ikke innlogging. Ved åpning av en verkstedrute
sender frontend uinnloggede brukere til `/oauth2/login` og tilbake til samme rute etter innlogging.
Forhåndslasting av lenker starter ikke innlogging. Wonderwall har `autoLogin: false` både lokalt og i Nais.
Alle API-ruter for verkstedet krever gyldig bruker i BFF og backend; skrivegruppen styrer endringstilgang.
Ved utløpt innlogging under redigering kan brukeren logge inn i en ny fane uten å miste lokale endringer.
Hvis backenden er nede, vises en gjenopprettbar feil mens gammel funksjonalitet fungerer uavhengig av dette.

- Skriving bruker CSRF double-submit: `GET /api/v1/csrf` hentes første gang en skriving trenger det, token sendes som `X-CSRF-Token`
  på alle POST/PUT/DELETE (også generering og forhåndsvisning). Kun `CSRF_REJECTED` gir nytt token og ett nytt forsøk; en 409 prøves aldri på nytt.
- Lagring er alltid eksplisitt. Ved revisjonskonflikt beholdes lokale endringer, og serverens versjon tas bare inn etter bekreftelse.
- Draft-modellen kommer fra `src/api/brevoppskrift-api.generated.ts` (generert av `./gradlew :brevoppskrift-backend:generateAuthoringApi`,
  rediger den aldri for hånd). `src/verksted/types.ts` bygger kun `Norm`-aliaser over de genererte typene, og `normalize.ts` fyller
  inn standardverdier for valgfrie felt. Konvoluttypene (`DraftRecord`, `DraftSummary`, `Me`, kapabiliteter, PR-jobb, vedleggsbruk)
  er genererte aliaser; ingenting av API-modellen er skrevet for hånd.
- Importerte kildeprofiler (tekstprofil, låst struktur) kan bare få tekst- og oversettelsesendringer. Alle struktur-, felt-,
  vilkår- og metadatakontroller er deaktivert/skjult, låste blokker kan ikke kopieres, slettes eller flyttes, og scenarier skjules.
- Ren logikk (`draftOps`, `validate`, `diff`, `comments`, `draftSession`, `aiSuggestion`) er enhetstestet uten DOM.
