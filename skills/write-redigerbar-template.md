# Skill: Write a redigerbar template (Kotlin DSL body)

**Parent recipe:** step 4 of [*Add a new redigerbar brev*](../AGENTS.md#add-a-new-redigerbar-caseworker-editable-brev) in `AGENTS.md`.

**Scope:** this skill covers only the deltas between `AutobrevTemplate` and `RedigerbarTemplate`. Start with [`write-template.md`](write-template.md) for Dto design, the template object skeleton, scope hierarchy, languages, conditionals, phrases, and registration — all of which apply unchanged to redigerbar templates.

## What a redigerbar template adds on top of a plain template

1. **The Dto is a three-way split** (`Saksbehandlervalg` + fagsystem data + the Dto itself implementing `RedigerbarBrevdata<Valg, Data>`).
2. **Three extra overrides** on the template object: `kategori`, `brevkontekst`, `sakstyper` — these drive Skribenten's brevvelger.
3. **One extra text source** in the DSL: `fritekst("initial text")`, declared as an extension on `RedigerbarTemplate` and therefore only available here.

Everything else (languages, scopes, phrases, registration in `AllTemplates`, common mistakes) is unchanged — consult `write-template.md`.

## Prerequisites — read first

On top of `write-template.md`'s prerequisites, read **one current redigerbar template in the same module** that is structurally similar. Good starting points:

- Straightforward, three languages, no saksbehandlerValg: `pensjon/maler/.../redigerbar/BekreftelsePaaPensjon.kt`
- Rich saksbehandlerValg + conditional sections: `pensjon/maler/.../redigerbar/VedtakEndringAvAlderspensjonInstitusjonsopphold.kt`
- Bokmal + Nynorsk only: `ufoere/maler/.../uforeavslag/UforeAvslagYrkesskadeGodkjent.kt`

## Dto: split into `Saksbehandlervalg` + fagsystem data

`RedigerbarTemplate<LetterData>` requires `LetterData : RedigerbarBrevdata<Valg : SaksbehandlerValgBrevdata, Data : FagsystemBrevdata>`:

```kotlin
data class XDto(
    override val saksbehandlerValg: Saksbehandlervalg,
    override val pesysData: PesysData,          // or FagsystemBrevdata for non-Pesys fagsystems
) : RedigerbarBrevdata<XDto.Saksbehandlervalg, XDto.PesysData> {

    data class Saksbehandlervalg(
        // One property per caseworker form input shown in Skribenten.
        // Booleans → checkbox; enums → dropdown; strings → text field.
        val harBarnetillegg: Boolean,
        val aarsak: Aarsak,
    ) : SaksbehandlerValgBrevdata

    data class PesysData(
        // Everything populated from the fagsystem. Not editable in Skribenten.
        val virkDatoFom: LocalDate,
        val belop: Kroner,
    ) : FagsystemBrevdata
}
```

- All the Dto design rules from `write-template.md` apply to both nested classes (non-nullable by default, no defaults on required fields, group related fields).
- Empty shortcuts when a side is not needed: `EmptySaksbehandlerValg`, `EmptyFagsystemdata`, or `EmptyRedigerbarBrevdata` for the whole thing.
- Generated selectors expose the split: `XDtoSelectors.saksbehandlerValg.harBarnetillegg`, `XDtoSelectors.pesysData.virkDatoFom`. The property names become the selector names — choose them deliberately.

## Extra overrides on the template object

```kotlin
@TemplateModelHelpers
object X : RedigerbarTemplate<XDto> {
    override val featureToggle = FeatureToggles.brevmalX.toggle            // required for every new redigerbar brev
    override val kode         = <Module>brevkoder.Redigerbar.X
    override val kategori     = Brevkategori.<...>                       // module-local
    override val brevkontekst = TemplateDescription.Brevkontekst.<...>   // see table below
    override val sakstyper    = setOf(Sakstype.<...>)                    // module-local

    override val template = createTemplate(
        languages = languages(Bokmal, Nynorsk),
        letterMetadata = LetterMetadata(/* displayTitle, distribusjonstype, brevtype */),
    ) { /* DSL body — see write-template.md */ }
}
```

| Override | Purpose |
|---|---|
| `featureToggle` | Unleash toggle that gates the brev in Skribenten's brevvelger. See "Feature toggle" below — **redigerbar templates only**. |
| `kategori` | The "folder" the brev appears under in Skribenten's brevvelger. |
| `brevkontekst` | Where the brev is offered — see table below. Does **not** drive attestation (that's `LetterMetadata.brevtype`). |
| `sakstyper` | Which sakstyper show the brev. Module-local. Shortcut sets exist (e.g. `Sakstype.pensjon`). |

**Inferring the values.** These three overrides are not arbitrary configuration — they follow from what the brev actually says and when it will be used. Infer them from the letter content rather than asking the user upfront:

- `kategori` — read the title, outline, and any `fraser` the brev includes. A brev whose body announces a vedtak fits `VEDTAK_*`; a brev explaining a process fits `INFORMASJONSBREV`; a request for documentation fits an innhenting-kategori. Pick the enum value whose name best matches the brev's communicative purpose.
- `brevkontekst` — Skribenten opens a case in one of two contexts: **sak context** (a plain case, always present) or **vedtak context** (opened from inside a specific decision, with extra vedtak data available). Pick based on where the caseworker needs to reach the brev: sak-only (may not be opened from a vedtak) → `SAK`; needs vedtak data so it only makes sense inside a vedtak → `VEDTAK`; a sak-brev that should *also* be reachable from vedtak context → `ALLE`.
- `sakstyper` — derive from which fagsystem concepts the Dto and text reference (alderspensjon, uføretrygd, gjenlevende, …). Module-local shortcut sets exist — mirror what neighbouring templates in the same domain use.

After inferring, **stop and ask the user to confirm all three values before continuing.** Present the inferred `kategori`, `brevkontekst`, and `sakstyper` together with a one-line justification for each ("I picked `VEDTAK` because the body references `virkDatoFom` and `belopsendring`"). Do not proceed to write / register the template until the user confirms or corrects them — these choices determine whether caseworkers can find the brev at all, and silent misclassification is a common defect.

`Brevkontekst` values — Skribenten opens a case in either **sak context** (always present) or **vedtak context** (only if opened from inside a decision). This value controls which of those contexts offer the brev:

| Value | Available in sak context | Available in vedtak context | Use when |
|---|---|---|---|
| `SAK`    | yes | no  | Sak-brev that should not appear when the caseworker is inside a vedtak. |
| `ALLE`   | yes | yes | Sak-brev that should *also* be reachable from vedtak context. |
| `VEDTAK` | no  | yes | Brev that needs vedtak data — only makes sense from inside a vedtak. |

## Feature toggle — required for every new redigerbar brev

Every new redigerbar template must be gated behind an Unleash feature toggle. This is **only** the case for redigerbar brev — autobrev are not toggle-gated at the template level (they are instead controlled by whichever fagsystem calls brevbaker).

Two steps per new brev:

1. **Add a constant** to the module's `FeatureToggles` enum (`pensjon/maler/.../FeatureToggles.kt` in the pensjon module; other modules have their own). The `key` string is what Unleash sees — name it after the template object:
   ```kotlin
   enum class FeatureToggles(private val key: String) {
       // ...existing entries...
       brevmalX("brevmalX"),
       ;
       val toggle = FeatureToggle(key)
   }
   ```
2. **Override `featureToggle`** on the template object (see skeleton above): `override val featureToggle = FeatureToggles.brevmalX.toggle`.

Without the override the template builds and registers, but the brev will not appear in Skribenten's brevvelger until the toggle constant exists and is flipped on in Unleash — so forgetting it silently hides the brev from production caseworkers.



`fritekst` is an extension declared on `RedigerbarTemplate` — only available inside redigerbar DSL bodies, not autobrev.

```kotlin
bokmal { +"Vi mottok søknaden din " + fritekst("dato") + "." }
```

Use `fritekst` when the brev **requires** the caseworker to type something in; it marks the spot as required caseworker input in Skribenten. Rules:

- The string argument is the **instruction/prompt shown to the caseworker** — phrase it as a directive describing what to enter, not as sample text. E.g. `fritekst("dato for fødsel av fellesbarn")`, not `fritekst("01.01.2024")`.
- The string must be non-empty — `fritekst("")`.
- Do not use `fritekst` for values that exist as structured data — use a `saksbehandlerValg` or `pesysData` field instead so the value is typed and validated.

## Three text sources — pick deliberately

Inside a language block (`bokmal { ... }`) you can mix three kinds of content. Each appears differently in Skribenten:

| Expression | Skribenten behaviour | Use when |
|---|---|---|
| `+"fixed text"` | Rendered as-is; saksbehandler cannot edit the literal in Skribenten. | The wording is legally/policy-fixed. |
| `fritekst("prompt")` | Required caseworker input — rendered as an editable field whose label is the prompt string. Caseworker must fill it in before sending. | The brev requires the caseworker to author a value that is not available as structured data (e.g. a free-form reason or a date the fagsystem doesn't hold). |
| `+ saksbehandlerValg.x.format()` / `+ pesysData.y` | Structured data. `saksbehandlerValg` fields become **form inputs** in Skribenten's ModelEditor; `pesysData` is read-only, populated from the fagsystem. | Values that must be typed / validated (enums, numbers, booleans). |

Putting an optional caseworker toggle on the top-level Dto (instead of nested inside `Saksbehandlervalg`) means no Skribenten form input is generated — the toggle becomes silently un-settable.

`LetterMetadata.displayTitle` is what the saksbehandler sees in Skribenten's brevvelger when picking a brev — it is the primary cue for "is this the right letter?". Keep it:

- **Short** — aim for ≤ 70 characters; the brevvelger truncates longer titles.
- **Situation-describing** — phrase it so a caseworker can tell *when* to use the brev, not just what it is named internally. E.g. prefer *"Vedtak – endring ved institusjonsopphold"* over *"VedtakEndringInstitusjon"*.
- **Consistent with neighbours** — mirror the phrasing style used by sibling templates in the same module/domain.

## Skribenten integration — scope

Beyond writing the template itself, **no extra steps are required in Skribenten** to make a new redigerbar brev appear:

- No backend registration (routing / allowlist / brevmetadata) in `skribenten-backend`.
- No `brevmetadata` / `brevkategori` entry in `skribenten-web/frontend`.
- No Cypress smoke test updates are required as part of adding the brev.

The brevkode, `kategori`, `brevkontekst`, `sakstyper`, and `displayTitle` on the template object are enough for Skribenten to discover and display the brev.

## Never delete a redigerbar template in production

A redigerbar brevkode that has reached production may be referenced by rows in Skribenten's database — deleting the template (or its brevkode) breaks saved drafts and historical records. Rules:

- **Never delete** a redigerbar template object or its brevkode constant once the brev is in production.
- **Never rename** the brevkode — it is an immutable ID (also stated in step 2 of the Golden Path).
- If the brev is being replaced, allocate a **new** brevkode and leave the old template in place (or move it under a `legacy/` package, as seen under `pensjon/maler/.../legacy/redigerbar/`).
- Renaming Dto fields has similar risk — saved drafts may reference the old shape. Prefer additive changes; when a rename is unavoidable, coordinate with the Skribenten team.

## Verify

Verification is the parent Golden Path's job — run the module's `:integrationTest` and the cross-module `AllTemplatesTest`. See [`../AGENTS.md`](../AGENTS.md#8-verify).




