# Brevbaker

## Lokal kjøring (utenfor docker-compose)
- Krever at pdf-bygger kjører lokalt (antatt at kjører på localhost:8081)
- Krever følgende miljøvariabler:
  ```shell
  PDF_BUILDER_URL=http://localhost:8081
  JWT_JWKS_URL_STS=http://localhost:8060/rest/isso/oauth2/connect/jwk_uri
  JWT_ISSUER_STS=http://vtp-pensjon:8060
  JWT_AUDIENCE_STS=brevbaker-123
  JWT_JWKS_URL_AZURE_AD=http://localhost:8060/rest/isso/oauth2/connect/jwk_uri
  JWT_ISSUER_AZURE_AD=https://login.microsoftonline.com/966ac572-f5b7-4bbe-aa88-c76419c0f851/v2.0
  JWT_AUDIENCE_AZURE_AD=brevbaker-123
  ```
- -Dio.ktor.development=true
