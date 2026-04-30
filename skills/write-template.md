# Skill: Write a letter template (Kotlin DSL body)

**Scope:** everything shared between `AutobrevTemplate` and `RedigerbarTemplate` — information-model design, the template object skeleton, `createTemplate(...)`, the scope hierarchy, languages, conditionals, phrases, and common mistakes. For redigerbar-specific extras (caseworker-editable fields, Skribenten-visible metadata) see [`write-redigerbar-template.md`](write-redigerbar-template.md).

**Canonical project docs** (may be partially outdated — prefer this skill + current source for module-local details): [`docs/modules/ROOT/pages/dsl/index.adoc`](../docs/modules/ROOT/pages/dsl/index.adoc), [`dsl/scopes.adoc`](../docs/modules/ROOT/pages/dsl/scopes.adoc), [`dsl/stegvis guide nytt brev.adoc`](../docs/modules/ROOT/pages/dsl/stegvis%20guide%20nytt%20brev.adoc).

## Before you write

### Pick the target module

Letter templates live in several maler modules — pick the one matching the domain. For the module you pick, locate these four things up front; you will need all of them:

| Module | Brevkode registry (`api-model`) | Fixtures file | `AllTemplates` registry object |
|---|---|---|---|
| `pensjon/maler` | `pensjon/api-model/.../maler/Pesysbrevkoder.kt` | `pensjon/maler/src/test/kotlin/no/nav/pensjon/brev/Fixtures.kt` | `ProductionTemplates` |
| `alder/maler` | `alder/api-model/.../model/Aldersbrevkoder.kt` | `alder/maler/src/test/kotlin/no/nav/pensjon/brev/alder/Fixtures.kt` | `AlderTemplates` |
| `ufoere/maler` | `ufoere/api-model/.../Ufoerebrevkoder.kt` | `ufoere/maler/src/test/kotlin/no/nav/pensjon/brev/ufore/Fixtures.kt` | `UfoereTemplates` |
| `etterlattemaler` | domain-local registry under `etterlattemaler/…` | `etterlattemaler/src/test/kotlin/no/nav/pensjon/etterlatte/Fixtures.kt` | `EtterlatteMaler` |
| `planlegge-pensjon-maler` | domain-local registry | `planlegge-pensjon-maler/src/test/kotlin/no/nav/pensjon/brev/planleggepensjon/Fixtures.kt` | `PlanleggePensjonTemplates` |

New maler modules may appear — locate the module's `*brevkoder*.kt`, `Fixtures.kt`, and `AllTemplates` implementation before starting.

Packages inside a module are organised by domain, not by a fixed `redigerbar/` / `auto/` subfolder. Put the new template next to siblings it is conceptually related to.

### Name the template and reserve a brevkode

The **template object name**, the **Dto class name**, and the **brevkode** should all reflect the *situation* the letter is used in — not the internal Exstream id, not the fagsystem field names, not the ticket number. Pick one situation-describing PascalCase base name and mirror it across all three:

- Template object:   `VedtakEndringAvGjenlevendepensjonVedFlyttingTilUtland`
- Dto class:         `VedtakEndringAvGjenlevendepensjonVedFlyttingTilUtlandDto`
- Brevkode constant: `PE_GP_ENDRING_FLYTTING_MELLOM_LAND` (domain prefix + situation in SCREAMING_SNAKE_CASE)

Mirror neighbouring templates in the same module for the prefix/casing style (`PE_AP_…` / `PE_GP_…` / `UT_…` / `GP_…` on the brevkode side; `Vedtak…` / `Avslag…` / `Innvilgelse…` / `Endring…` / `Opphoer…` / `Varsel…` / `Informasjon…` / `Bekreftelse…` on the Kotlin side). The prefix word on the Kotlin name should describe the letter's communicative act (vedtak, avslag, innvilgelse, varsel, informasjon, opphoer, endring, …); the rest should narrow it down to the specific situation.

Add the brevkode constant to the module's brevkode registry — under `Redigerbar` for redigerbar brev, `AutoBrev` / `Automatisk` for autobrev. **Rules for picking the code:**

- **Unique across all modules.** Enforced by `AllTemplatesTest."alle maler skal bruke en unik brevkode"` — CI fails on collisions.
- **≤ 50 characters.** It is the letter's ID.
- **Immutable once in production.** A brevkode in prod is a stable identifier external systems may reference — never rename, never reuse.
- **Never delete a template (or its brevkode) in production.** Saved drafts in Skribenten's database may reference redigerbar codes. When a brev is replaced, allocate a new code and leave the old template in place (optionally move it under a `legacy/` package).

### Read a neighbouring template

