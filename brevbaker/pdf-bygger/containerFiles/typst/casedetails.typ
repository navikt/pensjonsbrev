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

#let documentCasedetails(input, languageSettings, above: 48pt) = {
  let annenMottaker = input.annenMottakerNavn != none
  let rows = ()
  if annenMottaker {
    rows.push(([#languageSettings.annenmottakerprefix], [#input.annenMottakerNavn]))
  }
  if input.gjelderNavn != none {
    let prefix = if annenMottaker { languageSettings.gjeldernavnprefix } else { languageSettings.navnprefix }
    rows.push(([#prefix], [#input.gjelderNavn]))
  }
  if input.gjelderFoedselsnummer != none {
    rows.push(([#languageSettings.foedselsnummerprefix], [#input.gjelderFoedselsnummer]))
  }
  if input.saksnummer != none {
    rows.push(([#languageSettings.saksnummerprefix], [#input.saksnummer]))
  }
  if input.dokumentDato != none {
    let (label, value) = rows.pop()
    rows.push((label, [#value #h(1fr) #input.dokumentDato]))
  }
  set text(size: 11pt)
  block(
    grid(
      columns: 2,
      column-gutter: 24pt,
      row-gutter: 8pt,
      ..rows.flatten(),
    ),
    above: above,
  )
}
