---
mode: agent
description: Finish converting an Exstream-generated letter into a working brevbaker template
---

You are finishing the manual pass on a Kotlin file produced by the team's Exstream-to-brevbaker converter.

**Read first (do not skip):**
- `AGENTS.md`
- `.github/skills/convert-exstream-letter.md` — the step-by-step pipeline for this task.
- `.github/skills/write-template.md` — **read end-to-end, not just as a reference.** 
- `.github/skills/write-redigerbar-template.md` — only if the letter contains `<FRITEKST: …>` markers (then it must be a `RedigerbarTemplate`).

**Inputs**
- Path to the converter-produced file: ${input:sourceFile}

**Hard rules**
- Follow the steps of `.github/skills/convert-exstream-letter.md` in order. Do not skip ahead.
- Selector block at top of the converter output belongs in `pensjon/maler/.../legacy/LegacySelectors.kt`, not in the template file. Dedupe against existing entries before pasting.
- If the file contains `<FRITEKST: …>` markers, change the template type to `RedigerbarTemplate` and add the `Saksbehandlervalg` / fagsystem Dto split.
- Resolve all `FUNKSJON_PE_…("$/* TODO (SYS_TableRow) */")` placeholders into `forEach` iterations.
- Rebuild flattened table-cell `text(…)` sequences as real `table { row { cell { … } } }` — adjacent `text(…)` calls concatenate without separators.
- Infer `distribusjonstype` / `brevtype` (and `kategori` / `brevkontekst` / `sakstyper` if redigerbar) from the letter content, then stop and ask the user to confirm with a one-line justification before continuing.
**Verify at the end**
```bash
./gradlew :pensjon:maler:integrationTest
```

