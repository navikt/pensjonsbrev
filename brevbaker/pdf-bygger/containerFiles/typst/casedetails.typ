#import "input.typ": languageSettings, input

#let casedetails = {
  let annenMottaker = input.annenMottakerNavn != none
  set text(size: 11pt)
  block(
    grid(
      columns: 2,
      column-gutter: 16mm,
      row-gutter: 8.6pt,
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
    above: 56.5pt,
  )
}