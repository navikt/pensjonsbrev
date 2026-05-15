# Skill: Text and formatters

**Scope:** how to author text inside `bokmal { } / nynorsk { } / english { }` blocks — the unary-plus rule, concatenation, formatter catalogue, and inline word-level conditionals.

**Companion docs:** [`dsl/text/index.adoc`](../../../docs/modules/ROOT/pages/dsl/text/index.adoc), [`dsl/text/formattering.adoc`](../../../docs/modules/ROOT/pages/dsl/text/formattering.adoc), [`dsl/text/ifElse.adoc`](../../../docs/modules/ROOT/pages/dsl/text/ifElse.adoc).

## Text sources

Inside a language block you compose text from three kinds of fragments:

| Fragment | Use when |
|---|---|
| `+"fixed text"` | The wording is fixed. Unary `+` is required — a bare `"…"` is a compile error. |
| `+ expression.format()` | Rendering a value from the Dto via a generated selector (see formatter catalogue below). |
| `+ ifElse(pred, "ja", "nei")` | Single-word/phrase variant inside a sentence — see [`dsl-control-structures.md`](dsl-control-structures.md#inline-ifelse). |

Concatenate with `+`: `bokmal { +"Du får " + beloep.format() + " før skatt." }`.

Never call `.toString()` on an `Expression` — it throws at runtime. Use `.format()` to render a value, or `.ifNull(fallback)` to coalesce a nullable expression.

## Spacing — adjacent fragments concatenate verbatim

Adjacent `text(...)` calls, `+`-chained fragments, and inline `showIf` blocks render into a single continuous text run. **No implicit space, line break, or punctuation is inserted.** You own the leading/trailing spaces:

```kotlin
// wrong — renders "Du fårbeløpfør skatt."
text(bokmal { +"Du får" + beloep.format() + "før skatt." })

// right — spaces live on the literals
text(bokmal { +"Du får " + beloep.format() + " før skatt." })
```

Two adjacent `text(...)` calls render as `"aa"`, never `"a a"` or `"a\na"`. For a new paragraph use a new `paragraph { }`.

## Verbatim user-supplied wording is immutable

If the user hands you exact text they want in the letter (a drafted paragraph, a legal clause, a pre-translated language branch), render it exactly as given — do not silently polish grammar, shorten sentences, adjust punctuation, or "improve" the tone. If you believe a change is needed (typo, missing expression interpolation, wrong language branch, phrase already exists), **stop and ask the user** with the original and your proposed change side-by-side. Apply only after confirmation.

## Formatter catalogue

Every value goes through `.format()` (or a typed variant) before reaching text. The formatter is type- and language-aware.

### Numbers

| Type | Call | Bokmål / Nynorsk | English |
|---|---|---|---|
| `Int` | `n.format()` | `75` | `75` |
| `Kroner` | `k.format()` | `1 000 kroner` | `NOK 1,000` |
| `Kroner` (bare number) | `k.format(denominator = false)` | `1 000` | `1,000` |
| `Double` | `d.format()` (default `scale = 2`) | `66,67` | `66.67` |
| `Double` | `d.format(scale = 3)` | `66,667` | `66.667` |

### Dates

| Type | Call | Bokmål | English |
|---|---|---|---|
| `LocalDate` | `d.format()` | `15. januar 2025` | `15 January 2025` |
| `LocalDate` | `d.format(short = true)` | `15.01.2025` | `15/01/2025` |
| `LocalDate` | `d.formatMonthYear()` | `januar 2025` | `January 2025` |
| `Month` | `m.format()` | `januar` | `January` |
| `Month` | `m.format(short = true)` | `jan.` | `Jan` |
| `Year` | `y.format()` | `2025` | `2025` |

### Identifiers and lists

| Type | Call | Result |
|---|---|---|
| `Telefonnummer` | `t.format()` | `55 55 33 33` |
| `Foedselsnummer` | `f.format()` | `123456 78901` |
| `Collection<String>` | `xs.format()` | `a, b og c` (bokmål) / `a, b and c` (english) |

### Quoted strings

```kotlin
+ "Søknad om uføretrygd".quoted()      // bokmål: «Søknad om uføretrygd», english: 'Søknad om uføretrygd'
+ skjemanavn.quoted()                  // works on Expression<String> too
```

Bokmål/nynorsk use `«»` (guillemets), English uses `' '`.

## Typed-value formatters carry their own unit — don't append it

Some formatters render the value **with the unit included**. Appending a literal unit double-prints and passes the compiler silently — you only see the defect in the rendered letter.

| Type | `.format()` result includes |
|---|---|
| `Expression<Kroner>` / `Kroner?` | `" kroner"` (bokmål/nynorsk) / `"NOK "` prefix (english) |
| `Expression<Telefonnummer>` | spaces (no unit, but localised) |
| `Expression<Foedselsnummer>` | space between birth-date and personal-number |

```kotlin
// wrong — renders "Du får 1 000 kroner kroner før skatt."
bokmal { +"Du får " + beloep.format() + " kroner før skatt." }

// right — formatter already emitted "kroner"
bokmal { +"Du får " + beloep.format() + " før skatt." }
```

### Order of preference for `Kroner`

1. **Default — `.format()`.** Use everywhere in prose. If the surrounding sentence currently contains `" kroner"` / `" kr"` / `" NOK"`, **rewrite the sentence to drop the literal unit**; the formatter already emits a localised one. Don't reach for `denominator = false` to keep a hand-written `" kroner"` — change the prose instead.
   ```kotlin
   // wrong — silences the formatter just to keep literal "kroner"
   bokmal { +"Du får " + beloep.format(denominator = false) + " kroner hver måned før skatt." }

   // right — let the formatter emit the unit, drop it from the sentence
   bokmal { +"Du får " + beloep.format() + " hver måned før skatt." }
   ```
2. **Table cells — `KronerText` phrase.** Inside `cell { … }` the column header usually carries the unit, so the cell wants the bare number. Use the module's phrase rather than re-deriving the per-language suffix:
   ```kotlin
   cell { includePhrase(KronerText(beloep)) }
   ```
   `KronerText` lives in `fraser/common/TabellFormattering.kt` (pensjon; alder has its own copy). It already handles `" kr"` / `"NOK "` per language.
3. **`.format(denominator = false)` — last resort.** Only when (a) you need the bare number outside a table cell and (b) `KronerText` doesn't apply (e.g. inline math: `"(" + a.format(denominator = false) + " + " + b.format(denominator = false) + ") kroner"`). Even then, prefer rephrasing so each value carries its own unit via `.format()`.

**Related Dto rule:** if a field represents money, type it as `Kroner` — not `Int` — so the selector's `.format()` carries the unit. Using `Int` forces every call site to append `" kroner"` manually, which drifts as soon as a second language branch is added.

## Inline bold (or any single `FontType`) mid-sentence

A `text(...)` call carries one `FontType` for the whole call. To bold a fragment in the middle of a sentence, split into adjacent `text(...)` calls inside the same `paragraph`:

```kotlin
paragraph {
    text(bokmal { +"prefix " },  nynorsk { +"prefiks " })
    text(bokmal { +"highlight" }, nynorsk { +"utheving" }, BOLD)
    text(bokmal { +" suffix." }, nynorsk { +" suffiks." })
}
```

Adjacent `text(...)` calls concatenate verbatim (same rule as *Spacing — adjacent fragments concatenate verbatim*), so leading/trailing spaces belong on the literals. Each language branch must appear in each sibling call.

## Heading levels

Letters have four heading levels, from largest to smallest:

| Level | DSL | Where | Typical use |
|---|---|---|---|
| H1 | `title { }` | Once per template, at `TemplateRootScope` (also once per attachment) | The letter / attachment main title (e.g. "Vedtak om alderspensjon"). |
| H2 | `title1 { }` | `OutlineScope` | Major sections of the letter body. |
| H3 | `title2 { }` | `OutlineScope` | Subsections inside an H2. |
| H4 | `title3 { }` | `OutlineScope` | Rare. Sub-subsections — only when H3 already carries real structure. |

Rules:

- **Never skip a level.** Use `title1` before reaching for `title2`; use `title2` before `title3`. Skipping levels breaks the document outline (used by screen readers and PDF bookmarks).
- **One `title { }` per template.** It's the letter heading and is rendered with the letterhead — not a body section.
- **All title-scopes are `PlainTextScope`.** No `paragraph`, no `BOLD`, no nested control flow that changes scope. Use `text(bokmal { ... }, nynorsk { ... })` only.
- **Don't simulate headings with bold paragraphs.** A bold first line is not a heading — it has no outline level, no spacing, and no a11y semantics. Use the right `titleN` scope.
- **Attachments restart the hierarchy** at their own `title { }` — the attachment's H1 — and may use `title1`/`title2`/`title3` for inner sections independently of the parent letter.

```kotlin
createTemplate(...) {
    title { text(bokmal { +"Vedtak om alderspensjon" }, ...) }   // H1
    outline {
        title1 { text(bokmal { +"Begrunnelse" }, ...) }          // H2
        paragraph { text(bokmal { +"..." }, ...) }
        title2 { text(bokmal { +"Trygdetid" }, ...) }            // H3
        paragraph { text(bokmal { +"..." }, ...) }
    }
}
```



Use `ifElse` for **single-word or short-phrase** variants inside a sentence (singular/plural, gendered noun, …):

```kotlin
text(
    bokmal {
        +"Du har " + antall.format() + " "
        + ifElse(antall.equalTo(1), "utbetaling", "utbetalinger")
        + " denne måneden."
    },
    english {
        +"You have " + antall.format() + " "
        + ifElse(antall.equalTo(1), "payment", "payments")
        + " this month."
    }
)
```

For larger structural variations (a whole sentence or block), use `showIf` / `orShow` instead — see [`dsl-control-structures.md`](dsl-control-structures.md). Long `ifElse` chains across many languages drift apart over time; `showIf` keeps each branch a self-contained unit.

## Common imports

| Symbol | Import |
|---|---|
| `Kroner` | `no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner` (nested, **not** top-level) |
| `Year` / `Months` / `Days` / `Percent` | `no.nav.pensjon.brevbaker.api.model.BrevbakerType.Year` etc. (nested, **not** top-level) |
| `BOLD` (and other `FontType` values) | `no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Text.FontType.BOLD` — deeply nested; import the constant by its short name. |
| no-arg `.format()` for `Kroner` / `Telefonnummer` / `Foedselsnummer` | `no.nav.pensjon.brev.model.format` — separate from the generic `expression.format`. Without it, the no-arg call fails with *"No value passed for parameter 'formatter'"*. |
| Predicate operators (`and`, `or`, `not`, `equalTo`, …) | individual imports under `no.nav.pensjon.brev.template.dsl.expression.…` |
| `quoted()` | `no.nav.pensjon.brev.template.dsl.expression.quoted` |

## Common mistakes

- Bare string inside a language block — literals need unary `+`: `bokmal { +"Tekst" }`.
- Calling `.toString()` on an `Expression` — throws at runtime; use `.format()`.
- Appending `" kroner"` / `" kr"` / `" NOK"` after `Expression<Kroner>.format()` — see *Order of preference for Kroner* above.
- Typing a money field as `Int` instead of `Kroner` in the Dto — forces every call site to append the unit manually.
- Typing a year as `Int` instead of `Year`, a percentage as `Double` instead of `Percent`, or a count of months as `Int` instead of `Months` — all of these have localised `.format()` overloads under `BrevbakerType`; raw scalars compile but lose the language-aware formatter and the type signal in the Dto.
- Calling `.format()` directly on a nullable expression and concatenating — `Expression<T?>.format()` returns `Expression<String?>`, and `+` requires `Expression<String>`. Unwrap with `ifNotNull(expr) { v -> +"… " + v.format() + " …" }` instead of `.ifNull("")` (which silently prints empty). See [`dsl-control-structures.md`](dsl-control-structures.md#ifnotnull--null-safe-access-with-binding).
- Trying to bold a fragment inside a single `text(...)` — `text(...)` carries one `FontType` for the whole call. Split into sibling `text(...)` calls as shown in *Inline bold (or any single `FontType`) mid-sentence* above.
- Missing/extra space between concatenated fragments — adjacency does not insert whitespace.
- Silently editing user-supplied wording. If the user handed over exact text, render it verbatim; ask before changing.
