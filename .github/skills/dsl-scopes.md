# Skill: DSL scope hierarchy

**Scope:** the nested DSL receivers you write inside `createTemplate(...) { … }`, what each one allows, and how to read selector / phrase errors as "wrong scope".

**Companion docs:** [`dsl/scopes.adoc`](../../docs/modules/ROOT/pages/dsl/scopes.adoc) — authoritative listing.

## The hierarchy

```
createTemplate(...) {                       // TemplateRootScope
    title { ... }                           //   PlainTextScope
    outline {                               //   OutlineScope
        title1/title2/title3 { ... }        //     PlainTextScope
        paragraph {                         //     ParagraphScope
            text(bokmal { ... }, ...)       //       language-branched literals/expressions
            list { item { ... } }           //         TextScope inside item
            table {
                header { column { ... } }   //         PlainTextScope inside column
                row { cell { ... } }        //         TextScope inside cell
            }
            formText() / formChoice() / newline()
            includePhrase(ParagraphPhrase | TextOnlyPhrase | PlainTextOnlyPhrase)
        }
        includePhrase(OutlinePhrase)
    }
    includeAttachment(attachment, data)
    includeAttachmentIfNotNull(attachment, nullableData)
}
```

## What each scope allows

| Scope | Allows | Forbids |
|---|---|---|
| **TemplateRootScope** | `title`, `outline`, `includeAttachment`, `includeAttachmentIfNotNull` | text, paragraphs, phrases — those go inside `title`/`outline` |
| **OutlineScope** | `title1`/`title2`/`title3`, `paragraph`, `includePhrase(OutlinePhrase)`, control structures (`showIf`, `ifNotNull`, `forEach`) | inline `text(...)` — wrap it in `paragraph { }` |
| **ParagraphScope** | `text(...)`, `list`, `table`, `formText`, `formChoice`, `newline`, `includePhrase(ParagraphPhrase \| TextOnlyPhrase \| PlainTextOnlyPhrase)` | titles, nested paragraphs, attachments |
| **TextScope** (inside `item { … }` / `cell { … }`) | `text(...)`, `newLine()`, `includePhrase(TextOnlyPhrase \| PlainTextOnlyPhrase)` | lists, tables, ParagraphPhrases |
| **PlainTextScope** (inside `title { }`, `title1/2/3`, `column { }`) | `text(...)` with **no** formatting (no bold/italic), `includePhrase(PlainTextOnlyPhrase)` | TextOnlyPhrase, ParagraphPhrase, lists, tables |

Phrase types map 1:1 to scopes — see [`dsl-phrases.md`](dsl-phrases.md).

## Reading scope errors

The compiler error `Unresolved reference: paragraph` (or `text`, `title1`, `column`, …) usually means **you are one scope too high or low**, not that the symbol is missing. Check the bracket nesting:

- `text(...)` inside `outline { }` (skipping `paragraph`) → wrong; wrap in `paragraph { }`.
- `title1 { }` inside `paragraph { }` → wrong; titles live in `OutlineScope`.
- `paragraph { }` inside `title { }` → wrong; `title` is `PlainTextScope` (no paragraphs, no formatting).
- `includePhrase(MyOutlinePhrase)` inside `paragraph { }` → wrong; `OutlinePhrase` only includes in `OutlineScope`.

## Notes

- Use `title1`/`title2`/`title3` (not Markdown-style `#`) for section headings inside `outline`.
- Adjacent text fragments in the same text run concatenate without a separator — see [`dsl-text-and-formatting.md`](dsl-text-and-formatting.md).
- `newline()` exists but is sparingly used (typically for stacking address lines inside a single paragraph). For ordinary prose breaks, open a new `paragraph { }`.
- Attachments are themselves a self-contained tree starting at `OutlineScope` — see [`dsl-attachments.md`](dsl-attachments.md).
