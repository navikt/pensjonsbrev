#let casedetails(input, languageSettings, above: 48pt) = {
  let annenMottaker = input.annenMottakerNavn != none
  let dokumentDato = input.at("dokumentDato", default: none)
  set text(size: 11pt)
  block(
    grid(
      columns: 2,
      column-gutter: 24pt,
      row-gutter: 8pt,
      ..if annenMottaker {(
        [#languageSettings.annenmottakerprefix],
        [#input.annenMottakerNavn],
        [#languageSettings.gjeldernavnprefix],
      )} else {(
        [#languageSettings.navnprefix],
      )},
      [#input.gjelderNavn],
      [#languageSettings.foedselsnummerprefix], [#input.gjelderFoedselsnummer],
      [#languageSettings.saksnummerprefix],
      if dokumentDato != none {
        [#input.saksnummer #h(1fr) #dokumentDato]
      } else {
        [#input.saksnummer]
      },
    ),
    above: above,
  )
}

#let documentDate(dokumentDato, above: 48pt) = {
  set text(size: 11pt)
  block(align(right)[#dokumentDato], above: above)
}
