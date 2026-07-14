---
name: letter-editor-actions
description: Write/refactor LetterEditor actions safely with focus/ID/deleted invariants
applies_to:
  - skribenten-web/frontend/src/Brevredigering/LetterEditor/actions/**
---

# LetterEditor Actions Skill

Use this skill when changing files in `skribenten-web/frontend/src/Brevredigering/LetterEditor/actions`.

## Golden rules

1. Use `Action<LetterEditorState, ...>` + `withPatches(...)` for actions that mutate the letter model. Focus-only actions can use plain `produce(...)`.
2. Set `draft.saveStatus = "DIRTY"` only when the action actually changes the letter model.
3. Update `draft.focus` deterministically after mutation; never leave focus pointing at deleted content.
4. Guard early with type/index checks (`isValidIndex`, `isItemContentIndex`, `isTableCellIndex`, etc.).
5. Ensure valid end structure: if a block/item/cell becomes empty, it should normally receive `newLiteral()`.
6. Preserve semantics for existing IDs and `deleted*` arrays during move/split/merge (see below).

## ID and deletion semantics

Every element implements `Identifiable = { id: number | null; parentId: number | null }`. The **backend uses `id`/`parentId` to merge user edits with fresh template renders** after saksbehandlerValg changes.

### `id`: template origin vs. user-created

| Value | Meaning |
|---|---|
| `number` | Originated from the template render — stable, must be preserved. |
| `null` | Created by the saksbehandler — always kept verbatim during re-merge. |

- **Always set `id: null`** on new elements. Use the `new*` factory functions in `common.ts`.
- **Never mutate `id`** on server elements.
- **Exception — ID preservation during merge**: when merging nodes (e.g. adjacent lists), the merged node may take the `id` of one source node — but only when that source is first removed via `removeElements` (recording its id in `deleted*`) and the merged node is inserted via `addElements` (which un-deletes it). Mark such sites: `// INTENTIONAL ID COPY — ID preservation during merge (see letter-editor-actions SKILL.md).`

### `parentId`: tracking element origin

`parentId` is set at `toEdit()` time to the parent's `id`. The backend uses it to detect edited containers (`isEdited()`) and intentionally moved elements.

- **Always set `parentId: null`** on new elements. The `new*` factories do this.
- **Never mutate `parentId`** on server elements — it is read-only.

### `deleted*` arrays: tracking deleted template elements

| Container | Companion array |
|---|---|
| `EditedLetter` | `deletedBlocks` |
| `AnyBlock` | `deletedContent` |
| `ItemList` | `deletedItems` |
| `Item` | `deletedContent` |
| `Table` | `deletedRows` |

These `deleted*`-arrays hold **integer IDs only** (no point in adding IDs that are `null`). The backend uses these IDs to suppress re-introduction of deleted template elements after re-merge.

- **Removing**: use `removeElements(index, count, parent)` — it handles all guards. If manual, push `element.id` only when `typeof element.id === "number"` and `element.parentId === parent.id` and the id is not already tracked.
- **Re-adding**: use `addElements(elements, at, content, deletedContent)` — it removes the id from `deleted*` automatically.
- **Split-persistence**: when splitting a block/list, remove moved content via `removeElements` (not raw `splice`). The resulting deletion records are intentional — they prevent the backend from re-introducing that content in the source container after re-merge. The destination has `id: null` and is preserved verbatim.
- **Splitting containers**: when ambiguous which `deleted*` entries belong to each half, propagate all entries to both halves. The backend prunes stale IDs; silently dropping entries is unsafe.

### Edit overlay fields

Original template values are read-only. User edits live in overlay fields alongside them:

| Original field | Overlay field |
|---|---|
| `LiteralValue.text` | `editedText: string \| null` |
| `LiteralValue.fontType` | `editedFontType: FontType \| null` |
| `ItemList.listType` | `editedListType: ListType \| null` |

`null` = unmodified. Always write to the overlay field, never the original. Use `text()`, `effectiveListType()` etc. to read the effective value.

### `mergeLiteralsIfPossible`

Two adjacent literals can only merge when **at least one has `id: null`**. Two template literals are always kept separate. `addElements()` calls this automatically at insertion boundaries.

## Utility-first: preferred building blocks

Prefer helpers in `actions/common.ts` before writing custom mutation logic:
- `removeElements(...)` — removes and updates `deleted*` correctly.
- `addElements(...)` — inserts and cleans `deleted*`, attempts literal merge at boundaries.
- `findAdjoiningContent(...)` — finds contiguous segments instead of manual looping.
- `splitLiteralAtOffset(...)` — standardized literal split with correct `editedText` behavior.
- `splitMixedListBlock(draft, blockIndex)` — normalizes a block after list insertion (see below).
- `new*` constructors — `newLiteral`, `newItem`, `newItemList`, `newParagraph`, `newRow`, …

## List-specific rules

### One-list-per-block — the invariant

A block that contains a **new** (`id: null`) `ItemList` must contain **only** that list. Template lists (`id: non-null`) may co-exist with other content in the same block.

In practice: when a user action creates a new list (`id: null`) and that list is not immediately merged into a template list, `splitMixedListBlock` isolates it. If the new list does merge with an adjacent template list (gaining a non-null `id` via the intentional ID copy), no split is needed — the merged list inherits the template's block position.

### `splitMixedListBlock`

Call `splitMixedListBlock(draft, blockIndex)` after `toggleListOn` modifies a block. It:
1. Merges adjacent same-type lists within the block.
2. Partitions content into runs: each `id: null` ItemList is a single-element run; everything else (text, variables, tables, `id != null` ItemLists) forms "text" runs.
3. Keeps run[0] in the original block (preserving its `id`); subsequent runs become new `id: null` blocks inserted after.
4. Updates `draft.focus` if it was inside the block.

Not needed after `toggleListOff`, which already produces clean separate blocks.

### `editedListType` — switching list type without changing the original

`ItemList` has an `editedListType: ListType | null` field (an edit overlay, never set by templates). When a user switches a bullet list to a numbered list (or vice versa):

- `switchListType` sets `editedListType = newType` when `newType !== list.listType`.
- Reverting to the original type sets `editedListType = null` (no edit stored).
- `effectiveListType(list)` returns `editedListType ?? listType` — **always use this** when comparing or rendering the current list type.
- `toMarkup()` uses `editedListType ?? listType` for the rendered output.
- The Kotlin backend preserves `editedListType` through `UpdateEditedLetter` and applies it in `toMarkup()`.

### `toggleListOff` — always produces separate blocks

| Case | Original block | New block(s) after |
|---|---|---|
| List becomes empty | Text inline | — |
| First item extracted | Text (keeps original `id`) | Remaining list in a new `id: null` block |
| Last item extracted | List (keeps original `id`) | Extracted text in a new `id: null` block |
| Middle item extracted | First-half list (keeps original `id`) | Text block + second-half list (both `id: null`) |

The original block always stays at the **lower index** because both merge helpers keep the lower-indexed block as the survivor — the original `id` must never end up on a block that would be discarded by a subsequent merge.

### Normalization scope

Normalize only blocks the action creates or touches. Leave pre-existing mixed blocks (from templates or stored letters) unchanged unless directly modified.

## Table-specific rules

- No two tables adjacent in the same block without a separator literal.
- The last block in the letter must not end with a table (no caret target).
- After row/column operations, focus must point to a valid cell/header location.
- `applyTableSeparatorNormalization` in `common.ts` enforces these — used on load and after `deleteSelection`.
- Direct `splice(...)` is acceptable for pure insertions that don't move existing IDs (e.g. separator literals). For deletions/moves, use `removeElements`/`addElements` or update `deleted*` explicitly. Comment exceptions.

## Checklist before finishing

- [ ] The change follows the `withPatches` pattern.
- [ ] Focus is valid after all branches.
- [ ] `saveStatus` is set only on real model changes.
- [ ] `deleted*` arrays remain consistent.
- [ ] New elements use `id: null` and `parentId: null`; no existing IDs are mutated.
- [ ] Edit overlays (`editedText`, `editedFontType`, `editedListType`) are set instead of overwriting originals.
- [ ] Empty container cases are handled (`newLiteral()` when needed).
- [ ] Table-separator invariants are still satisfied.
- [ ] After inserting a **new** (`id: null`) list: `splitMixedListBlock` is called on the affected block.
- [ ] List type reads use `effectiveListType(list)`, not `list.listType` directly.
- [ ] After splitting a block or list: the original block retains its `id` at the lowest index.
