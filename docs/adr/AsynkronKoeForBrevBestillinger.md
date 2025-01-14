# Asynkron kø for brevbestillinger

* Status: [draft]
* Deciders: Håkon Heggholmen

Technical Story: [Lage asynkron kø for brevbestillinger]

## Context and Problem Statement

I dag har vi kun ett synkront løp for bestilling av brev. Det å rendre en PDF med LaTeX tar flere sekunder, og vi har 
begrenset med ledig synkron kapasitet til en hver tid. For å komme rundt dette problemet i dag må konsumenter hensynta
våres ledige kapasitet. Konsumenter må også ta stilling til tekniske feil som team pensjonsbrev er ansvarlige for å løse
, og ha mekanismer for å forsøke på nytt i disse tilfellene. Det koster også penger å alltid ha tilgjengelige instanser 
kjørende for å kunne håndtere store volum synkrone bestillinger.

## Decision Drivers
* [Ekstra kompleksitet i løsningene til konsumenter]
* [Forsinkelser i bestillingsløpet til konsumenter]
* [Uavklart ansvarsforhold ved tekniske feilmeldinger fra brevløsningen]
* [Kostnader ved ubrukt reservert CPU kapasitet på NAIS]

## Considered Options

* [option 1] Lage en kafka kø mellom brevbaker og pdf-bygger
* [option 2] 
* [option 3]

### [option 1] Lage en kafka kø mellom brevbaker og pdf-bygger

Lage en asynkron kø som tar i mot letter markup og produserer PDF.

Fordeler:
* Vi skjuler en del tekniske feil som kun vi skal agere på fra konsumenten.
* Vi samler kompleksitet fra for retry logikk og håndtering av våres tekniske feil ett sted.
* Vi kan skalere fort basert på antall meldinger i køen.
* Vi kan spare penger ved å redusere alltid reservert kapasitet på NAIS.
Ulemper:
* Vi får driftsansvar for køen.
* Vi får mer kompleksitet i kodebasen våres.