Before writing, read **one current template in the same module** that is structurally similar (same sakstype, similar complexity). Mirror its package layout, imports, and enum choices (`Brevkategori` / `Sakstype` / `Brevkontekst` are **module-local** — do not import `pensjon`'s versions by default).

## Information-model design (the Dto)

The Dto is the contract with the bestiller system. Design it *before* writing DSL — refactoring later forces API-model version bumps.

- **Data-minimise.** Only include fields the template actually renders. Business logic belongs upstream; let the Dto carry a descriptive boolean (`harBarnetillegg`) rather than the raw inputs that compute it.
- **Non-nullable by default.** The DSL is null-safe — a nullable field forces you to handle the null branch in every `bokmal/nynorsk/english` block. If the brev cannot be produced without the value, require it.
- **Group fields that are required together.** If A only makes sense when B is present, nest them in a data class so the impossible state is unrepresentable.
- **No defaults on required fields.** A silent default becomes a silent production bug.

```kotlin
data class MittBrevDto(
    val uforegrad: Percent,
    val virkningFom: LocalDate,
    val maanedligBeloep: Kroner,
    val harBarnetillegg: Boolean,
    val barnetilleggBarn: List<Barn> = emptyList(),
) {
    data class Barn(val navn: String, val fodselsdato: LocalDate)
}
```

Place the Dto in the module's `api-model` under the same domain folder as the template, so it is easy to find.

### Iterating on the Dto locally

Maler modules consume their `api-model` as a published artifact. While iterating on a new Dto locally you **must** bump the version in two places **and** publish to mavenLocal — otherwise the maler module sees stale bytecode and every reference to the new Dto / its `…DtoSelectors` is unresolved.

Concrete locations (replace `<module>` with the relevant domain module — `pensjon`, `alder`, `ufoere`, `etterlattemaler`, `planlegge-pensjon-maler`, …):

| File | What to change |
|---|---|
| `<module>/api-model/gradle.properties` | `version=<N>` → `version=<N+1>` |
| `<module>/maler/build.gradle.kts` | `val apiModelVersion = <N>` → `val apiModelVersion = <N+1>` |

Then publish and rebuild:

```bash
./gradlew :<module>:api-model:publishToMavenLocal
./gradlew :<module>:maler:compileKotlin   # forces KSP to regenerate selectors against the new artifact
```

## Template object skeleton

```kotlin
@TemplateModelHelpers                                      // KSP: generates XDtoSelectors
object X : AutobrevTemplate<XDto> {                        // or RedigerbarTemplate<XDto>
    override val kode = <Module>brevkoder.AutoBrev.X       // or .Redigerbar.X

    override val template = createTemplate(
        languages = languages(Bokmal, Nynorsk, English),   // see "Languages"
        letterMetadata = LetterMetadata(
            displayTitle = "...",
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        ),
    ) {
        title { text(bokmal { +"..." }, nynorsk { +"..." }, english { +"..." }) }
        outline {
            paragraph { text(bokmal { +"..." }, nynorsk { +"..." }, english { +"..." }) }
        }
        // includeAttachment(someAttachment, data)
    }
}
```

`@TemplateModelHelpers` is non-optional — without it the KSP processor does not generate `XDtoSelectors`, and the DSL body is red.

### Build after the first commit of a new Dto or template

Two unresolved-reference symptoms look identical at the call site but have different causes and different fixes. Diagnose which one you have before chasing anything else.

**Symptom A — selectors unresolved (`pesysData.…`, `saksbehandlerValg.…`, `it.field` after `ifNotNull`).**
`XDtoSelectors` (and every nested `…Selectors` class) is produced by KSP at build time from `@TemplateModelHelpers`. They **do not exist until a Gradle build has run**, so the first time you author a template against a new Dto, the IDE marks every selector red. Fix:

```bash
./gradlew :<module>:maler:build   # regenerates selectors under build/generated/ksp/
```

The IDE usually picks the new files up on the next re-index; otherwise *File → Invalidate Caches*.

**Symptom B — the Dto class itself is unresolved (`import …XDto` red, `RedigerbarTemplate<XDto>` shows `ERROR CLASS: Symbol not found`).**
The maler module consumes its sibling `api-model` as a **published Maven artifact**, not a source dependency. A new Dto added under `<module>/api-model/src/…` is invisible to the maler module until you publish it locally and bump the consumer's version. See *Iterating on the Dto locally* below.

> KSP side-effect of Symptom B: `:<module>:maler:kspKotlin` will print `w: [ksp] Some annotated symbols does not validate: [YourTemplate]` and silently produce **no** `XDtoSelectors` file. Every selector then looks like Symptom A — but rebuilding KSP will not fix it until the Dto resolves. Always clear Symptom B first.

Only after both symptoms are cleared do reported errors become trustworthy — do not chase selector-reference errors before then.

### Nested-Dto selector paths

For nested data classes inside the Dto, KSP mirrors the nesting in the generated selector object tree. E.g. for

```kotlin
data class XDto(val pesysData: PesysData) : RedigerbarBrevdata<…> {
    data class PesysData(val beregning: Beregning)
    data class Beregning(val brutto: Kroner, val komponent: Ytelseskomponent) {
        data class Ytelseskomponent(val brutto: Kroner, val netto: Kroner)
    }
}
```

the imports chain through `…Selectors` for each level:

```kotlin
import …XDtoSelectors.PesysDataSelectors.beregning
import …XDtoSelectors.PesysDataSelectors.BeregningSelectors.brutto
import …XDtoSelectors.PesysDataSelectors.BeregningSelectors.YtelseskomponentSelectors.brutto
```

Each selector is an extension property on `Expression<ReceiverType>`, so two selectors with the same simple name but different receiver types (here `Expression<Beregning>.brutto` and `Expression<Ytelseskomponent>.brutto`) can be imported side-by-side — Kotlin resolves the call by receiver: `pesysData.beregning.brutto` picks the first, `ifNotNull(pesysData.beregning.komponent) { it.brutto }` picks the second.

**Do not alias these imports with `as`** to "disambiguate". Renaming `…BrukerDataSelectors.minst20ArBotid as brukerMinst20ArBotid` removes the original name from scope, so `pesysData.bruker.minst20ArBotid` no longer resolves at all — the only remaining call site shape is the renamed `brukerMinst20ArBotid(pesysData.bruker)` style, which is not the DSL convention. Just import every same-named selector unaliased and let receiver-type resolution do its job.

**Sibling nesting vs. field-reachability.** The generated `…Selectors` path tracks **where the inner class is declared**, not where it is first reached via a field. If `Beregning` is declared as a *sibling* of `PesysData` (both nested directly under `XDto`) but is only *referenced* through `PesysData.beregning`, the selector still lives at the top level:

```kotlin
data class XDto(val pesysData: PesysData) : RedigerbarBrevdata<…> {
    data class PesysData(val beregning: Beregning)   // field on PesysData
    data class Beregning(val brutto: Kroner)         // class nested on XDto, NOT on PesysData
}
```

```kotlin
// right — Beregning is a sibling of PesysData, so its selectors sit at the top:
import …XDtoSelectors.BeregningSelectors.brutto

// wrong — there is no PesysDataSelectors.BeregningSelectors:
import …XDtoSelectors.PesysDataSelectors.BeregningSelectors.brutto
```

When the error is `Unresolved reference 'BeregningSelectors'` under `PesysDataSelectors`, check the Dto: if `Beregning` is declared at the outer level, move the import up one level. `build/generated/ksp/main/kotlin/…/XDtoSelectors.kt` is the authoritative tree.

### Common imports — what's where

The DSL is spread across several packages; a few paths trip people up on the first template:

| Symbol | Import |
|---|---|
| `Kroner` | `no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner` (it is *not* a top-level class) |
| `LetterMetadata`, `LetterMetadata.Distribusjonstype`, `…Brevtype` | `no.nav.pensjon.brevbaker.api.model.LetterMetadata` |
| `Language.Bokmal` / `Nynorsk` / `English` | `no.nav.pensjon.brev.template.Language.…` |
| `createTemplate`, `AutobrevTemplate`, `RedigerbarTemplate` | `no.nav.pensjon.brev.template.…` |
| `languages`, `text` | `no.nav.pensjon.brev.template.dsl.…` |
| `TemplateModelHelpers` | `no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers` |
| `.format()`, `and`, `or`, `not`, `equalTo`, `notEqualTo`, `isOneOf`, `greaterThan`, `lessThan`, `ifNull`, `safe` | each is a separate star-less import from `no.nav.pensjon.brev.template.dsl.expression.…` |
| `Expression<Kroner>.format()`, `Expression<Telefonnummer>.format()`, `Expression<Foedselsnummer>.format()` (no-arg overloads) | `no.nav.pensjon.brev.model.format` — **separate package from the generic `expression.format`.** Without this import the no-arg `.format()` call on a `Kroner` selector fails with *"No value passed for parameter 'formatter'"*. |
| `Brevkategori` | `no.nav.pensjon.brev.model.Brevkategori` (module-local — pensjon has its own, alder has its own) |
| `Sakstype`, `TemplateDescription.Brevkontekst` | `no.nav.pensjon.brev.api.model.…` |
| `Pesysbrevkoder` / module brevkode registry | `no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder` (pensjon); other modules have their own registry |

Import expression operators individually (`import …expression.and`, `import …expression.equalTo`, …) — there is no single star-import that pulls them all, and the IDE's quick-fix picker often guesses wrong when multiple classes (`Kroner`, `Language`) live in namespaces it has never seen before.

### `LetterMetadata` fields

| Field | Values | Effect |
|---|---|---|
| `displayTitle` | free text | Title shown to saksbehandler and archived with the letter — see conventions below. |
| `distribusjonstype` | `VEDTAK`, `VIKTIG`, `ANNET` | Drives distribution notification. |
| `brevtype` | `VEDTAKSBREV`, `INFORMASJONSBREV` | Affects signature / closing text / first page. |

### `displayTitle` conventions

`displayTitle` surfaces in two places: Skribenten's brevvelger (for redigerbar brev the caseworker picks manually) and the archive (for all brev, including autobrev produced by an automatic job). Keep it:

- **Norwegian Bokmål.** Not translated — the same `displayTitle` is used regardless of the brev's output language, and it is read by case workers.
- **Short** — aim for ≤ 70 characters; brevvelger truncates longer titles.
- **Situation-describing** — describe the situation the sak or vedtak is in/for, not the internal brev name. Typical patterns (from existing titles): *"Vedtak – endring av uføretrygd fordi du fyller 20 år"*, *"Varsel om opphør av ektefelletillegg til uføretrygd"*, *"Informasjon om hvilende rett til uføretrygd"*.

## Languages

`languages(...)` is type-safe and its argument list *is the type contract* for every `text { }` block in the template:

| Call | Type alias | Required DSL branches inside `text { }` |
|---|---|---|
| `languages(Bokmal)` | `LangBokmal` | `bokmal { }` |
| `languages(Bokmal, Nynorsk)` | `LangBokmalNynorsk` | `bokmal { }`, `nynorsk { }` |
| `languages(Bokmal, English)` | `LangBokmalEnglish` | `bokmal { }`, `english { }` |
| `languages(Bokmal, Nynorsk, English)` | `LangBokmalNynorskEnglish` | all three |

Pick the actual subset — do not pad with placeholder translations. Missing or extra branches are **compile errors**, not runtime.

## Scope hierarchy

A template is a nested tree of scopes; each scope exposes a specific set of DSL functions.

```
createTemplate(...) {                       // TemplateRootScope
    title { ... }                           //   PlainTextScope
    outline {                               //   OutlineScope
        title1 { ... } / title2 / title3    //     PlainTextScope
        paragraph {                         //     ParagraphScope
            text(...)                       //       language-branched literals/expressions
            list { item { ... } }           //       TextScope inside item
            table { ... }
            formText() / formChoice() / newline()
            includePhrase(ParagraphPhrase | TextOnlyPhrase | PlainTextOnlyPhrase)
        }
        includePhrase(OutlinePhrase)
    }
    includeAttachment(attachment, data)
    includeAttachmentIfNotNull(attachment, nullableData)
}
```

- Use `title1`/`title2`/`title3` for section headings inside `outline`.
- Prefer `includePhrase(...)` over copy-pasting wording (see "Phrases").
- `list { item { ... } }` and `table { ... }` — see [`dsl/paragraph/lister.adoc`](../docs/modules/ROOT/pages/dsl/paragraph/lister.adoc), [`tabeller.adoc`](../docs/modules/ROOT/pages/dsl/paragraph/tabeller.adoc).

## Text sources

Inside a language block (`bokmal { ... }`):

| Expression | Use when |
|---|---|
| `+"fixed text"` | The wording is fixed. Unary `+` is required — a bare `"..."` is a compile error. |
| `+ expression.format()` | Rendering a value from the Dto via a generated selector. `.format()` is type-specific (`Kroner(1000)` → `"1 000 kroner"` on Norwegian). |

Never call `.toString()` on an `Expression` — it throws at runtime. Use `.format()` to render a value as text, or `.ifNull(fallback)` to coalesce a nullable expression to a non-nullable one of the same type (the fallback must match the source expression's type — e.g. `Expression<Kroner?>.ifNull(Kroner(0))`).

**Verbatim user-supplied wording is immutable.** If the user hands you exact text they want in the letter (a drafted paragraph, a legal clause, a pre-translated language branch), render it exactly as given — do not silently polish grammar, shorten sentences, adjust punctuation, or "improve" the tone. If you believe a change is needed (typo, missing expression interpolation, wrong language branch, phrase already exists — see *Search before you write* below), **stop and ask the user** with the original and your proposed change side-by-side. Apply only after confirmation.

Concatenate with `+`: `bokmal { +"Du får " + beloep.format() + " før skatt." }`.

### Typed-value formatters carry their own unit — never append it by hand

A few built-in formatters render the value **with the unit included**, localised per language. Appending a literal unit to a `.format()` call of these types double-prints and passes the compiler silently — you'll only see the defect in the rendered letter.

| Type | `.format()` result (bokmål / nynorsk) | English | Import |
|---|---|---|---|
| `Expression<Kroner>` / `Expression<Kroner?>` | `"1 000 kroner"` | `"NOK 1 000"` | `no.nav.pensjon.brev.model.format` |
| `Expression<Telefonnummer>` | `"22 00 00 00"` (no unit, but formatted with spaces) | same | `no.nav.pensjon.brev.model.format` |
| `Expression<Foedselsnummer>` | `"DDMMYY XXXXX"` | same | `no.nav.pensjon.brev.model.format` |

```kotlin
// wrong — renders "Du får 1 000 kroner kroner før skatt."
bokmal { +"Du får " + beloep.format() + " kroner før skatt." }

// right — the formatter already emitted "kroner"
bokmal { +"Du får " + beloep.format() + " før skatt." }
```

#### Order of preference for `Kroner`

1. **Default — `.format()`.** Use this everywhere in prose. If the surrounding sentence currently contains `" kroner"` / `" kr"` / `" NOK"`, rewrite the sentence to drop the literal unit; the formatter already emits a localised unit. *Don't* reach for `denominator = false` to keep a hand-written `" kroner"` — change the prose instead.
   ```kotlin
   // wrong — keeps the literal "kroner" by silencing the formatter
   bokmal { +"Du får " + beloep.format(denominator = false) + " kroner hver måned før skatt." }

   // right — let the formatter emit the unit, drop it from the sentence
   bokmal { +"Du får " + beloep.format() + " hver måned før skatt." }
   ```
2. **Table cells — `KronerText` phrase.** Inside `cell { … }` the column header usually carries the unit, so the cell wants the bare number. Use the module's phrase rather than re-deriving the per-language suffix:
   ```kotlin
   cell { includePhrase(KronerText(beloep)) }
   ```
   `KronerText` lives in `fraser/common/TabellFormattering.kt` (pensjon; alder has its own copy). It already handles `" kr"` / `"NOK "` per language.
3. **`.format(denominator = false)` — last resort.** Only reach for it when (a) you need the bare number outside a table cell and (b) `KronerText` doesn't apply (e.g. inline interpolation in a math expression: `"(" + a.format(denominator = false) + " + " + b.format(denominator = false) + ") kroner"`). Even then, prefer rephrasing so each value carries its own unit via `.format()`.

`denominator = false` exists for table-cell rendering and similar edge cases — it is not the default knob for "I want a number without `kroner` in this sentence". Re-author the sentence first.

**Related Dto design rule:** if a field represents money, type it as `Kroner` — not `Int` — so the selector's `.format()` carries the unit. Using `Int` means every call site must append `" kroner"` manually, which drifts as soon as a second language branch is added.

### Tables — column alignment and width

NAV letter standard:

> Tekst er alltid venstrestilt for leseretning. Tall høyrestilles. I tabeller hvor kolonner blander tall og bokstaver bør det vurderes pr. tabell om innholdet skal venstrestilles eller høyrestilles.

Default column alignment is `LEFT`. Set `alignment = ColumnAlignment.RIGHT` explicitly on number columns:

```kotlin
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Table.ColumnAlignment

table(
    header = {
        column { text(bokmal { +"" }) }                                // label column — default LEFT
        column(alignment = ColumnAlignment.RIGHT) { text(bokmal { +"Beløp" }) }   // number column — RIGHT
    }
) { … }
```

For columns that mix numbers and text, choose per-table — there is no correct default. Decide up front and document the choice in a comment; don't let it drift row by row.

**Column width is controlled by `columnSpan`.** Every `column(...)` call takes a `columnSpan: Int` (default `1`) that sets its **relative** width — the total table width is the sum of all `columnSpan` values, and each column gets its share. Think of it as a weight, not pixels:

```kotlin
table(
    header = {
        column(columnSpan = 2) { text(bokmal { +"Beskrivelse" }) }                          // 2/5 of the table
        column(columnSpan = 1, alignment = ColumnAlignment.RIGHT) { text(bokmal { +"Antall" }) }      // 1/5
        column(columnSpan = 2, alignment = ColumnAlignment.RIGHT) { text(bokmal { +"Beløp" }) }       // 2/5
    }
) { … }
```

Decide the ratios deliberately per table: wide text columns (descriptions, long labels, merknader that may wrap) should get a larger span; narrow numeric columns (år, antall, single-line Kroner values) can sit on `columnSpan = 1`. If cell content wraps awkwardly in the rendered PDF, tune spans up or down — re-render to verify, don't guess. `columnSpan = 0` reserves a column with no width (rare — used by `tabeller.adoc` for alignment scaffolding).

**Neighbouring text blocks are concatenated.** Adjacent `text(...)` calls, adjacent `+`-chained fragments, and `showIf` blocks inline with surrounding text all render into a single continuous text run — no implicit space or line break is inserted. You are responsible for trailing/leading spaces and punctuation:

```kotlin
// two adjacent text(...) calls render as "aa" — NOT "a a" or "a\na"
text(bokmal { +"a" }, nynorsk { +"a" })
text(bokmal { +"a" }, nynorsk { +"a" })

// wrong — renders as "Du fårbeløp før skatt."
text(bokmal { +"Du får" + beloep.format() + "før skatt." }, ...)

// right — spaces are on the literals
text(bokmal { +"Du får " + beloep.format() + " før skatt." }, ...)
```

For a new paragraph use a new `paragraph { }`. `newline()` exists but is used **sparingly** — mostly for stacking address lines within a single paragraph. For ordinary prose breaks, always reach for `paragraph { }` first.

## Conditionals — always use DSL conditionals, never Kotlin `if`

The template body is a **builder that runs once at application startup**. A Kotlin `if` there is evaluated against whatever values are in scope at startup (usually nothing useful) and bakes a single branch into the template — subsequent letter orders will always render that same branch. DSL conditionals (`showIf`, `ifElse`, `ifNotNull`, `forEach`) instead capture the `Expression` and evaluate it per letter order.

Kotlin `if` on Dto data inside the DSL is therefore always wrong.

```kotlin
showIf(data.harBarnetillegg) {
    paragraph { text(bokmal { +"..." }, nynorsk { +"..." }) }
}.orShowIf(data.annetVilkaar) {
    paragraph { text(bokmal { +"..." }, nynorsk { +"..." }) }
}.orShow {
    paragraph { text(bokmal { +"..." }, nynorsk { +"..." }) }
}
```

- Terminate chains with `orShow { }` unless the predicate is provably exhaustive.
- `ifElse` vs `showIf`: use `ifElse(pred, "ja", "nei")` only when a **small inline fragment** of a sentence changes (a word, a number, a short noun phrase). When the conditional part is a whole sentence or block that is structurally the same across all languages, use `showIf` — otherwise you duplicate the conditional in every language branch and the language blocks drift apart.
- Nullable Dto field: `ifNotNull(data.maybeField) { it -> ... }`. A two-argument overload exists: `ifNotNull(a, b) { aNonNull, bNonNull -> ... }` — the block runs only when **both** are non-null, and both are bound as non-null inside. Use it instead of nesting two `ifNotNull` calls.
- Repeating over a collection: `forEach(data.barn) { barn -> ... }`.

### Branching on the result of `showIf`

`showIf` returns a `ShowElseScope` with three ways to extend the conditional — use these rather than opening a new `showIf` for the "else" case:

| Method | Effect |
|---|---|
| `orShow { ... }` | Runs when all preceding predicates were false. Terminates the chain. |
| `orShowIf(pred) { ... }` | Else-if. Chainable. |
| `orIfNotNull(expr) { it -> ... }` | Else-if-not-null — binds the non-null value for the else branch. Chainable. |

Do **not** confuse these with the expression-level `and` / `or` below; those combine boolean expressions *inside* a predicate, whereas `orShow` / `orShowIf` branch the conditional itself.

### Factor shared predicates into an outer `showIf`

When two or more adjacent `showIf` blocks share a common `and`-conjunct, lift that conjunct into an outer `showIf` and nest the specialised predicates inside. The rendered output is identical (nested `showIf` is logical AND), but each condition now appears exactly once, so editing the shared precondition later touches one line instead of several — and the reader sees the structure directly.

```kotlin
// wrong — `data.gate` is repeated in every branch
showIf(data.gate and data.kategori.equalTo(Kategori.A)) { … }
    .orShowIf(data.gate and data.kategori.equalTo(Kategori.B)) { … }
    .orShowIf(data.gate and data.kategori.equalTo(Kategori.C)) { … }

// right — outer `showIf` gates the whole block, inner chain picks the variant
showIf(data.gate) {
    showIf(data.kategori.equalTo(Kategori.A)) { … }
        .orShowIf(data.kategori.equalTo(Kategori.B)) { … }
        .orShowIf(data.kategori.equalTo(Kategori.C)) { … }
}
```

The same refactor applies when two *separate* `showIf` blocks share an AND-tail but differ only by one atom:

```kotlin
// wrong — three conjuncts repeated verbatim
showIf(data.a and not(data.flag) and not(data.b) and not(data.c)) { … variant 1 … }
showIf(data.a and     data.flag  and not(data.b) and not(data.c)) { … variant 2 … }

// right — lift the shared precondition, branch on the differing atom
showIf(data.a and not(data.b) and not(data.c)) {
    showIf(not(data.flag)) { … variant 1 … }.orShow { … variant 2 … }
}
```

Do the factoring whenever the shared conjunct is syntactically identical in each branch. Don't try to be clever about *semantically* equivalent predicates that happen to be written differently — keep them unchanged; the factoring is a textual-dedup pattern.

### Predicate-building operators (on `Expression<...>`)

The predicate passed to `showIf` / `ifElse` / `ifNotNull` is an `Expression`. Build it from Dto selectors by chaining these operators — never call Kotlin methods like `==` or `in` on the selector directly.

| Operator | Notes |
|---|---|
| `.equalTo(other)` | Equality on `Expression<T>`. |
| `.isOneOf(a, b, …)` | Membership test. |
| `.greaterThan(n)` / `.lessThan(n)` / … | Numeric comparisons. |
| `.not()` / `not(expr)` | Boolean negation. Use the method form on a single expression (`flag.not()`), and the prefix form to wrap a composite (`not(a or b)`) — Kotlin operator precedence makes `!(a or b)` awkward on infix expressions. |
| `a and b` / `a or b` (infix) | Combine two `Expression<Boolean>`. |
| `.ifNull(fallback)` | Nullable `Expression<T?>` → non-nullable `Expression<T>`. The fallback must be of the same type `T` as the source expression (a literal `T` or another `Expression<T>`). |
| `.format()` | Render a value as text — text-side, not predicate-side. |

Details: [`dsl/kontroll-struktur/index.adoc`](../docs/modules/ROOT/pages/dsl/kontroll-struktur/index.adoc).

## Phrases

Reuse shared wording through phrase classes instead of copy-pasting into multiple templates:

```kotlin
includePhrase(Felles.Hilsen)
includePhrase(BeregnaPaaNytt(data.krav.virkDatoFom))
```

### Standard end-of-letter phrases exist — reuse them

Each module's `Felles` / `FellesFraser` object defines ready-made phrases for content that typically closes a letter. Three recurring ones, named slightly differently across modules:

1. **Right to appeal** — e.g. `Felles.RettTilAaKlage` (pensjon), `DuHarRettTilAaKlage` (etterlatte).
2. **Right to access case documents** — e.g. `Felles.RettTilInnsyn(<vedlegg>)`, `RettTilInnsynRedigerbarebrev`, `DuHarRettTilInnsyn`. Variants that take the attachment reference embed the full "rettigheter og plikter" vedlegg.
3. **Contact information** — `Felles.HarDuSpoersmaal.<domene>` (e.g. `.ufoeretrygd`, `.pensjon`, `.omsorg`) or module-local `HarDuSpoersmaal(url, phone)`.

Whether your letter needs any of these is a content decision — do not add them reflexively. The pitfall is the opposite: **if your intended closing content already matches one of these phrases, re-author it inline instead of calling `includePhrase(...)`**. That forks the wording and the set of callers drifts apart over time. Before writing a closing paragraph, search the module's `Felles` / `FellesFraser` object for a match — see *Search before you write* below.

### Search before you write

Before authoring any non-trivial paragraph, **search existing phrases** in the module's `fraser/` directories (and the shared `fraser/common/`) for wording that covers the same content. Greppable entry points:

- `grep -R "includePhrase" <module>/src/main/kotlin/.../fraser/` — list all defined phrase classes in the module.
- Search the Bokmål text you are about to write for key noun phrases (e.g. *"hvilende rett"*, *"etterbetaling"*, *"barnetillegg"*) across `fraser/` directories.

Three outcomes:

| Match quality | Action |
|---|---|
| **Exact / semantically identical** | Reuse the existing phrase with `includePhrase(...)`. Do not duplicate wording. |
| **Near-match — same content, minor linguistic differences** (word choice, order, punctuation, singular/plural, formality) | **Stop and ask the user.** Present the existing phrase and the wording you were about to write side-by-side, and ask whether to (a) reuse the existing phrase as-is, (b) reuse but tweak the existing phrase (note: this affects all templates already using it), or (c) author a new phrase. Do not silently duplicate — it creates drift and makes future edits ambiguous. |
| **No match** | Author inline. Promote to a phrase class only when the same wording reaches ≥ 2 templates. |

### Where phrases live

- Cross-cutting: `<module>/src/main/kotlin/.../fraser/common/`
- Domain-specific: `<module>/src/main/kotlin/.../fraser/<domain>/`

### Phrase base classes — pick by scope where the phrase is used

| Base | Use inside |
|---|---|
| `OutlinePhrase<Lang>` | `outline { includePhrase(...) }` — can cover any outline content, up to an entire section of a letter or attachment (titles, paragraphs, nested `showIf`, further `includePhrase`, …). |
| `ParagraphPhrase<Lang>` | `paragraph { includePhrase(...) }` — emits text blocks |
| `TextOnlyPhrase<Lang>` | `paragraph { ... }` / inside text — inline text |
| `PlainTextOnlyPhrase<Lang>` | `title { }` or other `PlainTextScope` |


Details: [`dsl/fraser/oversikt.adoc`](../docs/modules/ROOT/pages/dsl/fraser/oversikt.adoc).

## Wire the fixture and register the template

### Wire the fixture

`Fixtures.create<XDto>()` is **not** reflection-based — every module's `Fixtures.kt` is a hand-written `when (letterDataType) { … }` dispatch on `KClass`. A new Dto needs three things, all in the module's `Fixtures.kt` (see the table in *Before you write*):

1. A `createXDto()` factory function. Place it under `…/fixtures/<package>/XDto.kt` (the file name typically matches the Dto class). Look at the closest sibling Dto's fixture file for shape.
2. An explicit `import …fixtures.<package>.createXDto` near the top of `Fixtures.kt` — these factories live in different packages and **are not glob-imported**.
3. A new branch in the `create` function:
   ```kotlin
   XDto::class -> createXDto() as T
   ```
   Without this branch, `Fixtures.create<XDto>()` falls into the `else` arm and throws `IllegalArgumentException("Don't know how to construct: …")` at integration-test time.

For nested types inside the Dto that are reused across letters, register them on the same `when` so other tests can also build them via `Fixtures.create`.

### Register the template

Every maler module has an `AllTemplates` object that lists its production templates. **You must add the new template to that object** — templates are not auto-discovered. A template missing from its registry will compile but will not be served by brevbaker and will not be picked up by the module's shared rendering test.

The registry object per module is in the table in *Before you write*.

Once registered, the module's shared `BrevmodulTest` subclass (e.g. `ProductionTemplatesTest`, `AlderTemplatesTest`, `UfoereTemplatesTest`, `EtterlatteTemplatesTest`) automatically renders the new brev for every language it declares — you do **not** need to hand-write an integration test.

## Common mistakes

- Bare string inside `text { }` — literals need unary `+`: `bokmal { +"Tekst" }`.
- Forgetting `@TemplateModelHelpers` — selectors never get generated; whole DSL body red.
- Adding a language branch that isn't in `languages(...)` (compile error) or missing one that is (compile error).
- Calling `.toString()` on an `Expression` — throws at runtime; chain DSL operators instead.
- Business logic inside the template body. Push it into the Dto (data-minimise) or into a phrase.
- Kotlin `if` on Dto values inside the DSL body. The template body runs once at application startup; a Kotlin `if` bakes a single branch into the template. Use `showIf` / `ifElse` / `ifNotNull` so the condition is evaluated per letter order.
- Nullable field rendering: not handling the null branch, or printing `null`. The DSL blocks this, but ugly `ifNotNull` nesting often signals the Dto should have been non-nullable in the first place.
- **Every new `Dto` field needs a fixture value, and the new Dto class needs a fixture branch in `Fixtures.kt`.** The dispatch is a hand-written `when` on `KClass`, not reflection — see *Wire the fixture* above. A missing `XDto::class -> createXDto() as T` branch fails the shared rendering test with `IllegalArgumentException("Don't know how to construct: …")`.
- Forgetting to register the template in the module's `AllTemplates` object.
- **Silently editing user-supplied wording.** If the user handed over exact text for the letter, render it verbatim. Stop and ask before changing grammar, punctuation, or phrasing — see *Text sources* above.
- **Re-authoring content that already exists as a standard phrase** — especially the end-of-letter trio (right-to-appeal, right-to-innsyn, "Har du spørsmål?"). If the closing content you are about to write matches one of these, use `includePhrase(...)`. See *Standard end-of-letter phrases exist* above.
- **Appending `" kroner"` / `" kr"` / `" NOK"` after `Expression<Kroner>.format()`** — the default formatter already emits the unit per language, so the suffix doubles up. Drop the literal unit from the surrounding prose (preferred), or use the `KronerText` phrase in table cells. Reach for `.format(denominator = false)` only as a last resort. See *Typed-value formatters carry their own unit → Order of preference for `Kroner`* above.
- **Typing a money field as `Int` instead of `Kroner` in the Dto** — forces every call site to append the unit manually and lose the per-language localisation. Use `Kroner` whenever the semantic is "amount of money".
- **Tables without column alignment or with unconsidered column widths** — numbers must be right-aligned per the NAV letter standard (default is LEFT; set `alignment = ColumnAlignment.RIGHT` on number columns), and every column's `columnSpan` weight should be chosen deliberately so wide text columns get proportionally more width than narrow numeric ones. See *Tables — column alignment and width* above.

## Verify

Run the module's `:integrationTest` task — **not** `manualTest`:

```bash
./gradlew :<module>:integrationTest
```

This runs the module's `BrevmodulTest` subclass, which renders every registered template in every language it declares, and fails on missing fixtures or rendering errors. Because templates are picked up from the `AllTemplates` object, no per-template test needs to be added.

Integration tests spin up pdf-bygger via **Testcontainers** — Docker must be running, but you do not need `docker compose up` yourself. `@Tag(TestTags.MANUAL_TEST)` is for ad-hoc visual inspection only; do not rely on it as the verification step.

Rendered output lands under each module's `build/test_html/` and `build/test_pdf/` — open those to inspect the letter visually.

**Faster iteration** (optional): `TESTCONTAINERS_REUSE_ENABLE=true` keeps pdf-bygger running between test invocations; `BRUK_LOKAL_PDF_BYGGER=true` tests against a locally built `pensjonsbrev-pdf-bygger:latest` instead of the GHCR image. See [`README.md`](../README.md#miljøvariabler-for-integrasjonstester).

