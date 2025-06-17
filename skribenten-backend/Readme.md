# Skribenten API
API for Skribenten. Kontaktflate for å snakke med database, Pesys o.l.

## Lokal kjøring
1. Kjør `./fetch-secrets.sh` i skribenten-backend mappen (viktig). Dette skal opprette filen `skribenten-backend/secrets/azuread.json`.
2. Kjør docker compose, slik at Skribenten har en database å gå mot: `docker compose --profile skribenten up -d --build`
3. Kjør `SkribentenAppKt` med VM-options `-Dio.ktor.development=true -Dconfig.resource=application-local.conf`.
