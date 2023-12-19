# Pensjonsbrev
This is a mono-repo for the microservices that together form the new letter ordering system.


## Lokal kjøring av brevbaker og pdf-bygger

For å kjøre løsningen lokalt må man ha docker og docker compose installert.
Kjør `docker compose up -d` i rot-katalogen til prosjektet.

### Debugge pdf-bygger lokalt
Pdf-byggeren er avhengig av ulike pakker for å kompilere LaTeX til pdf.
Derfor må man kjøre pdf-bygger java applikasjonen inne i containeren for at den skal fungere.
Når man kjører lokalt med docker compose er den allerede satt opp til remote debug på port 5016 ([se docker-compose.yml](docker-compose.yml)).

### Ytelsestesting med locust
Ytelsestesten er i utgangspunktet satt opp til å teste vedtaksbrevet UNG_UFOER_AUTO.
1. Evt. rediger `locust/autobrev_request.json` om du ønsker å teste et annet brev.
2. Kjør `./locust/fetch-secrets.sh`
3. Start docker compose med locust profil `docker compose --profile locust up`
4. Gå inn på locust grensesnittet via http://localhost:8089/ og skriv inn url til endepunktet du ønsker å ytelses-teste.
[Se dokumentasjon fra locust for mer info om bruk.](http://docs.locust.io/en/stable/quickstart.html#locust-s-web-interface)

## Endring av obligatoriske felter i API-model

Brevbakeren bruker pensjon-brevbaker-api-model for bestilling av brev.
Api modellen eksporteres som artifakt og brukes av eksterne systemer for å fylle ut informasjon som kreves ved bestilling av brev.

Vi må kunne endre på obligatoriske felter i api modellen uten å ødelegge pågående brevbestillinger i produksjon.
For å oppnå dette må man ha en overgangsperiode hvor brevbakeren er kompatibel med gammel og ny versjon av api modellen.
Vi kan ikke bytte direkte over til ny versjon uten å gjøre avsender inkompatibel med mottaker(brevbaker), noe som vil kreve nedetid.

### Lage duplikate felter i en overgangsperiode

La oss si at vi skal erstatte ett felt annetBeloep med barnetillegg:

```
// før
data class PensjonsBrevDto(
    val annetBeloep: Int,
)

// etter
data class PensjonsBrevDto(
    val barnetillegg: Kroner,
)
```

En strategi for overgangen kan se slik ut:
1. Påkrev begge versjoner av feltene samtidig og bruk denne modellen på avsender-siden. 
   Brevbakeren er satt opp til å ignorere ukjente felter(fail on unknown properties=false), så den vil fortsette å lese annetBeloep i dette tilfellet.
    ```
    // overgangsperiode
    data class PensjonsBrevDto(
        val barnetillegg: Kroner,
        val annetBeloep: Int,
    )
    ```
2. Ta i bruk de nye feltene i Brevbakeren. Nå vil brevbakeren ignorere det gamle feltet og bruke det nye:
   ```
   // 
   data class PensjonsBrevDto(
       val barnetillegg: Kroner,
   )
    ```
3. Ta i bruk den nye versjonen i avsender systemet.