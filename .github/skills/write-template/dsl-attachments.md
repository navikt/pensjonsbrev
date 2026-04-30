# Skill: Attachments (vedlegg)

**Scope:** how to define a vedlegg with `createAttachment`, include it from a template (always or conditionally), and choose between data-carrying and `EmptyVedleggData`.

**Companion docs:** [`dsl/vedlegg.adoc`](../../../docs/modules/ROOT/pages/dsl/vedlegg.adoc).

## What a vedlegg is

A vedlegg is a self-contained sub-document. In brevbaker output it lands in the same PDF as the main letter, but starts on its own page. It has its own languages, its own data type, an optional title, and an `OutlineScope` body — so all the same DSL building blocks (paragraphs, lists, tables, phrases, control structures) apply.

## Defining a vedlegg

```kotlin
@TemplateModelHelpers
val beregningsDetaljer = createAttachment<LangBokmalNynorsk, BeregningDto>(
    title = {
        text(
            bokmal { +"Beregning av uføretrygd" },
            nynorsk { +"Utrekning av uføretrygd" }
        )
    },
    includeSakspart = false,
) {
    // OutlineScope — the body of the attachment
    title1 { text(bokmal { +"Slik har vi beregnet uføretrygden din" }) }
    paragraph { table(header = { … }) { … } }
    showIf(harBarnetillegg) { … }
}
```

| Parameter | Effect |
|---|---|
| `title` | A `PlainTextScope` block — text only, no formatting. Shown as the heading on the vedlegg's first page. |
| `includeSakspart` | If `true`, the vedlegg gets its own sakspart-info header (recipient name, address, etc.). Set `false` for purely informational vedlegg that don't need to re-state the recipient. |
| Type parameters `<Lang, Data>` | `Lang` is the language type (e.g. `LangBokmalNynorsk`) — must be a subset of the parent template's languages. `Data` is the vedlegg's own data type, separate from the letter's Dto. |

`@TemplateModelHelpers` triggers KSP to generate selectors for the `Data` type, just like for a template. The body has access to those generated selectors.

## Including a vedlegg from a template

### Always include

```kotlin
override val template = createTemplate(/*…*/) {
    title { … }
    outline { … }

    includeAttachment(beregningsDetaljer, beregningsData)
}
```

The second argument is an `Expression<Data>` — a selector path on the letter's Dto, or a literal built with `.expr()`.

### Conditional inclusion

With a predicate:

```kotlin
includeAttachment(beregningsDetaljer, beregningsData, predicate = data.visBeregning)
```

The predicate is `Expression<Boolean>` — same operators as `showIf` (see [`dsl-control-structures.md`](dsl-control-structures.md#predicate-operators--build-expressionboolean-from-selectors)).

When the data itself can be null, use `includeAttachmentIfNotNull` — the vedlegg is included only when the expression resolves to non-null:

```kotlin
includeAttachmentIfNotNull(ekstraDetaljer, data.nullableDetails)
```

## Vedlegg without data — `EmptyVedleggData`

For purely static vedlegg (no per-letter inputs at all), declare the data type as `EmptyVedleggData`:

```kotlin
val generellInfo = createAttachment<LangBokmalNynorsk, EmptyVedleggData>(
    title = { text(bokmal { +"Generell informasjon" }, nynorsk { +"Generell informasjon" }) },
    includeSakspart = false,
) {
    paragraph { text(bokmal { +"Her er generell informasjon om Nav." }) }
}

// In the template:
includeAttachment(generellInfo, EmptyVedleggData)
```

## Where vedlegg live

Co-locate them with the templates that use them. Typical layout:

```
<module>/maler/src/main/kotlin/.../
├── vedlegg/
│   ├── DineRettigheterOgPlikter.kt
│   └── DetaljertBeregning.kt
└── MittBrev.kt
```

Cross-cutting vedlegg (e.g. a generic "Dine rettigheter og plikter" used by many letters) belong in a shared `vedlegg/` package; domain-specific vedlegg live near their callers.

## Common mistakes

- Forgetting `@TemplateModelHelpers` on the vedlegg `val` — the body's selectors won't be generated.
- Declaring a `Lang` for the vedlegg that is not a subset of the parent template's languages — compile error when including. Mirror the parent's languages or pick a subset.
- Using `EmptyVedleggData` as a type but forgetting to pass `EmptyVedleggData` (the singleton) to `includeAttachment` — compile error.
- Setting `includeSakspart = true` on a generic informational vedlegg — adds noise the reader doesn't need; the main letter already has it.
- Authoring large repeated content directly in the template body when it should be a vedlegg — vedlegg start a new page and are easier to skip; long appendices belong there, not in `outline`.
