# Editor accessibility tests

Playwright + `@axe-core/playwright` checks for the letter editor, targeting WCAG 2.1
A and AA. Automated checks support accessibility evaluation; they do not certify
compliance. No rules are excluded and no violations are accepted as a passing baseline.

## Run locally

From `skribenten-web/frontend`:

```sh
npm ci
npx playwright install chromium
npm run a11y
```

For a browsable report with the full `axe-results` JSON attachments:

```sh
npm run a11y:report
```

The report opens automatically on failure. Reopen it with `npx playwright show-report`.

The existing Playwright config builds the application and starts Vite preview on
port 4173. No backend, Docker services, credentials or real case data are needed.
An existing preview server is reused locally; rebuild with `npm run build` first
if its assets are stale. For UI mode, start `npm run dev` separately and run
`npm run e2e:ui -- test/e2e/accessibility` (port 5173).

Tests reuse `setupSakStubs`, its mocked user information/reservation and the existing
letter/model-specification JSON fixtures. They render the real Skribenten route,
not a standalone component. This does not test authentication or live integrations.
Chromium uses the existing 1200 x 1400 viewport. No separate config or test framework
is introduced. `npm run e2e` and the existing CI E2E command also discover these tests;
known violations make those runs fail until the underlying issues are fixed.

## States and assertions

- Editor at `/saksnummer/123456/brev/1`, including toolbar, case details and form.
- Same page with the reset-template confirmation dialog open. Scan the whole page,
  without explicit exclusions; the native dialog makes its background inert.
- Keyboard-only check: reach the reset control with Tab, open with Enter, confirm
  focus enters/stays in the dialog after a Tab press, dismiss with Escape and verify
  focus returns to the trigger. A 50-Tab bound detects an unreachable control;
  it is not a claim that the overall focus order is good or that every trap is absent.

Scans wait for letter text, the loaded Land field, saved status, an enabled reset
control, fonts and current animations. The dialog scan also waits for its cancel
button. No fixed sleep or `networkidle` dependency is used.

The axe tags are `wcag2a`, `wcag2aa` and `wcag21aa`: automated WCAG 2.0 A/AA rules
plus WCAG 2.1 AA additions. Best-practice and AAA rules are outside this suite's scope.
Every selected violation fails, regardless of severity. Failure messages summarize
rule ID, impact, WCAG criteria and affected-element counts. The `accessibility-details`
text attachment includes descriptions, selectors, HTML, failure summaries and help URLs.
JSON attachments retain passes, violations, inapplicable and `incomplete` results;
review `incomplete` manually rather than treating it as a pass. Reports can contain
rendered letter data: keep synthetic fixtures and do not share reports of real cases.

## Observed baseline

Local Chromium run with axe 4.13.0: one failing editor scan, one passing dialog scan
and one passing keyboard test. No violations were suppressed or fixed.

| WCAG | Axe rule / impact | Editor finding |
| --- | --- | --- |
| 1.4.3 Contrast (Minimum), AA | `color-contrast` / serious | Seven text elements: case-detail labels/values and letter date. `#909399` on white gives 3.07:1; normal 16px text requires 4.5:1. |
| 1.1.1 Non-text Content, A | `svg-img-alt` / serious | One `svg[font-size="24px"]` has `role="img"` without an accessible name. Evaluate whether it is meaningful or decorative before choosing a fix. |

The dialog has no definite violations for the selected tags. Its inert background
is not equivalent to the base editor passing, and a passing scan does not replace
the manual checks below. These findings describe this fixture/state/version only.

## Automated versus manual coverage

Axe detects some programmatic names/roles/relationships, missing text alternatives,
invalid ARIA, missing labels, language declarations and text contrast in the rendered
state. Passing only means those selected rules found no definite violations there.

These still need manual evaluation or dedicated interaction tests:

| Area | WCAG examples | Skribenten checks |
| --- | --- | --- |
| Keyboard and focus | 2.1.1, 2.1.2, 2.4.3, 2.4.7 | contentEditable entry/exit, caret/selection, table navigation, toolbar shortcuts, logical order and visible focus |
| Structure and meaning | 1.1.1, 1.3.1, 1.3.2, 2.4.1, 2.4.2, 2.4.4, 2.4.6, 3.1.1, 3.1.2 | screen-reader reading order, bypass behavior, meaningful titles/labels/headings/links, appropriate alternatives and language changes |
| Visual adaptation | 1.3.4, 1.4.1, 1.4.3, 1.4.4, 1.4.10, 1.4.11, 1.4.12, 1.4.13 | orientation, color-only cues, contrast in all states, focus/control contrast, 320px/high zoom reflow, text spacing, hover/focus content |
| Forms and errors | 1.3.5, 3.2.1, 3.2.2, 3.3.1-3.3.4 | input purpose, unexpected context changes, validation/error quality, suggestions and destructive-action prevention |
| Dynamic interactions | 3.2.3, 3.2.4, 4.1.2, 4.1.3 | consistent controls, custom-control name/role/value across states, complex dialogs, autosave/save/error announcements |

Screen-reader usability, understandable user experience and editor behavior are not
fully covered by axe or the one keyboard test. Test with a screen reader and a human
keyboard walkthrough, not just DOM assertions. Existing editor/table E2E tests are
useful foundations, but should be reviewed before claiming accessibility coverage.

## References and next tests

### Adding editor states

Keep editor accessibility tests in `editor.spec.ts`. The shared `beforeEach` loads
the editor with existing fixtures. Add state-specific scans under `WCAG 2.1 A/AA scans`:
perform the interaction, wait for the resulting content or control to be ready, then
call `expectNoAxeViolations(page, testInfo)`. Keep keyboard/focus assertions under
`Keyboard interaction`; a scan alone does not verify interaction behavior.

For numbered lists, assert that the list is rendered before scanning. For tables,
wait for their cells and headers. For a table context menu, wait for its menu items
and scan while it is open. Reuse fixtures and selectors from existing editor E2E tests.
If editing triggers autosave, mock a complete save response containing the submitted
letter and wait for the saved state before scanning. Do not exclude failing controls.

### Guidance

Use [NAV/Aksel guidance](https://aksel.nav.no/) as the primary interpretation and
component reference, especially [Modal accessibility](https://aksel.nav.no/komponenter/core/modal)
and [Button accessibility](https://aksel.nav.no/komponenter/core/button).
The installed Aksel version is 8.16.2; check guidance against that version when fixing.
Prefer semantic HTML and existing Aksel components over custom ARIA. Keep focus
indicators, avoid positive tabindex and clickable divs, provide meaningful accessible
names and ensure save/error statuses can be announced. Report findings first;
evaluate any production fixes separately.

Next small additions, reusing existing fixtures and editor tests:

1. Enter/exit contentEditable using the keyboard; verify caret/selection survives toolbar formatting.
2. Table header/body navigation, cell editing and keyboard entry/exit from table tools.
3. Save success/failure and validation states: axe scan plus live-region semantics and manual announcement checks.
4. Dialog Tab/Shift+Tab cycling, cancellation and focus restoration for table insertion.
5. 320px/400% zoom and text-spacing evaluation, with keyboard-visible-focus screenshots and human review.