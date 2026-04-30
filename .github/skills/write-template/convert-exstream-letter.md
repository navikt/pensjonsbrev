# Skill: Convert an OpenText Exstream letter to brevbaker

**Scope:** taking the Kotlin output of the automated Exstream-to-brevbaker converter and turning it into a compilable, idiomatic brevbaker template. The converter gets you ~80% there — this skill catalogues the ~20% that requires human judgement.

**Read first:** [`write-template.md`](write-template.md). Everything there (Dto design, language branches, concatenation, conditionals, phrases, registration, verify) applies to the converted template. This skill only documents the **deltas** introduced by the converter output.

## Where converted letters live

Legacy (Exstream-originated) templates go under `pensjon/maler/src/main/kotlin/no/nav/pensjon/brev/maler/legacy/` — autobrev directly under `legacy/`, redigerbar under `legacy/redigerbar/`. Follow existing neighbours (e.g. `EndretUforetrygdPGAOpptjeningLegacy.kt`, `InnvilgelseUforetrygd.kt`) for package and imports.

## Pipeline overview

The converter produces a single file with two sections:

1. **Selector block at the top** — extension functions on `Expression<PEgruppeN>` prefixed with a copy-into-`LegacySelectors` comment. Chains of `.safe{…}.safe{…}.ifNull(fallback)` that navigate the legacy Pesys data model with null-safety. **Discard for new conversions** — see Step 1.
2. **Template skeleton** — a `@TemplateModelHelpers object … : AutobrevTemplate<…>` stub with `// TODO` placeholders for metadata, and an `outline { … }` body that mirrors the Exstream source structure with trace comments.

The manual pass has seven steps. Work through them in order; each later step assumes the earlier ones have been done.

## Step 1 — Build a data-minimised, traceable Dto

The converter wires the template against the legacy `PEgruppe2` / `PEgruppe3` / `PEgruppe10` mega-classes via a block of `Expression<PEgruppeN>.foo_bar_baz()` selectors at the top of the file. **Discard that block.** New conversions instead get a fresh Dto containing only the fields the letter actually renders, because:

- the contract with the bestiller is small and stable, and
- every field carries a comment with its original Exstream `PE_…` source path so a downstream mapping team can locate the right PESYS XML node.

To build it:

1. List every selector used in the template body (and every reachable subfield through chained `.foo()` calls in the discarded selector block — that block is the easiest inventory of what the letter reads).
2. For each, look up the original `PE_…` variable in `pensjonsbrev-utils/exstreamConverter/src/main/resources/pe_xml_mappinger(in).csv`. Both the variable name (`Name` column) and the `rtv-brev brev …` path (`Layout` column) go into the comment, verbatim from the CSV — preserve any typos / casing differences (e.g. `BeregningYtelseomp` vs `BeregningYtelsesKomp`) since the mapping team will grep the CSV with these exact strings.
3. Group fields into nested data classes that mirror the rendering structure (e.g. `PesysData`, `AvdoedData`, `BrukerData`, `Beregning`, …). Sub-classes are siblings of `PesysData` inside the Dto — the KSP selector tree mirrors **declaration nesting**, not field reachability (see `write-template.md` *Sibling nesting vs. field-reachability*).
4. Collapse legacy oddities: `FF_GetArrayElement_Boolean(<list>, 1)` reads to a single `Boolean` if the letter only ever uses index 1 — note the original `FF_…` call in the comment.
5. Place the Dto under `<module>/api-model/.../maler/legacy/redigerbar/<Situation>Dto.kt` (or `.../legacy/<Situation>Dto.kt` for autobrev).

Sample comment style (one block per field):

```kotlin
// PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Tilleggspensjon_TPnetto
// (rtv-brev brev Vedtaksdata BeregningsData Beregning BeregningYtelsesKomp Tilleggspensjon TPnetto)
val netto: Kroner,
```

