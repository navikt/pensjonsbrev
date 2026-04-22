---
mode: ask
description: Review a letter template against the repo's conventions
---

Review the currently selected / open letter template file against the checklists in:

- `skills/write-template.md` (applies to both autobrev and redigerbar)
- `skills/write-redigerbar-template.md` (only if the template is a `RedigerbarTemplate`)

Check, at minimum:

1. **Dto design** — data-minimised, non-nullable by default, no defaults on required fields, related fields grouped.
2. **`@TemplateModelHelpers`** annotation present on the template object.
3. **Brevkode** — in the module's registry, ≤ 50 chars, looks unique.
4. **Text sources** — unary `+` on literals; `.format()` on expressions; no `.toString()` on `Expression`. For redigerbar: `fritekst("...")` used only for required caseworker input, with a prompt-style initial string.
5. **Conditionals** — no Kotlin `if` on Dto values; `showIf` / `ifElse` / `ifNotNull` used correctly (small inline vs. whole block).
6. **Concatenation** — spaces/punctuation on literals; adjacent `text(...)` calls render without separators.
7. **Phrases** — end-of-letter content is not silently re-authored; reuses existing `Felles` / `FellesFraser` phrases where appropriate.
8. **Registration** — present in the module's `AllTemplates` object.
9. **Spelling** — letter text (Bokmål, Nynorsk, English branches) is free of typos and the language in each branch matches the branch tag.

Output as a terse bulleted list of issues with file/line references. End with a one-line verdict: LGTM or CHANGES REQUESTED.



