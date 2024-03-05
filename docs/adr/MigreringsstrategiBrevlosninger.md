# Valg av migreringsstrategi for brevbaker

* Status: [accepted] 
* Deciders: Mo Amini , Håkon Heggholmen, Alexander Hoem Rosbach, Jeremy Elsom
* Date: 29.02.2024

Technical Story: [description | ticket/issue URL] 

## Context and Problem Statement

Brevmaler eksisterer idag i 3 systemer, exstream, doksys og brevbaker. Vi må få samlet malene i det valgte systemet, Brevbaker&trade;.
Hvordan oppnår vi det på en måte som reduserer vedlikeholdskompleksistet, øker endringsdyktighet og gir størst verdi fortest mulig.

## Decision Drivers 

* [driver 1] [Kostnader knyttet til gamle brevløsninger; herunder lisenser, gebyr pr brevproduksjon , drift av flere løsninger]
* [driver 2] [Komplekse verdikjeder og kode for å innhente data og populere brev med tilhørende logikk utenfor pensjo]
* [driver 3] [Endringer og utvikling av brev er utenfor pensjon, dette fører til mangel på eierskap av kode]
* [driver 4] [Gammel properitær tredjepartsteknologi]
* [driver 5] [Manglende kompetanse i pensjonsområdet for å endre og utvikle brev i metaforce og exstream] 
* [driver 6] [Dårlig utviklingsverktøy i gammle brevløsninger]
* [driver 7] [Avhengig av en bestemt versjon av Word ( 2016 ) som benyttes av redigeringsverktøy ( metawrite og exstream brevklient)]
* [driver 8] [Exstream er avhengig av BUS og stormaskin]
* [driver 9] [Det er mer teknisk og funksjonell gjeld i exstream enn doksys]
* [driver 10] [Forretningslogikk for et brev er spredd over flere systemer, også utenfor pensjon]
* [driver 11] [Mye funksjonell gjeld i brevmaler og system som gir høy risiko for feil innhold i brev]
* [driver 12] [Vanskelig å forstå det totale bildet av hvordan innholdet i et brev produseres]
* [driver 13] [Brevbaker støtter pr. idag kun auto brev]
* [driver 14] [Exstream har i hovedsak fagdomenet uføretrygd og doksys har i hovedsak alderspensjon]

## Considered Options

* Migrere maler i rekkefølgen: exstream, doksys
* Migrere maler i rekkefølgen: exstream auto, doksys auto, exstream redigerbar, doksys redigerbar 
* Migrere maler uten å forbedre funksjonell- og teknisk gjeld i rekkefølgen: exstream, doksys

## Decision Outcome

Chosen option: "[option 3]", fordi vi fokuserer på et system først og dermed kan redusere fra 3 til 2 brevløsninger tidligere. Ved nåværende tidspunkt har det blitt bestemt at 
det ikke blir opprettet et innholdsteam som kan støtte oss med faglige arbeidet. Og vi har blitt redusert med en utvikler som vil senke tempoet med å utvikle Brevbaker&trade; til å støtte 
redigerbare brev.


### Positive Consequences 

Vi blir kvitt en brevløsning tidligere

### Negative consequences 

vi overfører funksjonell- og teknisk gjeld i brevmaler

## Pros and Cons of the Options

### [option 1]
Migrere maler i rekkefølgen: exstream, doksys

Bra, fordi vi fokuserer på et system først og dermed kan redusere fra 3 til 2 brevløsninger tidligere
Bra, fordi vi fokuserer på et fagdomene om gangen
Bra, fordi vi fjerner funksjonell- og teknisk gjeld i brevmaler
Dårlig, fordi det er veldig tid- og ressurskrevende


### [option 2]
Migrere maler i rekkefølgen: exstream auto, doksys auto, exstream redigerbar, doksys redigerbar

Bra, fordi vi ikke er avhengig av støtte for redigerbare brev i brevbaker umiddelbart
Bra, fordi vi kan sanere auto maler i begge systemer
Bra, fordi vi fjerner funksjonell- og teknisk gjeld i brevmaler
Dårlig, fordi vi fokuserer på maler i flere system og dermed ikke kan redusere fra 3 til 2 brevløsninger før vi er helt ferdig
Dårlig, fordi det er veldig tid- og ressurskrevende


### [option 3]
Migrere maler uten å forbedre funksjonell- og teknisk gjeld i rekkefølgen: exstream, doksys

Bra, fordi vi fokuserer på et system først og dermed kan redusere fra 3 til 2 brevløsninger tidligere
Bra, fordi vi fokuserer på et fagdomene om gangen
Dårlig, fordi vi overfører funksjonell- og teknisk gjeld i brevmaler

## Links 

* [Link type] [Link to ADR] <!-- example: Refined by [ADR-0005](0005-example.md) -->
* … <!-- numbers of links can vary -->