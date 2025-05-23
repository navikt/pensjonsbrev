~~# Asynkron kø brevbestilling

* Status: [accepted]
* Deciders: Håkon Heggholmen, Alexander Hoem Rosbach, Mads Opheim

Technical Story: [Lage asynkron kø for brevbestillinger]

## Context and Problem Statement

I dag har vi kun ett synkront løp for bestilling av brev. Det å rendre en PDF med LaTeX tar flere sekunder, og vi har 
begrenset med ledig synkron kapasitet til en hver tid. Konsumenter må selv ta ansvar for å lage løsninger som ikke
overbelaster kapasiteten til brevbaker ved å ha en fornuftig algoritme for throttling av requests og retry ved server
feil (5xx)

## Decision Drivers
* [Ekstra kompleksitet i løsningene til konsumenter]
* [Forsinkelser i bestillingsløpet til konsumenter]
* [Uavklart ansvarsforhold ved tekniske feilmeldinger fra brevløsningen]
* [Kostnader ved ubrukt reservert CPU kapasitet på NAIS]

## Decision Outcome
Vi lager en kafka kø. Meldings-størrelsen kan økes, og ved test virker det som om meldingene 
er godt innenfor grensene. Kafka er godt støttet av NAIS og utviklere har bedre kjennskap til Kafka enn Redis.
Redis er også ikke like utprøvd som en kø mellom ulike tjenester.

### Positive Consequences <!-- optional -->

* Vi skjuler en del tekniske feil som kun vi skal agere på fra konsumenten.
* Vi samler kompleksitet fra for retry logikk og håndtering av våres tekniske feil ett sted.
* Vi kan skalere fort basert på antall meldinger i køen.
* Vi kan spare penger ved å redusere alltid reservert kapasitet på NAIS.

### Negative consequences <!-- optional -->

* Vi får driftsansvar for køen.
* Vi får mer kompleksitet i kodebasen våres.
* Vi må integrere tilbake til applikasjonen som kaller oss.
* Vi må passe på at vi ikke overbelaster bestiller/konsument.
## Considered Options

* [option 0] Sette opp rate-limiting av konsumenter, og overlate ansvaret til dem for å oppfylle dette.
* [option 1] Lage en kafka kø mellom brevbaker og pdf-bygger
* [option 2] Lage en redis kø mellom brevbaker og pdf-bygger
* [option 3] Lage en kafka kø med request/reply pattern rundt pdf-bygger

### [option 1] Lage en kafka kø mellom brevbaker og pdf-bygger

Lage en asynkron kø som tar i mot letter markup og produserer PDF.

Fordeler:
* Kafka er mye brukt i NAV, så vi får bruke mye eksisterende kompetanse og GCP støtte.
* Lettere for konsument å lese fordi Kafka er mye brukt i NAV.
* Lettere å skalere pdf-bygger basert på metrics som er tilgjengelige på gcp
Ulemper:
* Har begrenset kø-størrelse på 1MB, så vi kan i niche cases risikere at brev blir for store.


### [option 2] Lage en redis kø mellom brevbaker og pdf-bygger

Fordeler:
* Støtter større task elementer (512MB)
Ulemper:
* Ikke mange i nav som bruker redis for kø-mekanismer.
* Mer implementasjon må til for kø-mekanismen.


### [option 3] Lage en kafka kø med request/reply pattern rundt pdf-bygger
Fordeler:
* Vi trenger ikke å kjenne til kallende tjeneste (kafka setter svar på svar-kø de bestemmer selv).
* Kafka er mye brukt i NAV, så vi får bruke mye eksisterende kompetanse og GCP støtte.
Ulemper:
* Har begrenset kø-størrelse på 1MB, så vi kan i niche cases risikere at brev blir for store.
* Vi må vente til vi har gjordt om pdf-bygger til å ta i mot letter markup istedenfor LaTeX filer.~~
