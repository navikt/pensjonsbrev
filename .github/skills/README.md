# Skills

Task-specific recipes for AI coding assistants working in this repo.

Each skill is a single Markdown file describing a focused task, with:

- **When to use it** — the triggering situation.
- **Prerequisites** — files/context the agent must read first.
- **Steps** — ordered, copy-pasteable.
- **Verification** — the exact command that proves the task is done.
- **Common mistakes** — what breaks in practice.

## Available skills

### Recipes (full task workflows)

| File | Task |
|---|---|
| [`write-template.md`](write-template.md) | Author a letter template (Dto design, brevkode rules, template skeleton, fixture wiring, registration). Shared between autobrev and redigerbar. Cross-references the DSL sub-skills below for the DSL body itself. |
| [`write-redigerbar-template.md`](write-redigerbar-template.md) | Deltas on top of `write-template.md` for `RedigerbarTemplate`: `Saksbehandlervalg`/fagsystem split, Skribenten-visible metadata (`kategori`, `brevkontekst`, `sakstyper`), feature toggle, and the `fritekst(...)` editable placeholder. |
| [`convert-exstream-letter.md`](convert-exstream-letter.md) | Turn the output of the Exstream-to-brevbaker converter into a compilable, idiomatic legacy template. Covers data-minimised Dto design with PESYS traceability comments, `SYS_TableRow`→`forEach`, `<FRITEKST>` conversion, table restoration, and simplification. |

### DSL sub-skills (focused references for one DSL topic each)

Distilled from [`docs/modules/ROOT/pages/dsl/`](../../docs/modules/ROOT/pages/dsl/) and the practical experience of writing letter templates. Each is self-contained and cross-references its neighbours.

| File | Topic |
|---|---|
| [`dsl-scopes.md`](dsl-scopes.md) | Scope hierarchy (`TemplateRootScope` → `OutlineScope` → `ParagraphScope` → `TextScope` / `PlainTextScope`); reading scope-related compile errors. |
| [`dsl-text-and-formatting.md`](dsl-text-and-formatting.md) | `bokmal { +"…" }` text composition, formatter catalogue (Int, Kroner, Double, dates, Telefonnummer, Foedselsnummer, …), order of preference for `Kroner`. |
| [`dsl-control-structures.md`](dsl-control-structures.md) | `showIf` / `orShow` / `orShowIf`, `ifNotNull` / `orIfNotNull`, `ifElse`, `forEach`, predicate operators (`and` / `or` / `not` / `equalTo` / `isOneOf` / …). |
| [`dsl-tables-and-lists.md`](dsl-tables-and-lists.md) | `paragraph { table { … } }` and `paragraph { list { … } }`; column alignment, `columnSpan`, dynamic rows/items, `KronerText` for money cells. |
| [`dsl-phrases.md`](dsl-phrases.md) | `OutlinePhrase` / `ParagraphPhrase` / `TextOnlyPhrase` / `PlainTextOnlyPhrase`; search-before-you-write rule; standard end-of-letter phrases. |
| [`dsl-attachments.md`](dsl-attachments.md) | `createAttachment` / `includeAttachment` / `includeAttachmentIfNotNull`; `EmptyVedleggData`. |

## Using the skills from an AI assistant

The skills are plain Markdown and tool-agnostic. How they reach the assistant depends on the tool:

- **GitHub Copilot (JetBrains / VS Code)** — the repo ships prompt files under [`../prompts/`](../prompts/). Invoke `/write-letter` or `/review-letter-template` in Copilot Chat to run the skills with guided inputs. For ad-hoc use, attach a skill with `#file:.github/skills/write-template.md` in the chat input.
- **Claude Code, Junie** — read `AGENTS.md` at the repo root automatically and follow relative links, so the skills are picked up without extra setup.
- **JetBrains AI Assistant** — does not auto-load `AGENTS.md`; attach the skill file manually via the chat panel's file-attach button.


