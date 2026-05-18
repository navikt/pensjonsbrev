---
name: letter-editor-actions
description: Write/refactor LetterEditor actions safely with focus/ID/deleted invariants
applies_to:
  - skribenten-web/frontend/src/Brevredigering/LetterEditor/actions/**
---

# LetterEditor Actions Skill

Use this skill when changing files in `skribenten-web/frontend/src/Brevredigering/LetterEditor/actions`.

The goal is to keep action logic safe and consistent for:
- focus addressing (`Focus`/`LiteralIndex`)
- history handling (`withPatches`)
- ID/deleted-array bookkeeping (`deletedContent`, `deletedItems`, `deletedBlocks`, `deletedRows`)
- content invariants for text, lists, and tables

## Golden rules

1. Use `Action<LetterEditorState, ...>` + `withPatches(...)` for actions that mutate the letter model. Focus-only actions (for example `cursorPosition`/`updateFocus`) can use plain `produce(...)`.
2. Set `draft.saveStatus = "DIRTY"` only when the action actually changes the letter model.
3. Update `draft.focus` deterministically after mutation; never leave focus pointing at deleted content.
4. Guard early with type/index checks (`isValidIndex`, `isItemContentIndex`, `isTableCellIndex`, etc.).
5. Ensure valid end structure: if a block/item/cell becomes empty, it should normally receive `newLiteral()`.
6. Preserve semantics for existing IDs and `deleted*` arrays during move/split/merge.

## Utility-first: preferred building blocks

Prefer helpers in `actions/common.ts` before writing custom mutation logic:
- `removeElements(...)`: removes and updates `deleted*` correctly.
- `addElements(...)`: inserts and cleans `deleted*`, and attempts literal merge at boundaries.
- `findAdjoiningContent(...)`: finds contiguous segments instead of manual looping.
- `splitLiteralAtOffset(...)`: standardized literal split with correct `editedText` behavior.
- `new*` constructors: create new nodes consistently (`newLiteral`, `newItem`, `newItemList`, `newParagraph`, `newRow`, ...).

## Table-specific rules

Table actions must preserve these invariants:
- No two tables adjacent in the same block without a separator literal.
- The last block in the letter should not end with a table without a trailing literal (caret target).
- After row/column operations, focus must point to a valid cell/header location.

Existing normalization:
- `normalizeTableSeparators` / `applyTableSeparatorNormalization` in `actions/common.ts`
- used on load (`create`) and after `deleteSelection`.

Direct `splice(...)` can be acceptable for insertion-only changes that do not remove/move/reintroduce existing IDs (for example separator literals added by `applyTableSeparatorNormalization`). For deletions/moves in structures with `deleted*` tracking, use helpers (`removeElements`/`addElements`) or update `deleted*` explicitly. Comment exceptions explicitly.

## Refactor pattern (before/after)

### 1) Removal/insertion in parent content

```ts
// Preferred
removeElements(index, count, parent);
addElements(elements, at, parent.content, parent.deletedContent);
```

Avoid manual `splice` when the parent has a `deleted*` array that must be maintained.

### 2) Splitting a literal around the cursor

```ts
const tail = splitLiteralAtOffset(literal, offset);
addElements([tail], insertAt, parent.content, parent.deletedContent);
```

Avoid ad hoc slicing that duplicates `editedText`/`text` rules.

## Prioritized DRY/refactor targets

1. `actions/switchFontType.ts`
   - The TODO in the file points to reuse of `addElements`/`removeElements`.
   - Extract shared “split/replace literal segment + focus update” logic used in both block and itemList branches.
2. `actions/deleteSelection.ts`
   - A comment marks duplicated logic around text removal in parent containers.
   - Consolidate into one helper for “remove between start/end in text parent” with clear type guards.
3. `actions/paste.ts`
   - `ensureLiteralAtIndex` uses direct `textContents.splice(...)`.
   - Consider a helper that expresses the same intent using existing utility patterns where possible.
4. `actions/table.ts`
   - Several places use direct `splice` for columns/cells.
   - Keep as explicit exceptions until the structure gets dedicated `deleted*` arrays; optionally centralize these operations in one internal helper to reduce duplication.
5. `actions/merge.ts`
   - Multiple TODOs around generalizing itemList behavior.
   - Extract “break out of single empty list item” and “merge into neighboring list” into named helpers.

## Checklist before finishing

- [ ] The change follows the `withPatches` pattern.
- [ ] Focus is valid after all branches.
- [ ] `saveStatus` is set only on real model changes.
- [ ] `deleted*` arrays remain consistent.
- [ ] Empty container cases are handled (`newLiteral()` when needed).
- [ ] Table-separator invariants are still satisfied.
