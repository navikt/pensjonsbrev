---
mode: agent
description: Scaffold a new letter template (autobrev or redigerbar)
---

You are adding a new letter template to the Pensjonsbrev repo.

**Read first (do not skip):**
- `AGENTS.md` — repo-wide conventions.
- `skills/write-template.md` — the full recipe (module selection, brevkode rules, Dto design, DSL body, fixture, registration, verification).
- If the brev is a `RedigerbarTemplate`, also read `skills/write-redigerbar-template.md` for the `Saksbehandlervalg` / fagsystem-data Dto split, `kategori` / `brevkontekst` / `sakstyper`, and `fritekst(...)`.

**Inputs**
- Kind (autobrev or redigerbar): ${input:kind:redigerbar}
- Target maler module (e.g. `pensjon/maler`, `alder/maler`, `ufoere/maler`, `etterlattemaler`, `planlegge-pensjon-maler`): ${input:module}
- Proposed brevkode (≤ 50 chars, unique across all modules, immutable once in prod): ${input:brevkode}
- Short description of the situation the brev covers: ${input:situation}

**Hard rules**
- Follow the skills' steps in order. Do not invent a brevkode; use the one provided and verify uniqueness.
- For `kategori` / `brevkontekst` / `sakstyper`: infer from the letter content and **stop and ask the user to confirm** each value with a one-line justification before continuing.
- Never silently modify verbatim user-supplied wording — if a change looks needed, ask first.
- Search the module's `fraser/` directories for existing phrases before authoring new wording; on near-matches, ask the user.
- Do not add a per-template integration test — the module's `BrevmodulTest` subclass auto-renders every registered template.

**Verify at the end**
```bash
./gradlew :${input:module}:integrationTest
./gradlew :${input:module}:test
```

