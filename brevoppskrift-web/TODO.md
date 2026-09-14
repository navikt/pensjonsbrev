# TODO — brevoppskrift-web

Kjente, bevisst utsatte forbedringer. Ryddes ned etter hvert som punktene tas.

## Ugyldig HTML når en conditional ligger inne i en liste

Opphav: review av PR #3625.

Når en control structure / conditional ligger inne i en `list { }` / `numberedList { }`,
rendrer `TemplateDocumentationV2View` strukturen slik:

```html
<ul>
  <div class="conditional">
    <details class="conditional-predicate">…</details>
    <li>innhold</li>
  </div>
</ul>
```

`<ul>`/`<ol>` tillater kun `<li>` som umiddelbare barn, så dette er ugyldig HTML.
Reproduseres på `/template/autobrev/UT_VEDTAK_LAVERE_MINSTESATS_2026?language=BOKMAL&docVersion=v2`,
i avsnittet "Plikt til å opplyse om endringer - folketrygdloven § 21-3".

Alternativer vurdert i reviewen:

- Legge conditional-beskrivelsen i et eget `<li>` og skjule `::marker`/kulen på det
  list-elementet. Enkelt for punktlister, men hårete for nummererte lister siden det
  skjulte elementet fortsatt teller i nummereringen.
- **Anbefalt:** re-wire renderingen slik at conditional-metadata (predikat-disclosure)
  rendres *inne i* selve `<li>`-elementet for list-items, i stedet for i en wrapper rundt.

Naiv nøsting (`<ul><li><div><ul><li>innhold`) er ikke aktuelt — det ville representert
malen feil. Samme problem finnes i v1-visningen.

## Uklar nøsting av if/else i tabeller

Opphav: review av PR #3625.

I `/template/autobrev/UT_VEDTAK_LAVERE_MINSTESATS_2026?language=BOKMAL&docVersion=v2`,
tabell 2 under overskriften "Dette er din månedlige uføretrygd før skatt", ligger det
nederst i tabellen en nøstet if + else. Både i v1 og v2 er det uklart om de to if-ene er
nøstede eller likeverdige — det ser nærmest ut som en syntaksfeil.

Kilde i malen:
`pensjon/maler/src/main/kotlin/no/nav/pensjon/brev/maler/fraser/vedlegg/VedleggMaanedligeUfoeretrgdFoerSkatt.kt`
(rundt linje 200).

`TableRowMarker` har nå `aria-label` med "If"/"Else If"/"Else", så semantikken er tydelig
for skjermleser, men ikke visuelt. Mulige grep: innrykk per nøstingsnivå i markør-gutteren,
eller tekstlige markører i stedet for `▸`/`?`.

## Tab-rekkefølge i tabeller følger ikke radene

Opphav: review av PR #3625.

`TableV2View` rendrer markør-gutteren som en egen DOM-container *før* `.table-scroll`.
Tab-rekkefølgen blir derfor "alle markører først, deretter alt tabellinnhold", i stedet for
rad for rad slik man forventer.

Kan ikke løses med `tabindex` uten a11y-antipattern; krever at markørene legges i samme
DOM-flyt som radene. Gutteren ble nettopp innført for å unngå at en markør plassert til
venstre for x = 0 blir klippet av `overflow: auto` på det scrollbare gridet (se docstring
på `layoutTableRows`), så en løsning må håndtere begge hensyn.
