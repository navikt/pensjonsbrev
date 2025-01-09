# Backend for frontend (BFF) for skribenten-web

I hovedsak gjør applikasjonen:
* Servere frontend
* Validere token fra wonderwall
* Veksle til OBO-token for å proxye til andre api-er

## Lokal kjøring

### Oppsett av secrets
Dette vil populere secrets nødvendig for alle tjenestene i repoet.
```bash
gcloud auth login
python3 setup_local_azure_secrets.py
```

#### Autentiser PAT(public access token)

`@navikt/vite-mode` pakken ligger på github sitt registry, og er privat. Derfor trenger du en egen autnetisering for å kunne laste ned.

1. Følg guide for å opprette [PAT](https://docs.github.com/en/authentication/keeping-your-account-and-data-secure/managing-your-personal-access-tokens#creating-a-personal-access-token-classic). Du trenger `read:packages` scopet. Husk å kopier og lagre tokenet i 1password, dette er eneste gangen du kan hente det.
2. Logg inn med `npm login --registry=https://npm.pkg.github.com --auth-type=legacy`. Username er github brukernavnet ditt, men for passord bruker du tokenet du lagde i steg 1.

### Bygg BFF
```bash
npm i #Installer avhengigheter
npm run build #Bygg applikasjonen til /dist
```

#### Ha docker kjørende, og kjør BFF via docker
```bash
docker-compose up -d
```

For påfølgende bygg kan du be docker-compose om å bare bygge BFF-appen
```bash
npm run build
docker-compose up -d --build frontend
```

### Logg inn via Wonderwall
1. I `frontend`-katalogen kjører du `npm run dev`. Vite-devserver vil nå kjøre på `localhost:5173`.
2. Gå til `localhost:8080` og logg inn med en trygdeetat bruker.
   1. Etter redirect vil du få en feilmelding, det er forventet.
3. Gå så til `localhost:8080/vite-on`. Frontenden skal nå være tilgjengelig med API-kall og hele pakka.

### Valg av miljø som kjøres mot
Frontend kan targete lokalt, eller mot q2. 

#### For targeting lokalt
Se i [se i root readme](../../README.md) for oppstart av applikasjonen lokalt. 

#### For targeting Q2
Gå til https://pensjon-skribenten-web-q2.intern.dev.nav.no/vite-on

