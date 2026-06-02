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
6. Preserve semantics for existing IDs and `deleted*` arrays during move/split/merge (see **ID and deletion semantics** below).

## ID and deletion semantics

Every element in the `EditedLetter` model (`AnyBlock`, content items, list items, table rows/cells, …) implements `Identifiable`:

```ts
type Identifiable = { id: number | null; parentId: number | null };
```

Understanding `id` and `parentId` is critical because the **backend uses them to merge user edits with fresh template renders** after saksbehandlerValg changes.

### `id`: template origin vs. user-created

| Value | Meaning |
|---|---|
| `number` | Element originated from the brevbaker template render. Stable per logical template element. |
| `null` | Element was created by the saksbehandler (new paragraph, literal, list item, …). Never has a template counterpart. |

**Rules:**
- **Always set `id: null`** when creating new elements. Never fabricate or guess IDs. All `new*` factory functions in `common.ts` do this correctly — use them.
- **Never mutate `id`** on an element received from the server.
- The backend's `isNew()` check (`id == null`) is the sole sentinel for "user-created". These elements are always kept verbatim during re-merge.

**Exception — ID preservation during merge**: When merging several nodes into one (e.g., merging adjacent item lists into a single list), it is correct to give the resulting merged node the `id` of one of the original nodes. This preserves the backend's merge-tracking identity — without it the backend would see the node as both deleted and re-added from the fresh render. The pattern only applies when:
1. The source node with the copied `id` is removed via `removeElements` first (so its `id` enters `deleted*`), AND
2. `addElements` is used to insert the merged node (which removes the id from `deleted*` again).

Always mark such sites with a comment: `// INTENTIONAL ID COPY — ID preservation during merge (see letter-editor-actions SKILL.md).`

### `parentId`: tracking element origin

`parentId` records the `id` of the **containing parent at the time the element was placed** (set during `toEdit()` on the backend when converting a fresh render to an edit model).

The backend uses `parentId` for two things:
1. **`isEdited()` on a container** — if any child has `parentId != parent.id`, the container is considered edited (the child was moved in from another parent).
2. **Moved-element preservation during re-merge** — if a template element is no longer in the fresh render but `child.parentId != current parent.id`, the backend infers it was intentionally moved and preserves it.

**Rules:**
- **Always set `parentId: null`** on newly created elements. The `new*` factories do this.
- **Never mutate `parentId`** on elements received from the server — it is a read-only tracking field.
- When you *move* a template element to a different parent (e.g. reordering across blocks), its `parentId` will legitimately differ from its new parent's `id`. This is correct behavior and is how the backend detects intentional moves.

### `deleted*` arrays: tracking deleted template elements

Each container has a companion array for tracking deleted template elements:

| Container | Companion array |
|---|---|
| `EditedLetter` | `deletedBlocks` |
| `AnyBlock` | `deletedContent` |
| `ItemList` | `deletedItems` |
| `Item` | `deletedContent` |
| `Table` | `deletedRows` |

These arrays hold **integer IDs only** — never `null`. The backend uses them to filter template elements out of fresh renders so deleted content stays deleted after re-merge.

**Rules — when removing an element:**
- Use `removeElements(index, count, parent)` — it calls `deleteElement()` internally, which handles all the guards below.
- If you must remove manually, push `element.id` to the parent's `deleted*` array **only if all** of these hold:
  1. `element.id !== null` — don't track user-created elements.
  2. `element.parentId === parent.id` — element genuinely belongs to this parent (not moved in from elsewhere).
  3. The `id` is not already in `deleted*` and not still present in the content array.

**Rules — when re-adding an element:**
- Use `addElements(elements, at, parent.content, parent.deletedContent)` — it removes matching IDs from `deleted*` automatically.
- If you must add manually, remove the element's `id` from `deleted*` to un-mark it as deleted.

**Split-persistence mechanism — `removeElements` on moved content is intentional:**
When splitting a block or list (user presses Enter, or toggles off a middle list item), the content moving to the new user-created block/list is removed from the source via `removeElements`. This marks those IDs as deleted in the source's `deleted*` array — which looks like a "false deletion" but is intentionally correct: those deletion records prevent the backend from re-introducing the content back into the source block/list on the next re-merge. The new block/list has `id: null` (user-created) and is preserved verbatim by the backend, retaining the moved content. Do NOT replace `removeElements` with raw `splice` here — the deletion record is required.

**When splitting a container (e.g. splitting a list into two):**
- Decide which `deleted*` entries logically belong to each half. When it is ambiguous (e.g. `deletedItems` after splitting a list at an unknown original position), propagate all deleted entries to **both** halves — the backend prunes stale IDs after re-merge anyway, so having extras is safe; missing entries would let the backend re-introduce deleted items.
- **Do not silently drop `deleted*` entries** — a missing deletion record means the backend will re-introduce the deleted element from the fresh render.

### Edit overlay fields

These fields store user modifications **alongside** the original template value — never replace the original:

| Field | Type | Original | Edited |
|---|---|---|---|
| `LiteralValue.text` | `string` | template text (read-only) | `editedText: string \| null` |
| `LiteralValue.fontType` | `FontType` | template font (read-only) | `editedFontType: FontType \| null` |
| `ItemList.listType` | `ListType` | template list type (read-only) | `editedListType: ListType \| null` |

`null` means "unmodified; use original". The `text()` helper reads `editedText ?? text`. The backend's `toMarkup()` applies the same overlay pattern.

**Rule:** To edit text/font/list-type, set the `edited*` field. Never write to `text`, `fontType`, or `listType` directly.

### `mergeLiteralsIfPossible`

Two adjacent literals can only be merged into one when **at least one has `id: null`** (is user-created). Two template literals (both with non-null IDs) are always kept separate — merging them would discard an ID the backend needs for re-merge matching. `addElements()` calls this automatically at insertion boundaries.

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
- [ ] `deleted*` arrays remain consistent (see ID and deletion semantics above).
- [ ] New elements use `id: null` and `parentId: null`; no existing IDs are mutated.
- [ ] Edit overlays (`editedText`, `editedFontType`, `editedListType`) are set instead of overwriting originals.
- [ ] Empty container cases are handled (`newLiteral()` when needed).
- [ ] Table-separator invariants are still satisfied.
