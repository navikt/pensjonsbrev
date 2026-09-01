---
name: documentation
description: Dokumentasjon av kode, funksjonalitet, arkitektur og annet av teknisk natur
license: MIT
metadata:
  domain: general
  tags: documentation comments kdoc javadoc
---

# Dokumentasjon

Skriv kort og konsis dokumentasjon. Fokuser på nå-tilstand, og ekskluder historikk og diskusjoner/samtaler med mindre det er ytterst relevant.

## Kommentarer i kode

* Skriv kun kommentarer i kode når det er helt nødvendig for å forstå hvorfor koden er skrevet slik den er. 
* Kommentarer skal i størst mulig grad ikke være lenger enn én linje (150 tegn).
* Historikk skal ikke fremkomme i kommentarer.
* Vurder alltid om en kommentar virkelig er nødvendig.

## Dokumentasjon i kode

Dette gjelder KDoc, Javadoc og JSDoc (o.l.) av funksjoner, klasser, variabler osv. 

* Hovedsaklig skal det kun dokumenteres funksjonalitet, tiltenkt bruk og rammer for inndata, resultat og evt. side-effekter.
* Unngå historikk.
* Unngå å dokumentere noe som er åpenbart ut fra kildekoden det dokumenteres, med mindre det er relevant for den som skal bruke det som dokumenteres.
