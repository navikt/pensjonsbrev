#import "content/state.typ": section-start, section-end
#import "template.typ": logo, mainTitle
#import "input.typ": languageSettings, input

// Case details for attachments (simplified version without "annenMottaker")
#let attachmentCaseDetails = {
  set text(size: 11pt)
  block(
    grid(
      columns: 2,
      column-gutter: 16mm,
      row-gutter: 8.6pt,
      [#languageSettings.vedlegggjeldernavnprefix], [#input.gjelderNavn],
      [#languageSettings.foedselsnummerprefix], [#input.gjelderFoedselsnummer],
      [#languageSettings.saksnummerprefix], [#input.saksnummer #h(1fr) #input.dokumentDato],
    ),
    above: 57.5pt,
  )
}

#let startAttachment(title, sectionNumber: int, showCaseDetails: bool) = {
  pagebreak(to: "odd", weak: false)
  section-start(sectionNumber)

  // Show the NAV logo
  logo

  if showCaseDetails {
    attachmentCaseDetails
    mainTitle(title)
  } else {
    mainTitle(title)
  }
}

#let endAttachment(sectionNumber: int) = {
  section-end(sectionNumber)
}