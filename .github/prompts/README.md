# GitHub Copilot prompt files

Files in this directory are [Copilot prompt files](https://docs.github.com/en/copilot/customizing-copilot/adding-repository-custom-instructions-for-github-copilot) — they show up as slash-commands in Copilot Chat (VS Code and JetBrains IDEs).

| Prompt | Invoke in Copilot Chat | What it does |
|---|---|---|
| `write-letter.prompt.md` | `/write-letter` | Walks the skills in `.github/skills/` to scaffold a new autobrev or redigerbar letter template. Asks for module, brevkode, kind and situation. |
| `review-letter-template.prompt.md` | `/review-letter-template` | Reviews the open / selected template file against the skills' checklists. |
| `convert-exstream-letter.prompt.md` | `/convert-exstream-letter` | Finishes the manual pass on a file produced by the Exstream-to-brevbaker converter: resolves `TODO` selectors, `SYS_TableRow` placeholders, `<FRITEKST>` markers, flattened tables. |

Prompts are thin wrappers — the authoritative guidance lives in [`../skills/`](../skills/) and [`../../AGENTS.md`](../../AGENTS.md). Update the skills; prompts should only change when the invocation contract (inputs, mode) changes.

## How to use in JetBrains IDEs with Copilot

In the Copilot Chat panel:

- Type `/` to see the list of available prompts, or start typing `/write-letter` to invoke it.
- The agent will prompt for the declared inputs (module, brevkode, …) before running.

If you are using Copilot Chat without prompt-file support, you can still reference the skills inline by attaching them with `#file:.github/skills/write-template/SKILL.md`.

