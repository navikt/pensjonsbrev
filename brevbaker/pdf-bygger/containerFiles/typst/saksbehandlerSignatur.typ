#import "input.typ": languageSettings, input

#let casedetails = {
  let annenMottaker = input.annenMottakerNavn != none
  set text(size: 12pt)
  pad(
    grid(
      columns:2,
      rows: 2,
      column-gutter: 16mm,
      row-gutter: 10pt,
      if annenMottaker [
        #languageSettings.annenmottakerprefix
      ],
      if annenMottaker [
        #input.annenMottakerNavn
      ],
      if annenMottaker {
        [#languageSettings.gjeldernavnprefix]
      } else {
        [#languageSettings.navnprefix]
      },
      [#input.gjelderNavn],
      [#languageSettings.foedselsnummerprefix], [#input.gjelderFoedselsnummer],
      [#languageSettings.saksnummerprefix], 
      [#input.saksnummer #h(1fr) #input.dokumentDato] ,
    ),
    bottom:48pt,
  )
}