> **Pre-existing `PEgruppeN`-based legacy templates.** A handful of older converted templates still consume `PEgruppe2/3/10` directly and depend on extension functions in [`pensjon/maler/.../legacy/LegacySelectors.kt`](../../../pensjon/maler/src/main/kotlin/no/nav/pensjon/brev/maler/legacy/LegacySelectors.kt). Do not add new selectors there for fresh conversions — only touch the file if you are extending an existing legacy template that already reads through it. Inside that file, watch for `.ifNull(TODO)` stubs (won't compile) and Exstream-preserved typos like `beregningytelseomp` / `sertilegg` (the method name is wrong but the underlying `.safe{…}` chain points at the real field — leave it unless you also rename every call site).

## Step 2 — Fill in the template TODOs

The skeleton will have, at minimum:

| TODO | What to supply |
|---|---|
| Object name | Situation-describing `PascalCase` (e.g. `DelvisEksportAvGjenlevendepensjon`). The `…Legacy` suffix only appears on older `PEgruppeN`-based templates — do not add it for new conversions that built a fresh Dto in Step 1. |
| `AutobrevTemplate<// TODO>` / `RedigerbarTemplate<// TODO>` | The Dto type built in Step 1. |
| `override val kode` | A new entry in `Pesysbrevkoder.AutoBrev` / `.Redigerbar` — same rules as `write-template.md` (unique, ≤ 50 chars, immutable, never deleted). |
| `letterDataType` | `<YourDto>::class`. |
| `displayTitle` | Situation-describing Bokmål — see `write-template.md` *displayTitle* conventions. |
| `isSensitiv` | Only set `true` if the brev contains health / sensitive personal information; otherwise `false`. |
| `distribusjonstype` / `brevtype` | `LetterMetadata.Distribusjonstype.{VEDTAK|VIKTIG|ANNET}` and `LetterMetadata.Brevtype.{VEDTAKSBREV|INFORMASJONSBREV}` — **infer from content, then ask the user to confirm** (same pattern as `kategori`/`brevkontekst`/`sakstyper` in `write-redigerbar-template.md`). |
| `title { text(…) }` | Usually the same wording as the Exstream overskrift — lift it from the first outline paragraph if the converter left it blank. |

While transcribing the body, replace every `pe.foo_bar_baz()` call (which referenced the discarded `PEgruppeN` selector block) with the corresponding selector chain on the new Dto, e.g. `pesysData.beregning.tilleggspensjon.netto`.

## Step 3 — Resolve `SYS_TableRow` placeholders to `forEach`

Lines like

```kotlin
FUNKSJON_PE_..._PeriodeArsakListe_PeriodeArsakKode("\$/* TODO (SYS_TableRow) */").equalTo("endr_regel")
```

are unresolvable. Exstream iterated a table row; the converter cannot express that as a predicate, so it leaves the row reference as a `TODO` placeholder string.

The fix is to rewrite the enclosing `showIf` as a `forEach` over the list, with the per-item expression supplied by the lambda:

```kotlin
forEach(pe.periodeArsakListe()) { aarsak ->
    showIf(aarsak.periodeArsakKode().equalTo("endr_regel")) {
        paragraph { text(bokmal { +"- …" }, nynorsk { +"- …" }, english { +"- …" }) }
    }
}
```

Add a selector for the list (`List<PeriodeArsak>` or whatever the real type is) to `LegacySelectors.kt` in step 1 if it isn't there. The converter typically emits one `showIf` per enum value of the key — after the rewrite, consolidate those into a single `forEach` that contains all the per-`equalTo(…)` branches.

**Exception: a hard-coded index of `1` is usually not a `forEach`.** When the placeholder is a literal `1` — e.g. `FUNKSJON_FF_GetArrayElement_Boolean(pe.grunnlag_persongrunnlagsliste_…(), 1)` — the converter is just grabbing the first element of an array. If *every* use of that array in the letter is `[1]` (never a loop and never a different index), there is no reason to keep it as a list in the Dto. Model the field as a **scalar on the Dto** (`minst20ArBotidBruker: Boolean` rather than `List<Boolean>`) and drop the array entirely. Use `forEach` only when the Exstream code actually iterates variable rows (e.g. children in barnetillegg, opptjeningsår in a beregning) or indexes the array at more than one position.

## Step 4 — Convert `<FRITEKST: …>` markers

Exstream placeholders for caseworker free-text come through as literal strings inside `+"…"`:

```kotlin
bokmal { +"- <FRITEKST: Forklar kort hvilke inntekter som er endret>" },
```

**Every `<FRITEKST: …>` marker must be converted — no exceptions.** The angle-bracketed string is never shipped as literal prose in the rendered letter; it is always an instruction to the caseworker and must become a `fritekst("…")` call:

```kotlin
bokmal { +"- " + fritekst("Forklar kort hvilke inntekter som er endret") },
```

Rules:

- **Strip the `FRITEKST:` prefix and the surrounding `<` / `>`.** The remaining text is the prompt shown to the caseworker in Skribenten.
- If the whole paragraph is wrapped in `<FRITEKST: …>` (e.g. *"Vurder om brevets tekst er dekkende … Husk å fjern dette avsnittet."*), wrap the entire string in a single `fritekst(…)` call — do not keep any of it as a literal.
- If a short inline choice is embedded (e.g. `<FRITEKST: tre/fem>` inside a longer sentence), convert just that fragment to `fritekst("tre/fem")` and concatenate with the literal text around it.
- **When the `<FRITEKST: …>` wraps a *fork* between named alternatives** (e.g. *"Alt. 1 Informasjon om skatt … / Alt. 2 Kildeskatt …"* — where the caseworker is instructed to pick one and delete the rest), prefer a `Saksbehandlervalg` enum with `showIf` over `fritekst(…)`. An enum makes the choice a typed Skribenten form input instead of relying on the caseworker to hand-delete alternatives, and removes the risk of shipping both alternatives in the rendered letter.

**Three consequences:**

1. `fritekst` is only available inside a `RedigerbarTemplate` (see `write-redigerbar-template.md`). If the converter emitted `AutobrevTemplate` but the source has `<FRITEKST: …>` markers, **the template type is wrong** — convert to `RedigerbarTemplate` before continuing, and add the `Saksbehandlervalg` / fagsystem-data Dto split.
2. The converter duplicates the Bokmål FRITEKST string into the Nynorsk and English branches. After wrapping in `fritekst(…)`, the prompt string is shared across languages — the surrounding text around the `fritekst(…)` call is what must be translated.
3. The prompt passed to `fritekst(…)` is the instruction shown to the caseworker. Drop the `FRITEKST:` prefix and angle brackets; keep the descriptive imperative (*"Forklar kort hvilke inntekter som er endret"*).

## Step 5 — Restore tables that were flattened to adjacent `text(…)` calls

Inside a single `paragraph { … }` block the converter often emits two or three adjacent `text(…)` calls:

```kotlin
paragraph {
    text(bokmal { +"Grunnpensjon" }, nynorsk { +"Grunnpensjon" }, english { +"Basic pension" })
    text(bokmal { +pe.…_gpbrutto().format(denominator = false) + " kr" }, …)
    text(bokmal { +pe.…_gpnetto().format(denominator = false)  + " kr" }, …)
}
```

These were **table cells** in Exstream: label | brutto | netto. Kept as adjacent `text(…)` calls they concatenate (`"Grunnpensjon1 000 kr1 500 kr"` — see *Neighbouring text blocks are concatenated* in `write-template.md`). The fix is to author them as a real brevbaker `table { … }` with one `row { cell { … } cell { … } cell { … } }` per label (or a `forEach` over the underlying data).

Heuristic: **if a `paragraph { }` contains more than one `text(…)` call and no joining prose between them, it is a collapsed table — rebuild it.**

## Step 6 — Fix concatenation and spacing artifacts

The converter joins Exstream text fragments verbatim. Common artifacts:

- Missing space between sentences: `" kroner.Forventet årlig inntekt …"`. Add the missing space on the literal.
- Missing space between a value and its unit: `pe.beloep().format(denominator = false) + " kr"` is correct; `pe.beloep().format() + "kr"` is not.
- Missing space before or after a `fritekst(…)` call — see Step 4.
- Literal `kr` vs `kroner`, `NOK` in English — converter preserves the Exstream choice; verify it matches the brevbaker conventions for the domain (most current templates use `kroner` in bokmal/nynorsk and `NOK` in english).

Search the converted file for `"` followed by `+` and for `.format()` / `.format(denominator = false)` — each is a join site to eyeball.

## Step 7 — Simplification pass

The converter emits defensive, verbose DSL that mirrors Exstream's decision tree 1-to-1. Expect to simplify:

- **Merge nested `showIf`** into single calls with `and`: `showIf(A) { showIf(B) { … } }` → `showIf(A and B) { … }`.
- **Replace counter flags with `forEach`**: `showIf(pe.teller_periodisering().greaterThan(0)) { … }` blocks typically mean "for each period". Rewrite as `forEach(pe.perioder()) { periode -> … }` and drop the counter selector.
- **Deduplicate repeated blocks into phrases.** The converter unrolls shared structures — in the canonical example, the *"Pensjonen din er endret fordi / endr_vedtakhistorikk / endr_regel / …"* list appears once for the periodised path and once for the non-periodised path. Extract to an `OutlinePhrase`/`ParagraphPhrase` (see `write-template.md` *Phrases*) and include it twice.
- **Boolean selectors are predicates.** Calls like `pe.…_tpinnvilget()` already return `Expression<Boolean>` and can be used directly in `showIf(…)` — no `.equalTo(true)` needed. Strip if the converter emitted it.
- **Drop the trace comments** (`//[PE_GP_04_001_…]`) once the paragraph is verified to match the Exstream source. They are useful for the first-pass review, noise afterwards.
- **Collapse identical IF-arms** where Exstream had one `showIf(brutto != netto) { … }` and another `showIf(brutto = netto) { … }` that render identical content except for one column. Express as a single structure that omits the brutto column using `showIf` inside the row.

## Register and verify

Register the new template in `ProductionTemplates` — the converter does not do this. Then follow the Verify section of [`write-template.md`](write-template.md#verify).

### Verify preconditions (when the letter introduces a new Dto)

Most converted letters introduce a new Dto under `<module>/api-model/src/…`. The maler module consumes `api-model` as a **published Maven artifact**, so a source-only Dto is invisible to the template until it is published locally. Before running `:integrationTest`:

1. Bump `version=` in `<module>/api-model/gradle.properties` and the matching `apiModelVersion` in `<module>/maler/build.gradle.kts` to the next integer.
2. `./gradlew :<module>:api-model:publishToMavenLocal`
3. `./gradlew :<module>:maler:build` to run KSP and regenerate `…DtoSelectors` under `build/generated/ksp/`.

Symptoms you skipped this: `Unresolved reference 'XDto'` on the import line (api-model not published) or `Unresolved reference 'XDtoSelectors'` on selector imports (KSP hasn't run yet). See `write-template.md` *Build after the first commit of a new Dto or template* for the full diagnostic table. **Revert the version bumps before committing** unless you are also publishing a new api-model release.

Visual regression: when possible, run `AllTemplatesTest` / the module's `BrevmodulTest` and compare the rendered PDF with the original Exstream output. Wording should match exactly; layout is allowed to differ within brevbaker's styling. See [`README.md`](../../../README.md#oppdatere-latex-malavhengigheter) for the PDF-image diff script if you need to verify large letters byte-by-byte.

## Common pitfalls

- Template type still `AutobrevTemplate` after `fritekst(…)` conversion — won't compile.
- Adjacent `text(…)` calls left in place → runs together in the rendered PDF. See Step 5.
- `SYS_TableRow` placeholders left in `FUNKSJON_PE_…` calls → compile error on the unresolved function name.
- Duplicated selectors in `LegacySelectors.kt` — do not add one that already exists under a different name; reuse. If two variants with subtly different fallbacks exist, that is a separate cleanup task — flag it, do not choose unilaterally.
- The Bokmål `<FRITEKST: …>` string copied verbatim into `nynorsk` / `english` branches — conversion artifact; after wrapping in `fritekst(…)` the prompt is shared across languages by construction.
- Trusting `legacyLessThan` / `legacyGreaterThan` unconditionally — these exist precisely because Exstream date comparisons had null-tolerant semantics that plain `.lessThan` does not replicate. Keep the `legacy*` variants the converter emitted; do not "modernise" them without verifying the source intent.

