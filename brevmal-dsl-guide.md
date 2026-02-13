# Guide: Brevmal-DSL

Denne guiden beskriver hvordan du bruker brev-DSL-en til å lage brevmaler med logikk, dynamisk innhold og gjenbrukbare komponenter.

## Innhold

- [Oversikt](#oversikt)
- [Grunnleggende malstruktur](#grunnleggende-malstruktur)
- [Dokumentelementer](#dokumentelementer)
- [Flerspråklig tekst](#flerspråklig-tekst)
- [Uttrykk (Expression)](#uttrykk-expression)
- [Betinget logikk](#betinget-logikk)
- [Iterasjon med forEach](#iterasjon-med-foreach)
- [Gjenbrukbare fraser (Phrase)](#gjenbrukbare-fraser-phrase)
- [Vedlegg](#vedlegg)
- [Tips og best practices](#tips-og-best-practices)
- [Anti-patterns å unngå](#anti-patterns-å-unngå)

---

## Oversikt

Brev-DSL-en er et Kotlin-basert templatespråk for å definere brevmaler. Malene er typesikre, flerspråklige og kompileres til strukturerte brevdefinisjoner. All dynamisk logikk uttrykkes via DSL-konstruksjoner – ikke vanlig Kotlin-kode.

**Nøkkelegenskaper:**
- **Typesikkerhet** – Kotlin-kompilatoren verifiserer at malen er korrekt
- **Flerspråklighet** – Innebygd støtte for bokmål, nynorsk og engelsk
- **Expression-basert** – Dynamiske verdier bruker `Expression<T>`-systemet
- **Gjenbruk** – Fraser (`Phrase`) kan deles mellom maler

---

## Grunnleggende malstruktur

En brevmal er et Kotlin-objekt som implementerer `RedigerbarTemplate` (redigerbart brev) eller `AutobrevTemplate` (automatisk brev).

```kotlin
@TemplateModelHelpers
object MittBrev : RedigerbarTemplate<MittBrevDto> {

    override val kode = Pesysbrevkoder.Redigerbar.MITT_BREV
    override val kategori = Brevkategori.VEDTAK
    override val brevkontekst = TemplateDescription.Brevkontekst.VEDTAK
    override val sakstyper = setOf(Sakstype.UFOREP)

    override val template = createTemplate(
        languages = languages(Bokmal, Nynorsk),
        letterMetadata = LetterMetadata(
            displayTitle = "Mitt vedtaksbrev",
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        )
    ) {
        title {
            text(
                bokmal { +"Tittel på brevet" },
                nynorsk { +"Tittel på brevet" },
            )
        }
        outline {
            // Brevinnholdet defineres her
        }
    }
}
```

**Merk:** `@TemplateModelHelpers` genererer selector-extensions for DTO-en, slik at du kan navigere datamodellen med typesikre uttrykk.

### Maltyper

| Type | Interface | Beskrivelse |
|------|-----------|-------------|
| Redigerbart brev | `RedigerbarTemplate<Dto>` | Kan redigeres av saksbehandler |
| Automatisk brev | `AutobrevTemplate<Dto>` | Sendes automatisk uten redigering |

### Språkvalg

```kotlin
languages(Bokmal)                        // Kun bokmål
languages(Bokmal, Nynorsk)               // Bokmål og nynorsk
languages(Bokmal, Nynorsk, English)      // Alle tre språk
```

---

## Dokumentelementer

### Paragraph (avsnitt)

Det mest brukte elementet. Inneholder tekst, lister, tabeller og skjemafelt.

```kotlin
outline {
    paragraph {
        text(
            bokmal { +"Dette er et avsnitt med tekst." },
            nynorsk { +"Dette er eit avsnitt med tekst." },
        )
    }
}
```

### Overskrifter (title1, title2, title3)

Tre nivåer av overskrifter er tilgjengelige:

```kotlin
outline {
    title1 {
        text(
            bokmal { +"Hovedoverskrift" },
            nynorsk { +"Hovudoverskrift" },
        )
    }
    title2 {
        text(
            bokmal { +"Underoverskrift" },
            nynorsk { +"Underoverskrift" },
        )
    }
}
```

### Lister

Punktlister defineres med `list` og `item`:

```kotlin
paragraph {
    text(
        bokmal { +"For å ha rett til ytelsen må du:" },
        nynorsk { +"For å ha rett til ytinga må du:" },
    )
    list {
        item {
            text(
                bokmal { +"Være mellom 18 og 67 år." },
                nynorsk { +"Vere mellom 18 og 67 år." },
            )
        }
        item {
            text(
                bokmal { +"Ha vært medlem av folketrygden." },
                nynorsk { +"Ha vore medlem av folketrygda." },
            )
        }
    }
}
```

### Tabeller

Tabeller har en header med kolonnedefinisjoner og rader med celler:

```kotlin
paragraph {
    table(
        header = {
            column {
                text(
                    bokmal { +"Periode" },
                    nynorsk { +"Periode" },
                )
            }
            column(alignment = ColumnAlignment.RIGHT) {
                text(
                    bokmal { +"Beløp" },
                    nynorsk { +"Beløp" },
                )
            }
        }
    ) {
        row {
            cell {
                text(
                    bokmal { +"Januar 2025" },
                    nynorsk { +"Januar 2025" },
                )
            }
            cell {
                eval(beloep.format())
            }
        }
    }
}
```

**Kolonnealternativer:**
- `alignment` – `ColumnAlignment.LEFT` (standard), `ColumnAlignment.RIGHT`
- `columnSpan` – antall kolonner cellen spenner over

### Skjemafelt

For redigerbare brev kan du legge inn skjemafelt der saksbehandler fyller inn tekst:

```kotlin
paragraph {
    text(
        bokmal { +"Vi mottok søknaden din " + fritekst("dato") + "." },
        nynorsk { +"Vi mottok søknaden din " + fritekst("dato") + "." },
    )
}
```

**`fritekst(prompt)`** – Lager et felt med en ledetekst som saksbehandler erstatter med faktisk verdi.

Du kan også bruke `formText` og `formChoice` for strukturerte skjemafelt:

```kotlin
paragraph {
    formText(size = Form.Text.Size.LONG) {
        text(
            bokmal { +"Beskriv situasjonen:" },
            nynorsk { +"Beskriv situasjonen:" },
        )
    }
    formChoice {
        text(
            bokmal { +"Velg alternativ:" },
            nynorsk { +"Vel alternativ:" },
        )
    }
}
```

---

## Flerspråklig tekst

All tekst i maler defineres for hvert støttet språk med `text()`-funksjonen:

```kotlin
text(
    bokmal { +"Tekst på bokmål" },
    nynorsk { +"Tekst på nynorsk" },
    english { +"Text in English" },
)
```

### Sette sammen tekst og uttrykk

Bruk `+`-operatoren for å sette sammen statisk tekst og dynamiske uttrykk:

```kotlin
text(
    bokmal { +"Du får " + uforegrad.format() + " prosent uføretrygd." },
    nynorsk { +"Du får " + uforegrad.format() + " prosent uføretrygd." },
)
```

### Sitattegn

Bruk `.quoted()` for å legge til språkriktige anførselstegn:

```kotlin
text(
    bokmal { +"Vi viser til " + "folketrygdloven".quoted() + "." },
)
```

### Inline uttrykk med `eval()`

Bruk `eval()` for å sette inn et uttrykk direkte uten statisk tekst rundt:

```kotlin
paragraph {
    eval(beloep.format())
}
```

### Linjeskift

```kotlin
text(
    bokmal { +"Første linje" },
)
newline()
text(
    bokmal { +"Andre linje" },
)
```

---

## Uttrykk (Expression)

Expression-systemet er kjernen i dynamisk innhold. Alle verdier fra datamodellen er `Expression<T>` – de evalueres ved brevgenerering, ikke ved kompilering.

### Tilgang til data

I `outline`-blokken har du tilgang til datamodellen via genererte selectors:

```kotlin
outline {
    // Naviger datamodellen med selectors (generert av @TemplateModelHelpers)
    val uforegrad = pesysData.uforegrad
    val virkningsdato = pesysData.virkningFom
}
```

### Literaler

Konverter vanlige verdier til uttrykk med `.expr()`:

```kotlin
val grense = 100.expr()
val flagg = true.expr()
```

### Formatering

```kotlin
// Tall
uforegrad.format()                    // Heltall → "75"
beloep.format()                       // Kroner → "12 345"
prosent.format(scale = 2)             // Desimal → "75,50"

// Datoer
virkningsdato.format()                // LocalDate → "01.01.2025"
```

### Sammenligningsoperatorer

```kotlin
verdi.equalTo(annenVerdi)             // ==
verdi.notEqualTo(annenVerdi)          // !=
verdi.greaterThan(annenVerdi)         // >
verdi.lessThan(annenVerdi)            // <
verdi.greaterThanOrEqual(annenVerdi)  // >=
verdi.lessThanOrEqual(annenVerdi)     // <=
```

### Logiske operatorer

```kotlin
betingelse1 and betingelse2           // OG
betingelse1 or betingelse2            // ELLER
not(betingelse)                       // IKKE
```

### Null-håndtering

```kotlin
verdi.notNull()                       // Expression<Boolean> – er ikke null?
verdi.isNull()                        // Expression<Boolean> – er null?
verdi.ifNull(standardVerdi)           // Bruk standardverdi hvis null
```

### Enum-sjekk med `isOneOf`

Sjekk om en verdi er én av flere enum-verdier:

```kotlin
sivilstand.isOneOf(Sivilstand.GIFT, Sivilstand.REGISTRERT_PARTNER)
```

### Betinget uttrykk med `ifElse`

For inline betinget verdivalg (ikke visning):

```kotlin
val tekst = ifElse(
    erGift,
    "ektefelle".expr(),
    "samboer".expr()
)
```

### Samlingsoperasjoner

```kotlin
perioder.size()                       // Antall elementer
perioder.isEmpty()                    // Er tom?
perioder.isNotEmpty()                 // Er ikke tom?
perioder.map { it.beloep }           // Transformer elementer
```

### Aritmetikk

```kotlin
verdi1 + verdi2                       // Addisjon (Int)
verdi1 - verdi2                       // Subtraksjon (Int)
```

### Strengoperasjoner

```kotlin
streng1 + streng2                     // Sammenslåing
```

---

## Betinget logikk

Betinget logikk styrer hvilke deler av brevet som vises basert på data. **Viktig:** Bruk alltid DSL-konstruksjonene (`showIf`, `orShow`, etc.) – ikke vanlig Kotlin `if/else`.

### `showIf` – Vis innhold betinget

```kotlin
showIf(harBarnetillegg) {
    paragraph {
        text(
            bokmal { +"Du er innvilget barnetillegg." },
            nynorsk { +"Du er innvilga barnetillegg." },
        )
    }
}
```

### `showIf` ... `orShow` – If/else

```kotlin
showIf(borINorge) {
    paragraph {
        text(
            bokmal { +"Uføretrygden utbetales den 20. hver måned." },
            nynorsk { +"Uføretrygda blir utbetalt den 20. kvar månad." },
        )
    }
}.orShow {
    paragraph {
        text(
            bokmal { +"Utbetalingen til utenlandsk konto kan bli forsinket." },
            nynorsk { +"Utbetalinga til utanlandsk konto kan bli forseinka." },
        )
    }
}
```

### `showIf` ... `orShowIf` – Kjeding av betingelser

For flere gjensidig utelukkende betingelser:

```kotlin
showIf(ytelse.isOneOf(KonteringType.AFP_KOMP_TILLEGG)) {
    text(
        bokmal { +"AFP kompensasjonstillegg" },
        nynorsk { +"AFP kompensasjonstillegg" },
    )
}.orShowIf(ytelse.isOneOf(KonteringType.AFP_KRONETILLEGG)) {
    text(
        bokmal { +"AFP kronetillegg" },
        nynorsk { +"AFP kronetillegg" },
    )
}.orShow {
    text(
        bokmal { +"Annen ytelse" },
        nynorsk { +"Anna yting" },
    )
}
```

### `ifNotNull` – Null-sikker visning

Vis innhold kun dersom en nullable verdi finnes, og få tilgang til den utpakkede verdien:

```kotlin
ifNotNull(avtaleland) { land ->
    paragraph {
        text(
            bokmal { +"Vedtaket er gjort etter trygdeavtalen med " + land + "." },
            nynorsk { +"Vedtaket er gjort etter trygdeavtalen med " + land + "." },
        )
    }
}
```

**Flere nullable verdier:**

```kotlin
ifNotNull(trygdetidfom, trygdetidtom) { fom, tom ->
    paragraph {
        text(
            bokmal { +"Du har vært medlem fra " + fom.format() + " til " + tom.format() + "." },
            nynorsk { +"Du har vore medlem frå " + fom.format() + " til " + tom.format() + "." },
        )
    }
}
```

**Med fallback via `.orShow` eller `.orIfNotNull`:**

```kotlin
ifNotNull(spesifikkVerdi) { verdi ->
    text(bokmal { +"Verdien er: " + verdi })
}.orIfNotNull(alternativVerdi) { alt ->
    text(bokmal { +"Alternativ: " + alt })
}.orShow {
    text(bokmal { +"Ingen verdi funnet" })
}
```

### Nøsting av betingelser

Du kan nøste `showIf` inne i andre `showIf`-blokker:

```kotlin
showIf(harYrkesskade) {
    showIf(yrkesskadegrad.equalTo(uforegrad)) {
        paragraph {
            text(
                bokmal { +"Hele uførheten skyldes yrkesskade." },
                nynorsk { +"Heile uførleiken kjem av yrkesskade." },
            )
        }
    }.orShow {
        paragraph {
            text(
                bokmal { +"Deler av uførheten skyldes yrkesskade." },
                nynorsk { +"Delar av uførleiken kjem av yrkesskade." },
            )
        }
    }
}
```

### Hvor kan betingelser brukes?

`showIf`, `ifNotNull` og `forEach` er tilgjengelige i disse scopene:

| Scope | Beskrivelse | Tilgjengelige elementer |
|-------|------------|------------------------|
| `OutlineScope` | Hovednivå i brevet | `paragraph`, `title1-3`, `showIf`, `forEach`, `ifNotNull`, `includePhrase` |
| `ParagraphScope` | Inne i et avsnitt | `text`, `list`, `table`, `formText`, `formChoice`, `showIf`, `forEach`, `ifNotNull` |
| `TextOnlyScope` | Inne i tekst | `text`, `eval`, `newline`, `showIf`, `forEach`, `ifNotNull` |
| `ListScope` | Inne i en liste | `item`, `showIf`, `forEach`, `ifNotNull` |
| `TableScope` | Inne i en tabell | `row`, `showIf`, `forEach`, `ifNotNull` |

---

## Iterasjon med forEach

`forEach` itererer over en samling og gir tilgang til hvert element som et `Expression`:

```kotlin
forEach(perioder) { periode ->
    paragraph {
        text(
            bokmal { +"Periode: " + periode.fom.format() + " - " + periode.tom.format() },
            nynorsk { +"Periode: " + periode.fom.format() + " - " + periode.tom.format() },
        )
    }
}
```

### forEach i tabeller

Vanlig mønster for å generere tabellrader dynamisk:

```kotlin
paragraph {
    table(
        header = {
            column { text(bokmal { +"Måned" }, nynorsk { +"Månad" }) }
            column(alignment = ColumnAlignment.RIGHT) { text(bokmal { +"Beløp" }, nynorsk { +"Beløp" }) }
        }
    ) {
        forEach(utbetalinger) { utbetaling ->
            row {
                cell { eval(utbetaling.maaned.format()) }
                cell { eval(utbetaling.beloep.format()) }
            }
        }
    }
}
```

### forEach med betinget logikk

Du kan kombinere `forEach` med `showIf` inne i løkken:

```kotlin
forEach(tilbakekrevingsperioder) { periode ->
    row {
        cell { eval(periode.maaned.format()) }
        cell { eval(periode.bruttoBeloep.format()) }
        showIf(periode.skatteBeloep.notEqualTo(0.expr())) {
            cell { eval(periode.skatteBeloep.format()) }
        }
    }
}
```

---

## Gjenbrukbare fraser (Phrase)

Fraser lar deg pakke inn gjenbrukbare deler av brevinnhold som selvstendige klasser. Det finnes tre typer:

| Type | Brukes i | Kan inneholde |
|------|----------|---------------|
| `OutlinePhrase` | `OutlineScope` | `paragraph`, `title1-3`, kontrollstrukturer |
| `ParagraphPhrase` | `ParagraphScope` | `text`, `list`, `table`, kontrollstrukturer |
| `TextOnlyPhrase` | `TextOnlyScope` | `text`, `eval`, kontrollstrukturer |

### Definere en frase uten parametere

```kotlin
object Overskrift : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        title1 {
            text(
                bokmal { +"Vedtak" },
                nynorsk { +"Vedtak" },
                english { +"Decision" },
            )
        }
    }
}
```

### Definere en frase med parametere

```kotlin
data class HjelpestoenadInnledning(
    val aarInnvilget: Expression<Year>
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        paragraph {
            text(
                bokmal { +"Du har fått godkjent pensjonsopptjening for " + aarInnvilget.format() + "." },
                nynorsk { +"Du har fått godkjent pensjonsopptening for " + aarInnvilget.format() + "." },
                english { +"You have been credited pension earnings for " + aarInnvilget.format() + "." },
            )
        }
    }
}
```

### Bruke en frase

```kotlin
outline {
    includePhrase(Overskrift)
    includePhrase(HjelpestoenadInnledning(aarInnvilget = opptjeningAar))
}
```

### Fraser med kompleks logikk

Fraser kan inneholde all den samme logikken som resten av DSL-en:

```kotlin
data class BeregningsDetaljer(
    val harFlerePerioder: Expression<Boolean>,
    val perioder: Expression<Collection<BeregningsPeriode>>,
    val gjeldendePeriode: Expression<BeregningsPeriode>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        title1 {
            text(
                bokmal { +"Slik beregner vi uføretrygden din" },
                nynorsk { +"Slik reknar vi ut uføretrygda di" },
                english { +"How we calculate your disability benefit" },
            )
        }
        includePhrase(TabellGjeldendePeriode(gjeldendePeriode))

        showIf(harFlerePerioder) {
            paragraph {
                text(
                    bokmal { +"Du har flere beregningsperioder:" },
                    nynorsk { +"Du har fleire utrekningsperiodar:" },
                    english { +"You have multiple calculation periods:" },
                )
            }
            forEach(perioder) { periode ->
                includePhrase(TabellPeriode(periode))
            }
        }
    }
}
```

### Organisering av fraser

Fraser plasseres typisk i en `fraser/`-mappe ved siden av malene:

```
maler/
├── fraser/
│   ├── common/
│   │   ├── Felles.kt           # Delte fraser (rett til klage, kontaktinfo, etc.)
│   │   └── Constants.kt        # URL-er og konstanter
│   └── MittBrevFraser.kt       # Fraser spesifikke for ett brev
├── MittBrev.kt                  # Brevmalen
└── vedlegg/
    └── MittVedlegg.kt           # Vedlegg
```

---

## Vedlegg

### Inkludere vedlegg

```kotlin
// I template-blokken (utenfor outline)
createTemplate(...) {
    title { ... }
    outline { ... }

    // Fast vedlegg
    includeAttachment(vedleggDineRettigheter, pesysData)

    // Betinget vedlegg (inkluderes kun hvis data finnes)
    includeAttachmentIfNotNull(vedleggBeregning, pesysData.beregningsdata)
}
```

### Definere vedlegg

Vedlegg defineres med `createAttachment`:

```kotlin
val vedleggMaanedligPensjon =
    createAttachment<LangBokmalNynorskEnglish, MaanedligPensjonDto>(
        title = {
            text(
                bokmal { +"Månedlig pensjon før skatt" },
                nynorsk { +"Månadleg pensjon før skatt" },
                english { +"Monthly pension before tax" },
            )
        }
    ) {
        // Vedleggsinnhold – samme DSL som outline
        paragraph {
            text(...)
        }
        table(...) {
            forEach(perioder) { ... }
        }
    }
```

---

## Tips og best practices

### 1. Bruk fraser for gjenbruk

Trekk ut gjenbrukte avsnitt til `Phrase`-klasser i stedet for å kopiere tekst:

```kotlin
// ✅ Bra – gjenbrukbar frase
includePhrase(Felles.RettTilAKlage)
includePhrase(Felles.HarDuSporsmal)

// ❌ Dårlig – kopiert tekst i hver mal
paragraph {
    text(bokmal { +"Du har rett til å klage..." })
}
```

### 2. Bruk `orShow` / `orShowIf` i stedet for separate showIf-blokker

```kotlin
// ✅ Bra – tydelig at betingelsene er gjensidig utelukkende
showIf(borINorge) {
    paragraph { text(bokmal { +"Utbetaling den 20. hver måned." }) }
}.orShow {
    paragraph { text(bokmal { +"Utbetaling til utenlandsk konto." }) }
}

// ❌ Dårlig – uklart at det er if/else-logikk
showIf(borINorge) {
    paragraph { text(bokmal { +"Utbetaling den 20. hver måned." }) }
}
showIf(not(borINorge)) {
    paragraph { text(bokmal { +"Utbetaling til utenlandsk konto." }) }
}
```

### 3. Trekk ut komplekse betingelser til variabler

```kotlin
// ✅ Bra – lesbar betingelse med navn
val harBarnetillegg = barnetilleggSerkull or barnetilleggFelles
val ingenTillegg = not(harBarnetillegg) and not(ektefelletillegg) and not(gjenlevendetillegg)

showIf(ingenTillegg) {
    paragraph { text(bokmal { +"Du får " + totalNetto.format() + " per måned." }) }
}

// ❌ Dårlig – uleselig inline betingelse
showIf(not(btsbInnvilget) and not(btfbInnvilget) and not(etInnvilget) and not(gtInnvilget)) {
    ...
}
```

### 4. Bruk `isOneOf` for enum-sjekker

```kotlin
// ✅ Bra
showIf(sivilstand.isOneOf(Sivilstand.GIFT, Sivilstand.REGISTRERT_PARTNER)) {
    ...
}

// ❌ Dårlig
showIf(sivilstand.equalTo(Sivilstand.GIFT) or sivilstand.equalTo(Sivilstand.REGISTRERT_PARTNER)) {
    ...
}
```

### 5. Bruk `ifNotNull` i stedet for manuell null-sjekk

```kotlin
// ✅ Bra – trygt og gir tilgang til utpakket verdi
ifNotNull(avtaleland) { land ->
    text(bokmal { +"Avtalen med " + land })
}

// ❌ Dårlig – krasjer hvis null
text(bokmal { +"Avtalen med " + avtaleland.ifNull("".expr()) })
```

### 6. Hold maler korte – bryt opp med fraser

Hvis en mal blir over 200-300 linjer, vurder å trekke ut logiske seksjoner til egne fraser.

---

## Anti-patterns å unngå

### ❌ Vanlig Kotlin `if/else` for brevlogikk

```kotlin
// ❌ FEIL – dette evalueres ved kompilering, ikke ved brevgenerering!
val tekst = if (enExpression.equals(true)) "ja" else "nei"

// ✅ RIKTIG – bruk DSL-uttrykk
val tekst = ifElse(enExpression, "ja".expr(), "nei".expr())
```

Vanlig Kotlin `if/else` evaluerer `Expression`-objekter – ikke verdiene de representerer. Resultatet blir ofte feil fordi `Expression.equals()` sammenligner objektreferanser, ikke runtime-verdier.

### ❌ Dupliserte showIf-betingelser

```kotlin
// ❌ Dårlig – ytre showIf og indre showIf har samme betingelse
showIf(harBarnetillegg) {
    paragraph {
        showIf(harBarnetillegg) {  // Redundant!
            text(bokmal { +"Du har barnetillegg." })
        }
    }
}

// ✅ Bra – ingen redundans
showIf(harBarnetillegg) {
    paragraph {
        text(bokmal { +"Du har barnetillegg." })
    }
}
```

### ❌ Enormt dypt nøstede ifNotNull

```kotlin
// ❌ Dårlig – dyp nøsting som er vanskelig å lese
ifNotNull(a) { a ->
    ifNotNull(b) { b ->
        ifNotNull(c) { c ->
            ifNotNull(d) { d ->
                paragraph { text(bokmal { +a + b + c + d }) }
            }
        }
    }
}

// ✅ Bedre – bruk multi-parameter ifNotNull der mulig
ifNotNull(a, b) { a, b ->
    ifNotNull(c, d) { c, d ->
        paragraph { text(bokmal { +a + b + c + d }) }
    }
}
```

### ❌ Ustrukturert «flat» logikk

```kotlin
// ❌ Dårlig – mange uavhengige showIf-blokker etter hverandre uten struktur
showIf(betingelse1) { paragraph { text(...) } }
showIf(betingelse2) { paragraph { text(...) } }
showIf(betingelse3) { paragraph { text(...) } }
showIf(betingelse4) { paragraph { text(...) } }

// ✅ Bedre – grupper relatert logikk i fraser
includePhrase(YrkesskadeVurdering(yrkesskadegrad, uforegrad, yrkesskadeResultat))
includePhrase(BarnetilleggInformasjon(barnetilleggData))
includePhrase(InstitusjonsoppholdInformasjon(instData))
```

---

## Referanse: Tilgjengelige funksjoner

### I `TemplateRootScope` (toppnivå)
- `title { }` – Brevtittel
- `outline { }` – Brevinnhold
- `includeAttachment(template, data)` – Inkluder vedlegg
- `includeAttachmentIfNotNull(template, data)` – Betinget vedlegg

### I `OutlineScope`
- `title1 { }`, `title2 { }`, `title3 { }` – Overskrifter
- `paragraph { }` – Avsnitt
- `showIf(predicate) { }` – Betinget visning
- `ifNotNull(expr) { }` – Null-sikker visning
- `forEach(items) { }` – Iterasjon
- `includePhrase(phrase)` – Inkluder frase

### I `ParagraphScope`
- `text(bokmal { }, nynorsk { }, ...)` – Tekst
- `list { }` – Punktliste
- `table(header, init)` – Tabell
- `formText(size) { }` – Fritektsfelt
- `formChoice { }` – Flervalgsfelt
- `showIf` / `ifNotNull` / `forEach` – Kontrollstrukturer

### I `TextOnlyScope`
- `text(bokmal { }, ...)` – Tekst
- `eval(expression)` – Uttrykksverdi
- `newline()` – Linjeskift
- `showIf` / `ifNotNull` / `forEach` – Kontrollstrukturer

### I `ListScope`
- `item { }` – Listeelement
- `showIf` / `ifNotNull` / `forEach` – Kontrollstrukturer

### I `TableScope`
- `row { }` – Tabellrad
- `showIf` / `ifNotNull` / `forEach` – Kontrollstrukturer
