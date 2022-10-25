# Skribenten API
API for Skribenten. Kontaktflate for å snakke med database, Pesys o.l.

## Lokal kjøring
1. Kjør `./fetch-secrets.sh` i skribenten-backend mappen (viktig). Dette skal opprette filen `skribenten-backend/secrets/azuread.json`.
2. Kjør `SkribentenAppKt` med VM-options `-Dconfig.resource=application-local.conf`.
