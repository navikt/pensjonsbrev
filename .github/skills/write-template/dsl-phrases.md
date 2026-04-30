# Skill: Phrases (gjenbrukbare fraser)

**Scope:** the four phrase base classes (`OutlinePhrase`, `ParagraphPhrase`, `TextOnlyPhrase`, `PlainTextOnlyPhrase`), how to author and include them, and the search-before-you-write rule that keeps wording from forking.

**Companion docs:** [`dsl/fraser/oversikt.adoc`](../../../docs/modules/ROOT/pages/dsl/fraser/oversikt.adoc) and the per-type pages under that folder.

## Pick the phrase type by the scope where you'll include it

| Base class | Scope it includes into | Can contain |
|---|---|---|
| `OutlinePhrase<Lang>` | `OutlineScope` (i.e. inside `outline { … }`) | titles, paragraphs, lists, tables, nested `showIf`, further `includePhrase` — up to a whole letter section |
| `ParagraphPhrase<Lang>` | `ParagraphScope` (inside `paragraph { … }`) | text, lists, tables, form fields — but **no titles** |
| `TextOnlyPhrase<Lang>` | `TextScope` (inside `item { … }`, `cell { … }`) and `ParagraphScope` | inline text with formatting (`FontType.BOLD`, etc.), `newLine()` |
| `PlainTextOnlyPhrase<Lang>` | `PlainTextScope` (inside `title { }`, `title1/2/3`, `column { }`) and any text scope | ren tekst only, no formatting |

The picking rule is simple: **find the deepest scope you need to include in, and pick the phrase that fits there.** A phrase that supports more languages than the template is fine — only the language branches the template declares are emitted.

## Defining a phrase

### `OutlinePhrase` — section-level

```kotlin
object RettTilAaKlage : OutlinePhrase<LangBokmalNynorsk>() {
    override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
        title1 {
            text(
                bokmal { +"Du har rett til å klage" },
                nynorsk { +"Du har rett til å klage" }
            )
        }
        paragraph {
            text(
                bokmal { +"Du kan klage innen 6 uker." },
                nynorsk { +"Du kan klage innan 6 veker." }
            )
        }
    }
}
```

### Phrase with parameters — make it a `data class`

```kotlin
data class BeregnaPaaNytt(
    val virkDatoFom: Expression<LocalDate>,
) : ParagraphPhrase<LangBokmalNynorsk>() {
    override fun ParagraphOnlyScope<LangBokmalNynorsk, Unit>.template() {
        text(
            bokmal { +"Vi har beregnet på nytt fra " + virkDatoFom.format() + "." },
            nynorsk { +"Vi har rekna på nytt frå " + virkDatoFom.format() + "." }
        )
    }
}
```

`Expression<T>` parameters let the caller pass selectors, literals (`123.expr()`), or computed expressions.

### `TextOnlyPhrase` — inline text fragment, often parameterised

```kotlin
data class GarantipensjonSatsTypeText(
    val satsType: Expression<GarantipensjonSatsType>,
) : TextOnlyPhrase<LangBokmalNynorskEnglish>() {
    override fun TextOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        showIf(satsType.equalTo(GarantipensjonSatsType.HOY)) {
            text(
                bokmal { +"høy sats" },
                nynorsk { +"høg sats" },
                english { +"high rate" },
                FontType.BOLD,
            )
        }.orShow {
            text(
                bokmal { +"ordinær sats" },
                nynorsk { +"ordinær sats" },
                english { +"ordinary rate" },
            )
        }
    }
}
```

### `PlainTextOnlyPhrase` — for titles, table column headers

```kotlin
object VedtakTittel : PlainTextOnlyPhrase<LangBokmalNynorsk>() {
    override fun PlainTextOnlyScope<LangBokmalNynorsk, Unit>.template() {
        text(
            bokmal { +"Vedtak om uføretrygd" },
            nynorsk { +"Vedtak om uføretrygd" }
        )
    }
}
```

## Including a phrase

```kotlin
outline {
    includePhrase(RettTilAaKlage)                                  // no params
    includePhrase(BeregnaPaaNytt(virkDatoFom = data.krav.virkDatoFom))   // with params
}

cell { includePhrase(KronerText(beloep)) }                         // TextOnlyPhrase in a table cell

title { includePhrase(VedtakTittel) }                              // PlainTextOnlyPhrase in title
```

