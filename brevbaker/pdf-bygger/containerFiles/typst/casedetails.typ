#let casedetails(input, languageSettings) = {
  let annenMottaker = input.annenMottakerNavn != none
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
      [#input.saksnummer #h(1fr) #input.dokumentDato],
    ),
    above: 48pt,
  )
}