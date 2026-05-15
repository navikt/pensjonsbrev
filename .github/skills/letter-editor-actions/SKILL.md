---
name: letter-editor-actions
description: Skriv/refaktorer LetterEditor-actions trygt med fokus/ID/deleted-invarianter
applies_to:
  - skribenten-web/frontend/src/Brevredigering/LetterEditor/actions/**
---

# LetterEditor Actions Skill

Bruk denne skillen når du endrer filer i `skribenten-web/frontend/src/Brevredigering/LetterEditor/actions`.

Målet er å holde action-logikk trygg og konsistent for:
- fokus-adressering (`Focus`/`LiteralIndex`)
- historikk (`withPatches`)
- ID/deleted-array bokføring (`deletedContent`, `deletedItems`, `deletedBlocks`)
- innholdsinvarianter for tekst, lister og tabeller

## Golden rules

1. Bruk alltid `Action<LetterEditorState, ...>` + `withPatches(...)` for nye actions.
2. Sett `draft.saveStatus = "DIRTY"` kun når action faktisk endrer brevmodellen.
3. Oppdater `draft.focus` deterministisk etter mutasjon; aldri la fokus peke på slettet innhold.
4. Guard tidlig med type/index-sjekker (`isValidIndex`, `isItemContentIndex`, `isTableCellIndex`, osv.).
5. Sørg for gyldig sluttstruktur: en blokk/item/celle som blir tom må normalt få inn `newLiteral()`.
6. Bevar semantikk for eksisterende ID-er og `deleted*`-arrayer ved flytt/splitt/merge.

## Utility-first: foretrukne byggeklosser

Foretrekk helpers i `actions/common.ts` før egen mutasjonslogikk:
- `removeElements(...)`: fjerner og oppdaterer `deleted*` korrekt.
- `addElements(...)`: setter inn og rydder `deleted*`, samt prøver literal-merge ved grense.
- `findAdjoiningContent(...)`: finn sammenhengende segmenter i stedet for manuell loop.
- `splitLiteralAtOffset(...)`: standardisert literal-splitt med korrekt `editedText`-håndtering.
- `new*`-konstruktører: opprett nye noder konsistent (`newLiteral`, `newItem`, `newItemList`, `newParagraph`, `newRow`, ...).

## Tabellspesifikke regler

Tabell-actions må ivareta disse invariantene:
- Ingen to tabeller direkte etter hverandre i samme blokk uten separator-literal.
- Siste blokk i brevet skal ikke ende i tabell uten trailing literal (caret-target).
- Etter rad/kolonne-operasjoner skal fokus peke på gyldig celle/headerposisjon.

Eksisterende normalisering:
- `normalizeTableSeparators` / `applyTableSeparatorNormalization` i `actions/common.ts`
- brukes ved last (`create`) og etter `deleteSelection`.

Direkte `splice(...)` er kun akseptabelt der vi per i dag ikke har parent-nivå `deleted*`-tracking for strukturen som muteres (for eksempel deler av tabell-header/rad-celler). Kommenter eksplisitt hvorfor.

## Refaktormønster (før/etter)

### 1) Fjerning/innsetting i parent-content

```ts
// Foretrukket
removeElements(index, count, parent);
addElements(elements, at, parent.content, parent.deletedContent);
```

Unngå manuell `splice` når parent har `deleted*`-array som skal vedlikeholdes.

### 2) Splitting av literal rundt cursor

```ts
const tail = splitLiteralAtOffset(literal, offset);
addElements([tail], insertAt, parent.content, parent.deletedContent);
```

Unngå ad hoc slicing som dupliserer `editedText`/`text`-regler.

## Prioriterte DRY/refaktor-targets

1. `actions/switchFontType.ts`
   - TODO i filen peker på gjenbruk av `addElements`/`removeElements`.
   - Ekstraher felles “split/replace literal-segment + focus update” brukt i både block- og itemList-gren.
2. `actions/deleteSelection.ts`
   - Kommentar markerer duplisert logikk rundt tekstfjerning i parent.
   - Samle felles flyt til én helper for “remove between start/end in text parent” med tydelige typeguards.
3. `actions/paste.ts`
   - `ensureLiteralAtIndex` bruker direkte `textContents.splice(...)`.
   - Vurder helper som uttrykker samme intensjon gjennom eksisterende util-mønster der mulig.
4. `actions/table.ts`
   - Flere steder bruker direkte `splice` for kolonner/celler.
   - Hold som eksplisitt unntak inntil strukturen får egne `deleted*`-arrayer; sentraliser eventuelt disse operasjonene i én intern helper for mindre duplisering.
5. `actions/merge.ts`
   - Flere TODO-er rundt generalisering av itemList-case.
   - Ekstraher “break out of single empty list item” og “merge into neighboring list” til navngitte helpers.

## Sjekkliste før du er ferdig

- [ ] Endringen følger `withPatches`-mønster.
- [ ] Fokus er gyldig etter alle grener.
- [ ] `saveStatus` settes kun ved reell endring.
- [ ] `deleted*`-arrayer holdes konsistente.
- [ ] Tomme container-tilfeller håndteres (`newLiteral()` ved behov).
- [ ] Tabellseparator-invarianter er fortsatt oppfylt.
