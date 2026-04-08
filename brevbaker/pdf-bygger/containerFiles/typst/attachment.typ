#import "content/state.typ": section-start, section-end
#import "template.typ": logo, mainTitle
#import "input.typ": languageSettings, input
#import "casedetails.typ" : casedetails

#let startAttachment(title, sectionNumber: int, showCaseDetails: bool) = {
  pagebreak(to: "odd", weak: false)
  section-start(sectionNumber)

  logo
  if showCaseDetails {
    casedetails
  }

  mainTitle(title)
}

#let endAttachment(sectionNumber: int) = {
  section-end(sectionNumber)
}