## Where phrases live

```
<module>/maler/src/main/kotlin/.../fraser/
├── common/                      ← cross-cutting
│   ├── Felles.kt                ← end-of-letter phrases (RettTilAaKlage, RettTilInnsyn, HarDuSpoersmaal)
│   ├── KronerText.kt            ← table-cell money phrase (in TabellFormattering.kt)
│   └── …
├── uforetrygd/                  ← domain-specific
│   └── Inntektsavkorting.kt
└── barnetillegg/
    └── BarnetilleggInfo.kt
```

- Cross-cutting: `<module>/.../fraser/common/`
- Domain-specific: `<module>/.../fraser/<domain>/`

## Search before you write

Before authoring any non-trivial paragraph, **search existing phrases** in the module's `fraser/` directories (and the shared `fraser/common/`) for wording that covers the same content. Greppable entry points:

- `grep -R "includePhrase" <module>/maler/src/main/kotlin/.../fraser/` — list all defined phrase classes in the module.
- Search the Bokmål text you are about to write for key noun phrases (e.g. *"hvilende rett"*, *"etterbetaling"*, *"barnetillegg"*) across `fraser/` directories.

Three outcomes:

| Match quality | Action |
|---|---|
| **Exact / semantically identical** | Reuse the existing phrase with `includePhrase(...)`. Do not duplicate wording. |
| **Near-match — same content, minor linguistic differences** (word choice, order, punctuation, singular/plural, formality) | **Stop and ask the user.** Present the existing phrase and the wording you were about to write side-by-side, and ask whether to (a) reuse the existing phrase as-is, (b) reuse but tweak the existing phrase (note: this affects all templates already using it), or (c) author a new phrase. Do not silently duplicate — it creates drift and makes future edits ambiguous. |
| **No match** | Author inline. Promote to a phrase class only when the same wording reaches ≥ 2 templates. |

## Standard end-of-letter phrases — reuse, don't re-author

Each module's `Felles` / `FellesFraser` object defines ready-made phrases for content that typically closes a letter. Three recurring ones, named slightly differently across modules:

1. **Right to appeal** — e.g. `Felles.RettTilAaKlage` (pensjon), `DuHarRettTilAaKlage` (etterlatte).
2. **Right to access case documents** — e.g. `Felles.RettTilInnsyn(<vedlegg>)`, `RettTilInnsynRedigerbarebrev`, `DuHarRettTilInnsyn`. Variants that take an attachment reference embed the full "rettigheter og plikter" vedlegg.
3. **Contact information** — `Felles.HarDuSpoersmaal.<domene>` (e.g. `.ufoeretrygd`, `.pensjon`, `.omsorg`) or module-local `HarDuSpoersmaal(url, phone)`.

Whether your letter needs any of these is a content decision — do not add them reflexively. The pitfall is the opposite: **if your intended closing content already matches one of these phrases, re-author it inline instead of calling `includePhrase(...)`** — that forks the wording.

## Naming and granularity

- Use **descriptive names** that say what the phrase contains: `RettTilAaKlage`, not `Frase1`.
- **Hold each phrase to one responsibility** — separate `RettTilAaKlage`, `RettTilInnsyn`, `HarDuSpoersmaal` instead of one big `KlageOgInnsynOgSpoersmaal`.
- **Promotion threshold** — author inline first; promote to a phrase class only when the same wording reaches ≥ 2 templates. A phrase used by exactly one template adds indirection without saving anything.

## Common mistakes

- Picking the wrong phrase type for the scope — `OutlinePhrase` cannot be included in `paragraph { }`, etc.
- Authoring a new phrase that duplicates an existing one (especially the end-of-letter trio) — *Search before you write* first.
- A phrase used by exactly one template — keep it inline; promote when the second user appears.
- Tweaking a shared phrase's wording without checking the other call sites — every template using it gets the new wording.
- Forgetting that phrase parameters are `Expression<T>`, not raw values — pass selectors directly, or `Kroner(0).expr()` for literals.
