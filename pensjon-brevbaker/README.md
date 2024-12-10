# Brevbaker

## Lokal kjøring (utenfor docker-compose)
- Krever at pdf-bygger kjører lokalt (antatt at kjører på localhost:8081)
- Krever følgende miljøvariabler:
  ```shell
  PDF_BUILDER_URL=http://localhost:8081
  AZURE_OPENID_CONFIG_JWKS_URI=http://vtp-pensjon:8060/rest/isso/oauth2/connect/jwk_uri
  AZURE_OPENID_CONFIG_ISSUER=https://login.microsoftonline.com/966ac572-f5b7-4bbe-aa88-c76419c0f851/v2.0
  AZURE_APP_CLIENT_ID=brevbaker-123
  AZURE_APP_PRE_AUTHORIZED_APPS=[{"name":"pen","clientId":"pen-AzureClientId"}]
  ```
- -Dio.ktor.development=true

### Hvis du ikke er i team pensjonsbrev
- Hvis du ikke er i team pensjonsbrev, vil du ikke få tilgang til secret for å starte opp med Unleash. Da vil brevbaker i utgangspunktet ikke starte. Du kan omgå dette ved å konfigurere opp Unleash-variablene, og så vil det fungere helt fint fram til en toggle blir kalt. For det må du sette opp disse variablene:
  ````
    UNLEASH_SERVER_API_ENV=development
    UNLEASH_SERVER_API_TOKEN=token123
    UNLEASH_SERVER_API_URL=https://teampensjon-unleash-api.nav.cloud.nais.io
    NAIS_APP_NAME=brevbaker
    ```