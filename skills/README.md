# Skills

Task-specific recipes for AI coding assistants working in this repo.

Each skill is a single Markdown file describing a focused task, with:

- **When to use it** — the triggering situation.
- **Prerequisites** — files/context the agent must read first.
- **Steps** — ordered, copy-pasteable.
- **Verification** — the exact command that proves the task is done.
- **Common mistakes** — what breaks in practice.

Skills complement [`../AGENTS.md`](../AGENTS.md), which stays high-level. If a Golden Path in AGENTS.md has a step deep enough to deserve its own recipe, that step links here.

## Available skills

| File | Task |
|---|---|
| [`write-template.md`](write-template.md) | Author a letter template (Dto design, DSL scopes, languages, conditionals, phrases, registration). Shared between autobrev and redigerbar. |
| [`write-redigerbar-template.md`](write-redigerbar-template.md) | Deltas on top of `write-template.md` for `RedigerbarTemplate`: `Saksbehandlervalg`/fagsystem split, Skribenten-visible metadata (`kategori`, `brevkontekst`, `sakstyper`), and the `fritekst(...)` editable placeholder. |
| [`convert-exstream-letter.md`](convert-exstream-letter.md) | Turn the output of the Exstream-to-brevbaker converter into a compilable, idiomatic legacy template. Covers `LegacySelectors` wiring, `SYS_TableRow`→`forEach`, `<FRITEKST>` conversion, table restoration, and simplification. |


## Using the skills from an AI assistant

The skills are plain Markdown and tool-agnostic. How they reach the assistant depends on the tool:

- **GitHub Copilot (JetBrains / VS Code)** — the repo ships prompt files under [`../.github/prompts/`](../.github/prompts/). Invoke `/write-letter` or `/review-letter-template` in Copilot Chat to run the skills with guided inputs. For ad-hoc use, attach a skill with `#file:skills/write-template.md` in the chat input.
- **Claude Code, Junie** — read `AGENTS.md` at the repo root automatically and follow relative links, so the skills are picked up without extra setup.
- **JetBrains AI Assistant** — does not auto-load `AGENTS.md`; attach the skill file manually via the chat panel's file-attach button.


