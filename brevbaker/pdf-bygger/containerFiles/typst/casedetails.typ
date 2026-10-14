#let casedetails(input, languageSettings, above: 48pt) = {
  let annenMottakerNavn = input.at("annenMottakerNavn", default: none)
  let gjelderNavn = input.at("gjelderNavn", default: none)
  let gjelderFoedselsnummer = input.at("gjelderFoedselsnummer", default: none)
  let saksnummer = input.at("saksnummer", default: none)
  let dokumentDato = input.at("dokumentDato", default: none)

  let rows = ()
  if annenMottakerNavn != none {
    rows.push(([#languageSettings.annenmottakerprefix], [#annenMottakerNavn]))
  }
  if gjelderNavn != none {
    let prefix = if annenMottakerNavn != none { languageSettings.gjeldernavnprefix } else { languageSettings.navnprefix }
    rows.push(([#prefix], [#gjelderNavn]))
  }
  if gjelderFoedselsnummer != none {
    rows.push(([#languageSettings.foedselsnummerprefix], [#gjelderFoedselsnummer]))
  }
  if saksnummer != none {
    rows.push(([#languageSettings.saksnummerprefix], [#saksnummer]))
  }

  set text(size: 11pt)
  if rows.len() == 0 {
    if dokumentDato != none {
      block(width: 100%, align(right)[#dokumentDato], above: above)
    }
  } else {
    if dokumentDato != none {
      let (label, value) = rows.pop()
      rows.push((label, [#value #h(1fr) #dokumentDato]))
    }
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
}
