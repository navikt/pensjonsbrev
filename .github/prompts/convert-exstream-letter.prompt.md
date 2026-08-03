---
mode: agent
description: Finish converting an Exstream-generated letter into a working brevbaker template
---

You are finishing the manual pass on a Kotlin file produced by the team's Exstream-to-brevbaker converter.

**Read first (do not skip):**
- `AGENTS.md`
- `.github/skills/write-template/convert-exstream-letter.md` — the step-by-step pipeline for this task.
- `.github/skills/write-template/SKILL.md` — **read end-to-end, not just as a reference.**
- `.github/skills/write-template/write-redigerbar-template.md` — only if the letter contains `<FRITEKST: …>` markers (then it must be a `RedigerbarTemplate`).

**Inputs**
- Path to the converter-produced file: ${input:sourceFile}

**Verify at the end**
```bash
./gradlew :pensjon:maler:integrationTest
```

