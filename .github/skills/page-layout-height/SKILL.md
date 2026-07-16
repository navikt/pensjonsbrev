---
name: page-layout-height
description: Size route content correctly against .page-margins' bounded, non-scrolling height
applies_to:
  - skribenten-web/frontend/src/**
---

# Page Layout Height Skill

Use this skill when writing or reviewing a route component (or a component it composes,
e.g. `ThreeSectionLayout`) under `skribenten-web/frontend/src/routes/`.

## The invariant

`.page-margins` (in `appStyles.css`) has a bounded `height` and `overflow-y: hidden` — it is
the **single clip boundary** for main content, sized as `calc(100vh - header - breadcrumbs)`.
It does not scroll. Anything that overflows it is invisible and unreachable, including fixed
bottom action bars (e.g. a "Fortsett" button).

Route components under it must size themselves **relative to that boundary**, not against a
fixed viewport-derived height.

## Rules

- **Route-root element** (the sole child of `.page-margins`) → `height="100%"`.
- **A flexible region among stacked siblings** (e.g. a form/table area next to a footer
  action bar) → `flexGrow="1"` plus:
  - `overflowY="hidden"` when that level should own its own clip boundary, or
  - `minHeight="0"` when an ancestor already clips, or the element has its own internal
    scroll region.

## Common mistake

Never hardcode a component's `height` from a viewport-relative value (e.g. the old
`--main-page-content-height` CSS custom property — removed from `appStyles.css`; do not
reintroduce it or an equivalent).

A hardcoded height assumes the component is the *only* content on the page. Any inline
sibling rendered above it (e.g. an error `<Alert>`/`<ApiError>`) adds height that isn't
accounted for. Since `.page-margins` doesn't scroll, the excess pushes fixed bottom action
bars off-screen with no way to reach them. `flexGrow="1"` shrinks and grows correctly
instead, since `flex-shrink` defaults to `1`.

## Reference implementation

See `components/ThreeSectionLayout.tsx` — used by both
`routes/attester.$brevId/redigering.tsx` and `routes/attester.$brevId/forhandsvisning.tsx`,
so it must stay robust under both a fixed-height and a variable-height (e.g. an `ApiError`
above it) sibling layout.

## Verification

- `npx tsc --noEmit` (or the project's configured type-check script) — confirms prop types
  are still valid after switching between `height`/`flexGrow`/`minHeight`/`overflowY`.
- Manually resize the browser viewport short enough that content would overflow, and trigger
  any error state above the layout (e.g. a failed form submit rendering `<ApiError />`) to
  confirm the bottom action bar stays visible.
