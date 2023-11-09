# Backend for frontend (BFF) for fullmaktsregisteret

I hovedsak gjør applikasjonen:
* Servere frontend
* Validere token fra wonderwall
* Veksle til OBO-token for å proxye til andre api-er

## Lokal kjøring

Oppsett av secrets. Dette vil populere secrets nødvendig for alle tjenestene i repoet.
```bash
# Fra roten av prosjektet
gcloud auth login
python3 setup_local_azure_secrets.py
```

#### Autentiser PAT(public access token)

"@navikt/backend-for-frontend-utils" pakken ligger på github sitt registry, og er privat. Derfor trenger du en egen autnetisering for å kunne laste ned.

1. Følg guide for å opprette [PAT](https://docs.github.com/en/authentication/keeping-your-account-and-data-secure/managing-your-personal-access-tokens#creating-a-personal-access-token-classic). Du trenger `read:packages` scopet. Husk å kopier og lagre tokenet i 1password, dette er eneste gangen du kan hente det.
2. Logg inn med `npm login --registry=https://npm.pkg.github.com --auth-type=legacy`. Username er github brukernavnet ditt, men for passord bruker du tokenet du lagde i steg 1.

#### Innstaller avhengigheter
```bash
npm i #Installer avhengigheter
npm run build #Bygg applikasjonen til /dist
```

#### Ha docker kjørende og kjør frackend via docker
```bash
docker-compose up -d
```

For subsequent builds you can just build the frontend to avoid building wonderwall
```bash
docker-compose up -d --build frontend
```
