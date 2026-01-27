# Umami Analytics - Proof of Concept

Dette dokumentet beskriver implementasjonen av Umami-analyse i Skribenten frontend som en proof of concept (POC).

## Oversikt

Umami er NAVs foretrukne verktøy for å måle brukeratferd på en personvernvennlig måte. Denne POC demonstrerer hvordan Umami kan integreres i Skribenten for å gi verdifull innsikt i brukeratferd.

## Fordeler med Umami

### 1. Personvern

- **GDPR-kompatibel**: Umami samler ikke inn personopplysninger
- **Automatisk vasking**: NAVs Umami-proxy fjerner automatisk sensitive data som fødselsnummer, e-post og telefonnummer
- **Respekterer DNT**: Integrert støtte for "Do Not Track" preferanser

### 2. Tekniske fordeler

- **Lett**: Umami-scriptet er minimalt og påvirker ikke ytelsen
- **Ingen cookies**: Krever ikke samtykke for standard sidevisninger
- **Sanntidsdata**: Se brukerdata umiddelbart i dashboardet

### 3. Analyseverdi

- **Sidevisninger**: Automatisk sporing av hvilke sider som brukes mest
- **Brukerflyt**: Forstå hvordan brukere navigerer gjennom applikasjonen
- **Hendelsesporing**: Custom events for viktige handlinger (f.eks. vedlegg lagt til/fjernet)
- **Feilsporing**: Identifiser hvor brukere møter problemer

### 4. Innsikt for teamet

- **Produktbeslutninger**: Data-drevet prioritering av funksjoner
- **UX-forbedringer**: Identifiser forvirrende brukerflyter
- **Feilsøking**: Forstå kontekst når feil oppstår

## Implementasjon

### Filstruktur

```
src/
├── hooks/
│   └── useUmami.ts          # React hooks for tracking
├── utils/
│   ├── umami.ts             # Kjernefunksjonalitet
│   └── umamiConfig.ts       # Konfigurasjon
└── routes/
    └── __root.tsx           # Global rutesporing
```

### Kjernemoduler

#### 1. `umami.ts` - Hovedfunksjonalitet

```typescript
// Spor en hendelse
trackEvent("vedlegg lagt til", { type: "PE-001", status: "suksess" });

// Spor sidevisning
trackPageView("/saksnummer/123/brevbehandler");

// Komponent-scoped tracking
const tracker = createComponentTracker("Vedlegg");
tracker.trackAction("åpnet", { type: "modal" });
```

#### 2. `useUmami.ts` - React Hooks

```typescript
// Basic tracking hook
const { track, trackAction } = useUmami("Vedlegg");
track("vedlegg lagt til", { antall: 2 });

// Automatisk modal-sporing
useUmamiModal("LeggTilVedlegg", isModalOpen);

// Automatisk sidevisning-sporing
useUmamiPageView("Brevbehandler");

// Tid-brukt sporing
useUmamiTimeSpent("BrevRedigering", 10000);
```

### Eksempel: Vedlegg-komponent

```typescript
export const Vedlegg = (props: { ... }) => {
  const { track } = useUmami("Vedlegg");

  // Automatisk modal tracking
  useUmamiModal("LeggTilVedlegg", isModalOpen);

  const leggTilVedleggMutation = useMutation({
    // ...
    onSuccess: (data) => {
      track("vedlegg lagt til", {
        antall: data.valgteVedlegg?.length,
        status: "suksess",
      });
    },
    onError: () => {
      track("vedlegg lagt til", { status: "feil" });
    },
  });
};
```

## NAV Taksonomi

Vi følger NAVs standard taksonomi for hendelsesnavn:

| Handling    | Beskrivelse            |
| ----------- | ---------------------- |
| `klikk`     | Brukerklikk            |
| `vis`       | Visning av element     |
| `åpne`      | Åpning av modal/panel  |
| `lukke`     | Lukking av modal/panel |
| `endre`     | Endring av verdi       |
| `sende`     | Innsending av skjema   |
| `velge`     | Valg av element        |
| `fjerne`    | Fjerning av element    |
| `legge til` | Tillegging av element  |

### Eksempler på hendelsesnavn

- `vedlegg lagt til`
- `vedlegg fjernet`
- `modal åpnet`
- `brev ferdigstilt`
- `navigasjon side lastet`

## Oppsett

### 1. Få sporingskode

Kontakt **#ResearchOps** på Slack for å få:

- Website ID for dev-miljø
- Website ID for prod-miljø

### 2. Oppdater index.html

```html
<script
  defer
  src="https://umami.nav.no/script.js"
  data-website-id="DIN_WEBSITE_ID"
  data-domains="pensjonsbrev.intern.dev.nav.no"
  data-auto-track="true"
  data-do-not-track="true"
></script>
```

### 3. Sett miljøvariabler (valgfritt)

For fleksibel konfigurasjon kan du bruke miljøvariabler:

```bash
VITE_UMAMI_WEBSITE_ID=din-website-id
VITE_UMAMI_ENABLED=true
```

## Testing i dev-miljø

I utviklingsmodus logger tracking-kall til konsollen:

```
[Umami POC] Event tracked: vedlegg lagt til { antall: 1, status: "suksess" }
```

Dette gjør det enkelt å verifisere at tracking fungerer uten å trenge tilgang til Umami-dashboardet.

## Personvernhensyn

### Ikke spor:

- ❌ Innhold fra fritekstfelt
- ❌ Søkeord
- ❌ Fødselsnummer, e-post, telefonnummer
- ❌ Saksinnhold eller brevtekst

### OK å spore:

- ✅ Sidevisninger (anonymiserte URL-er)
- ✅ Knappeklikk
- ✅ Modal åpning/lukking
- ✅ Antall elementer (uten identifiserende info)
- ✅ Feiltyper (uten PII)

## Produksjonsklargjøring

Før produksjon:

1. [ ] Fyll ut Umami etterlevelsesdokument
2. [ ] Oppdater personvernsidene på nettsiden
3. [ ] Få godkjenning fra personvernombud
4. [ ] Bytt til prod website ID
5. [ ] Verifiser at tracking fungerer med målesjekk

## Analyse av data

Data fra Umami kan analyseres via:

- **Umami Dashboard**: Grunnleggende analyser
- **Datamarkedsplassen**: Avanserte analyser i Metabase, Grafana
- **Grafbyggeren**: Lag egne grafer og tabeller

## Ressurser

- [NAV Umami Guide](https://aksel.nav.no/god-praksis/artikler/umami-maling)
- [Umami Dokumentasjon](https://umami.is/docs)
- [NAV Taksonomi](https://aksel.nav.no/god-praksis/artikler/taksonomi)
- Slack: #ResearchOps

## Kontakt

For spørsmål om denne implementasjonen, kontakt teamet eller #ResearchOps på Slack.
