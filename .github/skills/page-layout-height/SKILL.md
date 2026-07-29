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

## Common mistake #2: wrapping a grid/flex item breaks its free stretch height

A direct child of a `display:grid` or `display:flex` container gets its height "for free" via
the container's default stretch behavior (CSS Grid's `align-items: stretch`, or a flex column
parent sizing its item to available space) — **without** needing an explicit `height` prop of
its own.

If you introduce a new wrapper `<Box>` around that child (e.g. to fix an unrelated issue, such
as forcing a fragment with multiple top-level elements into a single grid item), the original
child is now a **plain block descendant of the wrapper**, one level removed from the
grid/flex container. It no longer gets stretch sizing automatically, and — unless it declares
its own `height="100%"` — it silently falls back to sizing to its own content instead of being
bounded by its parent. Nothing errors; the component just grows past its intended box and
either overflows visibly or gets clipped by an ancestor's `overflow: hidden`, with any internal
`overflowY="auto"` scroll region never actually needing to scroll (there's no bound to exceed).

**Rule of thumb:** whenever you wrap a component that used to be a direct grid/flex item,
check whether that component (or something inside it) relied on stretch-derived height. If so,
give the *wrapped* component's own root an explicit `height="100%"` — don't assume the wrapper
alone is enough.

**Case study**: `ThreeSectionLayout`'s `right` slot used to spread `props.right` directly as an
`HGrid` child. A PR review correctly flagged that a fragment there (e.g. `ManagedLetterEditor`
+ a modal, both top-level siblings) could produce extra grid items and break the 3-column
layout, so it was wrapped in `<Box minHeight="0">{props.right}</Box>`. This broke PDF
scrolling in `attester.$brevId/forhandsvisning.tsx`: `PDFViewer`'s root `Box` had no explicit
`height`, so it had been relying entirely on being a direct `HGrid` item for its height. Once
nested inside the new wrapper, it grew to fit every PDF page instead of clipping+scrolling.
Fix: add `height="100%"` to `PDFViewer`'s own root `Box` (mirroring what `LetterEditor`'s root
already had, which is why `redigering.tsx`'s `right` slot didn't regress the same way).

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
- If you added a wrapper around an existing grid/flex item (see "Common mistake #2" above),
  manually verify any internal scroll region inside the wrapped component still scrolls (e.g.
  scroll a long PDF/list to the bottom) — a missing `height="100%"` on the wrapped component's
  root won't produce a type error or a visual break at rest, only a silently non-scrolling,
  overgrown box.
