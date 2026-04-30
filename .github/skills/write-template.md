# Skill: Write a letter template (Kotlin DSL body)

**Scope:** the recipe for adding a letter template — module/registry layout, brevkode rules, Dto design, the template object skeleton (`createTemplate(...)`, `LetterMetadata`), languages, fixture wiring, and registration. The actual DSL body — scopes, text, conditionals, tables, phrases, attachments — is split into focused sub-skills, cross-referenced below. For redigerbar-specific extras (caseworker-editable fields, Skribenten-visible metadata) see [`write-redigerbar-template.md`](write-redigerbar-template.md).

**DSL sub-skills** (each is a self-contained reference for one DSL topic):

- [`dsl-scopes.md`](dsl-scopes.md) — the scope hierarchy (`TemplateRootScope` / `OutlineScope` / `ParagraphScope` / `TextScope` / `PlainTextScope`)
- [`dsl-text-and-formatting.md`](dsl-text-and-formatting.md) — text composition rules and the formatter catalogue (Kroner, dates, etc.)
- [`dsl-control-structures.md`](dsl-control-structures.md) — `showIf` / `ifNotNull` / `forEach` / `ifElse` / predicates
- [`dsl-tables-and-lists.md`](dsl-tables-and-lists.md) — `table` and `list` authoring
- [`dsl-phrases.md`](dsl-phrases.md) — `OutlinePhrase` / `ParagraphPhrase` / `TextOnlyPhrase` / `PlainTextOnlyPhrase`
- [`dsl-attachments.md`](dsl-attachments.md) — `createAttachment` / `includeAttachment`

**Canonical project docs** (may be partially outdated — prefer this skill + current source for module-local details): [`docs/modules/ROOT/pages/dsl/index.adoc`](../../docs/modules/ROOT/pages/dsl/index.adoc), [`dsl/scopes.adoc`](../../docs/modules/ROOT/pages/dsl/scopes.adoc), [`dsl/stegvis guide nytt brev.adoc`](../../docs/modules/ROOT/pages/dsl/stegvis%20guide%20nytt%20brev.adoc).

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

Template bodies are a nested tree of scopes (`TemplateRootScope` → `OutlineScope` → `ParagraphScope` → `TextScope` → `PlainTextScope`). Each scope exposes a different set of DSL functions; picking the wrong nesting level is the most common compile-error.

See [`dsl-scopes.md`](dsl-scopes.md) for the full hierarchy table and how to read scope-related compile errors.

## Text and formatters

Inside a language block (`bokmal { … }`) you compose text from `+"literal"`, `+ expr.format()`, and `+ ifElse(pred, …, …)`. Adjacent fragments concatenate without inserted whitespace, so you own the leading/trailing spaces. Each value type has its own `.format()` overload (Int, Kroner, Double, dates, Telefonnummer, Foedselsnummer, …); some carry their own unit and must not be suffixed by hand.

See [`dsl-text-and-formatting.md`](dsl-text-and-formatting.md) for the formatter catalogue, the unary-plus rule, the spacing rule, and the **order of preference for `Kroner`** (`.format()` → `KronerText` phrase in cells → `denominator = false` only as last resort).

User-supplied verbatim wording is immutable — render exactly as given; ask before changing grammar, punctuation, or phrasing.

## Conditionals — always DSL, never Kotlin `if`

The template body is a builder that runs once at application startup. A Kotlin `if` on Dto data bakes a single branch into the template forever; per-letter branching needs the DSL conditionals (`showIf`, `ifElse`, `ifNotNull`, `forEach`).

See [`dsl-control-structures.md`](dsl-control-structures.md) for the full operator reference (`showIf`/`orShow`/`orShowIf`, `ifNotNull`/`orIfNotNull`, `ifElse`, `forEach`, predicate operators `and`/`or`/`not`/`equalTo`/`isOneOf`/…), the rule about factoring shared predicates into outer `showIf`, and the inline `ifElse` vs. block `showIf` decision.

## Tables and lists

`paragraph { list { item { … } } }` and `paragraph { table(header = { column { … } }) { row { cell { … } } } }`. Dynamic rows/items via `forEach`; conditional rows (never conditional cells); column alignment per the NAV letter standard (numbers right-aligned); column width via `columnSpan` weights.

See [`dsl-tables-and-lists.md`](dsl-tables-and-lists.md) for the full reference, including the `KronerText` phrase for money cells.

## Phrases

Reuse shared wording through phrase classes instead of copy-pasting:

```kotlin
includePhrase(Felles.RettTilAaKlage)
includePhrase(BeregnaPaaNytt(data.krav.virkDatoFom))
```

Each module has standard end-of-letter phrases (right to appeal, right to access case documents, contact info) — **search before authoring**. If your closing content matches an existing phrase, use `includePhrase(...)` instead of duplicating the wording.

See [`dsl-phrases.md`](dsl-phrases.md) for the four base classes (`OutlinePhrase` / `ParagraphPhrase` / `TextOnlyPhrase` / `PlainTextOnlyPhrase`), how to author parameterised phrases, where they live, and the search-before-you-write rule.

## Attachments

`includeAttachment(vedlegg, data)` / `includeAttachmentIfNotNull(vedlegg, nullableData)` from `TemplateRootScope`. Vedlegg are defined separately with `createAttachment<Lang, Data>(title = …, includeSakspart = …) { … }`.

See [`dsl-attachments.md`](dsl-attachments.md).

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

## Common mistakes (template recipe specific)

For DSL-specific mistakes (text fragments, formatters, conditionals, tables, phrases) see the *Common mistakes* sections of the relevant sub-skill above.

- Forgetting `@TemplateModelHelpers` — selectors never get generated; whole DSL body red.
- Adding a language branch that isn't in `languages(...)` (compile error) or missing one that is (compile error).
- Business logic inside the template body. Push it into the Dto (data-minimise) or into a phrase.
- Nullable field rendering: not handling the null branch, or printing `null`. The DSL blocks this, but ugly `ifNotNull` nesting often signals the Dto should have been non-nullable in the first place.
- **Every new `Dto` field needs a fixture value, and the new Dto class needs a fixture branch in `Fixtures.kt`.** The dispatch is a hand-written `when` on `KClass`, not reflection — see *Wire the fixture* above. A missing `XDto::class -> createXDto() as T` branch fails the shared rendering test with `IllegalArgumentException("Don't know how to construct: …")`.
- Forgetting to register the template in the module's `AllTemplates` object.

## Verify

Run the module's `:integrationTest` task — **not** `manualTest`:

```bash
./gradlew :<module>:integrationTest
```

This runs the module's `BrevmodulTest` subclass, which renders every registered template in every language it declares, and fails on missing fixtures or rendering errors. Because templates are picked up from the `AllTemplates` object, no per-template test needs to be added.

Integration tests spin up pdf-bygger via **Testcontainers** — Docker must be running, but you do not need `docker compose up` yourself. `@Tag(TestTags.MANUAL_TEST)` is for ad-hoc visual inspection only; do not rely on it as the verification step.

Rendered output lands under each module's `build/test_html/` and `build/test_pdf/` — open those to inspect the letter visually.

**Faster iteration** (optional): `TESTCONTAINERS_REUSE_ENABLE=true` keeps pdf-bygger running between test invocations; `BRUK_LOKAL_PDF_BYGGER=true` tests against a locally built `pensjonsbrev-pdf-bygger:latest` instead of the GHCR image. See [`README.md`](../../README.md#miljøvariabler-for-integrasjonstester).

