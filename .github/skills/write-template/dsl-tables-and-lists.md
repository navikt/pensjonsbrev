# Skill: Tables and lists

**Scope:** the two structured-content elements available in `ParagraphScope` — `list { item { … } }` and `table { header { column { … } } row { cell { … } } }`. Covers static authoring, dynamic rows/items via `forEach`, conditional rows/items, and column alignment / width.

**Companion docs:** [`dsl/paragraph/lister.adoc`](../../../docs/modules/ROOT/pages/dsl/paragraph/lister.adoc), [`dsl/paragraph/tabeller.adoc`](../../../docs/modules/ROOT/pages/dsl/paragraph/tabeller.adoc).

## Lists

### Static items

```kotlin
paragraph {
    text(bokmal { +"Du må melde fra om:" })
    list {
        item { text(bokmal { +"Endringer i inntekt" }) }
        item { text(bokmal { +"Endringer i sivilstand" }) }
        item { text(bokmal { +"Flytting til utlandet" }) }
    }
}
```

`item { … }` is `TextScope` — same rules as a single text run; phrases must be `TextOnlyPhrase` or `PlainTextOnlyPhrase`.

### Conditional items

```kotlin
list {
    item { text(bokmal { +"Dette vises alltid" }) }
    showIf(harBarnetillegg) {
        item { text(bokmal { +"Dette vises kun med barnetillegg" }) }
    }
}
```

### Dynamic items (`forEach`)

```kotlin
showIf(barn.isNotEmpty()) {
    paragraph {
        text(bokmal { +"Barnetillegget gjelder for:" })
        list {
            forEach(barn) { enkeltBarn ->
                item {
                    text(bokmal {
                        + enkeltBarn.navn + ", født " + enkeltBarn.fodselsdato.format()
                    })
                }
            }
        }
    }
}
```

Always think about the empty-list case — `forEach` over an empty collection produces nothing, which can leave a dangling lead-in sentence. Either gate the whole block with `showIf(xs.isNotEmpty())` (with an `.orShow { … }` if there is alternative wording for the empty case), or remove the lead-in.

## Tables

### Basic structure

```kotlin
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Table.ColumnAlignment

paragraph {
    table(
        header = {
            column { text(bokmal { +"Beskrivelse" }) }                                  // default LEFT
            column(alignment = ColumnAlignment.RIGHT) { text(bokmal { +"Beløp" }) }     // RIGHT
        }
    ) {
        row {
            cell { text(bokmal { +"Uføretrygd" }) }
            cell { eval(uforetrygdBeloep.format()) }
        }
        row {
            cell { text(bokmal { +"Barnetillegg" }) }
            cell { eval(barnetilleggBeloep.format()) }
        }
    }
}
```

- `header { column { … } }` is `PlainTextScope` — only ren tekst (no `TextOnlyPhrase` formatting).
- `row { cell { … } }` is `TextScope` — `text(...)`, `includePhrase(TextOnly | PlainTextOnly)`, `eval(...)`, `newLine()`.
- `eval(expression.format())` is the shorthand for emitting a single formatted expression in a cell.

### Column alignment — NAV letter standard

> Tekst er alltid venstrestilt for leseretning. Tall høyrestilles. I tabeller hvor kolonner blander tall og bokstaver bør det vurderes pr. tabell om innholdet skal venstrestilles eller høyrestilles.

Default is `LEFT`. Set `alignment = ColumnAlignment.RIGHT` explicitly on number columns. For mixed columns (numbers + text), choose per-table — there is no correct default. Decide up front and document the choice with a comment; don't let it drift row by row.

### Column width — `columnSpan`

Every `column(...)` takes `columnSpan: Int` (default `1`) that sets its **relative** width. Total table width = sum of all spans; each column gets its share. Think of it as a weight, not pixels.

```kotlin
table(
    header = {
        column(columnSpan = 2) { text(bokmal { +"Beskrivelse" }) }                                     // 2/5
        column(columnSpan = 1, alignment = ColumnAlignment.RIGHT) { text(bokmal { +"Antall" }) }       // 1/5
        column(columnSpan = 2, alignment = ColumnAlignment.RIGHT) { text(bokmal { +"Beløp" }) }        // 2/5
    }
) { … }
```

Decide ratios deliberately per table: wide text columns (descriptions, long labels, merknader that may wrap) should get a larger span; narrow numeric columns (år, antall, single-line Kroner) can sit on `columnSpan = 1`. If a cell wraps awkwardly in the rendered PDF, tune spans up or down — re-render to verify, don't guess.

### Conditional rows — never conditional cells

```kotlin
table(header = { … }) {
    row {
        cell { text(bokmal { +"Grunnbeløp" }) }
        cell { eval(grunnbeloep.format()) }
    }

    showIf(harBarnetillegg) {
        row {
            cell { text(bokmal { +"Barnetillegg" }) }
            cell { eval(barnetilleggBeloep.format()) }
        }
    }

    row {
        cell { text(bokmal { +"Totalt" }, fontType = FontType.BOLD) }
        cell { eval(totalBeloep.format()) }
    }
}
```

> **Antall celler i hver rad må matche antall kolonner i headeren.** Conditional cells (`showIf` around a single `cell { }`) are therefore not supported — the row would have a different cell count depending on data. Wrap whole rows.

### Dynamic rows (`forEach`)

```kotlin
table(header = { … }) {
    forEach(utbetalingsperioder) { periode ->
        row {
            cell { eval(periode.maaned.format()) }
            cell { eval(periode.beloep.format()) }
        }
    }
}
```

### Money cells — use `KronerText`

Inside `cell { … }` you usually want the bare number (the column header carries the `kr` / `NOK` unit). The cleanest way is the `KronerText` phrase from the module's `fraser/common/TabellFormattering.kt`:

```kotlin
cell { includePhrase(KronerText(beloep)) }
```

It handles `" kr"` (bokmål/nynorsk) and `"NOK …"` (english). Reach for `eval(beloep.format(denominator = false))` only as a last resort. See [`dsl-text-and-formatting.md`](dsl-text-and-formatting.md#order-of-preference-for-kroner).

## Best practices

- **Right-align numbers**, left-align text, decide per-table for mixed columns.
- **Keep tables narrow.** Many columns become unreadable; consider splitting into two tables or moving detail into surrounding prose.
- **Bold totals.** Use `text(bokmal { +"Sum" }, fontType = FontType.BOLD)` for the bottom-line row.
- **Empty-collection handling for dynamic rows** — `forEach` over an empty collection produces a header-only table. Either gate the whole table with `showIf(xs.isNotEmpty())` or accept the empty rendering deliberately.

## Common mistakes

- Setting `columnSpan` randomly without thinking about the layout — re-render and tune.
- Wrapping a single `cell { }` in `showIf` — row cell counts must match the header. Wrap the whole `row { }`.
- Using `text(bokmal { +x.format(denominator = false) + " kr" })` in a cell instead of `includePhrase(KronerText(x))`.
- Forgetting `eval(...)` and trying to drop a bare `Expression` into a cell.
- Tables for layout instead of data — if the structure is not "rows of records", prefer prose with paragraphs.
