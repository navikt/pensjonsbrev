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

### Reserve a brevkode

Add a constant to the module's brevkode registry — under `Redigerbar` for redigerbar brev, `AutoBrev` / `Automatisk` for autobrev. **Read these rules before picking a code:**

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

Maler modules consume their `api-model` as a published artifact. While iterating on a new Dto locally, publish it to the local Maven repository and bump `apiModelVersion` in the maler module's `gradle.properties` / `build.gradle.kts` so the new fields are visible:

```bash
./gradlew publishToMavenLocal
```

Revert the version bump before committing if you are not also publishing a new api-model release.

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

If the Dto introduces new nested types, extend the module's `Fixtures.kt` (see the table in *Before you write*) so `Fixtures.create<XDto>()` resolves via the reflection-based factory. Search neighbouring tests for `Fixtures.create<` to match the module's conventions.

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
- **Every new `Dto` field needs a fixture value.** `Fixtures.create<T>()` uses reflection and will fail without a registered value for new nested types — extend the module's `Fixtures.kt` when adding new types.
- Forgetting to register the template in the module's `AllTemplates` object.
- **Silently editing user-supplied wording.** If the user handed over exact text for the letter, render it verbatim. Stop and ask before changing grammar, punctuation, or phrasing — see *Text sources* above.
- **Re-authoring content that already exists as a standard phrase** — especially the end-of-letter trio (right-to-appeal, right-to-innsyn, "Har du spørsmål?"). If the closing content you are about to write matches one of these, use `includePhrase(...)`. See *Standard end-of-letter phrases exist* above.

## Verify

Run the module's `:integrationTest` task — **not** `manualTest`:

```bash
./gradlew :<module>:integrationTest
```

This runs the module's `BrevmodulTest` subclass, which renders every registered template in every language it declares, and fails on missing fixtures or rendering errors. Because templates are picked up from the `AllTemplates` object, no per-template test needs to be added.

Integration tests spin up pdf-bygger via **Testcontainers** — Docker must be running, but you do not need `docker compose up` yourself. `@Tag(TestTags.MANUAL_TEST)` is for ad-hoc visual inspection only; do not rely on it as the verification step.

Rendered output lands under each module's `build/test_html/` and `build/test_pdf/` — open those to inspect the letter visually.

**Faster iteration** (optional): `TESTCONTAINERS_REUSE_ENABLE=true` keeps pdf-bygger running between test invocations; `BRUK_LOKAL_PDF_BYGGER=true` tests against a locally built `pensjonsbrev-pdf-bygger:latest` instead of the GHCR image. See [`README.md`](../README.md#miljøvariabler-for-integrasjonstester).

