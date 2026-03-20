#import "content/state.typ": updateElementType, section-start, section-end, sectionCounter
#import "template.typ": logo, mainTitle
#import "input.typ": languageSettings, input

// Case details for attachments (simplified version without "annenMottaker")
#let attachmentCaseDetails = {
  set text(size: 12pt)
  pad(
    grid(
      columns: 2,
      rows: 3,
      column-gutter: 16mm,
      row-gutter: 10pt,
      [#languageSettings.vedlegggjeldernavnprefix], [#input.gjelderNavn],
      [#languageSettings.foedselsnummerprefix], [#input.gjelderFoedselsnummer],
      [#languageSettings.saksnummerprefix], [#input.saksnummer #h(1fr) #input.dokumentDato],
    ),
    bottom: 6pt,
  )
}

#let attachment(title, sectionNumber: int, showCaseDetails: bool, outline) = {
  pagebreak(to: "odd", weak: false)
  section-start(sectionNumber)

  // Show the NAV logo
  logo

  if showCaseDetails {
    // With case details (similar to LaTeX includesakinfo)
    v(37pt)
    attachmentCaseDetails
    mainTitle(title)
  } else {
    // Without case details
    v(28pt)
    mainTitle(title)
  }
  outline
  section-end(sectionNumber)
}